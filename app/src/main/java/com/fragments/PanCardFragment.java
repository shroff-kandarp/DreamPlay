package com.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dreamplay.R;
import com.dreamplay.VerifyUserActivity;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.SetOnTouchList;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class PanCardFragment extends Fragment {


    View view;

    View containerView;
    MaterialEditText nameBox;
    MaterialEditText panCardNumBox;
    MaterialEditText dobBox;
    MaterialEditText stateBox;
    boolean isDOBSelected = false;
    String iStateId = "";
    android.support.v7.app.AlertDialog list_state;
    ArrayList<String> items_txt_state = new ArrayList<String>();
    ArrayList<String> items_state_ids = new ArrayList<String>();

    ProgressBar loadingBar;

    MTextView noDataTxt;

    MButton btn_type2;

    GeneralFunctions generalFunc;
    VerifyUserActivity verifyUsrAct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pan_card, container, false);

        verifyUsrAct = (VerifyUserActivity) getActivity();
        generalFunc = verifyUsrAct.generalFunc;
        noDataTxt = (MTextView) view.findViewById(R.id.noDataTxt);
        nameBox = (MaterialEditText) view.findViewById(R.id.nameBox);
        dobBox = (MaterialEditText) view.findViewById(R.id.dobBox);
        stateBox = (MaterialEditText) view.findViewById(R.id.stateBox);
        panCardNumBox = (MaterialEditText) view.findViewById(R.id.panCardNumBox);
        loadingBar = (ProgressBar) view.findViewById(R.id.loadingBar);

        containerView = view.findViewById(R.id.containerView);

        btn_type2 = ((MaterialRippleLayout) view.findViewById(R.id.btn_type2)).getChildView();

        btn_type2.setId(Utils.generateViewId());

        dobBox.setOnClickListener(new setOnClickList());
        btn_type2.setOnClickListener(new setOnClickList());
        setLabels();
        removeInput();

        getUserData();
        return view;
    }

    public void setLabels() {

        nameBox.setBothText("Name", "Enter name");
        dobBox.setBothText("DOB", "Enter date of birth");

        stateBox.setBothText("State", "Select your state");
        panCardNumBox.setBothText("Pan Number", "Enter pan card number");

    }

    public void removeInput() {

        Utils.removeInput(stateBox);
        stateBox.setOnTouchListener(new SetOnTouchList());
        stateBox.setOnClickListener(new setOnClickList());

        Utils.removeInput(dobBox);
        dobBox.setOnTouchListener(new SetOnTouchList());
        dobBox.setOnClickListener(new setOnClickList());
    }

    public void getUserData() {
        containerView.setVisibility(View.GONE);
        btn_type2.setVisibility(View.GONE);
        noDataTxt.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getUserData");
        parameters.put("iMemberId", generalFunc.getMemberId());

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

                        if (obj_msg != null) {
                            nameBox.setText(generalFunc.getJsonValue("vName", obj_msg));
                            dobBox.setText(generalFunc.getJsonValue("dDOB", obj_msg));

                            if (!dobBox.getText().toString().equals("")) {
                                isDOBSelected = true;
                            }
                            stateBox.setText(generalFunc.getJsonValue("vState", obj_msg));

                            iStateId = generalFunc.getJsonValue("iStateId", obj_msg);

                        }

                        buildStateList(generalFunc.getJsonArray("StateList", obj_msg));
                        containerView.setVisibility(View.VISIBLE);
                        btn_type2.setVisibility(View.VISIBLE);

                    } else {
                        noDataTxt.setVisibility(View.VISIBLE);
//                        generalFunc.showGeneralMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));
                    }

                } else {
                    noDataTxt.setVisibility(View.VISIBLE);
//                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }

    public void buildStateList(JSONArray stateListArr) {

        for (int i = 0; i < stateListArr.length(); i++) {
            JSONObject obj_temp = generalFunc.getJsonObject(stateListArr, i);

            items_txt_state.add(generalFunc.getJsonValue("vState", obj_temp.toString()));
            items_state_ids.add(generalFunc.getJsonValue("iStateId", obj_temp.toString()));
        }

        CharSequence[] cs_currency_txt = items_txt_state.toArray(new CharSequence[items_txt_state.size()]);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActContext());
        builder.setTitle("Select State");

        builder.setItems(cs_currency_txt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection

                if (list_state != null) {
                    list_state.dismiss();
                }

                iStateId = items_state_ids.get(item);
                stateBox.setText(items_txt_state.get(item));

            }
        });

        list_state = builder.create();

    }

    public Context getActContext() {
        return verifyUsrAct.getActContext();
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == dobBox.getId()) {
                chooseDate();
            } else if (i == stateBox.getId()) {
                if (list_state != null) {
                    list_state.show();
                }
            } else if (i == btn_type2.getId()) {
                checkData();
            }
        }
    }

    public void chooseDate() {

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        String day = dayOfMonth < 10 ? ("0" + dayOfMonth) : ("" + dayOfMonth);
                        String month = monthOfYear < 10 ? ("0" + monthOfYear) : ("" + monthOfYear);

                        dobBox.setText("" + year + "-" + month + "-" + day);
                        isDOBSelected = true;
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMaxDate(now);
        dpd.setTitle("Select Date of birth");
        dpd.show(verifyUsrAct.getFragmentManager(), "Datepickerdialog");
    }

    public void checkData() {

    }
}
