package com.tmejs.andoridappjunction;

import android.util.Log;

import com.tmejs.andoridappjunction.activities.StartGameActivity;

/**
 * Created by Tmejs on 25.11.2017.
 */

public class GameWrapper {


    public static void anaylzeResponse(String response){
        Log.e(GameWrapper.class.toString(),response);
        Log.e(GameWrapper.class.toString(),"anaylzeResponse("+response+")");
        ApplicationController.switchActivity(StartGameActivity.class);

    }



}
