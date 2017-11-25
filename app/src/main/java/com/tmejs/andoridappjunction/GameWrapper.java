package com.tmejs.andoridappjunction;

import android.util.Log;

import com.google.gson.Gson;
import com.tmejs.andoridappjunction.activities.StartGameActivity;
import com.tmejs.andoridappjunction.activities.StartingGameActivity;

/**
 * Created by Tmejs on 25.11.2017.
 */

public class GameWrapper {


    public static void anaylzeResponse(String response){
        Log.e(GameWrapper.class.toString(),response);
        Log.e(GameWrapper.class.toString(),"anaylzeResponse("+response+")");
        ApplicationController.switchActivity(StartGameActivity.class);

    }


    public static void analyzeStartGameResponse(Object params) {
        if(params!=null){
            Gson gson= new Gson();
            //TODO tutaj pyknąć jakąś domenową klasę

            //TODO przejscie do okna dołączenia do gry

        }

        ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.ERROR_WHEN_STARTING_GAME));
        ApplicationController.switchActivity(StartingGameActivity.class);

    }
}
