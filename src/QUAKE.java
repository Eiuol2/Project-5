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



    }


