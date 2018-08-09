package simulation;

import simulation.Events.Event;

import java.util.*;

public class ServiceManager {
    private Map<String, Service> registeredService;
    private Map<String, Service> runningService;

    private double time;
    private Queue<Event> eventQueue;
    private List<Event> eventLog;

    ServiceManager(){
        registeredService = new HashMap<>();
        runningService = new HashMap<>();

        time = 0;
        eventQueue = new PriorityQueue<>();
        eventLog = new LinkedList<>();
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
        event.setManager(this);
    }
    public void start(){
        Event e;
        while ((e = nextEvent()) != null){
            e.trigger();
            eventLog.add(e);
        }
    }
    public List<Event> getEventLog(){
        return eventLog;
    }

    public ServiceManager registerService(Service service){
        registeredService.put(service.getServiceName(), service);
        runningService.put(service.getServiceName(), service);
        service.setManager(this);
        return this;
    }
    public Service disableService(String serviceName){
        Service service = runningService.get(serviceName);
        if (service != null){
            runningService.remove(serviceName);
        }
        return service;
    }
    public Service findRunningService(String name){
        return runningService.get(name);
    }
    public Service findRegisteredService(String name){
        return registeredService.get(name);
    }

}
