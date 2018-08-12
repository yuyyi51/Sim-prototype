package simulation;

import simulation.Events.FinishJobEvent;
import simulation.Events.SendRequestEvent;
import simulation.Events.SendResponseEvent;
import simulation.Events.StartJobEvent;
import simulation.Log.*;

import java.util.*;

public class Service {
    private String serviceName;
    private double processTime;
    private Queue<Request> jobQueue;
    private Map<Long, Request> requests;
    private List<String> dependence;
    private ServiceManager manager;
    private List<SimLog> workingLog;

    public Service(String serviceName, double processTime){
        this.serviceName = serviceName;
        this.processTime = processTime;
        jobQueue = new LinkedList<>();
        requests = new HashMap<>();
        dependence = new LinkedList<>();
        workingLog = new LinkedList<>();
    }

    private void addJob(Request job){
        jobQueue.add(job);
    }
    private void registerStartJobEvent(){
        StartJobEvent event = new StartJobEvent(manager.getTime(), this);
        manager.registerEvent(event);
    }
    private void registerFinishJobEvent(){
        FinishJobEvent event = new FinishJobEvent(manager.getTime() + processTime, this);
        manager.registerEvent(event);
    }
    public void startJob(){
        Request request = jobQueue.peek();
        record(new StartJobLog(manager.getTime(), serviceName, request.getId()));
        registerFinishJobEvent();
    }
    public void finishJob(){
        Request request = jobQueue.poll();
        record(new FinishJobLog(manager.getTime(), serviceName, request.getId()));
        if (dependence.isEmpty()){
            //直接向上级调用发送响应
            sendResponseToService(request.getSender(), request.getId(), request.getPreviousRequestID());
        }
        else {
            //等待依赖服务完成后，向上级调用发送响应
            for(String str: dependence){
                //向依赖服务发送请求
                sendRequestToService(str, request.getId());
            }
        }
        if (!isIdle()){
            registerStartJobEvent();
        }
    }
    private void sendRequestToService(String target, long previousReqID){
        Request request = new Request(manager.getTime(), serviceName, previousReqID);
        SendRequestEvent event = new SendRequestEvent(manager.getTime(),serviceName, target, request);
        manager.registerEvent(event);
        record(new SendRequestLog(manager.getTime(), serviceName, target, request.getId()));
    }
    private void sendResponseToService(String target, long reqID, long previousReqID){
        Response response;
        if (previousReqID >= 0){
            response = new Response(serviceName, reqID, previousReqID);
        }
        else {
            //用户终端发出的请求
            response = new Response(serviceName, reqID, -1);
        }
        SendResponseEvent event = new SendResponseEvent(manager.getTime(), serviceName, target, response);
        manager.registerEvent(event);
        record(new SendResponseLog(manager.getTime(), serviceName, target, reqID));
    }

    public boolean isIdle(){
        return jobQueue.isEmpty();
    }
    public String getServiceName(){
        return serviceName;
    }
    public void setManager(ServiceManager manager){
        this.manager = manager;
    }
    public Service addDependence(String name){
        dependence.add(name);
        return this;
    }
    public void receiveRequest(Request request){
        record(new ReceiveRequestLog(manager.getTime(), serviceName, request.getSender(), request.getId()));
        requests.put(request.getId(), request);
        if (isIdle()){
            registerStartJobEvent();
        }
        jobQueue.add(request);
    }
    public void receiveResponse(Response response){
        Request request = requests.get(response.getPreReqID());
        record(new ReceiveResponseLog(manager.getTime(), serviceName, response.getSender(), response.getReqID(), response.getPreReqID()));
        if (request != null && request.addCounter() >= dependence.size()){
            //该服务不为终端并且该请求已完成
            record(new FinishRequestLog(manager.getTime(), serviceName, request.getId(), manager.getTime()-request.getStartTime()));
            sendResponseToService(request.getSender(), request.getId(), request.getPreviousRequestID());
            requests.remove(response.getPreReqID());
        }
    }
    public void sendRequestAsEndpoint(double startTime, String target){
        Request request = new Request(startTime, serviceName);
        manager.registerEvent(new SendRequestEvent(startTime, serviceName, target, request));
        record(new SendRequestLog(startTime, serviceName, target, request.getId()));
    }
    public List<SimLog> getWorkingLog(){
        return workingLog;
    }
    protected void record(SimLog simLog){
        workingLog.add(simLog);
        manager.record(simLog);
    }
}
