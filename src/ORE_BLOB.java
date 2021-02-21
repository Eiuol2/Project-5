import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class ORE_BLOB extends Animated {

    public ORE_BLOB(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, 0, 0, actionPeriod, animationPeriod);
    }





    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> blobTarget =
                world.findNearest(this.getposition(), VEIN.class);
        long nextPeriod = this.getactionPeriod();

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getposition();

            if (moveToOreBlob(world, blobTarget.get(), scheduler)) {
                String QUAKE_KEY = "quake";
                NonStatic quake = Factory.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.getactionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                nextPeriod);
    }


    public boolean moveToOreBlob(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.getposition().adjacent(target.getposition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = nextPositionOreBlob(world, target.getposition());

            if (!this.getposition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                occupant.ifPresent(scheduler::unscheduleAllEvents);

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }


    public Point nextPositionOreBlob(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.getposition().x);
        Point newPos = new Point(this.getposition().x + horiz, this.getposition().y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass()
                == ORE.class)))
        {
            int vert = Integer.signum(destPos.y - this.getposition().y);
            newPos = new Point(this.getposition().x, this.getposition().y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass()
                    == ORE.class)))
            {
                newPos = this.getposition();
            }
        }

        return newPos;
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
