package com.tmejs.andoridappjunction.activities.system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.tmejs.andoridappjunction.ApplicationController;


/**
 * Created by Tmejs on 26.07.2017.
 */

public abstract class MyActivity extends Activity {


    //region Przeciążone metody z klasy Activity. Zgłaszanie do ApplicationContollera trzymającego stan aplikacji
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(this.getClass().toString(), "onCreate");
        ApplicationController.setCurrentActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(this.getClass().toString(), "onStart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.v(this.getClass().toString(), "onResume");
        ApplicationController.ActivityResumed(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().toString(), "onPause");
        ApplicationController.setIsCurrentPaused(true);
    }

    @Override
    public void startActivity(Intent intent) {
        //Informacja że jesteśmy w trakcie zmieniania activity
        ApplicationController.setActivityChanging(true);
        super.startActivity(intent);

    }

    @Override
    public void finish() {
        super.finish();
        Log.v(this.getClass().toString(), "finish");
        ApplicationController.setActivityChanging(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(this.toString(), "on stop: " + isChangingConfigurations());
        //Sprawdzenie czy nie jest obracany
        if (isChangingConfigurations()) {
            ApplicationController.onRollingEvent();
        } else {
            ApplicationController.setStoped(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(this.toString(), "onDestroy");
        ApplicationController.onDestroyEvent();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(this.toString(), "onRestart");
        ApplicationController.setIsRestarting();
    }
    //endregion

}
