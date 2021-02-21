import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class VEIN implements NonStatic {


    private final String id;
    private  Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;


    public void setPosition(Point p)
    {
        this.position = p;
    }

    public Point getposition()
    {
        return this.position;
    }


    public int getactionPeriod()
    {
        return this.actionPeriod;
    }




    public VEIN(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
    }


    public Action createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new Activity( this, world, imageStore, 0);
    }









    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.getposition());

        if (openPt.isPresent()) {
            String ORE_ID_PREFIX = "ore -- ";
            int ORE_CORRUPT_MIN = 20000;
            int ORE_CORRUPT_MAX = 30000;
            String ORE_KEY = "ore";
            NonStatic ore = Factory.createOre(ORE_ID_PREFIX + this.id, openPt.get(),
                    ORE_CORRUPT_MIN + Functions.rand.nextInt(
                            ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(ORE_KEY));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getactionPeriod());
    }


    public PImage getCurrentImage() {
        return ((this.images.get(this.imageIndex)));
    }


    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore){

                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getactionPeriod());


        }
    }


