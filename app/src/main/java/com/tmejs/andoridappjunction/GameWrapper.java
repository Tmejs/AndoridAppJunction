package com.tmejs.andoridappjunction;

import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tmejs.andoridappjunction.activities.AdminJoinGameActivity;
import com.tmejs.andoridappjunction.activities.PlayersInfoActivity;
import com.tmejs.andoridappjunction.activities.StartGameActivity;
import com.tmejs.andoridappjunction.activities.StartingGameActivity;
import com.tmejs.andoridappjunction.domain.Competition;
import com.tmejs.andoridappjunction.domain.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tmejs on 25.11.2017.
 */

public class GameWrapper {


    public static void anaylzeResponse(String response) {
        Log.e(GameWrapper.class.toString(), response);
        Log.e(GameWrapper.class.toString(), "anaylzeResponse(" + response + ")");
        ApplicationController.switchActivity(StartGameActivity.class);

    }


    public static void analyzeStartGameResponse(Object params) {
        if (params != null) {

            try {
                Gson gson = new Gson();
                //TODO tutaj pyknąć jakąś domenową klasę
                Competition comp = gson.fromJson((String) params, Competition.class);


                if (comp.compId != null) {
                    ApplicationController.APP_PARAMS.setParamValue(AppParams.COMPETITION_ID,comp.compId);
                    ApplicationController.switchActivity(AdminJoinGameActivity.class);
                    return;
                }
            } catch (JsonSyntaxException e) {
                Log.e("GameWrapper", "analyzeStartGameResponse(" + params + ")", e);
            }
        }

        ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.ERROR_WHEN_STARTING_GAME));
        ApplicationController.switchActivity(StartingGameActivity.class);

    }

    public static void analyzePlayersStatusResponse(Object params) {
        Gson gson = new Gson();
        //TODO tutaj trzeba zmienić typ domenowy
        final List<Player> players = gson.fromJson((String) params,new TypeToken<List<Player>>(){}.getType());

        if(players!=null){
            ApplicationController.switchActivity(PlayersInfoActivity.class, new ApplicationController.AfterActivityChanged() {
                @Override
                public void afterActivityChanged() {

                    //TODO uzupełnić danymi
                    ApplicationController.VIEWS_CONTROLLER.setListInTable(R.id.players_info_activity_players_table,players,new ArrayList<Pair<String, String>>(){{
                        add(new Pair<String, String>("nick","NICK"));
                        add(new Pair<String, String>("initialBillAmount","Started as"));

                    }});

                    //sprawdzenie czy kolejna runda?

                }
            });
        }

    }

    public static void analyzeStartNewRoundResponse(Object params) {
        //TODO przełązcenie na activity odpowiedzialne za daną grę.
        //TODO dorobienie odpowiedniego obiktu domenowego
    }
}
