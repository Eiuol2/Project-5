import processing.core.PImage;

import java.util.List;

public class QUAKE implements NonStatic  {

    private  Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;
    private final int animationPeriod;


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




    public QUAKE(
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }


    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public Action createAnimationAction(int repeatCount) {
        return new Animation( this, null,
                repeatCount);
    }

    public Action createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new Activity( this, world, imageStore, 0);
    }


    public int getAnimationPeriod() {
                return this.animationPeriod;
        }



    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }


    public PImage getCurrentImage() {
        return ((this.images.get(this.imageIndex)));
    }


    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {

                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getactionPeriod());
        int QUAKE_ANIMATION_REPEAT_COUNT = 10;
        scheduler.scheduleEvent(this, this.createAnimationAction(
                QUAKE_ANIMATION_REPEAT_COUNT),
                        this.getAnimationPeriod());



        }
    }


