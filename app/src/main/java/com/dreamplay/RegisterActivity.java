package com.dreamplay;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    CircleImageView hImgView;
    MaterialEditText nameBox;
    MaterialEditText emailBox;
    MaterialEditText mobileBox;
    MaterialEditText passwordBox;
    MButton btn_type2;

    View inviteArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        generalFunc = new GeneralFunctions(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        backImgView.setOnClickListener(new setOnClickList());

        hImgView = (CircleImageView) findViewById(R.id.hImgView);
        nameBox = (MaterialEditText) findViewById(R.id.nameBox);
        emailBox = (MaterialEditText) findViewById(R.id.emailBox);
        mobileBox = (MaterialEditText) findViewById(R.id.mobileBox);
        passwordBox = (MaterialEditText) findViewById(R.id.passwordBox);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();

        inviteArea = findViewById(R.id.inviteArea);

        btn_type2.setId(Utils.generateViewId());

        backImgView.setOnClickListener(new setOnClickList());

        btn_type2.setAlpha((float) 0.85);

        setData();

        new CreateRoundedView(getResources().getColor(R.color.appThemeColor_2), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 1), getResources().getColor(R.color.appThemeColor_2), btn_type2);
        new CreateRoundedView(getResources().getColor(android.R.color.transparent), Utils.dipToPixels(getActContext(), 8), Utils.dipToPixels(getActContext(), 1), Color.parseColor("#333333"), inviteArea);

    }

    public void setData() {
        nameBox.setBothText("Name", "Enter your name");
        emailBox.setBothText("Email", "Enter your email");
        mobileBox.setBothText("Mobile", "Enter your mobile no");
        passwordBox.setBothText("Password", "Enter your password");

        titleTxt.setText("Register & Play");

        btn_type2.setText("Register");
        emailBox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT);
        mobileBox.setInputType(InputType.TYPE_CLASS_NUMBER);

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
        passwordBox.setHelperText("Minimum 8 characters with 1 number/symbol");
        passwordBox.setHelperTextAlwaysShown(true);
    }

    public Context getActContext() {
        return RegisterActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == backImgView.getId()) {
                RegisterActivity.super.onBackPressed();
            } else if (view.getId() == btn_type2.getId()) {

            }
        }
    }
}
