package com.dreamplay;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adapter.TeamPlayerListAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateTeamActivity extends AppCompatActivity {

    MTextView titleTxt;
    ImageView backImgView;

    ImageView leftImgView;
    ImageView rightTeamImgView;
    MTextView infoTxtView;
    MTextView leftTeamNameTxtView;
    MTextView rightTeamNameTxtView;
    MTextView dateRemainsInfoTxtView;
    MTextView hintPlayRoleTxtView;

    MButton btn_type2;

    GeneralFunctions generalFunc;
    View containerView;
    ProgressBar loadingBar;

    String PAGE_TYPE = "";

    CountDownTimer timer;

    JSONObject obj_userData;

    View wicketKeeperArea;
    View wicketKeeperImgView;
    View batsManArea;
    View batsManImgView;
    View allRounderArea;
    View allRounderImgView;
    View bowlerArea;
    View bowlerImgView;
    View otherArea;
    View otherImgView;

    RecyclerView playerListRecyclerView;

    TeamPlayerListAdapter adapter;

    ArrayList<HashMap<String, String>> listWicketKeeperData = new ArrayList<>();
    ArrayList<HashMap<String, String>> listBastManData = new ArrayList<>();
    ArrayList<HashMap<String, String>> listBowlerData = new ArrayList<>();
    ArrayList<HashMap<String, String>> listAllRounderData = new ArrayList<>();
    ArrayList<HashMap<String, String>> listOtherData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        generalFunc = new GeneralFunctions(getActContext());
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        wicketKeeperImgView = (ImageView) findViewById(R.id.wicketKeeperImgView);
        batsManImgView = (ImageView) findViewById(R.id.batsManImgView);
        allRounderImgView = (ImageView) findViewById(R.id.allRounderImgView);
        bowlerImgView = (ImageView) findViewById(R.id.bowlerImgView);
        otherImgView = (ImageView) findViewById(R.id.otherImgView);
        playerListRecyclerView = (RecyclerView) findViewById(R.id.playerListRecyclerView);

        wicketKeeperArea = findViewById(R.id.wicketKeeperArea);
        batsManArea = findViewById(R.id.batsManArea);
        allRounderArea = findViewById(R.id.allRounderArea);
        bowlerArea = findViewById(R.id.bowlerArea);
        otherArea = findViewById(R.id.otherArea);

        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();

        hintPlayRoleTxtView = (MTextView) findViewById(R.id.hintPlayRoleTxtView);
        infoTxtView = (MTextView) findViewById(R.id.infoTxtView);
        leftTeamNameTxtView = (MTextView) findViewById(R.id.leftTeamNameTxtView);
        rightTeamNameTxtView = (MTextView) findViewById(R.id.rightTeamNameTxtView);
        dateRemainsInfoTxtView = (MTextView) findViewById(R.id.dateRemainsInfoTxtView);
        leftImgView = (ImageView) findViewById(R.id.leftImgView);
        rightTeamImgView = (ImageView) findViewById(R.id.rightTeamImgView);

        containerView = findViewById(R.id.containerView);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        btn_type2.setId(Utils.generateViewId());

        btn_type2.setOnClickListener(new setOnClickList());
        backImgView.setOnClickListener(new setOnClickList());
        wicketKeeperArea.setOnClickListener(new setOnClickList());
        batsManArea.setOnClickListener(new setOnClickList());
        allRounderArea.setOnClickListener(new setOnClickList());
        bowlerArea.setOnClickListener(new setOnClickList());
        otherArea.setOnClickListener(new setOnClickList());

        new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 4), getResources().getColor(R.color.appThemeColor_1), wicketKeeperImgView);

        new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 4), getResources().getColor(R.color.appThemeColor_1), batsManImgView);

        new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 4), getResources().getColor(R.color.appThemeColor_1), allRounderImgView);

        new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 4), getResources().getColor(R.color.appThemeColor_1), bowlerImgView);

        new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 4), getResources().getColor(R.color.appThemeColor_1), otherImgView);

        adapter = new TeamPlayerListAdapter(getActContext(), listWicketKeeperData, generalFunc, false);

        playerListRecyclerView.setAdapter(adapter);

        getMatchData();

        setLabels();
    }

    public void setLabels() {
        titleTxt.setText("CREATE TEAM");
        btn_type2.setText("Create Team");
    }


    public void getMatchData() {
        containerView.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getMatchContests");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("iMatchId", getIntent().getStringExtra("iMatchId"));
        parameters.put("isLoadUserData", "Yes");
        parameters.put("isLoadTeamOfMatchData", "Yes");

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
                        obj_userData = generalFunc.getJsonObject("MemberData", responseString);

                        if (obj_matchData != null) {
                            setMatchData(obj_matchData);
                        }

                        buildPlayerList(generalFunc.getJsonObject("PlayersOfMatch", responseString));
                        wicketKeeperArea.performClick();
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

    public void buildPlayerList(JSONObject obj_PlayersOfMatch) {

        JSONArray bowlersArr = generalFunc.getJsonArray("Bowlers", obj_PlayersOfMatch);
        if (bowlersArr != null) {
            for (int i = 0; i < bowlersArr.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(bowlersArr, i);

                HashMap<String, String> mapData = new HashMap<>();
                mapData.put("iPlayerId", generalFunc.getJsonValue("iPlayerId", obj_temp));
                mapData.put("iMatchId", generalFunc.getJsonValue("iMatchId", obj_temp));
                mapData.put("vTeamName", generalFunc.getJsonValue("vTeamName", obj_temp));
                mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                mapData.put("vPlayerName", generalFunc.getJsonValue("vPlayerName", obj_temp));
                mapData.put("vPlayingRole", generalFunc.getJsonValue("vPlayingRole", obj_temp));
                mapData.put("ePlayerType", generalFunc.getJsonValue("ePlayerType", obj_temp));
                mapData.put("vImgName", generalFunc.getJsonValue("vImgName", obj_temp));
                mapData.put("dAddedDate", generalFunc.getJsonValue("dAddedDate", obj_temp));
                mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                listBowlerData.add(mapData);
            }
        }


        JSONArray batsmanArr = generalFunc.getJsonArray("Batsman", obj_PlayersOfMatch);
        if (batsmanArr != null) {
            for (int i = 0; i < batsmanArr.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(batsmanArr, i);

                HashMap<String, String> mapData = new HashMap<>();
                mapData.put("iPlayerId", generalFunc.getJsonValue("iPlayerId", obj_temp));
                mapData.put("iMatchId", generalFunc.getJsonValue("iMatchId", obj_temp));
                mapData.put("vTeamName", generalFunc.getJsonValue("vTeamName", obj_temp));
                mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                mapData.put("vPlayerName", generalFunc.getJsonValue("vPlayerName", obj_temp));
                mapData.put("vPlayingRole", generalFunc.getJsonValue("vPlayingRole", obj_temp));
                mapData.put("ePlayerType", generalFunc.getJsonValue("ePlayerType", obj_temp));
                mapData.put("vImgName", generalFunc.getJsonValue("vImgName", obj_temp));
                mapData.put("dAddedDate", generalFunc.getJsonValue("dAddedDate", obj_temp));
                mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                listBastManData.add(mapData);
            }
        }


        JSONArray wicketkeeperArr = generalFunc.getJsonArray("Wicketkeeper", obj_PlayersOfMatch);
        if (wicketkeeperArr != null) {
            for (int i = 0; i < wicketkeeperArr.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(wicketkeeperArr, i);

                HashMap<String, String> mapData = new HashMap<>();
                mapData.put("iPlayerId", generalFunc.getJsonValue("iPlayerId", obj_temp));
                mapData.put("iMatchId", generalFunc.getJsonValue("iMatchId", obj_temp));
                mapData.put("vTeamName", generalFunc.getJsonValue("vTeamName", obj_temp));
                mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                mapData.put("vPlayerName", generalFunc.getJsonValue("vPlayerName", obj_temp));
                mapData.put("vPlayingRole", generalFunc.getJsonValue("vPlayingRole", obj_temp));
                mapData.put("ePlayerType", generalFunc.getJsonValue("ePlayerType", obj_temp));
                mapData.put("vImgName", generalFunc.getJsonValue("vImgName", obj_temp));
                mapData.put("dAddedDate", generalFunc.getJsonValue("dAddedDate", obj_temp));
                mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                listWicketKeeperData.add(mapData);
            }
        }

        JSONArray othersArr = generalFunc.getJsonArray("Others", obj_PlayersOfMatch);
        if (othersArr != null) {
            for (int i = 0; i < othersArr.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(othersArr, i);

                HashMap<String, String> mapData = new HashMap<>();
                mapData.put("iPlayerId", generalFunc.getJsonValue("iPlayerId", obj_temp));
                mapData.put("iMatchId", generalFunc.getJsonValue("iMatchId", obj_temp));
                mapData.put("vTeamName", generalFunc.getJsonValue("vTeamName", obj_temp));
                mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                mapData.put("vPlayerName", generalFunc.getJsonValue("vPlayerName", obj_temp));
                mapData.put("vPlayingRole", generalFunc.getJsonValue("vPlayingRole", obj_temp));
                mapData.put("ePlayerType", generalFunc.getJsonValue("ePlayerType", obj_temp));
                mapData.put("vImgName", generalFunc.getJsonValue("vImgName", obj_temp));
                mapData.put("dAddedDate", generalFunc.getJsonValue("dAddedDate", obj_temp));
                mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                listOtherData.add(mapData);
            }
        }

        JSONArray allrounderArr = generalFunc.getJsonArray("Allrounder", obj_PlayersOfMatch);
        if (allrounderArr != null) {
            for (int i = 0; i < allrounderArr.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(allrounderArr, i);

                HashMap<String, String> mapData = new HashMap<>();
                mapData.put("iPlayerId", generalFunc.getJsonValue("iPlayerId", obj_temp));
                mapData.put("iMatchId", generalFunc.getJsonValue("iMatchId", obj_temp));
                mapData.put("vTeamName", generalFunc.getJsonValue("vTeamName", obj_temp));
                mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                mapData.put("vPlayerName", generalFunc.getJsonValue("vPlayerName", obj_temp));
                mapData.put("vPlayingRole", generalFunc.getJsonValue("vPlayingRole", obj_temp));
                mapData.put("ePlayerType", generalFunc.getJsonValue("ePlayerType", obj_temp));
                mapData.put("vImgName", generalFunc.getJsonValue("vImgName", obj_temp));
                mapData.put("dAddedDate", generalFunc.getJsonValue("dAddedDate", obj_temp));
                mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                listAllRounderData.add(mapData);
            }
        }
    }

    public Context getActContext() {
        return CreateTeamActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(CreateTeamActivity.this);
            int i = view.getId();
            if (i == R.id.backImgView) {
                CreateTeamActivity.super.onBackPressed();
            } else if (i == btn_type2.getId()) {
                checkData();
            } else if (i == wicketKeeperArea.getId()) {
                hintPlayRoleTxtView.setText("Pick 1 Wicket-Keeper");
                adapter.setDataList(listWicketKeeperData);
                adapter.notifyDataSetChanged();
            } else if (i == batsManArea.getId()) {
                hintPlayRoleTxtView.setText("Pick 5 BatsMan");
                adapter.setDataList(listBastManData);
                adapter.notifyDataSetChanged();
            } else if (i == allRounderArea.getId()) {
                hintPlayRoleTxtView.setText("Pick 2 AllRounder");
                adapter.setDataList(listAllRounderData);
                adapter.notifyDataSetChanged();
            } else if (i == bowlerArea.getId()) {
                hintPlayRoleTxtView.setText("Pick 3 Bowlers");
                adapter.setDataList(listBowlerData);
                adapter.notifyDataSetChanged();
            } else if (i == otherArea.getId()) {
                hintPlayRoleTxtView.setText("");
                adapter.setDataList(listOtherData);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void checkData() {

    }
}
