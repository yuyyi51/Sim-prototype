package simulation;

public class Request {
    private static long sid;
    private long id;
    private String sender;
    private long previousRequestID;
    private int counter;
    private double startTime;
    Request(double startTime, String sender){
        this(startTime, sender, -1);
    }
    Request(double startTime, String sender, long previousRequestID){
        id = sid++;
        counter = 0;
        this.startTime = startTime;
        this.sender = sender;
        this.previousRequestID = previousRequestID;
    }
    long getId(){
        return id;
    }
    public long getPreviousRequestID(){
        return previousRequestID;
    }
    public String getSender(){
        return sender;
    }
    public int addCounter(){
        return ++counter;
    }
    public int getCounter(){
        return counter;
    }
    public double getStartTime(){
        return startTime;
    }
    public static void resetID(){
        sid = 0;
    }
}
