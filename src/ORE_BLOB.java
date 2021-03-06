import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class ORE_BLOB extends Animated {




    private static boolean burned = false;
    //    private PathingStrategy strategy = new SingleStepPathingStrategy();
    private PathingStrategy strategy = new AStarPathingStrategy();
    public ORE_BLOB(
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


/*
    public boolean transform(Entity entity1,
                             Point entity,
                             WorldModel world,
                             EventScheduler scheduler,
                             ImageStore imageStore,
                             boolean burned) {
        world.removeEntity(entity1);
        if (burned) {

            NonStatic burnt = Factory.createPikachu(BLOB_ID_SUFFIX, entity,
                    BLOB_PERIOD_SCALE,
                    BLOB_ANIMATION_MIN + Functions.rand.nextInt(
                            BLOB_ANIMATION_MAX
                                    - BLOB_ANIMATION_MIN),
                    imageStore.getImageList("pikachu"));


     //       scheduler.unscheduleAllEvents(entity1);


            world.addEntity(burnt);
            burnt.scheduleActions(scheduler, world, imageStore);
            System.out.println("hi");

            return true;
        } else {

            System.out.println("hello");
            NonStatic notburnt = Factory.createOreBlob(this.getId() + BLOB_ID_SUFFIX, this.getposition(),
                    this.getactionPeriod() / BLOB_PERIOD_SCALE,
                    BLOB_ANIMATION_MIN + Functions.rand.nextInt(
                            BLOB_ANIMATION_MAX
                                    - BLOB_ANIMATION_MIN),
                    imageStore.getImageList(BLOB_KEY), true);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(notburnt);
            notburnt.scheduleActions(scheduler, world, imageStore);

            return false;
        }

    }



*/

}
