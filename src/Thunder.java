import processing.core.PImage;

import java.util.List;

public class Thunder extends Animated {




    String BLOB_KEY = "blob";
    String BLOB_ID_SUFFIX = " -- blob";
    int BLOB_PERIOD_SCALE = 4;
    int BLOB_ANIMATION_MIN = 50;
    int BLOB_ANIMATION_MAX = 150;


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


