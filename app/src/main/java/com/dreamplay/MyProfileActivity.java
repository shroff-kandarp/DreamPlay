package com.dreamplay;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.SetOnTouchList;
import com.utils.Utils;
import com.view.GenerateAlertBox;
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

public class MyProfileActivity extends BaseActivity {

    GeneralFunctions generalFunc;

    ImageView backImgView;
    MTextView titleTxt;

    MaterialEditText nameBox;
    MaterialEditText dobBox;
    MaterialEditText addressBox;
    MaterialEditText cityBox;
    MaterialEditText stateBox;
    MaterialEditText countryBox;
    MaterialEditText mobileBox;
    MaterialEditText emailBox;

    RadioButton maleRadioBtn;
    RadioButton feMaleRadioBtn;

    ProgressBar loadingBar;

    View containerView;

    MButton btn_type2;

    boolean isDOBSelected = false;

    String iCountryId = "";
    String iStateId = "";
    android.support.v7.app.AlertDialog list_state;

    ArrayList<String> items_txt_state = new ArrayList<String>();
    ArrayList<String> items_state_ids = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        generalFunc = new GeneralFunctions(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);

        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        nameBox = (MaterialEditText) findViewById(R.id.nameBox);
        dobBox = (MaterialEditText) findViewById(R.id.dobBox);
        addressBox = (MaterialEditText) findViewById(R.id.addressBox);
        cityBox = (MaterialEditText) findViewById(R.id.cityBox);
        stateBox = (MaterialEditText) findViewById(R.id.stateBox);
        countryBox = (MaterialEditText) findViewById(R.id.countryBox);
        mobileBox = (MaterialEditText) findViewById(R.id.mobileBox);
        emailBox = (MaterialEditText) findViewById(R.id.emailBox);

        maleRadioBtn = (RadioButton) findViewById(R.id.maleRadioBtn);
        feMaleRadioBtn = (RadioButton) findViewById(R.id.feMaleRadioBtn);

        containerView = findViewById(R.id.containerView);

        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();

        btn_type2.setId(Utils.generateViewId());

        backImgView.setOnClickListener(new setOnClickList());
        dobBox.setOnClickListener(new setOnClickList());
        btn_type2.setOnClickListener(new setOnClickList());

        setLabels();
        removeInput();
        getUserData();
    }

    public void setLabels() {
        titleTxt.setText("My Profile");
        btn_type2.setText("Edit Profile");
        nameBox.setBothText("Name", "Enter your name");
        dobBox.setBothText("DOB", "Enter your date of birth");
        addressBox.setBothText("Address", "Enter your address");
        cityBox.setBothText("City", "Enter your city");
        stateBox.setBothText("State", "Select your state");
        countryBox.setBothText("Country", "Select your country");
        mobileBox.setBothText("Mobile", "Enter your mobile");
        emailBox.setBothText("Email", "Enter your email");

        Drawable icon_address = getResources().getDrawable(R.mipmap.ic_address_home);
        Bitmap bitmap = ((BitmapDrawable) icon_address).getBitmap();
        Drawable ic_addr = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, Utils.dipToPixels(getActContext(), 25), Utils.dipToPixels(getActContext(), 25), true));

        addressBox.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_addr, null);
    }

    public void removeInput() {
        Utils.removeInput(countryBox);
        countryBox.setOnTouchListener(new SetOnTouchList());
        countryBox.setOnClickListener(new setOnClickList());

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

                            if (!generalFunc.getJsonValue("eGender", obj_msg).equals("")) {
                                if (generalFunc.getJsonValue("eGender", obj_msg).equals("Male")) {
                                    maleRadioBtn.setChecked(true);
                                } else {
                                    feMaleRadioBtn.setChecked(true);
                                }
                            }

                            emailBox.setText(generalFunc.getJsonValue("vEmail", obj_msg));
                            mobileBox.setText(generalFunc.getJsonValue("vMobile", obj_msg));
                            cityBox.setText(generalFunc.getJsonValue("vCity", obj_msg));
                            addressBox.setText(generalFunc.getJsonValue("tAddress", obj_msg));

                            if (generalFunc.getJsonValue("eMobileVerified", obj_msg).equalsIgnoreCase("Yes")) {
                                mobileBox.setEnabled(false);
                            }
                            if (generalFunc.getJsonValue("eEmailVerified", obj_msg).equalsIgnoreCase("Yes")) {
                                emailBox.setEnabled(false);
                            }

                            countryBox.setText(generalFunc.getJsonValue("vCountry", obj_msg));
                            stateBox.setText(generalFunc.getJsonValue("vState", obj_msg));
                            iStateId = generalFunc.getJsonValue("iStateId", obj_msg);
                            iCountryId = generalFunc.getJsonValue("iCountryId", obj_msg);
                        }

                        buildStateList(generalFunc.getJsonArray("StateList", obj_msg));
                        containerView.setVisibility(View.VISIBLE);
                        btn_type2.setVisibility(View.VISIBLE);

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
        return MyProfileActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.backImgView) {
                MyProfileActivity.super.onBackPressed();
            } else if (i == dobBox.getId()) {
                chooseDate();
            } else if (i == stateBox.getId()) {
                if (list_state != null) {
                    list_state.show();
                }
            }else if (i == btn_type2.getId()) {
                checkData();
            }
        }
    }


    public void checkData() {
        boolean nameEntered = Utils.checkText(nameBox) ? true : Utils.setErrorFields(nameBox, "Required");
        boolean emailEntered = Utils.checkText(emailBox) ?
                (generalFunc.isEmailValid(Utils.getText(emailBox)) ? true : Utils.setErrorFields(emailBox, "Invalid email"))
                : Utils.setErrorFields(emailBox, "Required");
        boolean mobileEntered = Utils.checkText(mobileBox) ? true : Utils.setErrorFields(mobileBox, "Required");
//        boolean countryEntered = isCountrySelected ? true : false;
//        if (countryEntered == false) {
//            Utils.setErrorFields(countryBox, "Required");
//        }
        if (nameEntered == false || emailEntered == false || mobileEntered == false ) {
            return;
        }

        updateProfileData();
    }

    public void updateProfileData(){
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "updateProfileInformation");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("vName", Utils.getText(nameBox));
        parameters.put("vMobile", Utils.getText(mobileBox));
        parameters.put("vEmail", Utils.getText(emailBox));
        parameters.put("dDOB", Utils.getText(dobBox));
        parameters.put("eGender", maleRadioBtn.isChecked() ? "Male" : (feMaleRadioBtn.isChecked() ? "Female" : ""));
        parameters.put("tAddress", Utils.getText(addressBox));
        parameters.put("vCity", Utils.getText(cityBox));
        parameters.put("iCountryId", iCountryId);
        parameters.put("iStateId", iStateId);


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(final String responseString) {

                Utils.printLog("ResponseData", "Data::" + responseString);

                if (responseString != null && !responseString.equals("")) {
                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail) {

                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                        generateAlert.setCancelable(false);
                        generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                            @Override
                            public void handleBtnClick(int btn_id) {
                                if (btn_id == 1) {
                                    backImgView.performClick();
                                }
                            }
                        });
                        generateAlert.setContentMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));
                        generateAlert.setPositiveBtn("Ok");
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
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }
}
