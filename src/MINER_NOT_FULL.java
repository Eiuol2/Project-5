import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MINER_NOT_FULL extends Miner {



    public MINER_NOT_FULL(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }




    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                world.findNearest(this.getposition(), ORE.class);

        if (notFullTarget.isEmpty() || !this.move(world,
                notFullTarget.get(),
                scheduler)
                || !this.transform(this, world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.getactionPeriod());
        }
    }






}
