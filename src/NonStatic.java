import processing.core.PImage;

import java.util.List;

public abstract class NonStatic extends Entity{

    private int actionPeriod;

    public NonStatic(String id, Point position, List<PImage> images, int actionPeriod){
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }


    public abstract void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore);


    public abstract void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);

    public Action createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new Activity( this, world, imageStore, 0);
    }



    public int getactionPeriod() {return this.actionPeriod;}
}
