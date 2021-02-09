public class Animation implements Action {
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

    public Animation(
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



    public void executeAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                    this.entity.createAnimationAction(
                            Math.max(this.repeatCount - 1,
                                    0)),
                    this.entity.getAnimationPeriod());
        }
    }


}
