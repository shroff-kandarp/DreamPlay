package com.dreamplay;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONObject;

import java.util.HashMap;

public class CreateContestActivity extends AppCompatActivity {

    MTextView titleTxt;
    ImageView backImgView;


    GeneralFunctions generalFunc;
    View containerView;
    ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contest);

        generalFunc = new GeneralFunctions(getActContext());
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);

        containerView = findViewById(R.id.containerView);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        backImgView.setOnClickListener(new setOnClickList());

        getMemberData();

        setLabels();
    }

    public void setLabels() {
        titleTxt.setText("CREATE CONTEST");
    }


    public void getMemberData() {
        containerView.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getMemberData");
        parameters.put("iMemberId", generalFunc.getMemberId());

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setLoaderConfig(getActContext(), false, generalFunc);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(final String responseString) {

                Utils.printLog("ResponseData", "Data::" + responseString);
                loadingBar.setVisibility(View.GONE);
                if (responseString != null && !responseString.equals("")) {
                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail) {

                        JSONObject obj_msg = generalFunc.getJsonObject(Utils.message_str, responseString);


                        containerView.setVisibility(View.VISIBLE);

                    } else {
                        generalFunc.showGeneralMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));
                    }

                } else {
                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }


    public Context getActContext() {
        return CreateContestActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(CreateContestActivity.this);
            switch (view.getId()) {
                case R.id.backImgView:
                    CreateContestActivity.super.onBackPressed();
                    break;

            }
        }
    }
}
