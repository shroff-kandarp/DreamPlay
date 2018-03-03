package com.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.adapter.FixturesRecyclerAdapter;
import com.dreamplay.MainActivity;
import com.dreamplay.R;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends Fragment {

    View view;
    RecyclerView dataListRecyclerView;

    MTextView noDataTxtView;
    GeneralFunctions generalFunc;

    ProgressBar dataLoader;
    FixturesRecyclerAdapter adapter;
    ArrayList<HashMap<String, String>> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.fragment_live, container, false);
//        return view;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fixtures, container, false);

        generalFunc = new GeneralFunctions(getActContext());
        dataListRecyclerView = (RecyclerView) view.findViewById(R.id.dataListRecyclerView);
        noDataTxtView = (MTextView) view.findViewById(R.id.noDataTxtView);
        dataLoader = (ProgressBar)  view.findViewById(R.id.dataLoader);

        adapter = new FixturesRecyclerAdapter(getActContext(), list, generalFunc, false);
        dataListRecyclerView.setLayoutManager(new LinearLayoutManager(getActContext()));
        dataListRecyclerView.setNestedScrollingEnabled(false);
        dataListRecyclerView.setAdapter(adapter);

        findMatches();

        return view;
    }


    public Context getActContext() {
        return ((MainActivity) getActivity()).getActContext();
    }


    public void findMatches() {
        noDataTxtView.setVisibility(View.GONE);
        list.clear();
        adapter.notifyDataSetChanged();

        dataLoader.setVisibility(View.VISIBLE);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getListOfMatches");
        parameters.put("eMatchType", "LIVE");

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setLoaderConfig(getActContext(), false, generalFunc);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(final String responseString) {

                Utils.printLog("ResponseData", "Data::" + responseString);


                if (responseString != null && !responseString.equals("")) {
                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail) {

                        JSONArray msgArr = generalFunc.getJsonArray(Utils.message_str, responseString);

                        if (msgArr != null) {

                            for (int i = 0; i < msgArr.length(); i++) {
                                JSONObject temp_obj = generalFunc.getJsonObject(msgArr, i);

                                HashMap<String, String> map = new HashMap<>();
                                map.put("iMatchId", generalFunc.getJsonValue("iMatchId", temp_obj));
                                map.put("iUniqueId", generalFunc.getJsonValue("iUniqueId", temp_obj));
                                map.put("vTeam1", generalFunc.getJsonValue("vTeam1", temp_obj));
                                map.put("tTeam1Logo", generalFunc.getJsonValue("tTeam1Logo", temp_obj));
                                map.put("eTeam1TossWon", generalFunc.getJsonValue("eTeam1TossWon", temp_obj));
                                map.put("eTeam1Won", generalFunc.getJsonValue("eTeam1Won", temp_obj));
                                map.put("vTeam2", generalFunc.getJsonValue("vTeam2", temp_obj));
                                map.put("tTeam2Logo", generalFunc.getJsonValue("tTeam2Logo", temp_obj));
                                map.put("eTeam2TossWon", generalFunc.getJsonValue("eTeam2TossWon", temp_obj));
                                map.put("eTeam2Won", generalFunc.getJsonValue("eTeam2Won", temp_obj));
                                map.put("vMatchType", generalFunc.getJsonValue("vMatchType", temp_obj));
                                map.put("dStartDate", generalFunc.getJsonValue("dStartDate", temp_obj));
                                map.put("matchStartDate", generalFunc.getJsonValue("matchStartDate", temp_obj));
                                map.put("TYPE", ""+FixturesRecyclerAdapter.TYPE_ITEM);

                                list.add(map);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            noDataTxtView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        noDataTxtView.setVisibility(View.VISIBLE);
                        generalFunc.showGeneralMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));
                    }

                } else {
                    generalFunc.showError();
                }


                dataLoader.setVisibility(View.GONE);
            }
        });
        exeWebServer.execute();
    }

}
