package com.general.files;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.dreamplay.MainActivity;
import com.dreamplay.RegisterActivity;
import com.dreamplay.SignInActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.utils.Utils;

import java.util.HashMap;

/**
 * Created by Shroff on 19-Nov-17.
 */

public class LoginWithGoogle implements GoogleApiClient.OnConnectionFailedListener {
    Context mContext;

    GoogleApiClient mGoogleApiClient;

    GeneralFunctions generalFunc;
    SignInActivity signInAct;
    RegisterActivity registerAct;

    public LoginWithGoogle(Context mContext) {
        this.mContext = mContext;
        generalFunc = new GeneralFunctions(mContext);

        if (mContext instanceof SignInActivity) {
            signInAct = (SignInActivity) mContext;
        }

        if (mContext instanceof RegisterActivity) {
            registerAct = (RegisterActivity) mContext;
        }

        initializeGoogleLogin();

    }

    public void initializeGoogleLogin() {


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(signInAct == null ? registerAct : signInAct, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        ((Activity) mContext).startActivityForResult(signInIntent, Utils.GOOGLE_SIGN_IN_REQ_CODE);
    }

    public void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();

            String personName = acct.getDisplayName();
            String email = acct.getEmail();
            String id = acct.getId();

            registerUser(personName, "", "", "", "", email, id);
        }
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
        parameters.put("eRegisterFrom", "Google");

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setLoaderConfig(mContext, true, generalFunc);
        exeWebServer.setIsDeviceTokenGenerate(true, "vDeviceToken");
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(final String responseString) {

                Utils.printLog("ResponseData", "Data::" + responseString);

                if (responseString != null && !responseString.equals("")) {
                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail) {

                        generalFunc.storeUserData(generalFunc.getJsonValue(Utils.message_str, responseString));
                        (new StartActProcess(mContext)).startAct(MainActivity.class);
                        ActivityCompat.finishAffinity((Activity) mContext);
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Utils.printLog("GoogleSignIn", "::" + connectionResult.getErrorMessage());

        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }
}
