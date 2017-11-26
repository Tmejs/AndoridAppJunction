package com.tmejs.andoridappjunction.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.tmejs.andoridappjunction.AppParams;
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
                    if (s.length() > 0) {
                        setTestButtonEnable(true);
                    } else {
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


        ApplicationController.VIEWS_CONTROLLER.setOnClickListener(R.id.join_game_start_game_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = ApplicationController.VIEWS_CONTROLLER.getText(R.id.join_game_nick_text_view);
                if (nick == null || nick.isEmpty()) {
                    ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.TXT_INSERT_CORRECT_NICKAME));
                } else {
                    ApplicationController.APP_PARAMS.setParamValue(AppParams.PLAYER_NAME, nick);
                    try {
                        String initialBillAmount = ApplicationController.VIEWS_CONTROLLER.getText(R.id.join_game_starting_expanse);
                        ApplicationController.APP_PARAMS.setParamValue(AppParams.INIITIAL_PLAYER_AMOUNT, initialBillAmount);
                    } catch (Exception e) {

                    }
                    ApplicationController.waitForAllPlayersToSignIn();
                }
            }
        });
    }

    private void setTestButtonEnable(boolean b) {
        ApplicationController.VIEWS_CONTROLLER.setEnabled(R.id.join_game_test_button, b);
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
                if (resp.doExist) {
                    Log.e("zz", "doExst true");
                    if (resp.doInputInitialBill != null) {
                        if (resp.doInputInitialBill) {
                            Log.e("zz", "doInputInitialBill  true");
                            ApplicationController.VIEWS_CONTROLLER.setVisible(R.id.join_game_personal_expanse_layout, View.VISIBLE);
                        } else {
                            Log.e("zz", "doInputInitialBill false");
                            ApplicationController.VIEWS_CONTROLLER.setVisible(R.id.join_game_personal_expanse_layout, View.GONE);
                        }
                    }
                    setVisibilitiesBasedOnAuthentication(true);

                } else {
                    Log.e("zz", "doExst false");
                    ApplicationController.showNews(ApplicationController.getStringFromResources(R.string.TXT_WRONG_GAME_NUMBER));
                    setVisibilitiesBasedOnAuthentication(false);
                }
            }
        });
    }
}
