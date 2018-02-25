package com.dreamplay;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MTextView;

public class AppLoginActivity extends BaseActivity {

    public GeneralFunctions generalFunc;
    MTextView signInTxtView;
    MTextView signUpTxtView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_login);

        generalFunc = new GeneralFunctions(getActContext());

        signInTxtView = (MTextView) findViewById(R.id.signInTxtView);
        signUpTxtView = (MTextView) findViewById(R.id.signUpTxtView);

        new CreateRoundedView(getResources().getColor(android.R.color.transparent), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 1), getResources().getColor(R.color.white), signInTxtView);

        new CreateRoundedView(getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 1), getResources().getColor(R.color.white), signUpTxtView);

        signInTxtView.setOnClickListener(new setOnClickList());
        signUpTxtView.setOnClickListener(new setOnClickList());
    }

    public Context getActContext() {
        return AppLoginActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == signInTxtView.getId()) {
                (new StartActProcess(getActContext())).startAct(SignInActivity.class);
            } else if (view.getId() == signUpTxtView.getId()) {
                (new StartActProcess(getActContext())).startAct(RegisterActivity.class);
            }
        }
    }
}
