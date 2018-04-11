package com.dreamplay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adapter.TeamPlayerListAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.GenerateAlertBox;
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
    MTextView batsManCountTxtView;
    MTextView wicketCountTxtView;
    MTextView allRounderCountTxtView;
    MTextView bowlerCountTxtView;

    MTextView nextTxtView;
    MTextView countTotalPlayersTxtView;
    public MTextView countTotalCreditsInfoTxtView;

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

    public double totalAvailCredit = 0;
    public double OrigAvailCredit = 0;

    TeamPlayerListAdapter adapter;

    ArrayList<HashMap<String, String>> listWicketKeeperData = new ArrayList<>();
    ArrayList<HashMap<String, String>> listBastManData = new ArrayList<>();
    ArrayList<HashMap<String, String>> listBowlerData = new ArrayList<>();
    ArrayList<HashMap<String, String>> listAllRounderData = new ArrayList<>();
    ArrayList<HashMap<String, String>> listOtherData = new ArrayList<>();
    ArrayList<HashMap<String, String>> listOfAllPlayersData = new ArrayList<>();
    public String selectedType = "";

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
        batsManCountTxtView = (MTextView) findViewById(R.id.batsManCountTxtView);
        wicketCountTxtView = (MTextView) findViewById(R.id.wicketCountTxtView);
        allRounderCountTxtView = (MTextView) findViewById(R.id.allRounderCountTxtView);
        bowlerCountTxtView = (MTextView) findViewById(R.id.bowlerCountTxtView);
        nextTxtView = (MTextView) findViewById(R.id.nextTxtView);
        countTotalPlayersTxtView = (MTextView) findViewById(R.id.countTotalPlayersTxtView);
        countTotalCreditsInfoTxtView = (MTextView) findViewById(R.id.countTotalCreditsInfoTxtView);

        containerView = findViewById(R.id.containerView);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        PAGE_TYPE = getIntent().getStringExtra("PAGE_TYPE");

        btn_type2.setId(Utils.generateViewId());

        btn_type2.setOnClickListener(new setOnClickList());
        backImgView.setOnClickListener(new setOnClickList());
        wicketKeeperArea.setOnClickListener(new setOnClickList());
        batsManArea.setOnClickListener(new setOnClickList());
        allRounderArea.setOnClickListener(new setOnClickList());
        bowlerArea.setOnClickListener(new setOnClickList());
        otherArea.setOnClickListener(new setOnClickList());


        new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 4), getResources().getColor(R.color.appThemeColor_1), otherImgView);

        new CreateRoundedView(Color.parseColor("#BBBBBB"), Utils.dipToPixels(getActContext(), 15), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), batsManCountTxtView);

        new CreateRoundedView(Color.parseColor("#BBBBBB"), Utils.dipToPixels(getActContext(), 15), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), wicketCountTxtView);
        new CreateRoundedView(Color.parseColor("#BBBBBB"), Utils.dipToPixels(getActContext(), 15), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), allRounderCountTxtView);
        new CreateRoundedView(Color.parseColor("#BBBBBB"), Utils.dipToPixels(getActContext(), 15), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), bowlerCountTxtView);
        new CreateRoundedView(getResources().getColor(R.color.appThemeColor_TXT_1), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), nextTxtView);

        adapter = new TeamPlayerListAdapter(getActContext(), listWicketKeeperData, generalFunc, false);

        playerListRecyclerView.setAdapter(adapter);

        getMatchData();

        setLabels();

        adapter.countTotalPlayersTxtView = countTotalPlayersTxtView;
        adapter.countTotalCreditsInfoTxtView = countTotalCreditsInfoTxtView;
        nextTxtView.setOnClickListener(new setOnClickList());
    }

    public void setLabels() {
        if(getIntent().getStringExtra("iUserTeamId") == null){

            titleTxt.setText("CREATE TEAM");
            btn_type2.setText("Create Team");
        }else{

            titleTxt.setText("EDIT TEAM");
            btn_type2.setText("Edit Team");
        }
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
        if(getIntent().getStringExtra("iUserTeamId") != null){
            parameters.put("iUserTeamId", getIntent().getStringExtra("iUserTeamId"));
        }

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

//                        JSONObject obj_msg = generalFunc.getJsonObject(Utils.message_str, responseString);
                        JSONObject obj_matchData = generalFunc.getJsonObject("MatchData", responseString);
                        obj_userData = generalFunc.getJsonObject("MemberData", responseString);
//                        totalAvailCredit = GeneralFunctions.parseDouble(0, generalFunc.getJsonValue("WalletBalance", obj_userData));
                        totalAvailCredit = 100;
//                        OrigAvailCredit = GeneralFunctions.parseDouble(0, generalFunc.getJsonValue("WalletBalance", obj_userData));
                        OrigAvailCredit = 100;

                        if (obj_matchData != null) {
                            setMatchData(obj_matchData);
                        }

                        countTotalCreditsInfoTxtView.setText("CREDITS LEFT " + (int) totalAvailCredit + "/" + (int) totalAvailCredit);
                        setSelectedPlayerList(generalFunc.getJsonArray("PlayersOfTeam", responseString));
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

        int countOfSelectedBowler = 0;
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
                mapData.put("tPoints", "" + generalFunc.getJsonValue("tPoints", obj_temp));
                mapData.put("tCredits", "" + generalFunc.getJsonValue("tCredits", obj_temp));
                mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                listBowlerData.add(mapData);
                listOfAllPlayersData.add(mapData);

                if(adapter.chosenPlayersList.contains(generalFunc.getJsonValue("iPlayerId", obj_temp))){
                    int countOfPlayer = GeneralFunctions.parseInt(0,bowlerCountTxtView.getText().toString());
                    bowlerCountTxtView.setText(""+(countOfPlayer+1));


                    totalAvailCredit = totalAvailCredit - GeneralFunctions.parseDouble(0.0, generalFunc.getJsonValue("tCredits", obj_temp));

                    countOfSelectedBowler = countOfSelectedBowler + 1;

                    if(countOfSelectedBowler >= 5){

                        new CreateRoundedView(Color.parseColor("#32CD32"), Utils.dipToPixels(getActContext(), 15), Utils.dipToPixels(getActContext(), 0),getResources().getColor(R.color.appThemeColor_1), bowlerCountTxtView);
                    }
                }
            }
        }


        int countOfSelectedBatsMan = 0;
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
                mapData.put("tPoints", "" + generalFunc.getJsonValue("tPoints", obj_temp));
                mapData.put("tCredits", "" + generalFunc.getJsonValue("tCredits", obj_temp));
                mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                listBastManData.add(mapData);
                listOfAllPlayersData.add(mapData);

                if(adapter.chosenPlayersList.contains(generalFunc.getJsonValue("iPlayerId", obj_temp))){
                    int countOfPlayer = GeneralFunctions.parseInt(0,batsManCountTxtView.getText().toString());
                    batsManCountTxtView.setText(""+(countOfPlayer+1));

                    totalAvailCredit = totalAvailCredit - GeneralFunctions.parseDouble(0.0, generalFunc.getJsonValue("tCredits", obj_temp));

                    countOfSelectedBatsMan = countOfSelectedBatsMan + 1;

                    if(countOfSelectedBatsMan >= 5){

                        new CreateRoundedView(Color.parseColor("#32CD32"), Utils.dipToPixels(getActContext(), 15), Utils.dipToPixels(getActContext(), 0),getResources().getColor(R.color.appThemeColor_1), batsManCountTxtView);
                    }
                }
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
                mapData.put("tPoints", "" + generalFunc.getJsonValue("tPoints", obj_temp));
                mapData.put("tCredits", "" + generalFunc.getJsonValue("tCredits", obj_temp));
                mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                listWicketKeeperData.add(mapData);
                listOfAllPlayersData.add(mapData);

                if(adapter.chosenPlayersList.contains(generalFunc.getJsonValue("iPlayerId", obj_temp))){
                    int countOfPlayer = GeneralFunctions.parseInt(0,wicketCountTxtView.getText().toString());
                    wicketCountTxtView.setText(""+(countOfPlayer+1));

                    totalAvailCredit = totalAvailCredit - GeneralFunctions.parseDouble(0.0, generalFunc.getJsonValue("tCredits", obj_temp));

                    new CreateRoundedView(Color.parseColor("#32CD32"), Utils.dipToPixels(getActContext(), 15), Utils.dipToPixels(getActContext(), 0),getResources().getColor(R.color.appThemeColor_1), wicketCountTxtView);
                }
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
                mapData.put("tPoints", "" + generalFunc.getJsonValue("tPoints", obj_temp));
                mapData.put("tCredits", "" + generalFunc.getJsonValue("tCredits", obj_temp));
                mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                listOtherData.add(mapData);
                listOfAllPlayersData.add(mapData);

            }
        }

        int countOfSelectedAllRounder = 0;
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
                mapData.put("tPoints", "" + generalFunc.getJsonValue("tPoints", obj_temp));
                mapData.put("tCredits", "" + generalFunc.getJsonValue("tCredits", obj_temp));
                mapData.put("TYPE", "" + adapter.TYPE_ITEM);

                listAllRounderData.add(mapData);
                listOfAllPlayersData.add(mapData);

                if(adapter.chosenPlayersList.contains(generalFunc.getJsonValue("iPlayerId", obj_temp))){
                    int countOfPlayer = GeneralFunctions.parseInt(0,allRounderCountTxtView.getText().toString());
                    allRounderCountTxtView.setText(""+(countOfPlayer+1));


                    totalAvailCredit = totalAvailCredit - GeneralFunctions.parseDouble(0.0, generalFunc.getJsonValue("tCredits", obj_temp));

                    countOfSelectedAllRounder = countOfSelectedAllRounder + 1;

                    if(countOfSelectedAllRounder >= 3){

                        new CreateRoundedView(Color.parseColor("#32CD32"), Utils.dipToPixels(getActContext(), 15), Utils.dipToPixels(getActContext(), 0),getResources().getColor(R.color.appThemeColor_1), allRounderCountTxtView);
                    }
                }
            }
        }

        adapter.setAllPlayersList(listOfAllPlayersData);
    }

    public void setSelectedPlayerList(JSONArray arrSelectedPlayers){
        if(arrSelectedPlayers == null){
            return;
        }
        for (int i=0;i<arrSelectedPlayers.length();i++) {
            JSONObject obj_temp = generalFunc.getJsonObject(arrSelectedPlayers,i);
            String iPlayerId = generalFunc.getJsonValue("iPlayerId",obj_temp);
            if(!adapter.chosenPlayersList.contains(iPlayerId)){
                adapter.chosenPlayersList.add(iPlayerId);
                adapter.totalSelectedPlayers = adapter.totalSelectedPlayers + 1;
            }
        }
        countTotalPlayersTxtView.setText("PLAYERS " + adapter.chosenPlayersList.size() + "/11");
        countTotalCreditsInfoTxtView.setText("CREDITS LEFT " + String.format("%.2f", totalAvailCredit) + "/" + (int) OrigAvailCredit);
        adapter.notifyDataSetChanged();
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
            } else if (i == nextTxtView.getId()) {
                checkData();
            } else if (i == wicketKeeperArea.getId()) {
                selectedType = "Keeper";
                hintPlayRoleTxtView.setText("Pick 1 Wicket-Keeper");
                adapter.totalSizeForSelection = 1;
                adapter.countSelectionTxtView = wicketCountTxtView;

                adapter.totalSelectedPlayers = 0;
                for (int j = 0; j < listWicketKeeperData.size(); j++) {
                    if (adapter.chosenPlayersList.contains(listWicketKeeperData.get(j).get("iPlayerId"))) {
                        adapter.totalSelectedPlayers = adapter.totalSelectedPlayers + 1;
                    }
                }

                adapter.setDataList(listWicketKeeperData);
                adapter.notifyDataSetChanged();
                setSelectedType("Wicket");
            } else if (i == batsManArea.getId()) {
                selectedType = "BatsMan";
                hintPlayRoleTxtView.setText("Pick 3 to 5 BatsMan");
                adapter.totalSizeForSelection = 5;
                adapter.countSelectionTxtView = batsManCountTxtView;
                adapter.totalSelectedPlayers = 0;
                for (int j = 0; j < listBastManData.size(); j++) {
                    if (adapter.chosenPlayersList.contains(listBastManData.get(j).get("iPlayerId"))) {
                        adapter.totalSelectedPlayers = adapter.totalSelectedPlayers + 1;
                    }
                }

                adapter.setDataList(listBastManData);
                adapter.notifyDataSetChanged();
                setSelectedType("Batsman");
            } else if (i == allRounderArea.getId()) {
                selectedType = "AllRounder";
                hintPlayRoleTxtView.setText("Pick 1 to 3 AllRounder");
                adapter.totalSizeForSelection = 3;
                adapter.countSelectionTxtView = allRounderCountTxtView;
                adapter.totalSelectedPlayers = 0;
                for (int j = 0; j < listAllRounderData.size(); j++) {
                    if (adapter.chosenPlayersList.contains(listAllRounderData.get(j).get("iPlayerId"))) {
                        adapter.totalSelectedPlayers = adapter.totalSelectedPlayers + 1;
                    }
                }

                adapter.setDataList(listAllRounderData);
                adapter.notifyDataSetChanged();
                setSelectedType("AllRounder");
            } else if (i == bowlerArea.getId()) {
                selectedType = "Bowlers";
                hintPlayRoleTxtView.setText("Pick 3 to 5 Bowlers");
                adapter.totalSizeForSelection = 5;
                adapter.countSelectionTxtView = bowlerCountTxtView;
                adapter.totalSelectedPlayers = 0;
                for (int j = 0; j < listBowlerData.size(); j++) {
                    if (adapter.chosenPlayersList.contains(listBowlerData.get(j).get("iPlayerId"))) {
                        adapter.totalSelectedPlayers = adapter.totalSelectedPlayers + 1;
                    }
                }
                adapter.setDataList(listBowlerData);
                adapter.notifyDataSetChanged();
                setSelectedType("Bowler");
            } else if (i == otherArea.getId()) {
                hintPlayRoleTxtView.setText("");
                adapter.setDataList(listOtherData);
                adapter.notifyDataSetChanged();
            }
        }
    }


    public void setSelectedType(String eType) {
        if (eType.equalsIgnoreCase("Wicket")) {

            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 4), getResources().getColor(R.color.appThemeColor_1), wicketKeeperImgView);


            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), batsManImgView);

            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), allRounderImgView);

            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), bowlerImgView);
        }

        if (eType.equalsIgnoreCase("Batsman")) {

            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), wicketKeeperImgView);


            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 4), getResources().getColor(R.color.appThemeColor_1), batsManImgView);

            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), allRounderImgView);

            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), bowlerImgView);
        }


        if (eType.equalsIgnoreCase("AllRounder")) {

            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), wicketKeeperImgView);


            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), batsManImgView);

            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 4), getResources().getColor(R.color.appThemeColor_1), allRounderImgView);

            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), bowlerImgView);
        }


        if (eType.equalsIgnoreCase("Bowler")) {

            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), wicketKeeperImgView);


            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), batsManImgView);

            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 0), getResources().getColor(R.color.appThemeColor_1), allRounderImgView);

            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 30), Utils.dipToPixels(getActContext(), 4), getResources().getColor(R.color.appThemeColor_1), bowlerImgView);
        }
    }

    public void checkData() {

        if (adapter.chosenPlayersList.size() < 11) {
            generalFunc.showGeneralMessage("", "Please choose players.");
            return;
        }

        if (/*totalAvailCredit == 0 || */totalAvailCredit < 0) {
            generalFunc.showGeneralMessage("", "You don't have much credits to continue. Please choose another players");
            return;
        }
        Bundle bn = new Bundle();
        bn.putString("iMatchId", getIntent().getStringExtra("iMatchId"));
        bn.putString("SELECTED_PLAYER_LIST", TextUtils.join(",", adapter.chosenPlayersList));
        bn.putString("PAGE_TYPE", PAGE_TYPE);
        (new StartActProcess(getActContext())).startActForResult(ChooseCaptainActivity.class, bn, Utils.CHOOSE_CAPTAIN_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.CHOOSE_CAPTAIN_REQ_CODE && resultCode == RESULT_OK) {
            saveTeam(data.getStringExtra("CAPTAIN_ID"), data.getStringExtra("VICE_CAPTAIN_ID"));
        }
    }

    public void saveTeam(String CAPTAIN_ID, String VICE_CAPTAIN_ID) {
        String listOfPlayerIds = TextUtils.join(",", adapter.chosenPlayersList);

        Utils.printLog("listOfPlayerIds", "::" + listOfPlayerIds + "::CAPTAIN_ID::" + CAPTAIN_ID + "::VICE_CAPTAIN_ID::" + VICE_CAPTAIN_ID);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "saveContestTeam");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("iMatchId", getIntent().getStringExtra("iMatchId"));
        parameters.put("iContestId", getIntent().getStringExtra("iConstestId"));
        if(getIntent().getStringExtra("iUserTeamId") != null){
            parameters.put("iUserTeamId", getIntent().getStringExtra("iUserTeamId"));
        }
        parameters.put("SelectedPlayerList", listOfPlayerIds);
        parameters.put("CAPTAIN_ID", CAPTAIN_ID);
        parameters.put("VICE_CAPTAIN_ID", VICE_CAPTAIN_ID);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(final String responseString) {

                if (responseString != null && !responseString.equals("")) {
                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail) {

                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                        generateAlert.setCancelable(false);
                        generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                            @Override
                            public void handleBtnClick(int btn_id) {
                                (new StartActProcess(getActContext())).setOkResult();
                                backImgView.performClick();
                            }
                        });

                        generateAlert.setContentMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));
                        generateAlert.setPositiveBtn("OK");
                        generateAlert.showAlertBox();


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
}
