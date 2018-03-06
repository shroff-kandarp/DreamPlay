package com.dreamplay;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adapter.QuestionAnswerEAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionAnswerActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    ExpandableListView expandableList;

    QuestionAnswerEAdapter adapter;

    private int lastExpandedPosition = -1;
    ErrorView errorView;
    MTextView noHelpTxt;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);

        generalFunc = new GeneralFunctions(getActContext());


        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        loading = (ProgressBar) findViewById(R.id.loading);
        noHelpTxt = (MTextView) findViewById(R.id.noHelpTxt);
        errorView = (ErrorView) findViewById(R.id.errorView);

        expandableList = (ExpandableListView) findViewById(R.id.list);

        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        adapter = new QuestionAnswerEAdapter(getActContext(), listDataHeader, listDataChild,expandableList);

        expandableList.setAdapter(adapter);


        expandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        backImgView.setOnClickListener(new setOnClickList());

        setData();

        getCategoryQuestions();
    }

    public void setData() {

        titleTxt.setText(getIntent().getStringExtra("vTitle"));

    }


    public void closeLoader() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
        }
    }

    public void getCategoryQuestions() {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
        if (loading.getVisibility() != View.VISIBLE) {
            loading.setVisibility(View.VISIBLE);
        }

        if (listDataHeader.size() > 0) {
            listDataHeader.clear();
        }
        if (listDataChild.size() > 0) {
            listDataChild.clear();
        }

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getFAQData");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("iFaqcategoryId", getIntent().getStringExtra("iFaqcategoryId"));

        noHelpTxt.setVisibility(View.GONE);

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                noHelpTxt.setVisibility(View.GONE);

                if (responseString != null && !responseString.equals("")) {

                    closeLoader();

                    if (generalFunc.checkDataAvail(Utils.action_str, responseString) == true) {

                        JSONArray obj_arr = generalFunc.getJsonArray(Utils.message_str, responseString);

                        for (int i = 0; i < obj_arr.length(); i++) {

                            JSONObject obj_temp = generalFunc.getJsonObject(obj_arr, i);


                            listDataHeader.add(generalFunc.getJsonValue("tTitle", obj_temp.toString()));

                            List<String> answer = new ArrayList<String>();
                            answer.add(generalFunc.getJsonValue("tAnswer", obj_temp.toString()));

                            listDataChild.put(listDataHeader.get(i), answer);

                        }

                        adapter.notifyDataSetChanged();

                    } else {
                        noHelpTxt.setText("No data available");
                        noHelpTxt.setVisibility(View.VISIBLE);
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
                getCategoryQuestions();
            }
        });
    }


    public Context getActContext() {
        return QuestionAnswerActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(QuestionAnswerActivity.this);
            switch (view.getId()) {
                case R.id.backImgView:
                    QuestionAnswerActivity.super.onBackPressed();
                    break;

            }
        }
    }
}
