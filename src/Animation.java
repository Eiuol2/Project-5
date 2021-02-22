public class Animation extends Action {


    public Animation(
            Animated entity,
            WorldModel world,
            int repeatCount)
    {
    super(entity, world, repeatCount);
    }



    public void executeAction(EventScheduler scheduler)
    {
        Animated temp = (Animated) this.getEntity();
        temp.nextImage();

        if (this.getRepeatCount() != 1) {
            scheduler.scheduleEvent(temp,
                    temp.createAnimationAction(
                            Math.max(this.getRepeatCount() - 1,
                                    0)),
                    temp.getAnimationPeriod());
        }
    }


}
