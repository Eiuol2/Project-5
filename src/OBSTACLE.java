import processing.core.PImage;

import java.util.List;

public class OBSTACLE implements Entity  {


    private  Point position;
    private final List<PImage> images;
    private int imageIndex;


    public void setPosition(Point p)
    {
        this.position = p;
    }

    public Point getposition()
    {
        return this.position;
    }





    public OBSTACLE(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }


    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }

    @Override
    public int getAnimationPeriod() {
        return 0;
    }

    @Override
    public void nextImage() {

    }

    public Action createAnimationAction(int repeatCount) {
        return new Animation( this, null, null,
                repeatCount);
    }



    public PImage getCurrentImage() {
        return ((this.images.get(this.imageIndex)));
    }




    }


