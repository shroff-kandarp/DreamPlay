package com.dreamplay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.adapter.UserMatchTeamsAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.ErrorView;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserMatchTeamsActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    ProgressBar loading;
    MTextView noTeamsTxt;

    View noTeamArea;
    RecyclerView dataRecyclerView;
    UserMatchTeamsAdapter adapter;
    ErrorView errorView;

    MButton addTeamBtn;

    ArrayList<HashMap<String, String>> list;

    public LinearLayout previewContainerView;

    boolean isOpenForSelection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_match_teams);

        generalFunc = new GeneralFunctions(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        previewContainerView = (LinearLayout) findViewById(R.id.previewContainerView);

        loading = (ProgressBar) findViewById(R.id.loading);
        noTeamsTxt = (MTextView) findViewById(R.id.noTeamsTxt);
        dataRecyclerView = (RecyclerView) findViewById(R.id.dataRecyclerView);
        errorView = (ErrorView) findViewById(R.id.errorView);
        noTeamArea = findViewById(R.id.noTeamArea);
        addTeamBtn = ((MaterialRippleLayout) (findViewById(R.id.addTeamBtn))).getChildView();

        list = new ArrayList<>();
        adapter = new UserMatchTeamsAdapter(getActContext(), list, generalFunc, false);
        dataRecyclerView.setAdapter(adapter);

        new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 10), Utils.dipToPixels(getActContext(), 1), Color.parseColor("#DEDEDE"), noTeamArea);
        getUserMatchTeams();
        setLabels();

        addTeamBtn.setId(Utils.generateViewId());

        backImgView.setOnClickListener(new setOnClickList());
        addTeamBtn.setOnClickListener(new setOnClickList());
    }


    public void setLabels() {
        titleTxt.setText("MY TEAMS");
        addTeamBtn.setText("Create Team");
    }


    public void closeLoader() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
        }
    }

    public void getUserMatchTeams() {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
        if (loading.getVisibility() != View.VISIBLE) {
            loading.setVisibility(View.VISIBLE);
        }

        if (list.size() > 0) {
            list.clear();
        }
        adapter.notifyDataSetChanged();

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getUserMatchTeams");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("iMatchId", getIntent().getStringExtra("iMatchId"));
        parameters.put("isOpenForSelection", isOpenForSelection ? "Yes" : "No");

        noTeamArea.setVisibility(View.GONE);

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                noTeamArea.setVisibility(View.GONE);

                if (responseString != null && !responseString.equals("")) {

                    closeLoader();

                    if (generalFunc.checkDataAvail(Utils.action_str, responseString) == true) {

                        JSONArray obj_arr = generalFunc.getJsonArray(Utils.message_str, responseString);


                        for (int i = 0; i < obj_arr.length(); i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(obj_arr, i);

                            JSONArray obj_players_arr = generalFunc.getJsonArray("PlayerDetails", obj_temp);
                            int wkCount = 0;
                            int batCount = 0;
                            int allCount = 0;
                            int bowlCount = 0;

                            String captainName = "";
                            String viceCaptainName = "";
                            if (obj_players_arr != null) {
                                for (int j = 0; j < obj_players_arr.length(); j++) {
                                    JSONObject obj_tmP_player = generalFunc.getJsonObject(obj_players_arr, j);

                                    String eCaptain = generalFunc.getJsonValue("eCaptain", obj_tmP_player);
                                    String eViceCaptain = generalFunc.getJsonValue("eViceCaptain", obj_tmP_player);

                                    if (eCaptain.equalsIgnoreCase("yes")) {
                                        captainName = generalFunc.getJsonValue("vPlayerName", obj_tmP_player);
                                    }
                                    if (eViceCaptain.equalsIgnoreCase("yes")) {
                                        viceCaptainName = generalFunc.getJsonValue("vPlayerName", obj_tmP_player);
                                    }

                                    String ePlayerType = generalFunc.getJsonValue("ePlayerType", obj_tmP_player);

                                    if (ePlayerType.equalsIgnoreCase("Batsman")) {
                                        batCount = batCount + 1;
                                    }
                                    if (ePlayerType.equalsIgnoreCase("Bowler")) {
                                        bowlCount = bowlCount + 1;
                                    }
                                    if (ePlayerType.equalsIgnoreCase("Wicketkeeper")) {
                                        wkCount = wkCount + 1;
                                    }
                                    if (ePlayerType.equalsIgnoreCase("Allrounder")) {
                                        allCount = allCount + 1;
                                    }
                                }
                            }
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("PlayerDetailsList", obj_players_arr != null ? obj_players_arr.toString() : "");
                            map.put("captainName", captainName);
                            map.put("viceCaptainName", viceCaptainName);
                            map.put("wkCount", "" + wkCount);
                            map.put("batCount", "" + batCount);
                            map.put("allCount", "" + allCount);
                            map.put("bowlCount", "" + bowlCount);
                            map.put("iUserTeamId", "" + generalFunc.getJsonValue("iUserTeamId", obj_temp));
                            map.put("iContestId", "" + generalFunc.getJsonValue("iContestId", obj_temp));
                            map.put("iMatchId", "" + generalFunc.getJsonValue("iMatchId", obj_temp));
                            map.put("iJoinId", "" + generalFunc.getJsonValue("iJoinId", obj_temp));
                            map.put("TYPE", "" + adapter.TYPE_ITEM);

                            list.add(map);
                        }

                        adapter.notifyDataSetChanged();

                    } else {
                        noTeamsTxt.setText(generalFunc.getJsonValue("message", responseString));
                        noTeamArea.setVisibility(View.VISIBLE);
                    }
                } else {
                    generateErrorView();
                }
            }
        });
        exeWebServer.execute();
    }

    public void generateErrorView() {

        closeLoader();

        generalFunc.generateErrorView(errorView, "Error", "Please check your internet connection OR try again later.");

        if (errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
        }
        errorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                getUserMatchTeams();
            }
        });
    }

    public Context getActContext() {
        return UserMatchTeamsActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(UserMatchTeamsActivity.this);
            int i = view.getId();
            if (i == R.id.backImgView) {
                UserMatchTeamsActivity.super.onBackPressed();

            } else if (i == addTeamBtn.getId()) {
                Bundle bn = new Bundle();
                bn.putString("iMatchId", getIntent().getStringExtra("iMatchId"));
                bn.putString("iConstestId", "");
                bn.putString("PAGE_TYPE", "");
                (new StartActProcess(getActContext())).startActForResult(CreateTeamActivity.class, bn, Utils.CREATE_TEAM_REQ_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.CREATE_TEAM_REQ_CODE && resultCode == RESULT_OK) {
            getUserMatchTeams();
        }
    }
}
