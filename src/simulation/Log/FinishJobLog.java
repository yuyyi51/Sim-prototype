package simulation.Log;

public class FinishJobLog extends SimLog {
    protected String serviceName ;
    protected long reqID;
    static protected String format;
    public FinishJobLog(double startTime, String serviceName, long reqID){
        super(startTime);
        this.serviceName = serviceName;
        this.reqID = reqID;
    }
    @Override
    public String getLog() {
        if (format == null){
            return null;
        }
        return String.format(format, startTime, serviceName, reqID);
    }

    static public void setFormat(String format1){
        format = format1;
    }
}
