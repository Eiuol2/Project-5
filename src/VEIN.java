import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class VEIN extends NonStatic {



    public VEIN(
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
        Optional<Point> openPt = world.findOpenAround(this.getposition());

        if (openPt.isPresent()) {
            String ORE_ID_PREFIX = "ore -- ";
            int ORE_CORRUPT_MIN = 20000;
            int ORE_CORRUPT_MAX = 30000;
            String ORE_KEY = "ore";
            NonStatic ore = Factory.createOre(ORE_ID_PREFIX + this.getId(), openPt.get(),
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



    }


