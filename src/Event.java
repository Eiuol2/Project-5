public final class Event
{
    private final Action action;
    private final long time;
    private final Entity entity;


    public Action getaction()
    {
        return this.action;
    }

    public long gettime()
    {
        return this.time;
    }

    public Entity getEntity()
    {
        return this.entity;
    }

    public Event(Action action, long time, Entity entity) {
        this.action = action;
        this.time = time;
        this.entity = entity;
    }
}
