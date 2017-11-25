package com.tmejs.andoridappjunction;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;

import com.tmejs.andoridappjunction.activities.system.MyActivity;
import com.tmejs.andoridappjunction.usables.MyAsyncTask;

/**
 * Created by Tmejs on 27.07.2017.
 */

public class AsyncHelper {


    //Singleton
    private static AsyncHelper instance;

    private static Handler handler;

    public static AsyncHelper getInstance(MyActivity activity) {
        if (instance == null) {
            instance = new AsyncHelper(activity);
        }
        return instance;
    }

    private AsyncHelper(MyActivity activity) {
        handler = new Handler(activity.getApplicationContext().getMainLooper());
    }

    //
    //Aktywatory AsyncTasków
    private synchronized <P, T extends android.os.AsyncTask> void execute(T task) {
        execute(task, (P[]) null);
    }

    @SuppressLint("NewApi")
    //
    public <P, T extends android.os.AsyncTask> void execute(T task, P... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR, params);

        } else {
            task.execute(params);
        }
    }
//
//    public void sendResponse(final CommunicationStreamParams cs) {
//        executeAsync(new MyAsyncTask(new MyAsyncTask.RequestEvent() {
//            @Override
//            public Object request() {
//                try {
//                    //Podmieniamy CommunicationStream z tym ze zwrotki
//                    ApplicationController.REQ_SYNCHRONIZER.sendNewRequest(new CommunicationStream(cs), AwsController.getLogicServletConnection(), null);
//                } catch (Exception e) {
//                    Log.e("AsyncHelper", "Błąd przy wysyłaniu requestu o ID = " + cs.getHeader().getId());
//                }
//                return null;
//            }
//
//            @Override
//            public void postRequest(Object params) {
//
//            }
//        }));
//    }


    public void executeOnUi(Runnable runnable) {
        if (handler != null) handler.post(runnable);
    }


    public void executeAsync(final MyAsyncTask request) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Uruchamiamy request

                final MyAsyncTask.RequestEvent[] event = new MyAsyncTask.RequestEvent[1];
                event[0] = request.getRequestEvents();

                final Object[] map = new Object[1];
                if (event[0] != null) {
                    map[0] = event[0].request();
                    executeOnUi(new Runnable() {
                        @Override
                        public void run() {
                            event[0].postRequest(map[0]);
                        }
                    });
                }
            }
        }).start();
    }

}
