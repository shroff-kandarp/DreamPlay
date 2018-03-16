package com.dreamplay;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adapter.TeamPListForCaptainAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ChooseCaptainActivity extends AppCompatActivity {

    MTextView titleTxt;
    ImageView backImgView;

    ImageView leftImgView;
    ImageView rightTeamImgView;
    MTextView infoTxtView;
    MTextView leftTeamNameTxtView;
    MTextView rightTeamNameTxtView;
    MTextView dateRemainsInfoTxtView;

    MTextView saveTeamTextView;

    GeneralFunctions generalFunc;
    View containerView;
    View saveTeamArea;
    ProgressBar loadingBar;

    String PAGE_TYPE = "";

    CountDownTimer timer;

    JSONObject obj_userData;

    RecyclerView playerListRecyclerView;

    TeamPListForCaptainAdapter adapter;

    ArrayList<HashMap<String, String>> listOfPlayers = new ArrayList<>();
    List<String> listOfSelectedPlayers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_captain);

        generalFunc = new GeneralFunctions(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);

        playerListRecyclerView = (RecyclerView) findViewById(R.id.playerListRecyclerView);
        infoTxtView = (MTextView) findViewById(R.id.infoTxtView);
        leftTeamNameTxtView = (MTextView) findViewById(R.id.leftTeamNameTxtView);
        rightTeamNameTxtView = (MTextView) findViewById(R.id.rightTeamNameTxtView);
        dateRemainsInfoTxtView = (MTextView) findViewById(R.id.dateRemainsInfoTxtView);
        leftImgView = (ImageView) findViewById(R.id.leftImgView);
        rightTeamImgView = (ImageView) findViewById(R.id.rightTeamImgView);

        saveTeamTextView = (MTextView) findViewById(R.id.saveTeamTextView);

        saveTeamArea = findViewById(R.id.saveTeamArea);
        containerView = findViewById(R.id.containerView);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        backImgView.setOnClickListener(new setOnClickList());
        saveTeamTextView.setOnClickListener(new setOnClickList());
        adapter = new TeamPListForCaptainAdapter(getActContext(), listOfPlayers, generalFunc, false);

        playerListRecyclerView.setAdapter(adapter);

        String SELECTED_PLAYER_LIST = getIntent().getStringExtra("SELECTED_PLAYER_LIST");
        listOfSelectedPlayers = Arrays.asList(SELECTED_PLAYER_LIST.split(","));

        getMatchData();

        setLabels();
    }

    public void setLabels() {
        titleTxt.setText("CHOOSE CAPTAIN & VICE CAPTAIN");
    }


    public void getMatchData() {
        saveTeamArea.setVisibility(View.GONE);
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

                        containerView.setVisibility(View.VISIBLE);
                        saveTeamArea.setVisibility(View.VISIBLE);

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


    public void buildPlayerList(JSONObject obj_PlayersOfMatch) {


        JSONArray wicketkeeperArr = generalFunc.getJsonArray("Wicketkeeper", obj_PlayersOfMatch);
        if (wicketkeeperArr != null) {

            HashMap<String, String> mapWData = new HashMap<>();
            mapWData.put("vName", "WicketKeepers");
            mapWData.put("TYPE", "" + adapter.TYPE_HEADER);
            listOfPlayers.add(mapWData);

            for (int i = 0; i < wicketkeeperArr.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(wicketkeeperArr, i);

                HashMap<String, String> mapData = new HashMap<>();
                mapData.put("iPlayerId", generalFunc.getJsonValue("iPlayerId", obj_temp));
                if(listOfSelectedPlayers.contains(mapData.get("iPlayerId"))){
                    mapData.put("iMatchId", generalFunc.getJsonValue("iMatchId", obj_temp));
                    mapData.put("vTeamName", generalFunc.getJsonValue("vTeamName", obj_temp));
                    mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                    mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                    mapData.put("vPlayerName", generalFunc.getJsonValue("vPlayerName", obj_temp));
                    mapData.put("vPlayingRole", generalFunc.getJsonValue("vPlayingRole", obj_temp));
                    mapData.put("ePlayerType", generalFunc.getJsonValue("ePlayerType", obj_temp));
                    mapData.put("vImgName", generalFunc.getJsonValue("vImgName", obj_temp));
                    mapData.put("dAddedDate", generalFunc.getJsonValue("dAddedDate", obj_temp));
                    mapData.put("tPoints", "" + generalFunc.getJsonValue("tPoints", obj_temp));
                    mapData.put("tCredits", "" + generalFunc.getJsonValue("tCredits", obj_temp));
                    mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                    listOfPlayers.add(mapData);
                }
            }
        }

        JSONArray batsmanArr = generalFunc.getJsonArray("Batsman", obj_PlayersOfMatch);
        if (batsmanArr != null) {

            HashMap<String, String> mapBatsData = new HashMap<>();
            mapBatsData.put("vName", "Batsman");
            mapBatsData.put("TYPE", "" + adapter.TYPE_HEADER);
            listOfPlayers.add(mapBatsData);

            for (int i = 0; i < batsmanArr.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(batsmanArr, i);

                HashMap<String, String> mapData = new HashMap<>();
                mapData.put("iPlayerId", generalFunc.getJsonValue("iPlayerId", obj_temp));

                if(listOfSelectedPlayers.contains(mapData.get("iPlayerId"))){
                    mapData.put("iMatchId", generalFunc.getJsonValue("iMatchId", obj_temp));
                    mapData.put("vTeamName", generalFunc.getJsonValue("vTeamName", obj_temp));
                    mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                    mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                    mapData.put("vPlayerName", generalFunc.getJsonValue("vPlayerName", obj_temp));
                    mapData.put("vPlayingRole", generalFunc.getJsonValue("vPlayingRole", obj_temp));
                    mapData.put("ePlayerType", generalFunc.getJsonValue("ePlayerType", obj_temp));
                    mapData.put("vImgName", generalFunc.getJsonValue("vImgName", obj_temp));
                    mapData.put("dAddedDate", generalFunc.getJsonValue("dAddedDate", obj_temp));
                    mapData.put("POINTS", "" + ((int) Utils.randFloat(1, 200)));
                    mapData.put("CREDITS", "" + String.format("%.2f", Utils.randFloat(1, 10)));
                    mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                    listOfPlayers.add(mapData);
                }

            }
        }

        JSONArray allrounderArr = generalFunc.getJsonArray("Allrounder", obj_PlayersOfMatch);
        if (allrounderArr != null) {
            HashMap<String, String> mapAllData = new HashMap<>();
            mapAllData.put("vName", "All Rounders");
            mapAllData.put("TYPE", "" + adapter.TYPE_HEADER);
            listOfPlayers.add(mapAllData);
            for (int i = 0; i < allrounderArr.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(allrounderArr, i);

                HashMap<String, String> mapData = new HashMap<>();
                mapData.put("iPlayerId", generalFunc.getJsonValue("iPlayerId", obj_temp));
                if(listOfSelectedPlayers.contains(mapData.get("iPlayerId"))){
                    mapData.put("iMatchId", generalFunc.getJsonValue("iMatchId", obj_temp));
                    mapData.put("vTeamName", generalFunc.getJsonValue("vTeamName", obj_temp));
                    mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                    mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                    mapData.put("vPlayerName", generalFunc.getJsonValue("vPlayerName", obj_temp));
                    mapData.put("vPlayingRole", generalFunc.getJsonValue("vPlayingRole", obj_temp));
                    mapData.put("ePlayerType", generalFunc.getJsonValue("ePlayerType", obj_temp));
                    mapData.put("vImgName", generalFunc.getJsonValue("vImgName", obj_temp));
                    mapData.put("dAddedDate", generalFunc.getJsonValue("dAddedDate", obj_temp));
                    mapData.put("POINTS", "" + ((int) Utils.randFloat(1, 200)));
                    mapData.put("CREDITS", "" + String.format("%.2f", Utils.randFloat(1, 10)));
                    mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                    listOfPlayers.add(mapData);
                }

            }
        }

        JSONArray bowlersArr = generalFunc.getJsonArray("Bowlers", obj_PlayersOfMatch);
        if (bowlersArr != null) {
            HashMap<String, String> mapBData = new HashMap<>();
            mapBData.put("vName", "Bowlers");
            mapBData.put("TYPE", "" + adapter.TYPE_HEADER);
            listOfPlayers.add(mapBData);
            for (int i = 0; i < bowlersArr.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(bowlersArr, i);

                HashMap<String, String> mapData = new HashMap<>();
                mapData.put("iPlayerId", generalFunc.getJsonValue("iPlayerId", obj_temp));
                if(listOfSelectedPlayers.contains(mapData.get("iPlayerId"))){

                    mapData.put("iMatchId", generalFunc.getJsonValue("iMatchId", obj_temp));
                    mapData.put("vTeamName", generalFunc.getJsonValue("vTeamName", obj_temp));
                    mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                    mapData.put("iPid", generalFunc.getJsonValue("iPid", obj_temp));
                    mapData.put("vPlayerName", generalFunc.getJsonValue("vPlayerName", obj_temp));
                    mapData.put("vPlayingRole", generalFunc.getJsonValue("vPlayingRole", obj_temp));
                    mapData.put("ePlayerType", generalFunc.getJsonValue("ePlayerType", obj_temp));
                    mapData.put("vImgName", generalFunc.getJsonValue("vImgName", obj_temp));
                    mapData.put("dAddedDate", generalFunc.getJsonValue("dAddedDate", obj_temp));
                    mapData.put("POINTS", "" + ((int) Utils.randFloat(1, 200)));
                    mapData.put("CREDITS", "" + String.format("%.2f", Utils.randFloat(1, 10)));
                    mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                    listOfPlayers.add(mapData);
                }
            }
        }

        adapter.notifyDataSetChanged();

        /*JSONArray othersArr = generalFunc.getJsonArray("Others", obj_PlayersOfMatch);
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
                mapData.put("POINTS", "" + ((int) Utils.randFloat(1, 200)));
                mapData.put("CREDITS", "" + String.format("%.2f", Utils.randFloat(1, 10)));
                mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                listOtherData.add(mapData);
            }
        }*/
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
        return ChooseCaptainActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(ChooseCaptainActivity.this);
            int i = view.getId();
            if (i == R.id.backImgView) {
                ChooseCaptainActivity.super.onBackPressed();
            } else if (i == saveTeamTextView.getId()) {
                checkData();
            }
        }
    }

    public void checkData() {
        if (adapter.selectedCaptainId.equalsIgnoreCase("")) {

            GeneralFunctions.showMessage(GeneralFunctions.getCurrentView((Activity) getActContext()), "Please select captain for your team.");

            return;
        }
        if (adapter.selectedViceCaptainId.equalsIgnoreCase("")) {

            GeneralFunctions.showMessage(GeneralFunctions.getCurrentView((Activity) getActContext()), "Please select vice captain for your team.");

            return;
        }

        Bundle bn = new Bundle();
        bn.putString("CAPTAIN_ID", adapter.selectedCaptainId);
        bn.putString("VICE_CAPTAIN_ID", adapter.selectedViceCaptainId);

        (new StartActProcess(getActContext())).setOkResult(bn);
        backImgView.performClick();
    }
}
