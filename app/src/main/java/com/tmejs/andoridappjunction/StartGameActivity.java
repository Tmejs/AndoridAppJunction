package com.tmejs.andoridappjunction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class StartGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        ((Button)findViewById(R.id.activity_start_game_start_game_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("StartGameActivity", "Start game button clicked");

            }
        });
        ((Button)findViewById(R.id.activity_start_game_join_game_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("StartGameActivity", "Join game button clicked");

            }
        });
    }






}
