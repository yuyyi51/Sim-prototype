package simulation;

import Tool.Logger;
import simulation.Events.FinishJobEvent;
import simulation.Events.SendRequestEvent;
import simulation.Events.SendResponseEvent;
import simulation.Events.StartJobEvent;

import java.util.*;

public class Service {
    private String serviceName;
    private double processTime;
    private Queue<Request> jobQueue;
    private Map<Long, Request> requests;
    private List<String> dependence;
    private ServiceManager manager;
    private List<State> workingLog;

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
        Logger.Log("当前时间：%.2f，服务 %s 开始执行请求 %d", manager.getTime(), serviceName, request.getId());
        workingLog.add(new State(manager.getTime(), serviceName, "start"));
        registerFinishJobEvent();
    }
    public void finishJob(){
        Request request = jobQueue.poll();
        Logger.Log("当前时间：%.2f，服务 %s 结束执行请求 %d", manager.getTime(), serviceName, request.getId());
        workingLog.add(new State(manager.getTime(), serviceName, "stop"));
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
        Logger.Log("当前时间：%.2f，服务 %s 向服务 %s 发送请求，请求ID为 %d",manager.getTime(), serviceName, target, request.getId());
    }
    private void sendResponseToService(String target, long reqID, long previousReqID){
        Response response;
        if (previousReqID >= 0){
            response = new Response(serviceName, previousReqID);
        }
        else {
            //用户终端发出的请求
            response = new Response(serviceName, reqID);
        }
        SendResponseEvent event = new SendResponseEvent(manager.getTime(), serviceName, target, response);
        manager.registerEvent(event);
        Logger.Log("当前时间：%.2f，服务 %s 向服务 %s 发送对请求 %d 的响应", manager.getTime(), serviceName, target, reqID);
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
        Logger.Log("当前时间：%.2f，服务 %s 收到来自 %s 的请求，请求ID为 %d",manager.getTime(), serviceName, request.getSender(), request.getId());
        requests.put(request.getId(), request);
        if (isIdle()){
            registerStartJobEvent();
        }
        jobQueue.add(request);
    }
    public void receiveResponse(Response response){
        Request request = requests.get(response.getReqID());
        if (request != null){
            Logger.Log("当前时间：%.2f，服务 %s 收到来自 %s 的响应，对应的前置请求ID为 %d"
                    ,manager.getTime()
                    ,serviceName
                    ,response.getSender()
                    ,response.getReqID());
            if (request.addCounter() >= dependence.size()){
                //该请求已完成
                Logger.Log("当前时间：%.2f，服务 %s 对请求 %d 的操作已完成，用时 %.2f", manager.getTime(), serviceName, request.getId(), manager.getTime() - request.getStartTime());
                sendResponseToService(request.getSender(), request.getId(), request.getPreviousRequestID());
                requests.remove(response.getReqID());
            }
        }
        else {
            Logger.Log("当前时间：%.2f，终端 %s 收到来自 %s 的响应，对应的请求ID为 %d", manager.getTime(), serviceName, response.getSender(), response.getReqID());
        }
    }
    public void sendRequestAsEndpoint(double startTime, String target){
        Request request = new Request(startTime, serviceName);
        manager.registerEvent(new SendRequestEvent(startTime, serviceName, target, request));
    }
    public List<State> getWorkingLog(){
        return workingLog;
    }
}
