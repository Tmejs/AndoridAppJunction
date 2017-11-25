package com.tmejs.andoridappjunction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tmejs.andoridappjunction.activities.StartingGameActivity;
import com.tmejs.andoridappjunction.activities.system.MyActivity;
import com.tmejs.andoridappjunction.activities.system.WaitingActivity;
import com.tmejs.andoridappjunction.domain.Competition;
import com.tmejs.andoridappjunction.domain.Player;
import com.tmejs.andoridappjunction.domain.StartGame;
import com.tmejs.andoridappjunction.usables.MyAsyncTask;
import com.tmejs.andoridappjunction.utils.TCPUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tmejs on 25.11.2017.
 */

public class ApplicationController {

    //region Statyczne zmienne ważne w aplikacji
    public static AppParams APP_PARAMS;
    private static ApplicationController appController;
    public static AsyncHelper ASYNC_HELPER;
    public static ViewsController VIEWS_CONTROLLER = ViewsController.getInstance();


    public ApplicationController getInstance() {
        if (appController == null) {
            appController = new ApplicationController();
        }
        return appController;
    }

    //region Eventy aplikacji
    //Event dający możliwość reagowania na stan aplikacji
    private static OnRollingEvent onRollingEvent;
    private static OnMinimalizeEvent onMinimalizeEvent;
    private static OnMaxymalizeEvent onMaxymalizeEvent;
    private static OnDestroyEvent onDestroyEvent;


    private static AfterActivityChanged afterActivityChanged;

    public static void closeApp() {
        System.exit(-1);
    }


    public static String getRNameByID(Integer viewId) {
        if (viewId == 0xffffffff) return "";
        return getCurrentActivity().getResources().getResourceEntryName(viewId);
    }

    public static void startNewGame(final StartGame startGameObject) {
        switchAppToWaitingMode();

        ApplicationController.ASYNC_HELPER.executeAsync(new MyAsyncTask(new MyAsyncTask.RequestEvent() {
            @Override
            public Object request() {
                Gson gson = new Gson();
                String jsonRepresentation = gson.toJson(startGameObject);
//                Log.e("jsoning", jsonRepresentation);
                try {
                    return TCPUtil.sendRequest(jsonRepresentation);
                } catch (IOException e) {
                    return "";
                }
            }

            @Override
            public void postRequest(Object params) {
                //TODO sprawdzić czy poprawnie zwrocił id gry.
//                if()
                GameWrapper.analyzeStartGameResponse(params);

            }
        }));

    }

    public interface AfterActivityChanged {
        void afterActivityChanged();
    }

    public static void setStoped(MyActivity activity) {
        //jeśli aktualne Activity paused to zminimalizowany ekran
        //
        Log.i("ApplicationControler", "setStoped");
        if (isCurrentPaused && !isActivityChanging) {
            setIsMinimalized(true);
            onMinimalizeEvent();
        } else {
            setLastActivity(activity);
        }
    }

    public Boolean isActivitychanging() {
        return isActivityChanging;
    }

    public interface OnRollingEvent {
        /**
         * Interakcja na obrót ekranu
         */
        void onRollingEvent();

    }

    public static void runAsyncRequest(MyAsyncTask request) {
        ASYNC_HELPER.executeAsync(request);
    }


    public static String getStringFromResources(Integer stringId) {
        return getCurrentActivity().getResources().getString(stringId);
    }


    public interface OnMinimalizeEvent {
        /**
         * Interakcja na minimalizację aplikacji
         */
        void onMinimalizeEvent();
    }


    public interface OnMaxymalizeEvent {
        /**
         * Interakcja na minimalizację aplikacji
         */
        void onMaxymalizeEvent();
    }

    public interface OnDestroyEvent {

        /**
         * Interacja przy zamykaniu aplikacji
         */
        void onDestroyEvent();
    }


    //endregion

    //Context aplikacji
    //Ostatnie activity
    private static MyActivity lastActivity;
    //Aktualne activity
    private static MyActivity currentActivity;

