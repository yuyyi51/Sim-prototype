package simulation.Events;

import simulation.ServiceManager;

public abstract class Event implements Comparable<Event> {
    private double startTime;
    protected ServiceManager manager;

    Event(double startTime){
        this.startTime = startTime;
    }

    @Override
    public int compareTo(Event o) {
        return Double.compare(startTime, o.startTime);
    }

    public double getStartTime() {
        return startTime;
    }

    public abstract void trigger();

    public final void setManager(ServiceManager manager){
        this.manager = manager;
    }
}
