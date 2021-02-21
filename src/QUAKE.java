import processing.core.PImage;

import java.util.List;

public class QUAKE extends Animated {


    public QUAKE(String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, 0, 0, actionPeriod, animationPeriod);
    }



    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
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


