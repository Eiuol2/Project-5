public class Activity implements Action{
    private final NonStatic entity;
    private final WorldModel world;
    private final ImageStore imageStore;
    private final int repeatCount;


    public Entity getEntity()
    {
        return this.entity;
    }

    public WorldModel getWorld()
    {
        return this.world;
    }

    public ImageStore getimageStore()
    {
        return this.imageStore;
    }

    public int getrepeatCount()
    {
        return this.repeatCount;
    }

    public Activity(
            NonStatic entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        if (this.entity instanceof MINER_FULL) {
            entity.executeActivity(this.world,
                    this.imageStore, scheduler);
        }

        if (this.entity instanceof MINER_NOT_FULL) {
            entity.executeActivity(this.world,
                    this.imageStore, scheduler);
        }

        if (this.entity instanceof ORE) {
            entity.executeActivity(this.world,
                    this.imageStore, scheduler);
        }

        if (this.entity instanceof ORE_BLOB) {
            entity.executeActivity(this.world,
                    this.imageStore, scheduler);
        }

        if (this.entity instanceof QUAKE) {
            entity.executeActivity(this.world,
                    this.imageStore, scheduler);
        }

        if (this.entity instanceof VEIN) {
            entity.executeActivity(this.world,
                    this.imageStore, scheduler);
        }
/*
                throw new UnsupportedOperationException(String.format(
                        "executeActivityAction not supported for %s",
                        this.entity.getClass()));
        }

 */


    }}