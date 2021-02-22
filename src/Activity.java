import java.awt.*;

public class Activity extends Action{


    private ImageStore imageStore;

    public Activity(
            NonStatic entity,
            WorldModel world, ImageStore imageStore,
            int repeatCount)
    {
        super(entity, world, repeatCount);
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler) {
            getEntity().executeActivity(this.getWorld(),
                    this.imageStore, scheduler);


    }}
