import processing.core.PImage;

import java.util.List;


public class ORE extends NonStatic {




    public ORE(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Point pos = this.getposition();

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        String BLOB_KEY = "blob";
        String BLOB_ID_SUFFIX = " -- blob";
        int BLOB_PERIOD_SCALE = 4;
        int BLOB_ANIMATION_MIN = 50;
        int BLOB_ANIMATION_MAX = 150;
        NonStatic blob = Factory.createOreBlob(this.getId() + BLOB_ID_SUFFIX, pos,
                this.getactionPeriod() / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN + Functions.rand.nextInt(
                        BLOB_ANIMATION_MAX
                                - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }




    }


