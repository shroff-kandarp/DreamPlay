package com.dreamplay;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.adapter.DrawerMenuRecycleAdapter;
import com.adapter.TabAdapter;
import com.bannerslider.banners.Banner;
import com.bannerslider.banners.RemoteBanner;
import com.bannerslider.events.OnBannerClickListener;
import com.bannerslider.views.BannerSlider;
import com.fragments.FixturesFragment;
import com.fragments.LiveFragment;
import com.fragments.ResultsFragment;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity implements DrawerMenuRecycleAdapter.OnItemClickListener {

    public static final String MENU_PROFILE = "0";
    public static final String MENU_ACC = "1";
    public static final String MENU_INVITE_FRIENDS = "2";
    public static final String MENU_FAQ = "3";
    public static final String MENU_SUPPORT = "4";
    public static final String MENU_VERIFY = "5";
    public static final String MENU_POINTS_SYS = "6";
    public static final String MENU_HOW_PLAY = "7";
    public static final String MENU_LOGOUT = "8";

    ImageView menuImgView;
    ImageView logoImgView;
    MTextView titleTxt;
    MTextView topHTxtView;
    DrawerLayout mDrawerLayout;
    GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> menuDataList = new ArrayList<>();
    DrawerMenuRecycleAdapter drawerAdapter;
    RecyclerView menuRecyclerView;

    BannerSlider bannerSlider;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generalFunc = new GeneralFunctions(getActContext());

        bannerSlider = (BannerSlider) findViewById(R.id.bannerSlider);
        menuImgView = (ImageView) findViewById(R.id.menuImgView);
        logoImgView = (ImageView) findViewById(R.id.logoImgView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        topHTxtView = (MTextView) findViewById(R.id.topHTxtView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuRecyclerView = (RecyclerView) findViewById(R.id.menuRecyclerView);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        menuImgView.setOnClickListener(new setOnClickList());

        setLabels();

        buildMenu();
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);

    }

    public void setLabels() {
        titleTxt.setText("");
        logoImgView.setVisibility(View.VISIBLE);

        if (!generalFunc.getMemberId().equals("")) {
            String member_data = generalFunc.retriveValue(Utils.member_data_KEY);
            topHTxtView.setText(generalFunc.getJsonValue("vName", member_data));
        }
        setBannerData(null);
    }

    public void setupViewPager() {
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFrag(new FixturesFragment(), "FIXTURES");
        adapter.addFrag(new LiveFragment(), "Live");
        adapter.addFrag(new ResultsFragment(), "Results");
        viewPager.setAdapter(adapter);
    }


    public void buildMenu() {
        menuDataList.clear();
        menuDataList.add(getMenuItem("My Profile", "" + R.mipmap.ic_menu_profile, "" + DrawerMenuRecycleAdapter.TYPE_ITEM, MENU_PROFILE));
        menuDataList.add(getMenuItem("My Account", "" + R.mipmap.ic_menu_account, "" + DrawerMenuRecycleAdapter.TYPE_ITEM, MENU_ACC));
        menuDataList.add(getMenuItem("Invite Friends", "" + R.mipmap.ic_menu_invite, "" + DrawerMenuRecycleAdapter.TYPE_ITEM, MENU_INVITE_FRIENDS));
        menuDataList.add(getMenuItem("FAQ's", "" + R.mipmap.ic_menu_help_center, "" + DrawerMenuRecycleAdapter.TYPE_ITEM, MENU_FAQ));
        menuDataList.add(getMenuItem("Support", "" + R.mipmap.ic_menu_support, "" + DrawerMenuRecycleAdapter.TYPE_ITEM, MENU_SUPPORT));
        menuDataList.add(getMenuItem("Verify", "" + R.mipmap.ic_menu_verify, "" + DrawerMenuRecycleAdapter.TYPE_ITEM, MENU_VERIFY));
        menuDataList.add(getMenuItem("Fantasy Points System", "" + R.mipmap.ic_menu_points, "" + DrawerMenuRecycleAdapter.TYPE_ITEM, MENU_POINTS_SYS));
        menuDataList.add(getMenuItem("How to Play", "" + R.mipmap.ic_menu_play, "" + DrawerMenuRecycleAdapter.TYPE_ITEM, MENU_HOW_PLAY));
        menuDataList.add(getMenuItem("Logout", "" + R.mipmap.ic_menu_logout, "" + DrawerMenuRecycleAdapter.TYPE_ITEM, MENU_LOGOUT));


        drawerAdapter = new DrawerMenuRecycleAdapter(getActContext(), menuDataList, generalFunc, false);

        menuRecyclerView.setAdapter(drawerAdapter);
        menuRecyclerView.setNestedScrollingEnabled(false);

        drawerAdapter.notifyDataSetChanged();

        drawerAdapter.setOnItemClickListener(this);


    }

    public void setBannerData(List<Banner> banners1) {

        List<Banner> banners = new ArrayList<>();

        banners.add(new RemoteBanner("https://media2.mensxp.com/media/section/2015/Feb/cricket13663_1424161361.jpg"));
        banners.add(new RemoteBanner("https://cache.pakistantoday.com.pk/Boxing-Day-test-MCG-security-beefed-up-ahead-of-Mondays-match.jpg"));
        banners.add(new RemoteBanner("https://images.sftcdn.net/images/t_optimized,f_auto/p/15de3de8-a4f9-11e6-8486-00163ed833e7/1716164724/world-cricket-championship-2-screenshot.png"));
        banners.add(new RemoteBanner("https://static.sticksports.com/images/sc_pt_holder_20130328.jpg"));

        bannerSlider.setBanners(banners);

        bannerSlider.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onClick(int position) {
//                Toast.makeText(MainActivity.this, "Banner with position " + String.valueOf(position) + " clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public HashMap<String, String> getMenuItem(String name, String imgRes, String itemType, String itemID) {
        HashMap<String, String> data = new HashMap<>();
        data.put("name", name);
        data.put("Icon", imgRes);
        data.put("TYPE", itemType);
        data.put("ID", itemID);

        return data;
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

    @Override
    public void onItemClickList(View v, int position) {
        closeDrawer();
        Bundle bn = new Bundle();
        switch (menuDataList.get(position).get("ID")) {

            case MENU_LOGOUT:
                confirmSignOut();
                break;
        }
    }

    public void confirmSignOut() {
        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
            @Override
            public void handleBtnClick(int btn_id) {
                if (btn_id == 1) {
                    generateAlert.closeAlertBox();
                    generalFunc.signOut();
                } else if (btn_id == 0) {
                    generateAlert.closeAlertBox();
                }
            }
        });
        generateAlert.setContentMessage("Confirm", "Are you sure, you want to logout from this device?");
        generateAlert.setPositiveBtn("YES");
        generateAlert.setNegativeBtn("NO");
        generateAlert.showAlertBox();
    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.menuImgView) {
                checkDrawerState();
            }
        }
    }

    public Context getActContext() {
        return MainActivity.this;
    }
}
