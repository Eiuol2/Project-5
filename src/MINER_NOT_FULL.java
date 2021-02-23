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



    public boolean _minehelper(){
        return (this.getResourceCount() >= this.getResourceLimit());
    }



    public boolean moveToNotFull(WorldModel world,
                                 Entity target,
                                 EventScheduler scheduler)
    {
        if (this.getposition().adjacent(target.getposition())) {
            this.setResourceCount(1);
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else {
            Point nextPos = this.nextPositionMiner(world, target.getposition());

            if (!this.getposition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                occupant.ifPresent(scheduler::unscheduleAllEvents);

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                world.findNearest(this.getposition(), ORE.class);

        if (notFullTarget.isEmpty() || !this.moveToNotFull(world,
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
