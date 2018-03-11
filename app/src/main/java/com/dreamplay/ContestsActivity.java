package com.dreamplay;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONObject;

import java.util.HashMap;

public class ContestsActivity extends AppCompatActivity {

    MTextView titleTxt;
    MTextView myTeamsTxtView;
    MTextView joinedContectsTxtView;
    MTextView infoTxtView;
    MTextView leftTeamNameTxtView;
    MTextView rightTeamNameTxtView;
    MTextView dateRemainsInfoTxtView;
    ImageView backImgView;
    ImageView leftImgView;
    ImageView rightTeamImgView;

    View createContestArea;

    GeneralFunctions generalFunc;
    View containerView;
    ProgressBar loadingBar;
    String PAGE_TYPE = "";

    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contests);

        generalFunc = new GeneralFunctions(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        myTeamsTxtView = (MTextView) findViewById(R.id.myTeamsTxtView);
        joinedContectsTxtView = (MTextView) findViewById(R.id.joinedContectsTxtView);
        infoTxtView = (MTextView) findViewById(R.id.infoTxtView);
        leftTeamNameTxtView = (MTextView) findViewById(R.id.leftTeamNameTxtView);
        rightTeamNameTxtView = (MTextView) findViewById(R.id.rightTeamNameTxtView);
        dateRemainsInfoTxtView = (MTextView) findViewById(R.id.dateRemainsInfoTxtView);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        leftImgView = (ImageView) findViewById(R.id.leftImgView);
        rightTeamImgView = (ImageView) findViewById(R.id.rightTeamImgView);
        containerView = findViewById(R.id.containerView);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        createContestArea = findViewById(R.id.createContestArea);

        PAGE_TYPE = getIntent().getStringExtra("PAGE_TYPE");

        backImgView.setOnClickListener(new setOnClickList());
        myTeamsTxtView.setOnClickListener(new setOnClickList());
        joinedContectsTxtView.setOnClickListener(new setOnClickList());
        createContestArea.setOnClickListener(new setOnClickList());

        getMatchData();

        setLabels();
    }


    public void setLabels() {
        titleTxt.setText("CONTEST");
    }

    public void getMatchData() {
        containerView.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getMatchContests");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("iMatchId", getIntent().getStringExtra("iMatchId"));

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
                        JSONObject obj_matchData = generalFunc.getJsonObject("MatchData", responseString);

                        if (obj_matchData != null) {
                            setMatchData(obj_matchData);
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

    public void setMatchData(JSONObject match_data) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (!generalFunc.getJsonValue("tTeam1Logo", match_data).equals("")) {
            Picasso.with(getActContext())
                    .load(generalFunc.getJsonValue("tTeam1Logo", match_data))
                    .placeholder(R.drawable.no_team_img)
                    .into(leftImgView, null);
        }

        if (!generalFunc.getJsonValue("tTeam2Logo", match_data).equals("")) {
            Picasso.with(getActContext())
                    .load(generalFunc.getJsonValue("tTeam2Logo", match_data))
                    .placeholder(R.drawable.no_team_img)
                    .into(rightTeamImgView, null);
        }
        infoTxtView.setText(generalFunc.getJsonValue("vMatchType", match_data));
        leftTeamNameTxtView.setText(generalFunc.getJsonValue("vTeam1", match_data));
        rightTeamNameTxtView.setText(generalFunc.getJsonValue("vTeam2", match_data));


        String matchStartDateInMilli = generalFunc.getJsonValue("matchStartDateInMilli", match_data);
        String currentTimeInMilli = generalFunc.getJsonValue("currentTimeInMilli", match_data);
        if (matchStartDateInMilli != null && !matchStartDateInMilli.equals("")) {
            dateRemainsInfoTxtView.setVisibility(View.VISIBLE);

            long milliSecRemains = (GeneralFunctions.parseLong(0, matchStartDateInMilli));
            long currMilliSecRemains = GeneralFunctions.parseLong(0, currentTimeInMilli);

            milliSecRemains = milliSecRemains - currMilliSecRemains;
            if (milliSecRemains > 1) {
                dateRemainsInfoTxtView.setText(Utils.getDurationBreakdown(milliSecRemains));

                timer = new CountDownTimer(milliSecRemains, 1000) {
                    public void onTick(long millisUntilFinished) {
                        dateRemainsInfoTxtView.setText(Utils.getDurationBreakdown(millisUntilFinished));
                    }

                    public void onFinish() {
                        dateRemainsInfoTxtView.setText("");
                    }
                }.start();

            } else {
                dateRemainsInfoTxtView.setText(PAGE_TYPE.equals("LIVE") ? "In Progress" : "Completed");
            }
        } else {
            dateRemainsInfoTxtView.setText(PAGE_TYPE.equals("LIVE") ? "In Progress" : "Completed");
            dateRemainsInfoTxtView.setVisibility(View.VISIBLE);
        }
    }

    public Context getActContext() {
        return ContestsActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(ContestsActivity.this);
            switch (view.getId()) {
                case R.id.backImgView:
                    ContestsActivity.super.onBackPressed();
                    break;
                case R.id.myTeamsTxtView:
                    ContestsActivity.super.onBackPressed();
                    break;
                case R.id.joinedContectsTxtView:
                    ContestsActivity.super.onBackPressed();
                    break;
                case R.id.createContestArea:
                    (new StartActProcess(getActContext())).startAct(CreateContestActivity.class);
                    break;


            }
        }
    }
}
