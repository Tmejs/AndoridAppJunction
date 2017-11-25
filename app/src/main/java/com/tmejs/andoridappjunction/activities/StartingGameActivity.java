package com.tmejs.andoridappjunction.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.tmejs.andoridappjunction.ApplicationController;
import com.tmejs.andoridappjunction.R;
import com.tmejs.andoridappjunction.activities.system.MyActivity;
import com.tmejs.andoridappjunction.domain.Player;
import com.tmejs.andoridappjunction.domain.StartGame;

public class StartingGameActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_game);

        ApplicationController.VIEWS_CONTROLLER.setOnClickListener(R.id.starting_game_start_game_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });

        ApplicationController.VIEWS_CONTROLLER.setCheckedStateChangedEvent(R.id.starting_game_equal_spli_switch, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setIsEqualSplit(isChecked);
            }
        });
        ApplicationController.VIEWS_CONTROLLER.setCheckedStatus(R.id.starting_game_equal_spli_switch,true);

    }

    private void setIsEqualSplit(Boolean isEqualSplit){
        ApplicationController.VIEWS_CONTROLLER.setVisible(R.id.starting_game_personal_expanse_layout,!isEqualSplit);
    }


    private void startNewGame(){
        //gathering params
        StartGame params=new StartGame();

        try {
            params.player=new Player();
            params.player.name=  ApplicationController.VIEWS_CONTROLLER.getText(R.id.starting_game_nick_text_view);

            params.numberOfPlayers = new Integer(ApplicationController.VIEWS_CONTROLLER.getText(R.id.starting_game_couunt_of_players));

            params.equalSplit = ApplicationController.VIEWS_CONTROLLER.getIsChecked(R.id.starting_game_equal_spli_switch);

            params.numberOfRounds = new Integer(ApplicationController.VIEWS_CONTROLLER.getText(R.id.starting_game_count_of_rounds));

            params.totalBill = new Long(ApplicationController.VIEWS_CONTROLLER.getText(R.id.starting_game_bill_expane));
        } catch (Exception e) {
            showWrongDataInfo();
        }

        //Validation
        if (params.player.name != null && params.numberOfPlayers != null && params.numberOfRounds != null && params.totalBill != null && params.equalSplit!=null) {
            if(!params.equalSplit){
                try {
                    Log.e("params.totalBill:",ApplicationController.VIEWS_CONTROLLER.getText(R.id.starting_game_personal_expanse));
                    params.player.initialBillAmount = new Long(ApplicationController.VIEWS_CONTROLLER.getText(R.id.starting_game_personal_expanse));
                } catch (NumberFormatException e) {
                    ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.WRONG_PERSONAL_PAYMET));
                    return;
                }
                if(params.player.initialBillAmount>params.totalBill){
                    ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.WRONG_PERSONAL_PAYMET));
                    return;
                }
            }

            if(params.numberOfPlayers <3){
                ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.TO_SMALL_PLAYERS_COUNT));
                return;
            }else if(params.numberOfPlayers >10){
                ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.TO_BIG_PLAYERS_COUNT));
                return;
            }

            if(params.numberOfRounds <1){
                ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.TO_LOW_ROUNDS_NUMBER));
                return;
            }else if(params.numberOfRounds >5){
                ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.TO_BIG_ROUNDS_NUMBER));
                return;
            }

            if(params.totalBill <=0){
                ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.TO_LOW_SUM_PAYMENT));
                return;
            }

            startGame(params);
        }else{
            showWrongDataInfo();
        }

    }


    private void startGame(StartGame  startGameObject){
        ApplicationController.startNewGame(startGameObject);

    }

    private void showWrongDataInfo(){
        ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.INSERT_CORRECT_DATA));
    }


}
