package com.dreamplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.LoginWithFacebook;
import com.general.files.LoginWithGoogle;
import com.general.files.StartActProcess;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.PhoneAuthProvider;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignInActivity extends BaseActivity {
    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    MTextView goToregisterTxtView;
    MTextView forgetPassTxtView;
    ImageView backImgView;
    CircleImageView hImgView;
    MaterialEditText emailBox;
    MaterialEditText passBox;
    MButton btn_type2;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    View facebookArea;
    View googleArea;


    boolean isPasswordBoxSet = true;

    ImageView showPassImgView;
    CallbackManager mCallbackManager;

    LoginWithGoogle loginWithGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        generalFunc = new GeneralFunctions(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        goToregisterTxtView = (MTextView) findViewById(R.id.goToregisterTxtView);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        hImgView = (CircleImageView) findViewById(R.id.hImgView);
        emailBox = (MaterialEditText) findViewById(R.id.emailBox);
        passBox = (MaterialEditText) findViewById(R.id.passBox);
        forgetPassTxtView = (MTextView) findViewById(R.id.forgetPassTxtView);
        showPassImgView = (ImageView) findViewById(R.id.showPassImgView);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        facebookArea = findViewById(R.id.facebookArea);
        googleArea = findViewById(R.id.googleArea);
        btn_type2.setId(Utils.generateViewId());

        btn_type2.setOnClickListener(new setOnClickList());
        facebookArea.setOnClickListener(new setOnClickList());
        googleArea.setOnClickListener(new setOnClickList());
        forgetPassTxtView.setOnClickListener(new setOnClickList());
        backImgView.setOnClickListener(new setOnClickList());
        showPassImgView.setOnClickListener(new setOnClickList());
        goToregisterTxtView.setOnClickListener(new setOnClickList());
        titleTxt.setText("SignIn");
        btn_type2.setAlpha((float) 0.85);
        setData();

        mCallbackManager = CallbackManager.Factory.create();


        new CreateRoundedView(getResources().getColor(R.color.appThemeColor_2), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 1), getResources().getColor(R.color.appThemeColor_2), btn_type2);


        passBox.addTextChangedListener(new TextWatcher() {
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
    }

    public void setData() {
        emailBox.setBothText("Email Or Mobile", "Enter your email or mobile no");
//        emailBox.setBothText("Email", "Enter your email");
        passBox.setBothText("Password", "Enter your password");
        btn_type2.setText("Sign In");
        emailBox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT);

        passBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passBox.setTypeface(generalFunc.getDefaultFont(getActContext()));

    }

    public Context getActContext() {
        return SignInActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Bundle bn = new Bundle();
            if (view.getId() == backImgView.getId()) {
                SignInActivity.super.onBackPressed();
            } else if (view.getId() == btn_type2.getId()) {
                checkData();
            } else if (view.getId() == facebookArea.getId()) {
                new LoginWithFacebook(getActContext(), mCallbackManager);
            } else if (view.getId() == googleArea.getId()) {
                loginWithGoogle = new LoginWithGoogle(getActContext());
            } else if (view.getId() == forgetPassTxtView.getId()) {
                (new StartActProcess(getActContext())).startAct(ForgetPassActivity.class);
            } else if (view.getId() == goToregisterTxtView.getId()) {
                if (getIntent().getStringExtra("isFromRegister") != null) {
                    backImgView.performClick();
                } else {
                    bn.putString("isFromSignIn", "Yes");
                    (new StartActProcess(getActContext())).startActWithData(RegisterActivity.class, bn);
                }
            } else if (view.getId() == showPassImgView.getId()) {

                if (!isPasswordBoxSet) {
                    isPasswordBoxSet = true;
                    passBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    isPasswordBoxSet = false;
                    passBox.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        }
    }

    public void checkData() {
        boolean emailEntered = Utils.checkText(emailBox) ?
                (generalFunc.isEmailValid(Utils.getText(emailBox)) ? true : (android.text.TextUtils.isDigitsOnly(Utils.getText(emailBox)) ? true : Utils.setErrorFields(emailBox, "Invalid email or mobile")))
                : Utils.setErrorFields(emailBox, "Required");

        boolean passwordEntered = Utils.checkText(passBox) ?
                (Utils.getText(passBox).contains(" ") ? Utils.setErrorFields(passBox, "Password should not contain whitespace.")
                        : (Utils.getText(passBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(passBox, "Password must be" + " " + Utils.minPasswordLength + " or more character long.")))
                : Utils.setErrorFields(passBox, "Required");

        if (emailEntered == false
                || passwordEntered == false) {
            return;
        }

        loginUser();
    }

    public void loginUser() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "signInUser");
        parameters.put("vPassword", Utils.getText(passBox));
        parameters.put("vEmail", Utils.getText(emailBox));

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

}
