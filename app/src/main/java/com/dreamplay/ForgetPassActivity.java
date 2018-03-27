package com.dreamplay;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForgetPassActivity extends AppCompatActivity {
    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    MTextView goToSignInTxtView;
    ImageView backImgView;
    CircleImageView hImgView;
    MaterialEditText emailBox;
    MaterialEditText verificationCodeBox;
    MaterialEditText resetPassBox;
    MaterialEditText reResetPassBox;
    MButton btn_type2;

    ImageView showPassImgView;
    ImageView showRePassImgView;

    View resetPassArea;
    String verificationCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        generalFunc = new GeneralFunctions(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        goToSignInTxtView = (MTextView) findViewById(R.id.goToSignInTxtView);
        backImgView = (ImageView) findViewById(R.id.backImgView);

        showPassImgView = (ImageView) findViewById(R.id.showPassImgView);
        showRePassImgView = (ImageView) findViewById(R.id.showRePassImgView);
        hImgView = (CircleImageView) findViewById(R.id.hImgView);
        emailBox = (MaterialEditText) findViewById(R.id.emailBox);
        verificationCodeBox = (MaterialEditText) findViewById(R.id.verificationCodeBox);
        resetPassBox = (MaterialEditText) findViewById(R.id.resetPassBox);
        reResetPassBox = (MaterialEditText) findViewById(R.id.reResetPassBox);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();

        resetPassArea = findViewById(R.id.resetPassArea);

        showRePassImgView.setOnClickListener(new setOnClickList());
        showPassImgView.setOnClickListener(new setOnClickList());
        backImgView.setOnClickListener(new setOnClickList());
        btn_type2.setId(Utils.generateViewId());
        btn_type2.setOnClickListener(new setOnClickList());
        btn_type2.setAlpha((float) 0.85);


        new CreateRoundedView(getResources().getColor(R.color.appThemeColor_2), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 1), getResources().getColor(R.color.appThemeColor_2), btn_type2);

        setData();

        resetPassBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    showPassImgView.setVisibility(View.VISIBLE);
                } else {
                    showPassImgView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        reResetPassBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    showRePassImgView.setVisibility(View.VISIBLE);
                } else {
                    showRePassImgView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setData() {
        emailBox.setBothText("Email", "Enter your email");
        verificationCodeBox.setBothText("Verification Code", "Enter verification code");
        resetPassBox.setBothText("New Password", "Enter new password");
        reResetPassBox.setBothText("Re Enter Password", "Re Enter password");

        titleTxt.setText("Reset Password");

        btn_type2.setText("NEXT");
        emailBox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT);

        resetPassBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        resetPassBox.setTypeface(generalFunc.getDefaultFont(getActContext()));

        reResetPassBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        reResetPassBox.setTypeface(generalFunc.getDefaultFont(getActContext()));

        emailBox.setImeOptions(EditorInfo.IME_ACTION_DONE);
        emailBox.setHelperTextAlwaysShown(true);

    }

    public Context getActContext() {
        return ForgetPassActivity.this;
    }

    boolean isPassShown = false;
    boolean isRePassShown = false;

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Bundle bn = new Bundle();
            if (view.getId() == backImgView.getId()) {
                ForgetPassActivity.super.onBackPressed();
            } else if (view.getId() == btn_type2.getId()) {
                checkData();
            } else if (view.getId() == showPassImgView.getId()) {
                if (isPassShown) {
                    isPassShown = false;
                    resetPassBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    isPassShown = true;
                    resetPassBox.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            } else if (view.getId() == showRePassImgView.getId()) {
                if (isRePassShown) {
                    isRePassShown = false;
                    reResetPassBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    isRePassShown = true;
                    reResetPassBox.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        }
    }


    public void checkData() {
        boolean emailEntered = Utils.checkText(emailBox) ?
                (generalFunc.isEmailValid(Utils.getText(emailBox)) ? true : Utils.setErrorFields(emailBox, "Invalid email"))
                : Utils.setErrorFields(emailBox, "Required");


        if (emailEntered == false) {
            return;
        }

        if (!verificationCode.equals("")) {
            if (resetPassArea.getVisibility() == View.VISIBLE) {
                boolean passwordEntered = Utils.checkText(resetPassBox) ?
                        (Utils.getText(resetPassBox).contains(" ") ? Utils.setErrorFields(resetPassBox, "Password should not contain whitespace.")
                                : (Utils.getText(resetPassBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(resetPassBox, "Password must be" + " " + Utils.minPasswordLength + " or more character long.")))
                        : Utils.setErrorFields(resetPassBox, "Required");

                boolean rePassEntered = Utils.checkText(reResetPassBox) ? (Utils.getText(resetPassBox).toString().equals(Utils.getText(reResetPassBox)) ? true : Utils.setErrorFields(reResetPassBox, "Password doen't match. Please check again.")) : Utils.setErrorFields(reResetPassBox, "Required");

                if (passwordEntered && rePassEntered) {
                    changePassword();
                }
            } else {
                boolean isVerificationCorrect = Utils.checkText(verificationCodeBox) ? (Utils.getText(verificationCodeBox).toString().equals(verificationCode) ? true : Utils.setErrorFields(verificationCodeBox, "Invalid code.")) : Utils.setErrorFields(verificationCodeBox, "Required");

                if (isVerificationCorrect) {
                    verificationCodeBox.setVisibility(View.GONE);
                    resetPassArea.setVisibility(View.VISIBLE);
                    btn_type2.setText("Change Password");
                }
            }

            return;
        }

        resetPassword();
    }

    public void resetPassword() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "sendResetPassword");
        parameters.put("vEmail", Utils.getText(emailBox));

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setIsDeviceTokenGenerate(false, "vDeviceToken");
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(final String responseString) {

                Utils.printLog("ResponseData", "Data::" + responseString);

                if (responseString != null && !responseString.equals("")) {
                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail) {

                        emailBox.setVisibility(View.GONE);
                        verificationCodeBox.setVisibility(View.VISIBLE);

                        verificationCode = generalFunc.getJsonValue("VerificationCode", responseString);
                    }

                    generalFunc.showGeneralMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));

                } else {
                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }

    public void changePassword() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "changePassword");
        parameters.put("vEmail", Utils.getText(emailBox));
        parameters.put("newPassword", Utils.getText(resetPassBox));

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setIsDeviceTokenGenerate(false, "vDeviceToken");
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
}
