package com.tmejs.andoridappjunction;

import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tmejs.andoridappjunction.activities.AdminJoinGameActivity;
import com.tmejs.andoridappjunction.activities.MainActivity;
import com.tmejs.andoridappjunction.activities.PlayersInfoActivity;
import com.tmejs.andoridappjunction.activities.StartGameActivity;
import com.tmejs.andoridappjunction.activities.StartingGameActivity;
import com.tmejs.andoridappjunction.domain.Competition;
import com.tmejs.andoridappjunction.domain.Player;
import com.tmejs.andoridappjunction.domain.WaitingForOtherPLayersResponse;

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
                Competition comp = gson.fromJson((String) params, Competition.class);
                if (comp.competitionId != null) {
                    Log.e("analyzeStartGameResponse", "playerId(" + comp.currentPlayer.id + ")");
                    ApplicationController.APP_PARAMS.setParamValue(AppParams.COMPETITION_ID, comp.competitionId);
                    ApplicationController.APP_PARAMS.setParamValue(AppParams.PLAYER_ID, comp.currentPlayer.id);
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

    private static List<Player> getList() {
        ArrayList<Player> list = new ArrayList<>();
        Player newPlayer = new Player();

        newPlayer.name = "Test1";
        newPlayer.initialBillAmount = new Long(100);

        Player newPlayer2 = new Player();

        newPlayer2.name = "Test2";
        newPlayer2.initialBillAmount = new Long(200);

        list.add(newPlayer);
        list.add(newPlayer2);
        return list;
    }

    public static void analyzePlayersStatusResponse(Object params) {
        Gson gson = new Gson();

        final WaitingForOtherPLayersResponse response = gson.fromJson((String) params, WaitingForOtherPLayersResponse.class);
        if (response.responsePlayers != null) {
            ApplicationController.switchActivity(PlayersInfoActivity.class, new ApplicationController.AfterActivityChanged() {
                @Override
                public void afterActivityChanged() {
                    //Ustawieie listy  graczy
                    ApplicationController.VIEWS_CONTROLLER.setListInTable(R.id.players_info_activity_players_table, response.responsePlayers, new ArrayList<Pair<String, String>>() {{
                        add(new Pair<String, String>("name", "NICK"));
                        add(new Pair<String, String>("initialBillAmount", "To Pay"));
                        add(new Pair<String, String>("initialPercentage", "Started as"));
                        add(new Pair<String, String>("finalPercentage", "Current"));
                    }});

                    //Czy kolejna runda?
                    Boolean nextRound = response.isNextRound;
                    if (nextRound) {
                        ApplicationController.VIEWS_CONTROLLER.setText(R.id.players_info_activity_text_view, "RESULTS");
                        ApplicationController.VIEWS_CONTROLLER.setText(R.id.players_info_continue_button, "NEXT ROUND");
                        ApplicationController.VIEWS_CONTROLLER.setOnClickListener(R.id.players_info_continue_button, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ApplicationController.startNewRound();
                            }
                        });
                    } else {
                        ApplicationController.VIEWS_CONTROLLER.setText(R.id.players_info_activity_text_view, "GAME ENDED!");
                        ApplicationController.VIEWS_CONTROLLER.setText(R.id.players_info_continue_button, "BACK");
                        ApplicationController.VIEWS_CONTROLLER.setOnClickListener(R.id.players_info_continue_button, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ApplicationController.switchActivity(MainActivity.class);
                            }
                        });
                    }
                }
            });
        }
    }

    public static void analyzeStartNewRoundResponse(Object params) {
        //TODO przełązcenie na activity odpowiedzialne za daną grę.
        //TODO dorobienie odpowiedniego obiktu domenowego
    }
}
