package com.general.files;

import android.os.Handler;

import com.view.MTextView;

/**
 * Created by Shroff on 02-Mar-18.
 */

public class FixturesTimer implements Runnable  {
    public long interval = 1000;
    public MTextView timeTxtView;
    Handler handler;

    public FixturesTimer(Handler handler, MTextView timeTxtView, long interval) {
        this.handler = handler;
        this.timeTxtView = timeTxtView;
        this.interval = interval;
    }
    @Override
    public void run() {

        handler.postDelayed(this, 1000);
    }
}
