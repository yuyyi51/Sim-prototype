package simulation;

public class Response {
    private long reqID;
    private String sender;
    Response(String sender, long reqID){
        this.sender = sender;
        this.reqID = reqID;
    }
    public String getSender(){
        return sender;
    }

    public long getReqID() {
        return reqID;
    }
}
