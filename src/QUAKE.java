import processing.core.PImage;

import java.util.List;

public class QUAKE extends Animated {


    private static final boolean burned = false;

    public QUAKE(String id,
                 Point position,
                 List<PImage> images,
                 int actionPeriod,
                 int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod, burned);
    }



    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }



    }


