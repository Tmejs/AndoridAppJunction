package com.tmejs.andoridappjunction.activities.games;

import com.tmejs.andoridappjunction.activities.system.MyActivity;

/**
 * Created by Tmejs on 25.11.2017.
 */

public abstract class AbstarctGameActivity<T> extends MyActivity {

    public abstract void fillData(Object gamObject);
    public abstract void submit(T result);


}
