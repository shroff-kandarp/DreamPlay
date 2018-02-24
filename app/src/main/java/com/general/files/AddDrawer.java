package com.general.files;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.adapter.DrawerMenuRecycleAdapter;
import com.dreamplay.AppLoginActivity;
import com.dreamplay.ContactUsActivity;
import com.dreamplay.MainActivity;
import com.dreamplay.MyMessagesActivity;
import com.dreamplay.MyProfileActivity;
import com.dreamplay.R;
import com.dreamplay.StaticPageActivity;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shroff on 15-Nov-17.
 */

public class AddDrawer implements DrawerMenuRecycleAdapter.OnItemClickListener {


    public static final String MENU_HOME = "0";
    public static final String MENU_MY_PROFILE = "1";
    public static final String MENU_MY_MESSAGES = "2";
    public static final String MENU_MY_CONTACT_US = "3";
    public static final String MENU_MY_TERMS_CONDITION = "4";
    public static final String MENU_MY_HELP_CENTER = "5";
    public DrawerLayout mDrawerLayout;
    View view;
    Context mContext;
    View left_linear;
    MTextView topHTxtView;
    ImageView menuImgView;
    ImageView notificationImgView;
    RecyclerView menuRecyclerView;

    GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> menuDataList = new ArrayList<>();

    DrawerMenuRecycleAdapter drawerAdapter;

    public AddDrawer(Context mContext) {
        this.mContext = mContext;

        View actView = GeneralFunctions.getCurrentView((Activity) getActContext());
        this.view = actView;

        generalFunc = new GeneralFunctions(mContext);
        buildDrawer();
    }

    public void buildDrawer() {
        left_linear = view.findViewById(R.id.left_linear);
        menuImgView = (ImageView) view.findViewById(R.id.menuImgView);
        topHTxtView = (MTextView) view.findViewById(R.id.topHTxtView);
        notificationImgView = (ImageView) view.findViewById(R.id.notificationImgView);
//        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        menuRecyclerView = (RecyclerView) view.findViewById(R.id.menuRecyclerView);

        drawerAdapter = new DrawerMenuRecycleAdapter(getActContext(), menuDataList, generalFunc, false);

        menuRecyclerView.setAdapter(drawerAdapter);
        menuRecyclerView.setNestedScrollingEnabled(false);


        left_linear.setOnClickListener(new setOnClickList());
        notificationImgView.setOnClickListener(new setOnClickList());


        if (generalFunc.isUserLoggedIn() == false) {
            topHTxtView.setText("Hello, \nSign In OR Sign Up.");
        } else {
            topHTxtView.setText("Welcome");
            findUserInfo();
        }
        buildMenu();
    }

    public void checkDrawerState() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT) == true) {
            closeDrawer();
        } else {
            openDrawer();
        }
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }


    public void buildMenu() {

        menuDataList.add(getMenuItem("My Profile", "" + R.mipmap.ic_menu_home, "" + DrawerMenuRecycleAdapter.TYPE_HEADER, MENU_MY_PROFILE));

        menuDataList.add(getMenuItem("My Messages", "" + R.mipmap.ic_menu_message, "" + DrawerMenuRecycleAdapter.TYPE_ITEM, MENU_MY_MESSAGES));

        menuDataList.add(getMenuItem("Contact Us", "" + R.mipmap.ic_contact_us, "" + DrawerMenuRecycleAdapter.TYPE_ITEM, MENU_MY_CONTACT_US));
        menuDataList.add(getMenuItem("Terms & Conditions", "" + R.mipmap.ic_menu_terms, "" + DrawerMenuRecycleAdapter.TYPE_ITEM, MENU_MY_TERMS_CONDITION));
        menuDataList.add(getMenuItem("Help Center", "" + R.mipmap.ic_menu_help_center, "" + DrawerMenuRecycleAdapter.TYPE_ITEM, MENU_MY_HELP_CENTER));
        drawerAdapter.notifyDataSetChanged();

        drawerAdapter.setOnItemClickListener(this);

        menuImgView.setOnClickListener(new setOnClickList());
    }

    @Override
    public void onItemClickList(View v, int position) {
        closeDrawer();
        Bundle bn = new Bundle();
        switch (menuDataList.get(position).get("ID")) {
            case MENU_HOME:
//                    Intent intent = new Intent(mContext, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    mContext.startActivity(intent);
                goToHome();

//                (new StartActProcess(getActContext())).startAct(MainActivity.class);
                break;
            case MENU_MY_PROFILE:
                openMyProfile();
                break;
            case MENU_MY_TERMS_CONDITION:
                bn.putString("staticpage", "3");
                new StartActProcess(getActContext()).startActWithData(StaticPageActivity.class, bn);
                break;
            case MENU_MY_CONTACT_US:

                new StartActProcess(getActContext()).startAct(ContactUsActivity.class);
                break;
        }
    }

    public void openMyMessages() {
        if (generalFunc.isUserLoggedIn()) {
            (new StartActProcess(getActContext())).startAct(MyMessagesActivity.class);
        } else {
            openSignIn();
        }
    }

    public void openMyProfile() {
        if (generalFunc.isUserLoggedIn()) {
            (new StartActProcess(getActContext())).startAct(MyProfileActivity.class);
        } else {
            openSignIn();
        }
    }

    public void goToHome() {

        if (!(mContext instanceof MainActivity)) {
            (new StartActProcess(getActContext())).startAct(MainActivity.class);
            ActivityCompat.finishAffinity((Activity) mContext);
        }
    }

    public void openSignIn() {
        (new StartActProcess(getActContext())).startAct(AppLoginActivity.class);
    }

    public Context getActContext() {
        return mContext;
    }

    public HashMap<String, String> getMenuItem(String name, String imgRes, String itemType, String itemID) {
        HashMap<String, String> data = new HashMap<>();
        data.put("name", name);
        data.put("Icon", imgRes);
        data.put("TYPE", itemType);
        data.put("ID", itemID);

        return data;
    }

    public void findUserInfo() {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getUserInfo");
        parameters.put("customer_id", generalFunc.getMemberId());

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {


                if (responseString != null && !responseString.equals("")) {

                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);
                    if (isDataAvail == true) {
                        JSONObject obj_msg = generalFunc.getJsonObject(Utils.message_str, responseString);

                        topHTxtView.setText("Welcome, " + generalFunc.getJsonValue("firstname", obj_msg) + " " + generalFunc.getJsonValue("lastname", obj_msg));
                    }
                }
            }
        });
        exeWebServer.execute();
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.menuImgView:
                    checkDrawerState();
                    break;
                case R.id.topHTxtView:
                    if (!generalFunc.isUserLoggedIn()) {
                        openSignIn();
                    }
                    break;
            }
        }
    }
}
