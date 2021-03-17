import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class burnt extends Animated {

    private static boolean burned = true;

    //    private PathingStrategy strategy = new SingleStepPathingStrategy();
    private PathingStrategy strategy = new AStarPathingStrategy();
    public burnt(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod, burned);
    }



    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> blobTarget =
                world.findNearest(this.getposition(), OBSTACLE.class);
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


            List<Point> possible = strategy.computePath(this.getposition(), target.getposition(),
                    p -> world.withinBounds(p) && !(world.getOccupant(p).isPresent() && !(world.getOccupant(p).get().getClass()
                            == ORE.class)),
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





}
