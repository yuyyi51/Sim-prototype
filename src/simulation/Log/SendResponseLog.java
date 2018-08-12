package simulation.Log;

public class SendResponseLog extends SimLog {
    protected String serviceName;
    protected String target;
    protected long reqID;
    static protected String format;
    public SendResponseLog(double startTime, String serviceName, String target, long reqID){
        super(startTime);
        this.serviceName = serviceName;
        this.target = target;
        this.reqID = reqID;
    }
    @Override
    public String getLog() {
        if (format == null){
            return null;
        }
        return String.format(format, startTime, serviceName, target, reqID);
    }

    static public void setFormat(String format1){
        format = format1;
    }
}
