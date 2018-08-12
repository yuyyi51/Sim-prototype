package simulation.Log;

public class ReceiveResponseLog extends SimLog {
    protected String serviceName;
    protected String sender;
    protected long reqID;
    protected long preReqID;
    static protected String format;
    public ReceiveResponseLog(double startTime, String serviceName, String sender, long reqID, long preReqID){
        super(startTime);
        this.serviceName = serviceName;
        this.sender = sender;
        this.reqID = reqID;
        this.preReqID = preReqID;
    }
    @Override
    public String getLog() {
        if (format == null){
            return null;
        }
        return String.format(format, startTime, serviceName, sender, reqID, preReqID);
    }

    static public void setFormat(String format1){
        format = format1;
    }
}
