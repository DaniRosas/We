package com.we.wemvp.libs;

/**
 * Created by DaniRosas on 14/9/17.
 */

public interface EventBus {
    //Communicate events to a data bus

    //Register to the bus through an object
    void register(Object subscriber);

    void unregister(Object subscriber);

    void post(Object event);
}
