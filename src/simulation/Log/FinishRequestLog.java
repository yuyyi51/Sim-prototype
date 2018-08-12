package simulation.Log;

public class FinishRequestLog extends SimLog {
    protected String serviceName;
    protected long reqID;
    protected double processTime;
    static protected String format;
    public FinishRequestLog(double startTime, String serviceName, long reqID, double processTime){
        super(startTime);
        this.serviceName = serviceName;
        this.reqID = reqID;
        this.processTime = processTime;
    }
    @Override
    public String getLog() {
        if (format == null){
            return null;
        }
        return String.format(format, startTime, serviceName, reqID, processTime);
    }

    static public void setFormat(String format1){
        format = format1;
    }
}
