import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public final class EventScheduler
{
    private final PriorityQueue<Event> eventQueue;
    private final Map<Entity, List<Event>> pendingEvents;
    private final double timeScale;


    public PriorityQueue<Event> getEventQueue()
    {
        return this.eventQueue;
    }

    public Map<Entity, List<Event>> getPendingEvents()
    {
        return this.pendingEvents;
    }

    public double getTimeScale()
    {
        return this.timeScale;
    }




    public EventScheduler(double timeScale) {
        this.eventQueue = new PriorityQueue<>(new EventComparator());
        this.pendingEvents = new HashMap<>();
        this.timeScale = timeScale;
    }

}
