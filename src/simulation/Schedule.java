package simulation;

import simulation.Events.Event;

import java.util.PriorityQueue;
import java.util.Queue;

public class Schedule {
    private double time;
    private Queue<Event> eventQueue;

    Schedule(){
        time = 0;
        eventQueue = new PriorityQueue<>();
    }

    private Event nextEvent(){
        Event e = eventQueue.poll();
        if (e != null)
            time = e.getStartTime();
        return e ;
    }

    public double getTime() {
        return time;
    }
    public void registerEvent(Event event){
        eventQueue.add(event);
    }
    public void start(){
        Event e;
        while ((e = nextEvent()) != null){
            e.trigger();
        }
    }
}
