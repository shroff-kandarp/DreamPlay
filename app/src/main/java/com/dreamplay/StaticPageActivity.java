package com.dreamplay;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.MTextView;

import java.util.HashMap;

public class StaticPageActivity extends BaseActivity {


    public String static_page_id = "1";
    MTextView titleTxt;
    ImageView backImgView;
    GeneralFunctions generalFunc;
    ProgressBar loading;
    ErrorView errorView;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_page);


        generalFunc = new GeneralFunctions(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        loading = (ProgressBar) findViewById(R.id.loading);
        errorView = (ErrorView) findViewById(R.id.errorView);
        container = (LinearLayout) findViewById(R.id.container);

        static_page_id = getIntent().getStringExtra("PAGE_ID");

        setLabels();

        backImgView.setOnClickListener(new setOnClickList());

        loadAboutUsData();
    }


    public void setLabels() {

        if (static_page_id.equalsIgnoreCase("1")) {
            titleTxt.setText("About US");

        } else if (static_page_id.equalsIgnoreCase("2")) {
            titleTxt.setText("Privacy & Policy");

        } else if (static_page_id.equals("3")) {
            titleTxt.setText("Terms & Condition");

        } else {
            titleTxt.setText("Details");
        }

    }

    public void loadAboutUsData() {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
        if (loading.getVisibility() != View.VISIBLE) {
            loading.setVisibility(View.VISIBLE);
        }

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "staticPage");
        parameters.put("iPageId", static_page_id);
        parameters.put("iMemberId", generalFunc.getMemberId());

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {


                if (responseString != null && !responseString.equals("")) {

                    closeLoader();

                    loadAboutUsDetail(responseString);
                } else {
                    generateErrorView();
                }
            }
        });
        exeWebServer.execute();
    }

    public void loadAboutUsDetail(String aboutUsData) {
        String tPageDesc = generalFunc.getJsonValue(Utils.message_str, aboutUsData);

        WebView view = new WebView(this);
        view.setVerticalScrollBarEnabled(false);
        view.setHorizontalScrollBarEnabled(false);
        view.setBackgroundColor(getResources().getColor(R.color.appThemeColor_bg_parent_1));
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        view.setLongClickable(false);
        view.setHapticFeedbackEnabled(false);

        container.addView(view);

        view.loadDataWithBaseURL(null, generalFunc.wrapHtml(view.getContext(), tPageDesc), "text/html", "UTF-8", null);
    }

    public void closeLoader() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
        }
    }

    public void generateErrorView() {

        closeLoader();

        generalFunc.generateErrorView(errorView, "Error", "Please check your internet connetion OR try again later.");

        if (errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
        }
        errorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                loadAboutUsData();
            }
        });
    }

    public Context getActContext() {
        return StaticPageActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(StaticPageActivity.this);
            switch (view.getId()) {
                case R.id.backImgView:
                    StaticPageActivity.super.onBackPressed();
                    break;

            }
        }
    }
}
