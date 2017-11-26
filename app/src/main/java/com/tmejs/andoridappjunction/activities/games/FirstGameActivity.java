package com.tmejs.andoridappjunction.activities.games;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tmejs.andoridappjunction.ApplicationController;
import com.tmejs.andoridappjunction.R;
import com.tmejs.andoridappjunction.domain.games.FirstGameDomain;

import java.util.List;

public class FirstGameActivity extends AbstarctGameActivity<String, FirstGameDomain> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_game);
    }

    @Override
    public void fillData(FirstGameDomain gamObject) {

        fillGameQuestion(gamObject.question);

        fillTextFields(gamObject.possibleAnswerList);
    }

    private void fillGameQuestion(String question) {
        ApplicationController.VIEWS_CONTROLLER.setText(R.id.firt_game_question_text_view, question);
    }

    private void fillTextFields(List<String> list) {
        fillAnswerBoxFirstBox(R.id.first_game_first_answer, list.get(0));
        fillAnswerBoxFirstBox(R.id.first_game_second_answer,list.get(1));
        fillAnswerBoxFirstBox(R.id.first_game_third_answer,list.get(2));
        fillAnswerBoxFirstBox(R.id.first_game_forth_answer,list.get(3));

    }

    private void fillAnswerBoxFirstBox(final Integer viewId, final String s) {
        ApplicationController.VIEWS_CONTROLLER.setText(viewId, s);
        ApplicationController.VIEWS_CONTROLLER.setOnClickListener(viewId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(s);
            }
        });
    }

    @Override
    public void submit(String result) {
        long resultTime = System.currentTimeMillis()-responseTime ;
        ApplicationController.sendGameResult(result, resultTime);
    }
}
