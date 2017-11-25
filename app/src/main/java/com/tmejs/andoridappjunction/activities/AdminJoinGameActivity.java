package com.tmejs.andoridappjunction.activities;

import android.os.Bundle;
import android.view.View;

import com.tmejs.andoridappjunction.AppParams;
import com.tmejs.andoridappjunction.ApplicationController;
import com.tmejs.andoridappjunction.R;
import com.tmejs.andoridappjunction.activities.system.MyActivity;

public class AdminJoinGameActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_join_game);

        ApplicationController.VIEWS_CONTROLLER.setText(R.id.admin_join_game_game_code_text_view, ApplicationController.APP_PARAMS.getParamValue(AppParams.COMPETITION_ID));

        ApplicationController.VIEWS_CONTROLLER.setOnClickListener(R.id.admin_join_game_start_game_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }


    private void startGame() {
        ApplicationController.waitForAllPlayersToSignIn();
    }
}
