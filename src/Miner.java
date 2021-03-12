import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Miner extends Animated {

    private int resourceCount;
    private int resourceLimit;
 //   private PathingStrategy strategy = new SingleStepPathingStrategy();
    private PathingStrategy strategy = new AStarPathingStrategy();

    public Miner(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public boolean transform(
                             NonStatic entity,
                             WorldModel world,
                             EventScheduler scheduler,
                             ImageStore imageStore) {
        if (this.resourceCount >= 2) {
            NonStatic miner = Factory.createMinerFull(this.getId(), this.resourceLimit,
                    this.getposition(), this.getactionPeriod(),
                    this.getAnimationPeriod(),
                    this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        } else {
            NonStatic miner = Factory.createMinerNotFull(this.getId(), this.resourceLimit, this.resourceCount,
                    this.getposition(), this.getactionPeriod(),
                    this.getAnimationPeriod(),
                    this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return false;
        }

    }


    public boolean move(//try to refactor this
                        WorldModel world,
                        Entity target,
                        EventScheduler scheduler) {

        if (this.getposition().adjacent(target.getposition())) {
                if (target instanceof BLACKSMITH) { return true;}
                this.resourceCount += 1;
                world.removeEntity(target);
                scheduler.unscheduleAllEvents(target);

                return true;

        }
        else {

            List<Point> possible = this.strategy.computePath(this.getposition(), target.getposition(),
                    p -> world.withinBounds(p) && !(world.isOccupied(p)),
                    Point::adjacent,
                    PathingStrategy.CARDINAL_NEIGHBORS);


            Point nextPos;
            if (possible.size() == 0){
                nextPos = this.getposition();}
            else{
                nextPos = possible.get(0);}



            if (!this.getposition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                occupant.ifPresent(scheduler::unscheduleAllEvents);

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

/*
        public Point nextPositionMiner (WorldModel world, Point destPos)
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

 */

    }




