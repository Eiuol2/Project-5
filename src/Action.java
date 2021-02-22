import java.awt.*;

public abstract class Action
{

    private final NonStatic entity;
    private final WorldModel world;
    private final int repeatCount;

    public Action(NonStatic entity, WorldModel world, int repeatcount){
        this.entity = entity;
        this.world = world;
        this.repeatCount = repeatcount;
    }

    public int getRepeatCount() {return this.repeatCount;}

    public NonStatic getEntity()
    {
        return this.entity;
    }

    public WorldModel getWorld()
    {
        return this.world;
    }

    abstract void executeAction(EventScheduler scheduler);
}
