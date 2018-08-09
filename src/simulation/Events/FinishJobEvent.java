package simulation.Events;

import Tool.Logger;
import simulation.Service;

public class FinishJobEvent extends Event {
    protected Service service;

    public FinishJobEvent(double startTime, Service service){
        super(startTime);
        this.service = service;
    }

    @Override
    public void trigger() {
        service.finishJob();
    }
}
