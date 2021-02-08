import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class QUAKE implements Entity  {

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




    public QUAKE(
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



    public Action createAnimationAction(int repeatCount) {
        return new Animation( this, null, null,
                repeatCount);
    }

    public Action createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new Activity( this, world, imageStore, 0);
    }


    public int getAnimationPeriod() {
                return this.animationPeriod;
                /*
                throw new UnsupportedOperationException(
                        String.format("getAnimationPeriod not supported for %s",
                                this.kind));

                 */
        }



    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }


    public PImage getCurrentImage() {
        return ((this.images.get(this.imageIndex)));
    }


    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {

                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getactionPeriod());
                scheduler.scheduleEvent(this, this.createAnimationAction(
                        QUAKE_ANIMATION_REPEAT_COUNT),
                        this.getAnimationPeriod());



        }
    }


