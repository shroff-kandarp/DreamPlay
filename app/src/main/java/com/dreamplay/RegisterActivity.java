package com.dreamplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends BaseActivity {
    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    MTextView goToSignInTxtView;
    MTextView mobileNumTxtView;
    ImageView backImgView;
    CircleImageView hImgView;
    MaterialEditText nameBox;
    MaterialEditText emailBox;
    MaterialEditText mobileBox;
    MaterialEditText passwordBox;
    MaterialEditText mobileCodeBox;
    MButton btn_type2;
    ImageView showPassImgView;

    View facebookArea;
    View googleArea;
    View inviteArea;
    View otpEnterArea;
    View mobileArea;
    CallbackManager mCallbackManager;

    LoginWithGoogle loginWithGoogle;

    boolean isPasswordBoxSet = true;

    MButton mobileVerifyBtn;
    MButton mobileVerifyCancelBtn;

    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    boolean isMobileVerified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        generalFunc = new GeneralFunctions(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        mobileNumTxtView = (MTextView) findViewById(R.id.mobileNumTxtView);
        goToSignInTxtView = (MTextView) findViewById(R.id.goToSignInTxtView);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        backImgView.setOnClickListener(new setOnClickList());

        hImgView = (CircleImageView) findViewById(R.id.hImgView);
        nameBox = (MaterialEditText) findViewById(R.id.nameBox);
        emailBox = (MaterialEditText) findViewById(R.id.emailBox);
        mobileBox = (MaterialEditText) findViewById(R.id.mobileBox);
        passwordBox = (MaterialEditText) findViewById(R.id.passwordBox);
        mobileCodeBox = (MaterialEditText) findViewById(R.id.mobileCodeBox);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        mobileVerifyBtn = ((MaterialRippleLayout) findViewById(R.id.mobileVerifyBtn)).getChildView();
        mobileVerifyCancelBtn = ((MaterialRippleLayout) findViewById(R.id.mobileVerifyCancelBtn)).getChildView();
        showPassImgView = (ImageView) findViewById(R.id.showPassImgView);

        inviteArea = findViewById(R.id.inviteArea);
        facebookArea = findViewById(R.id.facebookArea);
        googleArea = findViewById(R.id.googleArea);
        otpEnterArea = findViewById(R.id.otpEnterArea);
        mobileArea = findViewById(R.id.mobileArea);

        btn_type2.setId(Utils.generateViewId());
        mobileVerifyBtn.setId(Utils.generateViewId());
        mobileVerifyCancelBtn.setId(Utils.generateViewId());
        mCallbackManager = CallbackManager.Factory.create();

        backImgView.setOnClickListener(new setOnClickList());
        googleArea.setOnClickListener(new setOnClickList());
        facebookArea.setOnClickListener(new setOnClickList());
        btn_type2.setOnClickListener(new setOnClickList());
        showPassImgView.setOnClickListener(new setOnClickList());
        goToSignInTxtView.setOnClickListener(new setOnClickList());
        mobileVerifyBtn.setOnClickListener(new setOnClickList());
        mobileVerifyCancelBtn.setOnClickListener(new setOnClickList());

        btn_type2.setAlpha((float) 0.85);

        setData();

        new CreateRoundedView(getResources().getColor(R.color.appThemeColor_2), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 1), getResources().getColor(R.color.appThemeColor_2), btn_type2);
        new CreateRoundedView(getResources().getColor(android.R.color.transparent), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 1), Color.parseColor("#333333"), inviteArea);
        new CreateRoundedView(Color.parseColor("#e4e4e6"), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 4), Color.parseColor("#d5d5d5"), mobileArea);

    }

    public void setData() {
        nameBox.setBothText("UserName", "Enter your user name");
        emailBox.setBothText("Email", "Enter your email");
        mobileBox.setBothText("Mobile", "Enter your mobile no");
        passwordBox.setBothText("Password", "Enter your password");
        mobileCodeBox.setBothText("", "Enter Code");

        titleTxt.setText("Register & Play");

        btn_type2.setText("Register");
        mobileVerifyBtn.setText("Confirm");
        mobileVerifyCancelBtn.setText("Cancel");
        emailBox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT);
        mobileBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        mobileCodeBox.setInputType(InputType.TYPE_CLASS_NUMBER);

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

        passwordBox.addTextChangedListener(new TextWatcher() {
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

    public Context getActContext() {
        return RegisterActivity.this;
    }

    public void checkData() {
        boolean nameEntered = Utils.checkText(nameBox) ? (Utils.getText(nameBox).contains(" ") ? Utils.setErrorFields(nameBox, "UserName should not contain whitespace.") : true) : Utils.setErrorFields(nameBox, "Required");
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

        registerUser(Utils.getText(nameBox), Utils.getText(mobileBox), "", "", Utils.getText(passwordBox), Utils.getText(emailBox), "", false);
    }

    public void checkUserName(String name, String mobile, String countryCode, String countryId, String password, String email, String id) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "checkUserName");
        parameters.put("userName", name);
        parameters.put("vEmail", email);
        parameters.put("vMobile", mobile);


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
                        verifyMobileNum();

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

    public void registerUser(String name, String mobile, String countryCode, String countryId, String password, String email, String id, boolean isFromUserName) {

        if (!isFromUserName) {
            checkUserName(Utils.getText(nameBox), Utils.getText(mobileBox), "", "", Utils.getText(passwordBox), Utils.getText(emailBox), "");
            return;
        }
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
            } else if (view.getId() == showPassImgView.getId()) {

                if (!isPasswordBoxSet) {
                    isPasswordBoxSet = true;
                    passwordBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    isPasswordBoxSet = false;
                    passwordBox.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            } else if (view.getId() == goToSignInTxtView.getId()) {
                if (getIntent().getStringExtra("isFromSignIn") != null) {
                    backImgView.performClick();
                } else {
                    bn.putString("isFromRegister", "Yes");
                    (new StartActProcess(getActContext())).startActWithData(SignInActivity.class, bn);
                }
            } else if (view.getId() == mobileVerifyBtn.getId()) {
                boolean isCodeEntered = Utils.checkText(mobileCodeBox) ? true : Utils.setErrorFields(mobileCodeBox, "Required");
                if (isCodeEntered) {

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, Utils.getText(mobileCodeBox));
                    signInWithPhoneAuthCredential(credential);
                }

            } else if (view.getId() == mobileVerifyCancelBtn.getId()) {
                otpEnterArea.setVisibility(View.GONE);
            }
        }
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updatePhoneVerified();
                        } else {

                            generalFunc.showGeneralMessage("Verification Failed", "Your mobile number verification failed. Please check your mobile number or try again later.");
                        }
                    }
                });
    }

    public void verifyMobileNum() {

        mobileNumTxtView.setText("+91" + Utils.getText(mobileBox));
        mobileCodeBox.setText("");
        otpEnterArea.setVisibility(View.VISIBLE);
        if (mResendToken == null) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(mobileNumTxtView.getText().toString(), 60, TimeUnit.SECONDS, RegisterActivity.this, mCallbacks);
        } else {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(mobileNumTxtView.getText().toString(), 60, TimeUnit.SECONDS, RegisterActivity.this, mCallbacks, mResendToken);
        }
    }

    public void updatePhoneVerified() {

        registerUser(Utils.getText(nameBox), Utils.getText(mobileBox), "", "", Utils.getText(passwordBox), Utils.getText(emailBox), "", true);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Utils.printLog("Verification", ":Completed:");

            updatePhoneVerified();
        }


        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            Utils.printLog("ErrorOnVerification", "::" + e.getMessage());
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

        }


        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;
            Utils.printLog("VerificationID", "::" + mVerificationId);

        }
    };
}
