package simulation;

public class Response {
    private long reqID;
    private long preReqID;
    private String sender;
    Response(String sender, long reqID, long preReqID){
        this.sender = sender;
        this.reqID = reqID;
        this.preReqID = preReqID;
    }
    public String getSender(){
        return sender;
    }

    public long getReqID() {
        return reqID;
    }

    public long getPreReqID() {
        return preReqID;
    }
}
