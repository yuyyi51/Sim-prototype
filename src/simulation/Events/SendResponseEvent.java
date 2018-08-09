package simulation.Events;

import simulation.Response;

public class SendResponseEvent extends Event{
    protected String sender;
    protected String target;
    protected Response response;
    public SendResponseEvent(double startTime, String sender, String target, Response response){
        super(startTime);
        this.sender = sender;
        this.target = target;
        this.response = response;
    }
    @Override
    public void trigger() {
        manager.findRunningService(target).receiveResponse(response);
    }
}
