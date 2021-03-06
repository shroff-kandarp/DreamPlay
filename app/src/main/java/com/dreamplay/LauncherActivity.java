package com.dreamplay;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;

public class LauncherActivity extends BaseActivity {

    GeneralFunctions generalFunc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        generalFunc = new GeneralFunctions(getActContext());

        GeneralFunctions.printHashKey(getActContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (generalFunc.isUserLoggedIn()) {

                    (new StartActProcess(getActContext())).startAct(MainActivity.class);
                } else {
                    (new StartActProcess(getActContext())).startAct(AppLoginActivity.class);
                }
                ActivityCompat.finishAffinity((Activity) getActContext());

            }
        }, 2500);

    }

    public Context getActContext() {
        return LauncherActivity.this;
    }
}
