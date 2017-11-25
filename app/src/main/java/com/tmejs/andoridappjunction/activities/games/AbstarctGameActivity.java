package com.tmejs.andoridappjunction.activities.games;

import com.tmejs.andoridappjunction.activities.system.MyActivity;

/**
 * Created by Tmejs on 25.11.2017.
 */

public abstract class AbstarctGameActivity<ResultType,GameDomain> extends MyActivity {

    public long responseTime;

    @Override
    protected void onResume() {
        super.onResume();
        responseTime=System.nanoTime();
    }

    public abstract void fillData(GameDomain gamObject);
    public abstract void submit(ResultType result);
}
