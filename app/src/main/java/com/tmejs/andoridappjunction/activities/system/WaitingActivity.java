package com.tmejs.andoridappjunction.activities.system;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;

import com.tmejs.andoridappjunction.R;
public class WaitingActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
//        WebView web = (WebView) findViewById(R.id.webView);
//        web.setBackgroundColor(Color.TRANSPARENT); //for gif without background
//        web.loadUrl("http://media.giphy.com/media/88EvfARM1YaCQ/giphy.gif");
    }
}
