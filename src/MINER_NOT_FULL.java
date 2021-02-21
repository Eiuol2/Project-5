import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MINER_NOT_FULL extends Animated {



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



    public boolean transformNotFull(
            NonStatic entity,
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit()) {
            NonStatic miner = Factory.createMinerFull(this.getId(), this.getResourceLimit(),
                    this.getposition(), this.getactionPeriod(),
                    this.getAnimationPeriod(),
                    this.getImages());

            world.removeEntity(entity);
            scheduler.unscheduleAllEvents(entity);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public Point nextPositionMiner(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.getposition().x);
        Point newPos = new Point(this.getposition().x + horiz, this.getposition().y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.getposition().y);
            newPos = new Point(this.getposition().x, this.getposition().y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.getposition();
            }
        }

        return newPos;
    }


    public boolean moveToNotFull(WorldModel world,
                                 Entity target,
                                 EventScheduler scheduler)
    {
        if (this.getposition().adjacent(target.getposition())) {
            this.setResourceCount(this.getResourceCount() + 1);
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
                || !this.transformNotFull(this, world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.getactionPeriod());
        }
    }





    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getactionPeriod());
                scheduler.scheduleEvent(this,
                        this.createAnimationAction(0),
                        this.getAnimationPeriod());

    }

}
