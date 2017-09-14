package com.we.wemvp.libs;

/**
 * Created by DaniRosas on 14/9/17.
 */

public class GreenRobotEventBus implements EventBus {
    org.greenrobot.eventbus.EventBus eventBus;

    //I'll have uniquely one instance of this object in full app
    private static class SingletonHolder {
        private static final GreenRobotEventBus INSTANCE = new GreenRobotEventBus();
    }

    public static GreenRobotEventBus getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public GreenRobotEventBus(){
        eventBus = org.greenrobot.eventbus.EventBus.getDefault();
    }


    public void register(Object subscriber){
        eventBus.register(subscriber);
    }

    public void unregister(Object subscriber){
        eventBus.unregister(subscriber);
    }

    public void post(Object event){
        eventBus.post(event);
    }
}
