package com.tmejs.andoridappjunction.usables;

/**
 * Created by Tmejs on 17.08.2017.
 */


/**
 * @author Mateusz Rząd (mateusz.rzad@gmail.com)
 */
public class MyAsyncTask {

    private final RequestEvent reqEvent;


    public interface RequestEvent<T> {
        T request();


        void postRequest(T params);
    }


    public MyAsyncTask(RequestEvent event) {
        reqEvent = event;
    }


    public RequestEvent getRequestEvents() {
        return reqEvent;
    }

}
