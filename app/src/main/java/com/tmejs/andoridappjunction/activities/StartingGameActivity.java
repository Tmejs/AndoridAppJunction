package com.tmejs.andoridappjunction.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tmejs.andoridappjunction.R;
import com.tmejs.andoridappjunction.activities.system.MyActivity;

public class StartingGameActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_game);
    }
}