    //Czy aktualny thread zatrzymany
    private static Boolean isCurrentPaused = false;

    //Informacja czy jesteśmy w trakcie zmieniania activity
    private static Boolean isActivityChanging = false;

    //Informacja czy aplikacja została zminimalizowanaf
    private static Boolean isMinimalized = false;
    //endregion


    //region Gettery i Settery klasy. Najlepiej nie dotykać bo się może posypać.

    /**
     * Pobranie aktualnie wyświetlanego activity
     *
     * @return
     */
    public static MyActivity getCurrentActivity() {
        return currentActivity;
    }


    public static MyActivity getLastActivity() {
        return lastActivity;
    }


    public static Boolean isMinimalized() {
        return isMinimalized;
    }

    public static void setCurrentActivity(MyActivity activity) {
        lastActivity = currentActivity;
        currentActivity = activity;
    }

    public static void setLastActivity(MyActivity la) {
        lastActivity = la;
    }

    public static void setIsCurrentPaused(Boolean isPaused) {
        Log.i("ApplicationControler", "setIsCurrentPaused -> " + isPaused);
        //UStawiamy że activity jest spauzowane
        isCurrentPaused = isPaused;
    }

    public static void setActivityChanging(Boolean isChanging) {
        Log.i("ApplicationControler", "setActivityChanging");
        isActivityChanging = isChanging;
    }

    public static void onRollingEvent() {
        Log.i("ApplicationControler", "onRollingEvent");
        isCurrentPaused = false;

        //Wywołanie eventu onRolling
        if (onRollingEvent != null) {
            onRollingEvent.onRollingEvent();
        }
    }

    public static void runOnUiThread(Runnable runnable) {
        ASYNC_HELPER.executeOnUi(runnable);
    }

    public static void setIsMinimalized(Boolean isMin) {
        Log.v("ApplicationControler", "setIsMinimalized  " + isMin);
        isMinimalized = isMin;
    }


    public static void ActivityResumed(MyActivity activity) {
        Log.v("ApplicationControler", "ActivityResumed  ");
        setIsMinimalized(false);
        setIsCurrentPaused(false);
        setCurrentActivity(activity);


        if (isActivityChanging) {
            //wywołanie eventu po zmianie activity
            if (afterActivityChanged != null) {
                afterActivityChanged.afterActivityChanged();
                afterActivityChanged = null;
            }
        }

        setActivityChanging(false);

    }

    public static void setIsRestarting() {
        Log.v("ApplicationControler", "setIsRestarting  ");
        if (isMinimalized) {
            onMaxymalizeEvent();
            setIsMinimalized(false);
        }
    }

    public static void onDestroyEvent() {
        Log.v("ApplicationControler", "onDestroyEvent");
        if (isMinimalized) onAppDestroyEvent();
    }


    private static void onAppDestroyEvent() {
        Log.e("ApplicationControler", "onAppDestroyEvent");
        if (onDestroyEvent != null) {
            onDestroyEvent.onDestroyEvent();
        }
    }

    private static void onMinimalizeEvent() {
        Log.e("ApplicationControler", "onMinimalizeEvent");

        //Wywołanie eventu onMinimalize
        if (onMinimalizeEvent != null) {
            onMinimalizeEvent.onMinimalizeEvent();
        }
    }

    private static void onMaxymalizeEvent() {
        Log.e("ApplicationControler", "onMaxymalizeEvent");

        //Wywołanie eventu onMaxymalize
        if (onMaxymalizeEvent != null) {
            onMaxymalizeEvent.onMaxymalizeEvent();
        }
    }

    public static void setOnRollingEvent(OnRollingEvent onRollingEvent) {
        ApplicationController.onRollingEvent = onRollingEvent;
    }


