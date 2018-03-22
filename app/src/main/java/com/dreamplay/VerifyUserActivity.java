package com.dreamplay;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.adapter.TabAdapter;
import com.fragments.BankDetailsFragment;
import com.fragments.PanCardFragment;
import com.fragments.VerifyMobileEmailFragment;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.MTextView;

public class VerifyUserActivity extends AppCompatActivity {
    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;

    ViewPager viewPager;
    TabLayout tabLayout;

    public Uri fileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_user);

        generalFunc = new GeneralFunctions(getActContext());

        backImgView = (ImageView) findViewById(R.id.backImgView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        backImgView.setOnClickListener(new setOnClickList());

        setLabel();

        buildTab();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current state
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }
    public void buildTab() {
        final TabAdapter adapter = new TabAdapter(getSupportFragmentManager());

        VerifyMobileEmailFragment verifyFrag = new VerifyMobileEmailFragment();
        PanCardFragment panCardFrag = new PanCardFragment();
        BankDetailsFragment bankDetailsFrag = new BankDetailsFragment();
        adapter.addFrag(verifyFrag, "MOBILE & EMAIL");
        adapter.addFrag(panCardFrag, "PAN");
        adapter.addFrag(bankDetailsFrag, "BANK");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }


    public Context getActContext() {
        return VerifyUserActivity.this;
    }


    private void setLabel() {
        titleTxt.setText("Account Verification");
    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(VerifyUserActivity.this);
            Bundle bn = new Bundle();
            switch (view.getId()) {
                case R.id.backImgView:
                    VerifyUserActivity.super.onBackPressed();
                    break;
            }
        }
    }
}
