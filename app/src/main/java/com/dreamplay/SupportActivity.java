package com.dreamplay;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.MTextView;

public class SupportActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;

    LinearLayout aboutUsArea, privacyArea, contactUsArea, termsCondArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        generalFunc = new GeneralFunctions(getActContext());

        backImgView = (ImageView) findViewById(R.id.backImgView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        aboutUsArea = (LinearLayout) findViewById(R.id.aboutUsArea);
        privacyArea = (LinearLayout) findViewById(R.id.privacyArea);
        contactUsArea = (LinearLayout) findViewById(R.id.contactUsArea);
        termsCondArea = (LinearLayout) findViewById(R.id.termsCondArea);


        backImgView.setOnClickListener(new setOnClickList());
        aboutUsArea.setOnClickListener(new setOnClickList());
        privacyArea.setOnClickListener(new setOnClickList());
        contactUsArea.setOnClickListener(new setOnClickList());
        termsCondArea.setOnClickListener(new setOnClickList());

        setLabel();
    }

    public Context getActContext() {
        return SupportActivity.this;
    }


    private void setLabel() {
        titleTxt.setText("SUPPORT");
    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(SupportActivity.this);
            Bundle bn = new Bundle();
            switch (view.getId()) {

                case R.id.backImgView:
                    SupportActivity.super.onBackPressed();
                    break;
                case R.id.aboutUsArea:
                    bn.putString("PAGE_ID", "1");
                    new StartActProcess(getActContext()).startActWithData(StaticPageActivity.class, bn);
                    break;
                case R.id.privacyArea:
                    bn.putString("PAGE_ID", "2");
                    new StartActProcess(getActContext()).startActWithData(StaticPageActivity.class, bn);
                    break;
                case R.id.contactUsArea:
                    new StartActProcess(getActContext()).startAct(ContactUsActivity.class);
                    break;
                case R.id.termsCondArea:
                    bn.putString("PAGE_ID", "3");
                    new StartActProcess(getActContext()).startActWithData(StaticPageActivity.class, bn);
                    break;

            }
        }
    }

}
