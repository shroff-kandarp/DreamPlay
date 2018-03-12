package com.dreamplay;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import org.json.JSONObject;

import java.util.HashMap;

public class CreateContestActivity extends AppCompatActivity {

    MTextView titleTxt;
    ImageView backImgView;

    ImageView leftImgView;
    ImageView rightTeamImgView;
    MTextView infoTxtView;
    MTextView leftTeamNameTxtView;
    MTextView rightTeamNameTxtView;
    MTextView dateRemainsInfoTxtView;

    MTextView entryFeeTxtView;

    MButton btn_type2;

    MaterialEditText contestNameBoxBox;
    MaterialEditText totalWinningAmountBox;
    MaterialEditText contestSizeBox;

    Switch allowFriendsWithMTeamsSwitch;


    GeneralFunctions generalFunc;
    View containerView;
    ProgressBar loadingBar;

    String PAGE_TYPE = "";

    CountDownTimer timer;

    JSONObject obj_userData;

    int CONTEST_ENTRY_PERCENTAGE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contest);

        generalFunc = new GeneralFunctions(getActContext());
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);

        entryFeeTxtView = (MTextView) findViewById(R.id.entryFeeTxtView);
        contestNameBoxBox = (MaterialEditText) findViewById(R.id.contestNameBoxBox);
        totalWinningAmountBox = (MaterialEditText) findViewById(R.id.totalWinningAmountBox);
        contestSizeBox = (MaterialEditText) findViewById(R.id.contestSizeBox);
        allowFriendsWithMTeamsSwitch = (Switch) findViewById(R.id.allowFriendsWithMTeamsSwitch);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();

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

        getMatchData();

        setLabels();
    }

    public void setLabels() {
        titleTxt.setText("CREATE CONTEST");
        btn_type2.setText("Create Contest");

        contestNameBoxBox.setBothText("Give your contest name");
        contestNameBoxBox.setHelperText("Give your contest aa cool name (optional)");

        totalWinningAmountBox.setBothText("Total winning amount (Rs)");
        totalWinningAmountBox.setHelperText("min 0 & max 10,000");

        contestSizeBox.setBothText("Contest Size");
        contestSizeBox.setHelperText("min 2 & max 100");

        totalWinningAmountBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        contestSizeBox.setInputType(InputType.TYPE_CLASS_NUMBER);

        contestNameBoxBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        totalWinningAmountBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        totalWinningAmountBox.setImeOptions(EditorInfo.IME_ACTION_DONE);

        totalWinningAmountBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int contestSize = GeneralFunctions.parseInt(0, Utils.getText(contestSizeBox));
                double winningAmount = GeneralFunctions.parseDouble(0, Utils.getText(totalWinningAmountBox));

                double amount = winningAmount / contestSize;

                if (contestSize < 1) {
                    amount = 0;
                }
                if (winningAmount < 1) {
                    amount = 0;
                }
                double finalValue = (amount * CONTEST_ENTRY_PERCENTAGE) / 100;

                entryFeeTxtView.setText("₹" + Math.round(finalValue));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        contestSizeBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int contestSize = GeneralFunctions.parseInt(0, Utils.getText(contestSizeBox));
                double winningAmount = GeneralFunctions.parseDouble(0, Utils.getText(totalWinningAmountBox));

                double amount = winningAmount / contestSize;

                if (contestSize < 1) {
                    amount = 0;
                }
                if (winningAmount < 1) {
                    amount = 0;
                }
                double finalValue = (amount * CONTEST_ENTRY_PERCENTAGE) / 100;

                entryFeeTxtView.setText("₹" + Math.round(finalValue));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public void getMatchData() {
        containerView.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getMatchContests");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("iMatchId", getIntent().getStringExtra("iMatchId"));
        parameters.put("isLoadUserData", "Yes");

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
                        CONTEST_ENTRY_PERCENTAGE = GeneralFunctions.parseInt(0, generalFunc.getJsonValue("CONTEST_ENTRY_PERCENTAGE", generalFunc.getJsonObject("ConfigurationData", obj_userData.toString())));

                        Utils.printLog("CONTEST_ENTRY_PERCENTAGE", ":" + CONTEST_ENTRY_PERCENTAGE);
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
        return CreateContestActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(CreateContestActivity.this);
            int i = view.getId();
            if (i == R.id.backImgView) {
                CreateContestActivity.super.onBackPressed();

            } else if (i == btn_type2.getId()) {

                checkData();

            }
        }
    }

    public void checkData() {
        boolean isTotalWinAmtProper = Utils.checkText(totalWinningAmountBox) ? (GeneralFunctions.parseDouble(0, Utils.getText(totalWinningAmountBox)) > 10000 ? Utils.setErrorFields(totalWinningAmountBox, "Invalid amount. Amount should be less then 10,000") : true) : Utils.setErrorFields(totalWinningAmountBox, "Required");

        boolean isContestSizeProper = Utils.checkText(contestSizeBox) ? ((GeneralFunctions.parseInt(0, Utils.getText(contestSizeBox)) > 100 || GeneralFunctions.parseInt(0, Utils.getText(contestSizeBox)) < 2) ? Utils.setErrorFields(contestSizeBox, "Invalid Size") : true) : Utils.setErrorFields(contestSizeBox, "Required");

        if (isTotalWinAmtProper == false || isContestSizeProper == false) {
            return;
        }

        openCreateTeamAct();
    }

    public void openCreateTeamAct() {
        Bundle bn = new Bundle();
        bn.putString("iMatchId", getIntent().getStringExtra("iMatchId"));
        bn.putString("PAGE_TYPE", PAGE_TYPE);
        (new StartActProcess(getActContext())).startActForResult(CreateTeamActivity.class, bn, Utils.CREATE_TEAM_REQ_CODE);
    }
}
