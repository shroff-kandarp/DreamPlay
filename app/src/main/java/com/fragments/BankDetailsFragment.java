package com.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dreamplay.R;
import com.dreamplay.VerifyUserActivity;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.ImageFilePath;
import com.general.files.ImageSourceDialog;
import com.general.files.StartActProcess;
import com.general.files.UploadImage;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_CANCELED;

public class BankDetailsFragment extends Fragment implements UploadImage.SetResponseListener {


    View view;

    View containerView;
    MaterialEditText bankNameBox;
    MaterialEditText bankBranchBox;
    MaterialEditText accNameBox;
    MaterialEditText accountNumBox;
    MaterialEditText reAccountNumBox;
    MaterialEditText ifscCodeBox;
    MaterialEditText aadharNumBox;

    View photoUploadArea;
    View imgAdd;

    ImageView passBookImgView;

    ProgressBar loadingBar;

    MTextView noDataTxt;

    MButton uploadPassbookImageBtn;
    MButton btn_type2;

    GeneralFunctions generalFunc;
    VerifyUserActivity verifyUsrAct;

    boolean isAllInfoEditable = true;
    boolean isImageUploadEnable = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bank_details, container, false);

        verifyUsrAct = (VerifyUserActivity) getActivity();
        generalFunc = verifyUsrAct.generalFunc;
        noDataTxt = (MTextView) view.findViewById(R.id.noDataTxt);
        bankNameBox = (MaterialEditText) view.findViewById(R.id.bankNameBox);
        bankBranchBox = (MaterialEditText) view.findViewById(R.id.bankBranchBox);
        accNameBox = (MaterialEditText) view.findViewById(R.id.accNameBox);
        accountNumBox = (MaterialEditText) view.findViewById(R.id.accountNumBox);
        reAccountNumBox = (MaterialEditText) view.findViewById(R.id.reAccountNumBox);
        ifscCodeBox = (MaterialEditText) view.findViewById(R.id.ifscCodeBox);
        loadingBar = (ProgressBar) view.findViewById(R.id.loadingBar);
        aadharNumBox = (MaterialEditText) view.findViewById(R.id.aadharNumBox);
        passBookImgView = (ImageView) view.findViewById(R.id.passBookImgView);
        imgAdd = view.findViewById(R.id.imgAdd);
        photoUploadArea = view.findViewById(R.id.photoUploadArea);

        containerView = view.findViewById(R.id.containerView);

        btn_type2 = ((MaterialRippleLayout) view.findViewById(R.id.btn_type2)).getChildView();
        uploadPassbookImageBtn = ((MaterialRippleLayout) view.findViewById(R.id.uploadPassbookImageBtn)).getChildView();

        btn_type2.setId(Utils.generateViewId());
        uploadPassbookImageBtn.setId(Utils.generateViewId());

        photoUploadArea.setOnClickListener(new setOnClickList());
        btn_type2.setOnClickListener(new setOnClickList());
        uploadPassbookImageBtn.setOnClickListener(new setOnClickList());

        new CreateRoundedView(getActContext().getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(getActContext(), 25), 0, getActContext().getResources().getColor(R.color.appThemeColor_1), imgAdd);
        setLabels();

        getUserData();
        return view;
    }


    public void setLabels() {

        bankNameBox.setBothText("Bank Name", "Enter your bank name");
        bankBranchBox.setBothText("Bank branch", "Enter bank branch name");
        accNameBox.setBothText("Account Name", "Enter your account name");
        accountNumBox.setBothText("Account Number", "Enter your account number");
        reAccountNumBox.setBothText("Confirm Account Number", "Re Enter your account number");
        ifscCodeBox.setBothText("IFSC Code", "Enter your ifsc code");
        aadharNumBox.setBothText("Aadhar Card Num.", "Enter your aadhar card number");
        btn_type2.setText("Submit");
        uploadPassbookImageBtn.setText("Upload Passbook's image");
    }


    public void getUserData() {
        containerView.setVisibility(View.GONE);
        btn_type2.setVisibility(View.GONE);
        noDataTxt.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);

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

                            String vBankName = generalFunc.getJsonValue("vBankName", obj_msg);
                            String vIFSCCode = generalFunc.getJsonValue("vIFSCCode", obj_msg);
                            String vBankBranchName = generalFunc.getJsonValue("vBankBranchName", obj_msg);
                            String vMemberNameOnBank = generalFunc.getJsonValue("vMemberNameOnBank", obj_msg);
                            String vAccountNumber = generalFunc.getJsonValue("vAccountNumber", obj_msg);
                            String vAadharNum = generalFunc.getJsonValue("vAadharNum", obj_msg);
                            final String vPassbookImage = generalFunc.getJsonValue("vPassbookImage", obj_msg);

                            if (!vBankName.equals("") && !vIFSCCode.equals("") && !vMemberNameOnBank.equals("") && !vAccountNumber.equals("")) {
                                isAllInfoEditable = false;
                                bankNameBox.setEnabled(false);
                                ifscCodeBox.setEnabled(false);
                                accNameBox.setEnabled(false);
                                accountNumBox.setEnabled(false);
                                aadharNumBox.setEnabled(false);

//                                bankNameBox.getLabelFocusAnimator().start();
//                                ifscCodeBox.getLabelFocusAnimator().start();
//                                accNameBox.getLabelFocusAnimator().start();
//                                accountNumBox.getLabelFocusAnimator().start();
//                                aadharNumBox.getLabelFocusAnimator().start();
                            }
                            bankNameBox.setText(vBankName);
                            ifscCodeBox.setText(vIFSCCode);
                            bankBranchBox.setText(vBankBranchName);
                            accNameBox.setText(vMemberNameOnBank);
                            accountNumBox.setText(vAccountNumber);
                            aadharNumBox.setText(vAadharNum);

                            if (!vAccountNumber.equals("")) {
                                reAccountNumBox.setVisibility(View.GONE);
                            }


                            if (!vPassbookImage.equals("")) {
                                isImageUploadEnable = false;
                            }

                            if (!vPassbookImage.equals("")) {
                                isImageUploadEnable = false;

                                Picasso.with(getActContext())
                                        .load(vPassbookImage)
                                        .into(passBookImgView, null);

                                photoUploadArea.setVisibility(View.GONE);
                                passBookImgView.setVisibility(View.VISIBLE);

                                passBookImgView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        (new StartActProcess(getActContext())).openURL(vPassbookImage);
                                    }
                                });
                            }
                        }

                        containerView.setVisibility(View.VISIBLE);
                        btn_type2.setVisibility(View.VISIBLE);

                    } else {
                        noDataTxt.setVisibility(View.VISIBLE);
//                        generalFunc.showGeneralMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));
                    }

                } else {
                    noDataTxt.setVisibility(View.VISIBLE);
//                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }


    public Context getActContext() {
        return verifyUsrAct.getActContext();
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == btn_type2.getId()) {
                if (isAllInfoEditable == false) {
                    generalFunc.showGeneralMessage("", "You can't edit information once its added.");
                    return;
                }
                checkData();
            } else if (i == uploadPassbookImageBtn.getId()) {
                if (isImageUploadEnable == false) {
                    generalFunc.showGeneralMessage("", "You can't upload image once its added.");
                    return;
                }
                addNewImage();
            } else if (i == photoUploadArea.getId()) {
                if (isImageUploadEnable == false) {
                    generalFunc.showGeneralMessage("", "You can't upload image once its added.");
                    return;
                }
                addNewImage();
            }
        }
    }

    public void addNewImage() {
        if (generalFunc.isCameraStoragePermissionGranted()) {

            ImageSourceDialog dialog = new ImageSourceDialog(getActContext(), verifyUsrAct.fileUri, getCurrentFragment());
            dialog.setFileUriListener(new ImageSourceDialog.FileURICreateListener() {
                @Override
                public void onFileUriCreate(Uri fileUri) {
                    verifyUsrAct.fileUri = fileUri;
                }
            });
            dialog.run();
        }
    }


    public Fragment getCurrentFragment() {
        return this;
    }

    public void checkData() {
//|| Utils.checkText(bankBranchBox) == false

        boolean isIFSCEntered = Utils.checkText(ifscCodeBox) ? true : Utils.setErrorFields(ifscCodeBox, "Required");
        boolean isBankNameEntered = Utils.checkText(bankNameBox) ? true : Utils.setErrorFields(bankNameBox, "Required");

        boolean isAccNameEntered = Utils.checkText(accNameBox) ? true : Utils.setErrorFields(accNameBox, "Required");
//        boolean isAadharEntered = Utils.checkText(aadharNumBox) ? true : Utils.setErrorFields(aadharNumBox, "Required");
        boolean isAccNumEntered = Utils.checkText(accountNumBox) ? (Utils.getText(accountNumBox).toString().length() != 10 ? Utils.setErrorFields(accountNumBox, "Account number must be 10 digits long") : true) : Utils.setErrorFields(accountNumBox, "Required");
        boolean isReAccNumEntered = Utils.checkText(reAccountNumBox) ? (Utils.getText(accountNumBox).trim().equals(Utils.getText(reAccountNumBox).trim()) ? true : Utils.setErrorFields(reAccountNumBox, "Invalid account number")) : Utils.setErrorFields(reAccountNumBox, "Required");

        if (isIFSCEntered == false || isBankNameEntered == false /*|| isAadharEntered == false*/ || isAccNameEntered == false || isAccNumEntered == false || isReAccNumEntered == false) {

            GeneralFunctions.showMessage(GeneralFunctions.getCurrentView((Activity) getActContext()), "All information is required.");
        } else {
//            updateBankDetails();

            final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
            generateAlert.setCancelable(false);
            generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                @Override
                public void handleBtnClick(int btn_id) {
                    if (btn_id == 1) {
                        updateBankDetails();
                    } else if (btn_id == 0) {
                        generateAlert.closeAlertBox();
                    }
                }
            });

            generateAlert.setContentMessage("Confirm", "Please make sure you have added correct information. Once its added, you will not be able to change information. Are you sure, you want to add entered details?");
            generateAlert.setPositiveBtn("YES");
            generateAlert.setNegativeBtn("NO");
            generateAlert.showAlertBox();
        }
    }

    public void updateBankDetails() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "updateBankDetails");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("vMemberNameOnBank", Utils.getText(accNameBox));
        parameters.put("vAccountNumber", Utils.getText(accountNumBox));
        parameters.put("vBankBranchName", Utils.getText(bankBranchBox));
        parameters.put("vIFSCCode", Utils.getText(ifscCodeBox));
        parameters.put("vBankName", Utils.getText(bankNameBox));
