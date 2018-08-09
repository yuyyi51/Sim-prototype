package simulation;

import Tool.Logger;
import simulation.Events.SendRequestEvent;


public class Main {

    public static void main(String[] args) {
	// write your code here
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
        Request request = new Request("user");
        manager.registerEvent(new SendRequestEvent(0,"user","s0",request));

        manager.start();
    }
}
