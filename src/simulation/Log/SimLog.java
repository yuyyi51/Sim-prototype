package simulation.Log;

public abstract class SimLog {
    protected double startTime;
    public SimLog(double startTime){
        this.startTime = startTime;
    }
    public abstract String getLog();
}
