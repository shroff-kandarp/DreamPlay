package com.dreamplay;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONObject;

import java.util.HashMap;

public class ContactUsActivity extends BaseActivity {
    MTextView titleTxt;
    ImageView backImgView;
    MTextView emailTxtView;
    MTextView phoneTxtView;
    MTextView emailTapTxtView;
    MTextView callTapTxtView;
    GeneralFunctions generalFunc;
    View containerView;
    ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        generalFunc = new GeneralFunctions(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        emailTxtView = (MTextView) findViewById(R.id.emailTxtView);
        phoneTxtView = (MTextView) findViewById(R.id.phoneTxtView);
        emailTapTxtView = (MTextView) findViewById(R.id.emailTapTxtView);
        callTapTxtView = (MTextView) findViewById(R.id.callTapTxtView);
        containerView = findViewById(R.id.containerView);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        setLabels();

        backImgView.setOnClickListener(new setOnClickList());
        emailTapTxtView.setOnClickListener(new setOnClickList());
        callTapTxtView.setOnClickListener(new setOnClickList());

        getUserData();
    }

    public void setLabels() {
        titleTxt.setText("Contact US");
    }

    public void getUserData() {
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

                        if (obj_msg != null) {
                            String ADMIN_EMAIL = generalFunc.getJsonValue("ADMIN_EMAIL", generalFunc.getJsonObject("ConfigurationData", obj_msg.toString()));
                            String ADMIN_PHONE = generalFunc.getJsonValue("ADMIN_PHONE", generalFunc.getJsonObject("ConfigurationData", obj_msg.toString()));


                            phoneTxtView.setText(ADMIN_PHONE);
                            emailTxtView.setText(ADMIN_EMAIL);
                        }

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
        return ContactUsActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(ContactUsActivity.this);
            switch (view.getId()) {
                case R.id.backImgView:
                    ContactUsActivity.super.onBackPressed();
                    break;
                case R.id.callTapTxtView:
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + phoneTxtView.getText().toString()));
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                    break;
                case R.id.emailTapTxtView:
                    try {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto:" + emailTxtView.getText().toString()));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                        startActivity(Intent.createChooser(emailIntent, "Send email using..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getActContext(), "No email clients installed.", Toast.LENGTH_SHORT).show();
                    }

                    break;

            }
        }
    }
}
