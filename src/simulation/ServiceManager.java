package simulation;

import simulation.Events.Event;

import java.util.HashMap;
import java.util.Map;

public class ServiceManager {
    private Map<String, Service> registeredService;
    private Map<String, Service> runningService;
    private Schedule schedule;

    ServiceManager(){
        registeredService = new HashMap<>();
        runningService = new HashMap<>();
        schedule = new Schedule();
    }
    public Schedule getSchedule(){
        return schedule;
    }
    public ServiceManager registerService(Service service){
        registeredService.put(service.getServiceName(), service);
        runningService.put(service.getServiceName(), service);
        service.setManager(this);
        return this;
    }
    public void registerEvent(Event event){
        schedule.registerEvent(event);
        event.setManager(this);
    }
    public double getTime(){
        return schedule.getTime();
    }
    public Service findRunningService(String name){
        return runningService.get(name);
    }
    public void start(){
        schedule.start();
    }

}
