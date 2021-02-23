import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Miner extends Animated {

    private int resourceCount;
    private int resourceLimit;

    public Miner(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public boolean transform(//try to refactor this
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
                this.resourceCount += 1;
                if (target instanceof BLACKSMITH) { return true;}
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

        public int getResourceLimit () {
            return this.resourceLimit;
        }

        public int getResourceCount () {
            return this.resourceCount;
        }

        public void setResourceCount ( int nes){
            this.resourceCount += nes;
        }


    }




