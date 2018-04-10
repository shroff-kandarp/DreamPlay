package com.dreamplay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adapter.ContestListRecycleAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
    MTextView myTeamsCountTxtView;
    MTextView joinedContestCountTxtView;

    View createContestArea;

    GeneralFunctions generalFunc;
    View containerView;
    ProgressBar loadingBar;
    public String PAGE_TYPE = "";

    CountDownTimer timer;

    ArrayList<HashMap<String, String>> list_contest = new ArrayList<>();
    ContestListRecycleAdapter adapter;

    RecyclerView contestListRecyclerView;

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
        contestListRecyclerView = (RecyclerView) findViewById(R.id.contestListRecyclerView);
        myTeamsCountTxtView = (MTextView) findViewById(R.id.myTeamsCountTxtView);
        joinedContestCountTxtView = (MTextView) findViewById(R.id.joinedContestCountTxtView);

        createContestArea = findViewById(R.id.createContestArea);


        new CreateRoundedView(getResources().getColor(R.color.appThemeColor_TXT_1), Utils.dipToPixels(getActContext(), 13), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), myTeamsCountTxtView);

        new CreateRoundedView(getResources().getColor(R.color.appThemeColor_TXT_1), Utils.dipToPixels(getActContext(), 13), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), joinedContestCountTxtView);

        adapter = new ContestListRecycleAdapter(getActContext(), list_contest, generalFunc, false);
        contestListRecyclerView.setAdapter(adapter);
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

        list_contest.clear();
        adapter.notifyDataSetChanged();

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

                list_contest.clear();
                adapter.notifyDataSetChanged();

                if (responseString != null && !responseString.equals("")) {
                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail) {

                        JSONObject obj_msg = generalFunc.getJsonObject(Utils.message_str, responseString);
                        JSONObject obj_matchData = generalFunc.getJsonObject("MatchData", responseString);
                        JSONArray obj_contestData = generalFunc.getJsonArray("Contests", responseString);

                        if (obj_matchData != null) {
                            setMatchData(obj_matchData);
                        }
                        setContestData(obj_contestData);
                        myTeamsCountTxtView.setText(generalFunc.getJsonValue("MatchTeamCount", responseString));

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

    private void setContestData(JSONArray obj_contestData) {
        int countOfJoined = 0;
        if (obj_contestData != null) {

            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("TYPE", "" + adapter.TYPE_HEADER);
            list_contest.add(headerMap);
            for (int i = 0; i < obj_contestData.length(); i++) {
                JSONObject obj_tmp = generalFunc.getJsonObject(obj_contestData, i);
                HashMap<String, String> mapData = new HashMap<>();
                mapData.put("iConstestId", generalFunc.getJsonValue("iConstestId", obj_tmp));
                mapData.put("iContestCategoryId", generalFunc.getJsonValue("iContestCategoryId", obj_tmp));
                mapData.put("iMatchId", generalFunc.getJsonValue("iMatchId", obj_tmp));
                mapData.put("iMemberId", generalFunc.getJsonValue("iMemberId", obj_tmp));
                mapData.put("vConstestName", generalFunc.getJsonValue("vConstestName", obj_tmp));
                mapData.put("vWinningAmount", generalFunc.getJsonValue("vWinningAmount", obj_tmp));
                mapData.put("vContestSize", generalFunc.getJsonValue("vContestSize", obj_tmp));
                mapData.put("eMultipleTeamJoin", generalFunc.getJsonValue("eMultipleTeamJoin", obj_tmp));
                mapData.put("eConfirmed", generalFunc.getJsonValue("eConfirmed", obj_tmp));
                mapData.put("eMegaContest", generalFunc.getJsonValue("eMegaContest", obj_tmp));
                mapData.put("tWinners", generalFunc.getJsonValue("tWinners", obj_tmp));
                mapData.put("tEntryFees", generalFunc.getJsonValue("tEntryFees", obj_tmp));
                mapData.put("vInviteCode", generalFunc.getJsonValue("vInviteCode", obj_tmp));
                mapData.put("eStatus", generalFunc.getJsonValue("eStatus", obj_tmp));
                mapData.put("vCategoryName", generalFunc.getJsonValue("vCategoryName", obj_tmp));
                mapData.put("vCategoryDescription", generalFunc.getJsonValue("vCategoryDescription", obj_tmp));
                mapData.put("vCategoryImage", generalFunc.getJsonValue("vCategoryImage", obj_tmp));
                mapData.put("fPriceStartRange", generalFunc.getJsonValue("fPriceStartRange", obj_tmp));
                mapData.put("fPriceEndRange", generalFunc.getJsonValue("fPriceEndRange", obj_tmp));
                mapData.put("eUserJoined", generalFunc.getJsonValue("eUserJoined", obj_tmp));
                mapData.put("ContestTeamCount", generalFunc.getJsonValue("ContestTeamCount", obj_tmp));
                mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                if (mapData.get("eUserJoined") != null && mapData.get("eUserJoined").equalsIgnoreCase("yes")) {
                    countOfJoined = countOfJoined + 1;
                }
                list_contest.add(mapData);
            }
            joinedContestCountTxtView.setText("" + countOfJoined);
            adapter.notifyDataSetChanged();
        } else {

        }
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
            Bundle bn = new Bundle();
            switch (view.getId()) {
                case R.id.backImgView:
                    ContestsActivity.super.onBackPressed();
                    break;
                case R.id.myTeamsTxtView:
                    bn.putString("iMatchId", getIntent().getStringExtra("iMatchId"));
                    bn.putString("PAGE_TYPE", PAGE_TYPE);
                    (new StartActProcess(getActContext())).startActWithData(UserMatchTeamsActivity.class, bn);
                    break;
                case R.id.joinedContectsTxtView:
                    bn.putString("iMatchId", getIntent().getStringExtra("iMatchId"));
                    bn.putString("PAGE_TYPE", PAGE_TYPE);
                    (new StartActProcess(getActContext())).startActWithData(JoinedContestActivity.class, bn);
                    break;
                case R.id.createContestArea:
                    bn.putString("iMatchId", getIntent().getStringExtra("iMatchId"));
                    bn.putString("PAGE_TYPE", PAGE_TYPE);
                    (new StartActProcess(getActContext())).startActWithData(CreateContestActivity.class, bn);
                    break;


            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Utils.CREATE_TEAM_REQ_CODE && resultCode == RESULT_OK){
            getMatchData();
        }
    }
}
