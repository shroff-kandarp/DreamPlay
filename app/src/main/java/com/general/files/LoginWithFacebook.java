package com.general.files;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.dreamplay.MainActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.utils.Utils;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Shroff on 19-Nov-17.
 */

public class LoginWithFacebook implements FacebookCallback<LoginResult> {
    Context mContext;

    CallbackManager mCallbackManager;


    GeneralFunctions generalFunc;


    public LoginWithFacebook(Context mContext, CallbackManager mCallbackManager) {
        this.mContext = mContext;
        this.mCallbackManager = mCallbackManager;

        generalFunc = new GeneralFunctions(mContext);
        initializeFacebookLogin();
    }

    public void initializeFacebookLogin() {

        LoginButton loginButton = new LoginButton(mContext);


//        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends", "user_about_me"));
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_about_me"));

        loginButton.registerCallback(mCallbackManager, this);
        loginButton.performClick();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {

        Utils.showLoader(mContext);
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject me,
                            GraphResponse response) {
                        // Application code
                        Utils.closeLoader();

                        if (response.getError() != null) {
                            // handle error
                            Utils.printLog("onError", "onError:" + response.getError());

                            generalFunc.showGeneralMessage("", "Please try again later.");

                        } else {

                            String email_str = generalFunc.getJsonValue("email", me.toString());
                            String name_str = generalFunc.getJsonValue("name", me.toString());
                            String first_name_str = generalFunc.getJsonValue("first_name", me.toString());
                            String last_name_str = generalFunc.getJsonValue("last_name", me.toString());
                            String fb_id_str = generalFunc.getJsonValue("id", me.toString());

                            registerFbUser(email_str, first_name_str, last_name_str, fb_id_str);

                            generalFunc.logOUTFrmFB();
                        }
                    }


                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void registerFbUser(String email_str, String first_name_str, String last_name_str, String fb_id_str) {

        registerUser(first_name_str + " " + last_name_str, "", "", "", "", email_str, fb_id_str);
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
        parameters.put("eRegisterFrom", "Facebook");

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
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }
}
