import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class burnt extends Animated {


    String BLOB_KEY = "blob";
    String BLOB_ID_SUFFIX = " -- blob";
    int BLOB_PERIOD_SCALE = 4;
    int BLOB_ANIMATION_MIN = 50;
    int BLOB_ANIMATION_MAX = 150;


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

            if (moveBack(world, blobTarget.get(), scheduler, imageStore)) {
                String QUAKE_KEY = "quake";
                NonStatic quake = Factory.createQuake(this.getposition(),
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

    public boolean moveBack(
            WorldModel world,
            Entity target,
            EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.getposition().adjacent(target.getposition())) {
            Point prev = this.getposition();
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            NonStatic blob = Factory.createOreBlob(this.getId() + BLOB_ID_SUFFIX, prev,
                    4000,
                    BLOB_ANIMATION_MIN + Functions.rand.nextInt(
                            BLOB_ANIMATION_MAX
                                    - BLOB_ANIMATION_MIN),
                    imageStore.getImageList(BLOB_KEY), false);

            world.addEntity(blob);
            blob.scheduleActions(scheduler, world, imageStore);

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
