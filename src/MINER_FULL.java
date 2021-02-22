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


    public void transformFull(//try to refactor this
            NonStatic entity,
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        NonStatic miner = Factory.createMinerNotFull(this.getId(), this.getResourceLimit(),
                this.getposition(), this.getactionPeriod(),
                this.getAnimationPeriod(),
                this.getImages());

        world.removeEntity(entity);
        scheduler.unscheduleAllEvents(entity);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }





    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(this.getposition(), BLACKSMITH.class);

        if (fullTarget.isPresent() && moveToFull(world,
                fullTarget.get(), scheduler))
        {
            transformFull(this, world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.getactionPeriod());
        }
    }




    public boolean moveToFull(//try to refactor this
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.getposition().adjacent(target.getposition())) {
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



    }

