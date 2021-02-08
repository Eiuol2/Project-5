import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class BLACKSMITH implements Entity  {
}

    private final String BLOB_KEY = "blob";
    private final String BLOB_ID_SUFFIX = " -- blob";
    private final int BLOB_PERIOD_SCALE = 4;
    private final int BLOB_ANIMATION_MIN = 50;
    private final int BLOB_ANIMATION_MAX = 150;

    private final String QUAKE_KEY = "quake";
    private final int QUAKE_ANIMATION_REPEAT_COUNT = 10;



    private final String ORE_ID_PREFIX = "ore -- ";
    private final int ORE_CORRUPT_MIN = 20000;
    private final int ORE_CORRUPT_MAX = 30000;
    private final String ORE_KEY = "ore";




    private final String id;
    private  Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int resourceLimit;
    private int resourceCount;
    private final int actionPeriod;
    private final int animationPeriod;


    public void setPosition(Point p)
    {
        this.position = p;
    }



    public String getid()
    {
        return this.id;
    }

    public Point getposition()
    {
        return this.position;
    }


    public int getactionPeriod()
    {
        return this.actionPeriod;
    }




    public BLACKSMITH(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }


    public void transformFull(
            Entity entity,
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        Entity miner = Functions.createMinerNotFull(this.id, this.resourceLimit,
                this.position, this.actionPeriod,
                this.animationPeriod,
                this.images);

        world.removeEntity(entity);
        scheduler.unscheduleAllEvents(entity);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    public boolean transformNotFull(
            Entity entity,
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            Entity miner = Functions.createMinerFull(this.id, this.resourceLimit,
                    this.position, this.actionPeriod,
                    this.animationPeriod,
                    this.images);

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
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }


    public boolean moveToNotFull(WorldModel world,
                                 Entity target,
                                 EventScheduler scheduler)
    {
        if (this.position.adjacent(target.position)) {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else {
            Point nextPos = this.nextPositionMiner(world, target.getposition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public Action createAnimationAction(int repeatCount) {
        return new Animation( this, null, null,
                repeatCount);
    }

    public Action createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new Activity( this, world, imageStore, 0);
    }


    public int getAnimationPeriod() {
        switch (this.kind) {
            case MINER_FULL:
            case MINER_NOT_FULL:
            case ORE_BLOB:
            case QUAKE:
                return this.animationPeriod;
            default:
                throw new UnsupportedOperationException(
                        String.format("getAnimationPeriod not supported for %s",
                                this.kind));
        }
    }

    public void executeMinerFullActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(this.position, EntityKind.BLACKSMITH);

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


    public void executeMinerNotFullActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                world.findNearest(this.position, EntityKind.ORE);

        if (!notFullTarget.isPresent() || !this.moveToNotFull(world,
                notFullTarget.get(),
                scheduler)
                || !this.transformNotFull(this, world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.getactionPeriod());
        }
    }


    public void executeOreActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Point pos = this.position;

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Entity blob = Functions.createOreBlob(this.getid() + BLOB_ID_SUFFIX, pos,
                this.actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN + Functions.rand.nextInt(
                        BLOB_ANIMATION_MAX
                                - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }



    public void executeOreBlobActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> blobTarget =
                world.findNearest(this.position, EntityKind.VEIN);
        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getposition();

            if (moveToOreBlob(world, blobTarget.get(), scheduler)) {
                Entity quake = Functions.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                nextPeriod);
    }

    public void executeQuakeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public void executeVeinActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.getposition());

        if (openPt.isPresent()) {
            Entity ore = Functions.createOre(ORE_ID_PREFIX + this.id, openPt.get(),
                    ORE_CORRUPT_MIN + Functions.rand.nextInt(
                            ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(ORE_KEY));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getactionPeriod());
    }
    public boolean moveToFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.position.adjacent(target.getposition())) {
            return true;
        }
        else {
            Point nextPos = this.nextPositionMiner(world, target.getposition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }



    public boolean moveToOreBlob(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.position.adjacent(target.getposition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = nextPositionOreBlob(world, target.getposition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }


    public Point nextPositionOreBlob(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getKind()
                == EntityKind.ORE)))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getKind()
                    == EntityKind.ORE)))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }
    public PImage getCurrentImage() {
        return ((this.images.get(this.imageIndex)));
    }


    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        switch (this.getKind()) {
            case MINER_FULL:
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getactionPeriod());
                scheduler.scheduleEvent(this,
                        this.createAnimationAction(0),
                        this.getAnimationPeriod());
                break;

            case MINER_NOT_FULL:
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getactionPeriod());
                scheduler.scheduleEvent(this,
                        this.createAnimationAction(0),
                        this.getAnimationPeriod());
                break;

            case ORE:
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getactionPeriod());
                break;

            case ORE_BLOB:
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getactionPeriod());
                scheduler.scheduleEvent(this,
                        this.createAnimationAction(0),
                        this.getAnimationPeriod());
                break;

            case QUAKE:
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getactionPeriod());
                scheduler.scheduleEvent(this, this.createAnimationAction(
                        QUAKE_ANIMATION_REPEAT_COUNT),
                        this.getAnimationPeriod());
                break;

            case VEIN:
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getactionPeriod());
                break;

            default:
        }
    }

