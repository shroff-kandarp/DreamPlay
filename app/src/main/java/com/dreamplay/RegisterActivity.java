package com.dreamplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.LoginWithFacebook;
import com.general.files.LoginWithGoogle;
import com.general.files.StartActProcess;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends BaseActivity {
    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    MTextView goToSignInTxtView;
    ImageView backImgView;
    CircleImageView hImgView;
    MaterialEditText nameBox;
    MaterialEditText emailBox;
    MaterialEditText mobileBox;
    MaterialEditText passwordBox;
    MButton btn_type2;

    View facebookArea;
    View googleArea;
    View inviteArea;
    CallbackManager mCallbackManager;

    LoginWithGoogle loginWithGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        generalFunc = new GeneralFunctions(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        goToSignInTxtView = (MTextView) findViewById(R.id.goToSignInTxtView);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        backImgView.setOnClickListener(new setOnClickList());

        hImgView = (CircleImageView) findViewById(R.id.hImgView);
        nameBox = (MaterialEditText) findViewById(R.id.nameBox);
        emailBox = (MaterialEditText) findViewById(R.id.emailBox);
        mobileBox = (MaterialEditText) findViewById(R.id.mobileBox);
        passwordBox = (MaterialEditText) findViewById(R.id.passwordBox);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();

        inviteArea = findViewById(R.id.inviteArea);
        facebookArea = findViewById(R.id.facebookArea);
        googleArea = findViewById(R.id.googleArea);

        btn_type2.setId(Utils.generateViewId());
        mCallbackManager = CallbackManager.Factory.create();

        backImgView.setOnClickListener(new setOnClickList());
        googleArea.setOnClickListener(new setOnClickList());
        facebookArea.setOnClickListener(new setOnClickList());
        btn_type2.setOnClickListener(new setOnClickList());
        goToSignInTxtView.setOnClickListener(new setOnClickList());

        btn_type2.setAlpha((float) 0.85);

        setData();

        new CreateRoundedView(getResources().getColor(R.color.appThemeColor_2), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 1), getResources().getColor(R.color.appThemeColor_2), btn_type2);
        new CreateRoundedView(getResources().getColor(android.R.color.transparent), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 1), Color.parseColor("#333333"), inviteArea);

    }

    public void setData() {
        nameBox.setBothText("Name", "Enter your name");
        emailBox.setBothText("Email", "Enter your email");
        mobileBox.setBothText("Mobile", "Enter your mobile no");
        passwordBox.setBothText("Password", "Enter your password");

        titleTxt.setText("Register & Play");

        btn_type2.setText("Register");
        emailBox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT);
        mobileBox.setInputType(InputType.TYPE_CLASS_NUMBER);

        passwordBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordBox.setTypeface(generalFunc.getDefaultFont(getActContext()));

        nameBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        emailBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mobileBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        passwordBox.setImeOptions(EditorInfo.IME_ACTION_DONE);

        mobileBox.setHelperText("You will receive an OTP for verification");
        mobileBox.setHelperTextAlwaysShown(true);
        emailBox.setHelperText("No spam. We promise!");
        emailBox.setHelperTextAlwaysShown(true);
        passwordBox.setHelperText("Minimum " + Utils.minPasswordLength + " characters long");
        passwordBox.setHelperTextAlwaysShown(true);
    }

    public Context getActContext() {
        return RegisterActivity.this;
    }

    public void checkData() {
        boolean nameEntered = Utils.checkText(nameBox) ? true : Utils.setErrorFields(nameBox, "Required");
        boolean emailEntered = Utils.checkText(emailBox) ?
                (generalFunc.isEmailValid(Utils.getText(emailBox)) ? true : Utils.setErrorFields(emailBox, "Invalid email"))
                : Utils.setErrorFields(emailBox, "Required");
        boolean mobileEntered = Utils.checkText(mobileBox) ? (Utils.getText(mobileBox).toString().length() != 10 ? Utils.setErrorFields(mobileBox, "Mobile must should be 10 digits long.") : true) : Utils.setErrorFields(mobileBox, "Required");

        boolean passwordEntered = Utils.checkText(passwordBox) ?
                (Utils.getText(passwordBox).contains(" ") ? Utils.setErrorFields(passwordBox, "Password should not contain whitespace.")
                        : (Utils.getText(passwordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(passwordBox, "Password must be" + " " + Utils.minPasswordLength + " or more character long.")))
                : Utils.setErrorFields(passwordBox, "Required");

        if (nameEntered == false || emailEntered == false || mobileEntered == false
                || passwordEntered == false) {
            return;
        }

        registerUser(Utils.getText(nameBox), Utils.getText(mobileBox), "", "", Utils.getText(passwordBox), Utils.getText(emailBox), "");
    }

    public void registerUser(String name, String mobile, String countryCode, String countryId, String password, String email, String id) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "registerUser");
        parameters.put("vName", name);
        parameters.put("vMobile", mobile);
        parameters.put("vCountry", countryCode);
        parameters.put("iCountryId", countryId);
        parameters.put("vPassword", password);
        parameters.put("vEmail", email);
        parameters.put("vSocialId", id);
        parameters.put("eRegisterFrom", "");

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setIsDeviceTokenGenerate(true, "vDeviceToken");
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(final String responseString) {

                Utils.printLog("ResponseData", "Data::" + responseString);

                if (responseString != null && !responseString.equals("")) {
                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail) {

                        generalFunc.storeUserData(generalFunc.getJsonValue(Utils.message_str, responseString));
                        (new StartActProcess(getActContext())).startAct(MainActivity.class);
                        ActivityCompat.finishAffinity((Activity) getActContext());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.GOOGLE_SIGN_IN_REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            loginWithGoogle.handleSignInResult(result);
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Bundle bn = new Bundle();
            if (view.getId() == backImgView.getId()) {
                RegisterActivity.super.onBackPressed();
            } else if (view.getId() == btn_type2.getId()) {
                checkData();
            } else if (view.getId() == facebookArea.getId()) {
                new LoginWithFacebook(getActContext(), mCallbackManager);
            } else if (view.getId() == googleArea.getId()) {
                loginWithGoogle = new LoginWithGoogle(getActContext());
            } else if (view.getId() == goToSignInTxtView.getId()) {
                if (getIntent().getStringExtra("isFromSignIn") != null) {
                    backImgView.performClick();
                } else {
                    bn.putString("isFromRegister", "Yes");
                    (new StartActProcess(getActContext())).startActWithData(SignInActivity.class, bn);
                }
            }
        }
    }
}
