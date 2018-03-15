package com.dreamplay;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adapter.TabAdapter;
import com.fragments.FantasyPointsFragment;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FantasyPointsActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    ProgressBar loading;
    MTextView noPointsTxtView;
    ErrorView errorView;
    ViewPager viewPager;

    TabLayout tabLayout;

    public ArrayList<HashMap<String, String>> fantasyPointsCategoryList = new ArrayList<>();
    public ArrayList<HashMap<String, String>> matchCategoryList = new ArrayList<>();
    public ArrayList<HashMap<String, String>> pointsTypesList = new ArrayList<>();
    public ArrayList<HashMap<String, String>> fantasyPointsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fantasy_points);

        generalFunc = new GeneralFunctions(getActContext());


        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);

        loading = (ProgressBar) findViewById(R.id.loading);
        noPointsTxtView = (MTextView) findViewById(R.id.noPointsTxtView);
        errorView = (ErrorView) findViewById(R.id.errorView);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

//        getFantasyPoints//iMemberId
//        http://ec2-18-220-193-12.us-east-2.compute.amazonaws.com/phpMyAdmin/sql.php?db=dream_play&table=points_type&pos=0

//        https://drive.google.com/file/d/1-5Ha2zB_0SIV5W7IsHOZb8V-1ITIr8Cs/view?usp=sharing

        backImgView.setOnClickListener(new setOnClickList());


        getFantasyData();
        setLabels();

    }

    public void setLabels() {
        titleTxt.setText("FANTASY POINTS");
    }


    public void closeLoader() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
        }
    }

    public void getFantasyData() {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
        if (loading.getVisibility() != View.VISIBLE) {
            loading.setVisibility(View.VISIBLE);
        }

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getFantasyPoints");
        parameters.put("iMemberId", generalFunc.getMemberId());

        noPointsTxtView.setVisibility(View.GONE);

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                noPointsTxtView.setVisibility(View.GONE);

                if (responseString != null && !responseString.equals("")) {

                    closeLoader();

                    if (generalFunc.checkDataAvail(Utils.action_str, responseString) == true) {

                        JSONArray obj_arr = generalFunc.getJsonArray(Utils.message_str, responseString);
                        JSONArray fantasyPointsCategory_arr = generalFunc.getJsonArray("FantasyPointsCategory", responseString);
                        JSONArray matchCategory_arr = generalFunc.getJsonArray("MatchCategory", responseString);
                        JSONArray pointsType_arr = generalFunc.getJsonArray("PointsType", responseString);
                        JSONArray fantasyPoints_arr = generalFunc.getJsonArray("FantasyPoints", responseString);


                        for(int i=0;i<fantasyPoints_arr.length();i++){
                            JSONObject obj_temp = generalFunc.getJsonObject(fantasyPoints_arr, i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("eType", generalFunc.getJsonValue("eType", obj_temp.toString()));
                            map.put("iFantasyPointsCategoryId", generalFunc.getJsonValue("iFantasyPointsCategoryId", obj_temp.toString()));
                            map.put("iMatchCategoryId", generalFunc.getJsonValue("iMatchCategoryId", obj_temp.toString()));
                            map.put("iPointId", generalFunc.getJsonValue("iPointId", obj_temp.toString()));
                            map.put("iPointTypeId", generalFunc.getJsonValue("iPointTypeId", obj_temp.toString()));
                            map.put("dPoint", generalFunc.getJsonValue("dPoint", obj_temp.toString()));

                            fantasyPointsList.add(map);
                        }
                        for(int i=0;i<matchCategory_arr.length();i++){
                            JSONObject obj_temp = generalFunc.getJsonObject(matchCategory_arr, i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("vName", generalFunc.getJsonValue("vName", obj_temp.toString()));
                            map.put("iMatchCategoryId", generalFunc.getJsonValue("iMatchCategoryId", obj_temp.toString()));

                            matchCategoryList.add(map);
                        }

                        for(int i=0;i<pointsType_arr.length();i++){
                            JSONObject obj_temp = generalFunc.getJsonObject(pointsType_arr, i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("vName", generalFunc.getJsonValue("vName", obj_temp.toString()));
                            map.put("iPointTypeId", generalFunc.getJsonValue("iPointTypeId", obj_temp.toString()));
                            map.put("eType", generalFunc.getJsonValue("eType", obj_temp.toString()));

                            pointsTypesList.add(map);
                        }

                        final TabAdapter adapter = new TabAdapter(getSupportFragmentManager());

                        for (int i = 0; i < fantasyPointsCategory_arr.length(); i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(fantasyPointsCategory_arr, i);

                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("vName", generalFunc.getJsonValue("vName", obj_temp.toString()));
                            map.put("iFantasyPointsCategoryId", generalFunc.getJsonValue("iFantasyPointsCategoryId", obj_temp.toString()));
                            map.put("tDescription", generalFunc.getJsonValue("tDescription", obj_temp.toString()));
//                            map.put("QUESTION_LIST", obj_temp.toString());

                            FantasyPointsFragment frag = new FantasyPointsFragment();
                            Bundle bn = new Bundle();
                            bn.putString("iFantasyPointsCategoryPositionId",""+i);
                            frag.setArguments(bn);
                            adapter.addFrag(frag, map.get("vName"));

                            fantasyPointsCategoryList.add(map);
                        }

                        viewPager.setAdapter(adapter);
                        tabLayout.setupWithViewPager(viewPager);
                        viewPager.setVisibility(View.VISIBLE);
                        tabLayout.setVisibility(View.VISIBLE);

                    } else {
                        noPointsTxtView.setText("No data available");
                        noPointsTxtView.setVisibility(View.VISIBLE);
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
                getFantasyData();
            }
        });
    }

    public Context getActContext() {
        return FantasyPointsActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(FantasyPointsActivity.this);
            switch (view.getId()) {
                case R.id.backImgView:
                    FantasyPointsActivity.super.onBackPressed();
                    break;

            }
        }
    }
}
