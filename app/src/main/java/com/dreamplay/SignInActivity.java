package com.dreamplay;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;

import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignInActivity extends AppCompatActivity {
    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    CircleImageView hImgView;
    MaterialEditText emailBox;
    MButton btn_type2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        generalFunc = new GeneralFunctions(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        hImgView = (CircleImageView) findViewById(R.id.hImgView);
        emailBox = (MaterialEditText) findViewById(R.id.emailBox);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();

        btn_type2.setId(Utils.generateViewId());

        backImgView.setOnClickListener(new setOnClickList());
        titleTxt.setText("SignIn");
        btn_type2.setAlpha((float) 0.85);
        setData();

        new CreateRoundedView(getResources().getColor(R.color.appThemeColor_2), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 1), getResources().getColor(R.color.appThemeColor_2), btn_type2);
    }

    public void setData() {
        emailBox.setBothText("Email Or Mobile", "Enter your email or mobile no");
        btn_type2.setText("Next");
        emailBox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT);
    }

    public Context getActContext() {
        return SignInActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == backImgView.getId()) {
                SignInActivity.super.onBackPressed();
            }
        }
    }
}
