public interface NonStatic extends Entity{

    void nextImage();

    int getAnimationPeriod();

    void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore);


    void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);


    Action createAnimationAction(int repeatCount);



}
