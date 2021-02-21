public class Animation implements Action {
    private final Animated entity;
    private final WorldModel world;
    private final int repeatCount;


    public Entity getEntity()
    {
        return this.entity;
    }

    public WorldModel getWorld()
    {
        return this.world;
    }

    public Animation(
            Animated entity,
            WorldModel world,
            int repeatCount)
    {
        this.entity = entity;
        this.world = world;
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
