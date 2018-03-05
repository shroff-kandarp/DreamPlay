package com.dreamplay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import org.json.JSONObject;

import java.util.HashMap;

public class InviteFriendsActivity extends AppCompatActivity {

    GeneralFunctions generalFunc;
    MTextView titleTxt;
    MTextView inviteCodeTxt;
    MTextView shareHTxtLbl;
    MTextView detailsTxt;

    ImageView backImgView;

    View containerView;
    ProgressBar loadingBar;

    MButton btn_type2;
    JSONObject obj_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        generalFunc = new GeneralFunctions(getActContext());

        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        shareHTxtLbl = (MTextView) findViewById(R.id.shareHTxtLbl);
        inviteCodeTxt = (MTextView) findViewById(R.id.inviteCodeTxt);
        detailsTxt = (MTextView) findViewById(R.id.detailsTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        containerView = findViewById(R.id.containerView);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);


        setLabels();

        getUserData();

        btn_type2.setId(Utils.generateViewId());
        backImgView.setOnClickListener(new setOnClickList());
        btn_type2.setOnClickListener(new setOnClickList());
    }

    public void setLabels() {

        titleTxt.setText("INVITE FRIENDS");
        btn_type2.setText("INVITE");

    }

    public Context getActContext() {
        return InviteFriendsActivity.this;
    }


    public void getUserData() {
        containerView.setVisibility(View.GONE);
        btn_type2.setVisibility(View.GONE);
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

                        obj_msg = generalFunc.getJsonObject(Utils.message_str, responseString);
                        detailsTxt.setText(generalFunc.getJsonValue("INVITE_FRIENDS_CONTENT_MSG", generalFunc.getJsonObject("ConfigurationData", obj_msg.toString())));
                        inviteCodeTxt.setText(generalFunc.getJsonValue("vInviteCode", obj_msg));

                        btn_type2.setVisibility(View.VISIBLE);
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

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.backImgView) {
                InviteFriendsActivity.super.onBackPressed();
            } else if (i == btn_type2.getId()) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "INVITE FRIENDS");

                String shareMsg = generalFunc.getJsonValue("INVITE_FRIENDS_SHARE_MSG", generalFunc.getJsonObject("ConfigurationData", obj_msg.toString()));
                shareMsg = shareMsg.replace("###@###", inviteCodeTxt.getText().toString());
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        }
    }
}
