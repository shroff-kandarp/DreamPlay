package com.fragments;

import android.app.Activity;
import android.content.Context;
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
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import org.json.JSONObject;

import java.util.HashMap;

public class BankDetailsFragment extends Fragment {


    View view;

    View containerView;
    MaterialEditText bankNameBox;
    MaterialEditText bankBranchBox;
    MaterialEditText accNameBox;
    MaterialEditText accountNumBox;
    MaterialEditText ifscCodeBox;

    ProgressBar loadingBar;

    MTextView noDataTxt;

    MButton btn_type2;

    GeneralFunctions generalFunc;
    VerifyUserActivity verifyUsrAct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bank_details, container, false);

        verifyUsrAct = (VerifyUserActivity) getActivity();
        generalFunc = verifyUsrAct.generalFunc;
        noDataTxt = (MTextView) view.findViewById(R.id.noDataTxt);
        bankNameBox = (MaterialEditText) view.findViewById(R.id.bankNameBox);
        bankBranchBox = (MaterialEditText) view.findViewById(R.id.bankBranchBox);
        accNameBox = (MaterialEditText) view.findViewById(R.id.accNameBox);
        accountNumBox = (MaterialEditText) view.findViewById(R.id.accountNumBox);
        ifscCodeBox = (MaterialEditText) view.findViewById(R.id.ifscCodeBox);
        loadingBar = (ProgressBar) view.findViewById(R.id.loadingBar);

        containerView = view.findViewById(R.id.containerView);

        btn_type2 = ((MaterialRippleLayout) view.findViewById(R.id.btn_type2)).getChildView();

        btn_type2.setId(Utils.generateViewId());

        btn_type2.setOnClickListener(new setOnClickList());
        setLabels();

        getUserData();
        return view;
    }


    public void setLabels() {

        bankNameBox.setBothText("Bank Name", "Enter your bank name");
        bankBranchBox.setBothText("Bank branch", "Enter bank branch name");
        accNameBox.setBothText("Account Name", "Enter your account name");
        accountNumBox.setBothText("Account Number", "Enter your account number");
        ifscCodeBox.setBothText("IFSC Code", "Enter your ifsc code");
        btn_type2.setText("Submit");
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
                            bankNameBox.setText(generalFunc.getJsonValue("vBankName", obj_msg));
                            ifscCodeBox.setText(generalFunc.getJsonValue("vIFSCCode", obj_msg));
                            bankBranchBox.setText(generalFunc.getJsonValue("vBankBranchName", obj_msg));
                            accountNumBox.setText(generalFunc.getJsonValue("vMemberNameOnBank", obj_msg));
                            accountNumBox.setText(generalFunc.getJsonValue("vAccountNumber", obj_msg));
                        }

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


    public Context getActContext() {
        return verifyUsrAct.getActContext();
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == btn_type2.getId()) {
                checkData();
            }
        }
    }


    public void checkData() {

        if (Utils.checkText(bankNameBox) == false || Utils.checkText(bankBranchBox) == false || Utils.checkText(accNameBox) == false || Utils.checkText(accountNumBox) == false || Utils.checkText(ifscCodeBox) == false) {

            GeneralFunctions.showMessage(GeneralFunctions.getCurrentView((Activity) getActContext()), "All information is required.");
        } else {
            updateBankDetails();
        }
    }

    public void updateBankDetails() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "updateBankDetails");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("vMemberNameOnBank", Utils.getText(accNameBox));
        parameters.put("vAccountNumber", Utils.getText(accountNumBox));
        parameters.put("vBankBranchName", Utils.getText(bankBranchBox));
        parameters.put("vIFSCCode", Utils.getText(ifscCodeBox));
        parameters.put("vBankName", Utils.getText(bankNameBox));

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(final String responseString) {

                Utils.printLog("ResponseData", "Data::" + responseString);
                loadingBar.setVisibility(View.GONE);
                if (responseString != null && !responseString.equals("")) {
                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail) {

                        JSONObject obj_msg = generalFunc.getJsonObject(Utils.message_str, responseString);

                        getUserData();
                    }
                    generalFunc.showGeneralMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));

                } else {
                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }
}
