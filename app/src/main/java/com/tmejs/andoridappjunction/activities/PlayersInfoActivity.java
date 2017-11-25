package com.tmejs.andoridappjunction.activities;


import android.os.Bundle;
import android.view.View;

import com.tmejs.andoridappjunction.ApplicationController;
import com.tmejs.andoridappjunction.R;
import com.tmejs.andoridappjunction.activities.system.MyActivity;

public class PlayersInfoActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_info);


        ApplicationController.VIEWS_CONTROLLER.setOnClickListener(R.id.players_info_continue_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewRound();
            }
        });
    }


    private void startNewRound(){
        ApplicationController.startNewRound();
    }
}
