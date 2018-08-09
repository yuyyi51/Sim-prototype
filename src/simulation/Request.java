package simulation;

public class Request {
    private static long sid;
    private long id;
    private String sender;
    private long previousRequestID;
    private int counter;
    Request(String sender){
        this(sender, -1);
    }
    Request(String sender, long previousRequestID){
        id = sid++;
        counter = 0;
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
    public static void resetID(){
        sid = 0;
    }
}
