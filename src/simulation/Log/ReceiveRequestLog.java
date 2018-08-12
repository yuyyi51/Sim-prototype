package simulation.Log;

public class ReceiveRequestLog extends SimLog {
    protected String serviceName;
    protected String sender;
    protected long reqID;
    static protected String format;
    public ReceiveRequestLog(double startTime, String serviceName, String sender, long reqID){
        super(startTime);
        this.serviceName = serviceName;
        this.sender = sender;
        this.reqID = reqID;
    }
    @Override
    public String getLog() {
        if (format == null){
            return null;
        }
        return String.format(format, startTime, serviceName, sender, reqID);
    }

    static public void setFormat(String format1){
        format = format1;
    }
}
