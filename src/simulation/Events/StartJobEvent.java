package simulation.Events;

import Tool.Logger;
import simulation.Service;

public class StartJobEvent extends Event {
    protected Service service;

    public StartJobEvent(double startTime, Service service){
        super(startTime);
        this.service = service;
    }

    @Override
    public void trigger() {
        service.startJob();
    }
}
