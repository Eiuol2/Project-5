public interface NonStatic extends Entity{

    void nextImage();

    void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore);


    void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);




}
