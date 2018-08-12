package simulation;

import simulation.Events.Event;
import simulation.Log.SimLog;

import java.util.*;

public class ServiceManager {
    private Map<String, Service> registeredService;
    private Map<String, Service> runningService;

    private double time;
    private Queue<Event> eventQueue;
    private List<SimLog> workingLog;

    ServiceManager(){
        registeredService = new HashMap<>();
        runningService = new HashMap<>();

        time = 0;
        eventQueue = new PriorityQueue<>();
        workingLog = new LinkedList<>();
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
        }
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
    public void record(SimLog simLog){
        workingLog.add(simLog);
    }
    public List<SimLog> getWorkingLog(){
        return workingLog;
    }
}
