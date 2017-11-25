package com.tmejs.andoridappjunction.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.tmejs.andoridappjunction.ApplicationController;
import com.tmejs.andoridappjunction.R;
import com.tmejs.andoridappjunction.activities.system.MyActivity;
import com.tmejs.andoridappjunction.domain.AskDoGameExistResponse;


public class JoinGameActivity extends MyActivity {


    private Boolean isClickListenerAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);


        setVisibilitiesBasedOnAuthentication(false);

        //Tylko razdodajemy
        if (!isClickListenerAdded) {
            ApplicationController.VIEWS_CONTROLLER.setTextChangeEvent(R.id.starting_game_game_code_edit_text, new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    setVisibilitiesBasedOnAuthentication(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()>0){
                        setTestButtonEnable(true);
                    }else{
                        setTestButtonEnable(false);
                    }
                }
            });
        }

        ApplicationController.VIEWS_CONTROLLER.setOnClickListener(R.id.join_game_test_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationController.askDoGameExist(ApplicationController.VIEWS_CONTROLLER.getText(R.id.starting_game_game_code_edit_text));
            }
        });
    }

    private void setTestButtonEnable(boolean b) {
        ApplicationController.VIEWS_CONTROLLER.setEnabled(R.id.join_game_test_button,b);
    }


    private void setVisibilitiesBasedOnAuthentication(Boolean isOk) {

        if (!isOk) {
            ApplicationController.VIEWS_CONTROLLER.setVisible(R.id.join_game_additional_data_layout, View.GONE);
            ApplicationController.VIEWS_CONTROLLER.setVisible(R.id.join_game_test_game_button, View.VISIBLE);
        } else {
            ApplicationController.VIEWS_CONTROLLER.setVisible(R.id.join_game_additional_data_layout, View.VISIBLE);
            ApplicationController.VIEWS_CONTROLLER.setVisible(R.id.join_game_test_game_button, View.GONE);
        }
    }

    public void setValidationResult(final AskDoGameExistResponse resp) {


        ApplicationController.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(resp.doExist) {
                    if(resp.doInputInitialBill){
                        ApplicationController.VIEWS_CONTROLLER.setVisible(R.id.join_game_personal_expanse_layout,View.VISIBLE);
                    }else{
                        ApplicationController.VIEWS_CONTROLLER.setVisible(R.id.join_game_personal_expanse_layout,View.GONE);
                    }
                    setVisibilitiesBasedOnAuthentication(true);

                }else{
                    setVisibilitiesBasedOnAuthentication(false);
                }
            }
        });
    }
}
