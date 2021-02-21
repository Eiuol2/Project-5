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
            entity.executeActivity(this.world,
                    this.imageStore, scheduler);


    }}
