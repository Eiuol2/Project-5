import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MINER_FULL extends Miner {





    public MINER_FULL(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
    }





    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(this.getposition(), BLACKSMITH.class);

        if (fullTarget.isPresent() && this.move(world,
                fullTarget.get(), scheduler))
        {
            this.transform(this, world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.getactionPeriod());
        }
    }






}

