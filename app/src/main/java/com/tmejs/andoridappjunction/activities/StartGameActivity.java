package com.tmejs.andoridappjunction.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tmejs.andoridappjunction.ApplicationController;
import com.tmejs.andoridappjunction.R;
import com.tmejs.andoridappjunction.activities.system.MyActivity;
import com.tmejs.andoridappjunction.activities.system.WaitingActivity;

public class StartGameActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        ((Button) findViewById(R.id.activity_start_game_start_game_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("StartGameActivity", "Start game button clicked");
                ApplicationController.showNews("Zmieniam na StartingGameActivity");
                ApplicationController.switchActivity(StartingGameActivity.class);

            }
        });
        ((Button) findViewById(R.id.activity_start_game_join_game_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("StartGameActivity", "Join game button clicked");
                ApplicationController.switchActivity(JoinGameActivity.class);
            }
        });
    }


}
