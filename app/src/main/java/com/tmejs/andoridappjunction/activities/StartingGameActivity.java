package com.tmejs.andoridappjunction.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.tmejs.andoridappjunction.ApplicationController;
import com.tmejs.andoridappjunction.R;
import com.tmejs.andoridappjunction.activities.system.MyActivity;
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
            params.nick =  ApplicationController.VIEWS_CONTROLLER.getText(R.id.starting_game_nick_text_view);

            params.playersCount = new Integer(ApplicationController.VIEWS_CONTROLLER.getText(R.id.starting_game_couunt_of_players));

            params.equalSplit = ApplicationController.VIEWS_CONTROLLER.getIsChecked(R.id.starting_game_equal_spli_switch);

            params.roundCount = new Integer(ApplicationController.VIEWS_CONTROLLER.getText(R.id.starting_game_count_of_rounds));



            params.sumPayment = new Integer(ApplicationController.VIEWS_CONTROLLER.getText(R.id.starting_game_bill_expane));
        } catch (Exception e) {
            showWrongDataInfo();
        }
        try {
            Log.e("params.nick:",params.nick);
            Log.e("params.playersCount:", params.playersCount.toString());
            Log.e("params.equalSplit:",params.equalSplit.toString());
            Log.e("params.roundCount:",params.roundCount.toString());
            Log.e("params.sumPayment:",params.sumPayment.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Validation
        if (params.nick != null && params.playersCount != null && params.roundCount != null && params.sumPayment != null && params.equalSplit!=null) {

            if(!params.equalSplit){
                try {
                    params.personalPayment = new Integer(ApplicationController.VIEWS_CONTROLLER.getText(R.id.starting_game_personal_expanse));
                } catch (NumberFormatException e) {
                    ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.WRONG_PERSONAL_PAYMET));
                    return;
                }
                if(params.personalPayment>params.sumPayment){
                    ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.WRONG_PERSONAL_PAYMET));
                    return;
                }
            }

            if(params.playersCount<3){
                ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.TO_SMALL_PLAYERS_COUNT));
                return;
            }else if(params.playersCount>10){
                ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.TO_BIG_PLAYERS_COUNT));
                return;
            }

            if(params.roundCount<1){
                ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.TO_LOW_ROUNDS_NUMBER));
                return;
            }else if(params.roundCount>5){
                ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.TO_BIG_ROUNDS_NUMBER));
                return;
            }

            if(params.sumPayment<=0){
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
