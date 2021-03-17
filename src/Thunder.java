import processing.core.PImage;

import java.util.List;

public class Thunder extends Animated {




    private static final boolean burned = false;

    public Thunder(String id,
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


