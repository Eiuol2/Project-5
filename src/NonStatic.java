public interface NonStatic extends Entity{

    void nextImage();

    int getAnimationPeriod();

    void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);


    Action createAnimationAction(int repeatCount);



}