//        parameters.put("vAadharNum", Utils.getText(aadharNumBox));

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

                        JSONObject obj_msg = generalFunc.getJsonObject(Utils.message_str, responseString);

                        getUserData();
                    }
                    generalFunc.showGeneralMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));

                } else {
                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utils.printLog("Image", "Selected::");

        if (requestCode == ImageSourceDialog.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            boolean isStoragePermissionAvail = generalFunc.isStoragePermissionGranted();
            if (resultCode == verifyUsrAct.RESULT_OK) {
                // successfully captured the image
                // display it in image view

                final ArrayList<String[]> paramsList = new ArrayList<>();
                paramsList.add(Utils.generateImageParams("iMemberId", generalFunc.getMemberId()));
                paramsList.add(Utils.generateImageParams("type", "addPassbookImage"));

                final String selPath = new ImageFilePath().getPath(getActContext(), verifyUsrAct.fileUri);

                if (Utils.isValidImageResolution(selPath) == true && isStoragePermissionAvail) {
//                    new UploadImage(getActContext(), selPath, Utils.TempProfileImageName, paramsList,imgSelectType).execute();

                    final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                    generateAlert.setCancelable(false);
                    generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                        @Override
                        public void handleBtnClick(int btn_id) {
                            if (btn_id == 1) {

                                UploadImage uploadImg = new UploadImage(getActContext(), selPath, Utils.TempProfileImageName, paramsList, "");
                                uploadImg.setLoadingMessage("Uploading your passbook image...");
                                uploadImg.setResponseListener(BankDetailsFragment.this);
                                uploadImg.execute();
                            } else if (btn_id == 0) {
                                generateAlert.closeAlertBox();
                            }
                        }
                    });

                    generateAlert.setContentMessage("Confirm", "Please make sure you have selected correct image. Once its added, you will not be able to change. Are you sure, you want to upload selected image?");
                    generateAlert.setPositiveBtn("YES");
                    generateAlert.setNegativeBtn("NO");
                    generateAlert.showAlertBox();

                } else {
                    generalFunc.showGeneralMessage("", "Please select image which has minimum is 256 * 256 resolution.");
                }


            } else if (resultCode == RESULT_CANCELED) {

            } else {
                generalFunc.showGeneralMessage("", "Failed to capture image. Please try again.");
            }
        }
        if (requestCode == ImageSourceDialog.SELECT_PICTURE) {
            if (resultCode == verifyUsrAct.RESULT_OK) {
                boolean isStoragePermissionAvail = generalFunc.isStoragePermissionGranted();


                final ArrayList<String[]> paramsList = new ArrayList<>();
                paramsList.add(Utils.generateImageParams("iMemberId", generalFunc.getMemberId()));

                paramsList.add(Utils.generateImageParams("type", "addPassbookImage"));

                Uri selectedImageUri = data.getData();


                final String selectedImagePath = new ImageFilePath().getPath(getActContext(), selectedImageUri);


                if (Utils.isValidImageResolution(selectedImagePath) == true && isStoragePermissionAvail) {

                    final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                    generateAlert.setCancelable(false);
                    generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                        @Override
                        public void handleBtnClick(int btn_id) {
                            if (btn_id == 1) {


                                UploadImage uploadImg = new UploadImage(getActContext(), selectedImagePath, Utils.TempProfileImageName, paramsList, "");
                                uploadImg.setLoadingMessage("Uploading your passbook image...");
                                uploadImg.setResponseListener(BankDetailsFragment.this);
                                uploadImg.execute();
                            } else if (btn_id == 0) {
                                generateAlert.closeAlertBox();
                            }
                        }
                    });

                    generateAlert.setContentMessage("Confirm", "Please make sure you have selected correct image. Once its added, you will not be able to change. Are you sure, you want to upload selected image?");
                    generateAlert.setPositiveBtn("YES");
                    generateAlert.setNegativeBtn("NO");
                    generateAlert.showAlertBox();

                } else {
                    generalFunc.showGeneralMessage("", "Please select image which has minimum is 256 * 256 resolution.");
                }

            }
        }

    }

    @Override
    public void onFileUploadResponse(String responseString, String type) {

        generalFunc.showGeneralMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));

        getUserData();
    }
}
