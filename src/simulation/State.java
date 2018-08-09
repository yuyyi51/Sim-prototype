package simulation;

public class State implements Comparable<State> {
    protected String service;
    protected double time;
    protected String state;
    @Override
    public int compareTo(State o) {
        return Double.compare(time, o.time);
    }
    public State(double time, String service,  String state){
        this.time = time;
        this.service = service;
        this.state = state;
    }
    public String getState(){
        return state;
    }

    public double getTime() {
        return time;
    }
}
