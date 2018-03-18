package com.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dreamplay.R;
import com.dreamplay.VerifyUserActivity;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
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
import com.view.ErrorView;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyMobileEmailFragment extends Fragment {


    View view;

    View containerView;
    MTextView noDataTxtView;
    ErrorView errorView;
    ProgressBar loadingBar;

    GeneralFunctions generalFunc;

    VerifyUserActivity verifyUsrAct;

    View mobileArea;
    ImageView emailImgView;
    MTextView mobileNumTxtView;
    MTextView emailVerifyTxtView;
    MButton mobileVerifyBtn;

    View emailArea;
    ImageView mobileImgView;
    MTextView emailTxtView;
    MTextView mobileVerifyTxtView;
    MButton emailVerifyBtn;

    String userMobileNumber = "";

    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    MaterialEditText mobileCodeBox;
    MButton mobileCodeVerifyBtn;
    View verifyMobileArea;

    View verifyEmailArea;
    MaterialEditText emailCodeBox;
    MButton emailCodeVerifyBtn;

    String emailVerificationCode = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_verify_mobile_email, container, false);

        verifyUsrAct = (VerifyUserActivity) getActivity();
        generalFunc = verifyUsrAct.generalFunc;

        containerView = view.findViewById(R.id.containerView);
        verifyMobileArea = view.findViewById(R.id.verifyMobileArea);
        noDataTxtView = (MTextView) view.findViewById(R.id.noDataTxtView);
        errorView = (ErrorView) view.findViewById(R.id.errorView);
        loadingBar = (ProgressBar) view.findViewById(R.id.loadingBar);

        mobileCodeBox = (MaterialEditText) view.findViewById(R.id.mobileCodeBox);
        mobileCodeVerifyBtn = ((MaterialRippleLayout) view.findViewById(R.id.mobileCodeVerifyBtn)).getChildView();

        emailCodeBox = (MaterialEditText) view.findViewById(R.id.emailCodeBox);
        emailCodeVerifyBtn = ((MaterialRippleLayout) view.findViewById(R.id.emailCodeVerifyBtn)).getChildView();

        verifyEmailArea = view.findViewById(R.id.verifyEmailArea);

        mobileArea = view.findViewById(R.id.mobileArea);
        mobileImgView = (ImageView) view.findViewById(R.id.mobileImgView);
        mobileNumTxtView = (MTextView) view.findViewById(R.id.mobileNumTxtView);
        mobileVerifyTxtView = (MTextView) view.findViewById(R.id.mobileVerifyTxtView);
        mobileVerifyBtn = ((MaterialRippleLayout) view.findViewById(R.id.mobileVerifyBtn)).getChildView();
        mobileVerifyBtn.setId(Utils.generateViewId());

        mobileVerifyBtn.setOnClickListener(new setOnClickList());

        emailArea = view.findViewById(R.id.emailArea);
        emailImgView = (ImageView) view.findViewById(R.id.emailImgView);
        emailTxtView = (MTextView) view.findViewById(R.id.emailTxtView);
        emailVerifyTxtView = (MTextView) view.findViewById(R.id.emailVerifyTxtView);
        emailVerifyBtn = ((MaterialRippleLayout) view.findViewById(R.id.emailVerifyBtn)).getChildView();
        emailVerifyBtn.setId(Utils.generateViewId());

        emailVerifyBtn.setOnClickListener(new setOnClickList());
        emailCodeVerifyBtn.setOnClickListener(new setOnClickList());

        new CreateRoundedView(Color.parseColor("#e4e4e6"), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 4), Color.parseColor("#d5d5d5"), mobileArea);
        new CreateRoundedView(Color.parseColor("#e4e4e6"), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 4), Color.parseColor("#d5d5d5"), emailArea);

        mobileVerifyBtn.setText("Verify");
        emailVerifyBtn.setText("Verify");
        mobileCodeVerifyBtn.setText("Verify");
        emailCodeVerifyBtn.setText("Verify");

        mobileCodeVerifyBtn.setId(Utils.generateViewId());
        mobileCodeVerifyBtn.setOnClickListener(new setOnClickList());

        emailCodeVerifyBtn.setId(Utils.generateViewId());
        emailCodeVerifyBtn.setOnClickListener(new setOnClickList());

        mobileCodeBox.setBothText("", "Verification Code");
        emailCodeBox.setBothText("", "Verification Code");
        mobileCodeBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        emailCodeBox.setInputType(InputType.TYPE_CLASS_NUMBER);

        getUserData();
        return view;
    }

    public Context getActContext() {
        return verifyUsrAct.getActContext();
    }

    public void getUserData() {
        containerView.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);

        noDataTxtView.setVisibility(View.GONE);

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

                            String vMobile = generalFunc.getJsonValue("vMobile", obj_msg);
                            String vEmail = generalFunc.getJsonValue("vEmail", obj_msg);
                            String vPhoneCode = generalFunc.getJsonValue("vPhoneCode", obj_msg);
                            String eMobileVerified = generalFunc.getJsonValue("eMobileVerified", obj_msg);
                            String eEmailVerified = generalFunc.getJsonValue("eEmailVerified", obj_msg);

                            if (eMobileVerified.equalsIgnoreCase("yes")) {
                                mobileImgView.setImageResource(R.mipmap.ic_mobile_verified);
                                ((MaterialRippleLayout) mobileVerifyBtn.getParent()).setVisibility(View.GONE);
                                mobileVerifyTxtView.setText("Your mobile number is verified.");
                            } else {
                                mobileImgView.setImageResource(R.mipmap.ic_mob_not_verify);
                                ((MaterialRippleLayout) mobileVerifyBtn.getParent()).setVisibility(View.VISIBLE);
                                mobileVerifyTxtView.setText("Your mobile number is not verified.");
                            }
                            verifyMobileArea.setVisibility(View.GONE);
                            verifyEmailArea.setVisibility(View.GONE);
                            mobileVerifyBtn.setText("Verify");

                            mobileNumTxtView.setText("+" + vPhoneCode + "-" + vMobile);

                            userMobileNumber = "+" + vPhoneCode + vMobile;
                            if (eEmailVerified.equalsIgnoreCase("yes")) {
                                emailImgView.setImageResource(R.mipmap.ic_email_verified);
                                ((MaterialRippleLayout) emailVerifyBtn.getParent()).setVisibility(View.GONE);
                                emailVerifyTxtView.setText("Your email is verified.");
                            } else {
                                emailImgView.setImageResource(R.mipmap.ic_email_not_verified);
                                ((MaterialRippleLayout) emailVerifyBtn.getParent()).setVisibility(View.VISIBLE);
                                emailVerifyTxtView.setText("Your email is not verified.");
                            }

                            emailTxtView.setText(vEmail);

                            containerView.setVisibility(View.VISIBLE);

                            noDataTxtView.setVisibility(View.GONE);
                        } else {
                            noDataTxtView.setText("No data available. Please check back later OR check your internet connection.");
                            noDataTxtView.setVisibility(View.VISIBLE);
                        }


                    } else {
                        noDataTxtView.setText(generalFunc.getJsonValue(Utils.message_str, responseString));
                        noDataTxtView.setVisibility(View.VISIBLE);
                    }

                } else {
                    noDataTxtView.setText("No data available. Please check back later OR check your internet connection.");
                    noDataTxtView.setVisibility(View.VISIBLE);
                }
            }
        });
        exeWebServer.execute();
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == mobileVerifyBtn.getId()) {

                if (mResendToken == null) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(userMobileNumber, 60, TimeUnit.SECONDS, verifyUsrAct, mCallbacks);
                } else {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(userMobileNumber, 60, TimeUnit.SECONDS, verifyUsrAct, mCallbacks, mResendToken);
                }

                verifyMobileArea.setVisibility(View.VISIBLE);

                mobileVerifyBtn.setText("Resend Code");
            } else if (view.getId() == mobileCodeVerifyBtn.getId()) {
                boolean isCodeEntered = Utils.checkText(mobileCodeBox) ? true : Utils.setErrorFields(mobileCodeBox, "Required");
                if (isCodeEntered) {

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, Utils.getText(mobileCodeBox));
                    signInWithPhoneAuthCredential(credential);
                }

            } else if (view.getId() == emailVerifyBtn.getId()) {
                sendEmailToVerify();
            } else if (view.getId() == emailCodeVerifyBtn.getId()) {
                boolean isCodeEntered = Utils.checkText(emailCodeBox) ? (emailVerificationCode.equalsIgnoreCase(Utils.getText(emailCodeBox)) ? true : Utils.setErrorFields(emailCodeBox, "InValid Code")) : Utils.setErrorFields(emailCodeBox, "Required");
                if (isCodeEntered) {
                    updateEmailVerified();
                }

            }
        }
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(verifyUsrAct, new OnCompleteListener<AuthResult>() {
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

    public void updatePhoneVerified() {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "makePhoneVerify");
        parameters.put("iMemberId", generalFunc.getMemberId());

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


                        getUserData();

                        generalFunc.showGeneralMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));

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

    public void updateEmailVerified() {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "makeEmailVerify");
        parameters.put("iMemberId", generalFunc.getMemberId());

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


                        getUserData();

                        generalFunc.showGeneralMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));

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


    public void sendEmailToVerify() {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "sendEmailVerification");
        parameters.put("iMemberId", generalFunc.getMemberId());

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


                        emailVerificationCode = generalFunc.getJsonValue("VerificationCode", responseString);

                        generalFunc.showGeneralMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));

                        verifyEmailArea.setVisibility(View.VISIBLE);

                        emailVerifyBtn.setText("Resend Code");
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
