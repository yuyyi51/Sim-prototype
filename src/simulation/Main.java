package simulation;

import simulation.Log.*;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Main {

    public static ServiceManager test1(){
        ServiceManager manager = new ServiceManager();
        Service s1 = new Service("s1", 500);
        Service s2 = new Service("s2", 1000);
        Service database = new Service("database", 300);
        s1.addDependence("s2");
        s2.addDependence("database");
        Service user = new Service("user", 0);
        manager.registerService(s1)
                .registerService(s2)
                .registerService(database)
                .registerService(user);
        user.sendRequestAsEndpoint(0, "s1");
        manager.start();
        return manager;
    }

    public static ServiceManager test2(){
        ServiceManager manager = new ServiceManager();
        Service s1 = new Service("s1", 500);
        Service s2 = new Service("s2", 1000);
        Service s3 = new Service("s3", 300);
        s1.addDependence("s2");
        s1.addDependence("s3");
        Service user = new Service("user", 0);
        manager.registerService(s1)
                .registerService(s2)
                .registerService(s3)
                .registerService(user);
        user.sendRequestAsEndpoint(0, "s1");
        manager.start();
        Request.resetID();
        return manager;
    }

    public static ServiceManager test3(){
        ServiceManager manager = new ServiceManager();
        Service s0 = new Service("s0", 100);
        Service s1a = new Service("s1a", 500);
        Service s1b = new Service("s1b", 1000);
        Service s2a = new Service("s2a", 1200);
        Service s2b = new Service("s2b", 5000);
        Service s2c = new Service("s2c", 1500);
        Service database = new Service("database", 500);
        s0.addDependence("s1a").addDependence("s1b");
        s1a.addDependence("s2a").addDependence("s2b");
        s1b.addDependence("s2b").addDependence("s2c");
        s2a.addDependence("database");
        s2b.addDependence("database");
        s2c.addDependence("database");
        Service user = new Service("user",0);
        manager.registerService(s0)
                .registerService(s1a)
                .registerService(s1b)
                .registerService(s2a)
                .registerService(s2b)
                .registerService(s2c)
                .registerService(database)
                .registerService(user);
        user.sendRequestAsEndpoint(0, "s0");

        manager.start();
        Request.resetID();
        return manager;
    }
    public static ServiceManager complexTest(int layer){
        //金字塔形结构
        //该结构过于复杂，测试效果不好
        Random random = new Random();
        ServiceManager manager = new ServiceManager();
        for(int i = 1 ; i < layer ; ++i){
            for (int j = 1 ; j <= i ; ++j){
                String name = String.format("s-%d-%d",i,j);
                Service s = new Service(name, (int)(random.nextDouble()*100.0) + 50);
                s.addDependence(String.format("s-%d-%d",i+1,j));
                s.addDependence(String.format("s-%d-%d",i+1,j+1));
                manager.registerService(s);
            }
        }
        Service database = new Service("database", 50);
        manager.registerService(database);
        for (int j = 1 ; j <= layer ; ++j){
            String name = String.format("s-%d-%d",layer,j);
            Service s = new Service(name, (int)(random.nextDouble()*100.0) + 50);
            s.addDependence("database");
            manager.registerService(s);
        }
        Service user = new Service("user", 0);
        manager.registerService(user);
        user.sendRequestAsEndpoint(0, "s-1-1");
        manager.start();
        return manager;
    }
    public static ServiceManager flatTest(int count){
        Random random = new Random();
        ServiceManager manager = new ServiceManager();
        Service s = new Service("s", 100);
        manager.registerService(s);
        Service d = new Service("database", 0.01);
        manager.registerService(d);
        for (int i = 0 ; i < count ; ++i){
            String name = String.format("s-%d", i);
            Service service = new Service(name, (int)(random.nextDouble()*100.0) + 50);
            service.addDependence("database");
            manager.registerService(service);
            s.addDependence(name);
        }
        Service u = new Service("user", 0);
        manager.registerService(u);
        u.sendRequestAsEndpoint(0, "s");
        manager.start();
        return manager ;
    }
    public static void main(String[] args) {
        StartJobLog.setFormat("当前时间：%.2f，服务 %s 开始执行请求 %d");
        FinishJobLog.setFormat("当前时间：%.2f，服务 %s 结束执行请求 %d");
        SendRequestLog.setFormat("当前时间：%.2f，服务 %s 向服务 %s 发送请求，请求ID为 %d");
        ReceiveRequestLog.setFormat("当前时间：%.2f，服务 %s 收到来自 %s 的请求，请求ID为 %d");
        SendResponseLog.setFormat("当前时间：%.2f，服务 %s 向服务 %s 发送对请求 %d 的响应");
        ReceiveResponseLog.setFormat("当前时间：%.2f，服务 %s 收到来自 %s 对请求 %d 的响应，对应的前置请求ID为 %d");
        FinishRequestLog.setFormat("当前时间：%.2f，服务 %s 对请求 %d 的操作已完成，用时 %.2f");

        Scanner scanner = new Scanner(System.in);
        Date t1 = new Date();
        var manager = test3();
        float time = new Date().getTime() - t1.getTime();
        time /= 1000.0;
        System.out.println(String.format("运行用时%.4f秒，请输入回车以输出日志", time));
        scanner.nextLine();
        //获取总体执行日志
        var log = manager.getWorkingLog();
        for (SimLog s:log){
            System.out.println(s.getLog());
        }
        System.out.println();
        //获取单一服务执行日志
        var log2 = manager.findRegisteredService("s1a").getWorkingLog();
        for (SimLog s:log2){
            System.out.println(s.getLog());
        }

    }
}
