import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    private final int TIMER_ACTION_PERIOD = 100;

    private final int VIEW_WIDTH = 640;
    private final int VIEW_HEIGHT = 480;
    private final int TILE_WIDTH = 32;
    private final int TILE_HEIGHT = 32;
    private static final int TILE_SIZE = 32;
    private final int WORLD_WIDTH_SCALE = 2;
    private final int WORLD_HEIGHT_SCALE = 2;

    private final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String LOAD_FILE_NAME = "world.sav";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    public static final String VEIN_KEY = "vein";
    public static final int VEIN_NUM_PROPERTIES = 5;
    public static final int VEIN_ID = 1;
    public static final int VEIN_COL = 2;
    public static final int VEIN_ROW = 3;
    public static final int VEIN_ACTION_PERIOD = 4;

    String BLOB_KEY = "blob";
    String BLOB_ID_SUFFIX = " -- blob";
    int BLOB_PERIOD_SCALE = 4;
    int BLOB_ANIMATION_MIN = 50;
    int BLOB_ANIMATION_MAX = 150;


    public static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private long nextTime;




    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
    }

    private Point mouseToPoint(int x, int y)
    {
        return new Point(mouseX/TILE_SIZE, mouseY/TILE_SIZE);
    }

    public void mousePressed()
    {
        Point pressed = mouseToPoint(mouseX, mouseY);
        pressed = view.getViewport().viewportToWorld(pressed.x, pressed.y);

        NonStatic blob = Factory.createPikachu(BLOB_ID_SUFFIX, pressed,
                3000,
                BLOB_ANIMATION_MIN + Functions.rand.nextInt(
                        BLOB_ANIMATION_MAX
                                - BLOB_ANIMATION_MIN),
                imageStore.getImageList("pikachu"));



        if (!world.isOccupied(pressed) && world.withinBounds(pressed)) {
            world.tryAddEntity(blob);
            blob.scheduleActions(scheduler, world, imageStore);

            List<Point> bolts = new ArrayList<>();
            int createFlash = 0, attempts = 0;
            Random rand = new Random();
            String id = "scorched";

            world.setBackground(pressed, new Background(id, imageStore.getImageList(id)));

            while (createFlash < 10 && attempts < 25){

                int offsetx = rand.nextInt(5)-2;
                int offsety = rand.nextInt(5)-2;
                Point flash = new Point(pressed.x + offsetx, pressed.y + offsety);


                if (!bolts.contains(flash)) {
                    if (!world.isOccupied(flash) && world.withinBounds(flash)) {

                        world.setBackground(flash, new Background(id, imageStore.getImageList(id)));
                        bolts.add(flash);
                        createFlash++;
                    } else if (world.isOccupied(flash) && world.getOccupancyCell(flash).getClass() == ORE_BLOB.class) {
                        world.setBackground(flash, new Background(id, imageStore.getImageList(id)));
                        PIKACHU p = (PIKACHU) blob;
                        p.setWillBurn(true);
                        p.addWillBurnCount();
                        bolts.add(flash);
                    }
                }
                attempts++;
            }
        }

        redraw();

    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                              imageStore.getImageList(
                                                     DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            Functions.load(in, world, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities())
        {
            if (entity instanceof BLACKSMITH || entity instanceof OBSTACLE || entity instanceof tree){
                continue;
            }
            else{
                NonStatic temp = (NonStatic) entity;
                temp.scheduleActions(scheduler, world, imageStore);}

        }
    }

    public static void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
