package simulation.Events;

import Tool.Logger;
import simulation.Request;
import simulation.Service;

public class SendRequestEvent extends Event {
    protected String target;
    protected String sender;
    protected Request request;
    public SendRequestEvent(double startTime, Service sender, Service target, Request request){
        super(startTime);
        this.target = target.getServiceName();
        this.sender = sender.getServiceName();
        this.request = request;
    }
    public SendRequestEvent(double startTime, Service sender, String target, Request request){
        super(startTime);
        this.target = target;
        this.sender = sender.getServiceName();
        this.request = request;
    }
    public SendRequestEvent(double startTime, String sender, String target, Request request){
        super(startTime);
        this.target = target;
        this.sender = sender;
        this.request = request;
    }
    @Override
    public void trigger() {
        manager.findRunningService(target).receiveRequest(request);
    }
}
