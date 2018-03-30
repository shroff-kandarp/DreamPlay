package com.dreamplay;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adapter.ContestTeamAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ContestDetailsActivity extends AppCompatActivity {
    MTextView titleTxt;
    ImageView backImgView;
    ProgressBar loading;

    GeneralFunctions generalFunc;


    ImageView leftImgView;
    ImageView rightTeamImgView;
    MTextView infoTxtView;
    MTextView leftTeamNameTxtView;
    MTextView rightTeamNameTxtView;
    MTextView dateRemainsInfoTxtView;
    MTextView contestHTxtView;
    MTextView teamHTxtView;
    MTextView teamVTxtView;

    MTextView entryAmountTxtView;
    MTextView winnersTxtView;
    MTextView winningAmountTxtView;

    View containerView;
    ProgressBar loadingBar;

    CountDownTimer timer;

    String PAGE_TYPE = "";

    ContestTeamAdapter adapter;
    RecyclerView dataRecyclerView;

    ArrayList<HashMap<String, String>> teamDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_details);

        generalFunc = new GeneralFunctions(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        infoTxtView = (MTextView) findViewById(R.id.infoTxtView);
        leftTeamNameTxtView = (MTextView) findViewById(R.id.leftTeamNameTxtView);
        rightTeamNameTxtView = (MTextView) findViewById(R.id.rightTeamNameTxtView);
        dateRemainsInfoTxtView = (MTextView) findViewById(R.id.dateRemainsInfoTxtView);
        leftImgView = (ImageView) findViewById(R.id.leftImgView);
        rightTeamImgView = (ImageView) findViewById(R.id.rightTeamImgView);
        entryAmountTxtView = (MTextView) findViewById(R.id.entryAmountTxtView);
        winnersTxtView = (MTextView) findViewById(R.id.winnersTxtView);
        winningAmountTxtView = (MTextView) findViewById(R.id.winningAmountTxtView);
        contestHTxtView = (MTextView) findViewById(R.id.contestHTxtView);
        teamHTxtView = (MTextView) findViewById(R.id.teamHTxtView);
        teamVTxtView = (MTextView) findViewById(R.id.teamVTxtView);
        dataRecyclerView = (RecyclerView) findViewById(R.id.dataRecyclerView);

        PAGE_TYPE = getIntent().getStringExtra("PAGE_TYPE");

        containerView = findViewById(R.id.containerView);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        adapter = new ContestTeamAdapter(getActContext(), teamDataList, generalFunc, false);

        new CreateRoundedView(getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(getActContext(), 10), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), contestHTxtView);
        new CreateRoundedView(getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(getActContext(), 10), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), teamHTxtView);

        setLabels();

        backImgView.setOnClickListener(new setOnClickList());

        dataRecyclerView.setAdapter(adapter);

        getMatchData();
    }

    public void setLabels() {
        titleTxt.setText("CONTEST DETAILS");
    }


    public void getMatchData() {
        containerView.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getContestDetails");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("iMatchId", getIntent().getStringExtra("iMatchId"));
        parameters.put("iContestId", getIntent().getStringExtra("iConstestId"));

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
                        JSONObject obj_contestData = generalFunc.getJsonObject("ContestDetailData", responseString);
                        JSONObject obj_configData = generalFunc.getJsonObject("ConfigurationData", responseString);
                        JSONArray arr_userTeam = generalFunc.getJsonArray("UserTeamsData", responseString);

                        if (obj_matchData != null) {
                            setMatchData(obj_matchData);
                        }
                        winnersTxtView.setText(generalFunc.getJsonValue("tWinners", obj_contestData));
                        entryAmountTxtView.setText("₹ " + generalFunc.getJsonValue("tEntryFees", obj_contestData));
                        winningAmountTxtView.setText("₹ " + generalFunc.getJsonValue("vWinningAmount", obj_contestData));

                        int CREATE_MAX_TEAM = GeneralFunctions.parseInt(1, generalFunc.getJsonValue("CREATE_MAX_TEAM", obj_configData));

                        if (CREATE_MAX_TEAM > 1) {
                            teamHTxtView.setText("M");
                            teamVTxtView.setText("You can join this contest with more then 1 team upto " + CREATE_MAX_TEAM + " teams");
                        } else {
                            teamHTxtView.setText("S");
                            teamVTxtView.setText("You can join this contest with 1 team only.");
                        }

                        if (arr_userTeam != null) {
                            HashMap<String, String> dataUser = new HashMap<>();

                            for (int i = 0; i < arr_userTeam.length(); i++) {
                                JSONObject obj_tmp = generalFunc.getJsonObject(arr_userTeam, i);

                                HashMap<String, String> map = new HashMap<>();
                                map.put("iUserTeamId", generalFunc.getJsonValue("iUserTeamId", obj_tmp));
                                map.put("iMemberId", generalFunc.getJsonValue("iMemberId", obj_tmp));
                                map.put("iContestId", generalFunc.getJsonValue("iContestId", obj_tmp));
                                map.put("iMatchId", generalFunc.getJsonValue("iMatchId", obj_tmp));
                                map.put("vName", generalFunc.getJsonValue("vName", obj_tmp));
                                map.put("TeamCount", generalFunc.getJsonValue("TeamCount", obj_tmp));
                                map.put("tPoints", generalFunc.getJsonValue("tPoints", obj_tmp));
                                map.put("tRank", generalFunc.getJsonValue("tRank", obj_tmp));
                                map.put("TYPE", "" + adapter.TYPE_ITEM);

                                if (dataUser.get(map.get("iMemberId")) != null) {
                                    int existCountNum = GeneralFunctions.parseInt(1, dataUser.get(map.get("iMemberId")));

                                    dataUser.put(map.get("iMemberId"), "" + (existCountNum + 1));
                                    map.put("CurrentCount", "" + (existCountNum + 1));

                                } else {
                                    map.put("CurrentCount", "1");

                                    dataUser.put(map.get("iMemberId"), "1");
                                }
                                teamDataList.add(map);
                            }

                            adapter.notifyDataSetChanged();
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
        return ContestDetailsActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(ContestDetailsActivity.this);
            switch (view.getId()) {
                case R.id.backImgView:
                    ContestDetailsActivity.super.onBackPressed();
                    break;

            }
        }
    }
}