    public static void showNews(final String txt) {
        getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getCurrentActivity(), txt, Toast.LENGTH_LONG).show();
            }
        });

    }


    public static void setOnMinimalizeEvent(OnMinimalizeEvent onMinimalizeEvent) {
        ApplicationController.onMinimalizeEvent = onMinimalizeEvent;
    }

    public static void setOnMaxymalizeEvent(OnMaxymalizeEvent onMaxymalizeEvent) {
        ApplicationController.onMaxymalizeEvent = onMaxymalizeEvent;
    }

    public static void setOnDestroyEvent(OnDestroyEvent onDestroyEvent) {
        ApplicationController.onDestroyEvent = onDestroyEvent;
    }

    /**
     * Zmiana activity
     *
     * @param activityClass Klasa docelowego acitivty
     * @return
     */
    public static Boolean switchActivity(Class activityClass) {
        Log.i(ApplicationController.class.toString(), "switchActivity(" + activityClass.toString() + ")");
        Intent newIntent = new Intent(getCurrentActivity(), activityClass);
        getCurrentActivity().startActivity(newIntent);
        return true;
    }


    /**
     * Zmiana activity z evetem wywoływanym po pojawieniu się activity na ekranie
     *
     * @param activityClass
     * @param aac
     * @return
     */
    public static Boolean switchActivity(Class activityClass, AfterActivityChanged aac) {
        Log.i(ApplicationController.class.toString(), "switchActivity(" + activityClass.toString() + ") with event");
        afterActivityChanged = aac;
        Intent newIntent = new Intent(getCurrentActivity(), activityClass);
        getCurrentActivity().startActivity(newIntent);
        return true;
    }


    //endregion


    //region Inicjalizacja klas w aplikacji

    /**
     * Inicjalizacja klas do statycznych zmiennych
     *
     * @param startActivity
     */
    public static void init(MyActivity startActivity) {
        Log.e("AppControl", "init()");
        initAppParams(startActivity);
        Log.e("AppControl", "AppParams Initialized");
        intitAsyncHelper(startActivity);


    }


    public static void intitAsyncHelper(MyActivity activity) {
        ASYNC_HELPER = AsyncHelper.getInstance(activity);
    }



    /**
     * Inicjalizacja parametrów aplikacji na podstawie SharedPreferences podanego Activity
     *
     * @param startActivity Activity którego shared preferences będziemy pobierać.
     */
    private static void initAppParams(MyActivity startActivity) {
        if (ApplicationController.APP_PARAMS == null)
            ApplicationController.APP_PARAMS = AppParams.getInstance(startActivity.getSharedPreferences(AppParams.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE));
    }

    private static void switchAppToWaitingMode() {
        ApplicationController.switchActivity(WaitingActivity.class);
    }


    public static void startTestGame() {
        switchAppToWaitingMode();

        final Competition comp = new Competition();
        comp.admin = new Player();
        comp.admin.avatarId = Long.getLong("1");
        comp.admin.name = "Mati";
        comp.numberOfPlayers = 5;


        ApplicationController.ASYNC_HELPER.executeAsync(new MyAsyncTask(new MyAsyncTask.RequestEvent<String>() {
            @Override
            public String request() {
                Gson gson = new Gson();
                String jsonRepresentation = gson.toJson(comp);
                Log.e("jsoning", jsonRepresentation);
                try {
                    return TCPUtil.sendRequest(jsonRepresentation);
                } catch (IOException e) {
                    return "";
                }
            }

            @Override
            public void postRequest(String params) {
                GameWrapper.anaylzeResponse(params);
            }
        }));


    }

    public void sendGameResponse(String response, Integer time) {

    }


    public void sendGameResponse(Integer responseId, Integer time) {


    }

    public static void joinUserToGame(Competition comp) {
        switchAppToWaitingMode();

        ArrayList<Pair<String, String>> list = new ArrayList<>();

        String response;
        try {
            Gson gson = new Gson();
            String jsonRepresentation = gson.toJson(comp);
            Log.e("jsoning", jsonRepresentation);
            response = TCPUtil.sendRequest(jsonRepresentation);
        } catch (IOException e) {
            //Tutaj jakas lipa, chyba najlepiej zakmnąć apkę.

        }


    }


    public void startNewGame(Integer usersCount, Integer valueToPay, Boolean doEqualSplit) {

    }


}
