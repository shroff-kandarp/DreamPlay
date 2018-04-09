package com.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.general.files.SetOnTouchList;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_CANCELED;

/**
 * A simple {@link Fragment} subclass.
 */
public class AaadharFragment extends Fragment implements UploadImage.SetResponseListener {

    View view;

    View containerView;
    MaterialEditText aadharCardNumBox;
    MaterialEditText cityBox;
    MaterialEditText stateBox;
    String iStateId = "";
    android.support.v7.app.AlertDialog list_state;
    ArrayList<String> items_txt_state = new ArrayList<String>();
    ArrayList<String> items_state_ids = new ArrayList<String>();

    ProgressBar loadingBar;

    MTextView noDataTxt;


    View photoUploadArea;
    View photoUploadAreaBack;
    View imgAdd;
    View imgAddBack;

    ImageView aadharCardImgView;
    ImageView aadharCardBackImgView;

    MButton btn_type2;

    GeneralFunctions generalFunc;
    VerifyUserActivity verifyUsrAct;
    boolean isAllInfoEditable = true;

    boolean isImageUploadEnable = true;
    boolean isBackImageUploadEnable = true;
    boolean isBackSidePressed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_aaadhar, container, false);

        verifyUsrAct = (VerifyUserActivity) getActivity();
        generalFunc = verifyUsrAct.generalFunc;
        noDataTxt = (MTextView) view.findViewById(R.id.noDataTxt);
        aadharCardNumBox = (MaterialEditText) view.findViewById(R.id.aadharCardNumBox);
        stateBox = (MaterialEditText) view.findViewById(R.id.stateBox);
        cityBox = (MaterialEditText) view.findViewById(R.id.cityBox);
        loadingBar = (ProgressBar) view.findViewById(R.id.loadingBar);
        aadharCardImgView = (ImageView) view.findViewById(R.id.aadharCardImgView);
        aadharCardBackImgView = (ImageView) view.findViewById(R.id.aadharCardBackImgView);
        imgAddBack = view.findViewById(R.id.imgAddBack);
        imgAdd = view.findViewById(R.id.imgAdd);
        photoUploadArea = view.findViewById(R.id.photoUploadArea);
        photoUploadAreaBack = view.findViewById(R.id.photoUploadAreaBack);
        containerView = view.findViewById(R.id.containerView);

        btn_type2 = ((MaterialRippleLayout) view.findViewById(R.id.btn_type2)).getChildView();

        btn_type2.setId(Utils.generateViewId());

        photoUploadAreaBack.setOnClickListener(new setOnClickList());
        photoUploadArea.setOnClickListener(new setOnClickList());
        btn_type2.setOnClickListener(new setOnClickList());

        new CreateRoundedView(getActContext().getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(getActContext(), 25), 0, getActContext().getResources().getColor(R.color.appThemeColor_1), imgAdd);
        new CreateRoundedView(getActContext().getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(getActContext(), 25), 0, getActContext().getResources().getColor(R.color.appThemeColor_1), imgAddBack);

        setLabels();
        removeInput();

        getUserData();

        return view;
    }

    public void setLabels() {

        aadharCardNumBox.setBothText("Aadhar Card", "Enter aadhar card number");

        stateBox.setBothText("State", "Select your state");
        cityBox.setBothText("City", "Enter city");
        btn_type2.setText("Submit");
    }

    public void removeInput() {

        Utils.removeInput(stateBox);
        stateBox.setOnTouchListener(new SetOnTouchList());
        stateBox.setOnClickListener(new setOnClickList());

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

                            String vAadharState = generalFunc.getJsonValue("vAadharState", obj_msg);
                            String vState = generalFunc.getJsonValue("vState", obj_msg);
                            final String vAadharImage = generalFunc.getJsonValue("vAadharImage", obj_msg);
                            final String vAadharImage1 = generalFunc.getJsonValue("vAadharImage1", obj_msg);
                            final String vAadharNum = generalFunc.getJsonValue("vAadharNum", obj_msg);
                            final String vAadharCity = generalFunc.getJsonValue("vAadharCity", obj_msg);


                            aadharCardNumBox.setText(vAadharNum);
                            cityBox.setText(vAadharCity);

                            if (!vAadharState.equals("")) {
                                stateBox.setText(vAadharState);
                                iStateId = generalFunc.getJsonValue("iPanStateId", obj_msg);
                            } else if (!vState.equals("")) {
                                stateBox.setText(vAadharState.equals("") ? generalFunc.getJsonValue("vState", obj_msg) : vAadharState);
                                iStateId = generalFunc.getJsonValue("iStateId", obj_msg);
                            }

                            if ( !vAadharCity.equals("") && !vState.equals("") && !vAadharNum.equals("")) {
                                stateBox.setOnClickListener(null);
                                cityBox.setEnabled(false);
                                aadharCardNumBox.setEnabled(false);

                                isAllInfoEditable = false;

                                aadharCardNumBox.getLabelFocusAnimator().start();
                                cityBox.getLabelFocusAnimator().start();
                                stateBox.getLabelFocusAnimator().start();
                            }

                            if (!vAadharImage.equals("")) {
                                isImageUploadEnable = false;

                                Picasso.with(getActContext())
                                        .load(vAadharImage)
                                        .into(aadharCardImgView, null);

                                photoUploadArea.setVisibility(View.GONE);
                                aadharCardImgView.setVisibility(View.VISIBLE);

                                aadharCardImgView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        (new StartActProcess(getActContext())).openURL(vAadharImage);
                                    }
                                });
                            }
                            if (!vAadharImage1.equals("")) {
                                isBackImageUploadEnable = false;

                                Picasso.with(getActContext())
                                        .load(vAadharImage1)
                                        .into(aadharCardBackImgView, null);

                                photoUploadAreaBack.setVisibility(View.GONE);
                                aadharCardBackImgView.setVisibility(View.VISIBLE);

                                aadharCardBackImgView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        (new StartActProcess(getActContext())).openURL(vAadharImage1);
                                    }
                                });
                            }
                        }

                        buildStateList(generalFunc.getJsonArray("StateList", obj_msg));
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

    public void buildStateList(JSONArray stateListArr) {

        for (int i = 0; i < stateListArr.length(); i++) {
            JSONObject obj_temp = generalFunc.getJsonObject(stateListArr, i);

            items_txt_state.add(generalFunc.getJsonValue("vState", obj_temp.toString()));
            items_state_ids.add(generalFunc.getJsonValue("iStateId", obj_temp.toString()));
        }

        CharSequence[] cs_currency_txt = items_txt_state.toArray(new CharSequence[items_txt_state.size()]);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActContext());
        builder.setTitle("Select State");

        builder.setItems(cs_currency_txt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection

                if (list_state != null) {
                    list_state.dismiss();
                }

                iStateId = items_state_ids.get(item);
                stateBox.setText(items_txt_state.get(item));

            }
        });

        list_state = builder.create();

    }

    public Context getActContext() {
        return verifyUsrAct.getActContext();
    }

    @Override
    public void onFileUploadResponse(String responseString, String type) {

        generalFunc.showGeneralMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));

        getUserData();
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == stateBox.getId()) {
                if (list_state != null) {
                    list_state.show();
                }
            } else if (i == btn_type2.getId()) {
                if (isAllInfoEditable == false) {
                    generalFunc.showGeneralMessage("", "You can't edit information once its added.");
                    return;
                }
                checkData();
            } else if (i == photoUploadArea.getId()) {
                if (isImageUploadEnable == false) {
                    generalFunc.showGeneralMessage("", "You can't upload image once its added.");
                    return;
                }
                isBackSidePressed = false;
                addNewImage();
            } else if (i == photoUploadAreaBack.getId()) {
                if (isBackImageUploadEnable == false) {
                    generalFunc.showGeneralMessage("", "You can't upload image once its added.");
                    return;
                }
                isBackSidePressed = true;
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

        boolean isAadharCardNumEntered = Utils.checkText(aadharCardNumBox) ? (Utils.getText(aadharCardNumBox).toString().length() != 12 ? Utils.setErrorFields(aadharCardNumBox, "Aadhar card number must be 12 character long.") : true) : Utils.setErrorFields(aadharCardNumBox, "Required");

        if (iStateId.equals("") || Utils.checkText(cityBox) == false || isAadharCardNumEntered == false) {

            GeneralFunctions.showMessage(GeneralFunctions.getCurrentView((Activity) getActContext()), "All information is required.");
        } else {

            final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
            generateAlert.setCancelable(false);
            generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                @Override
                public void handleBtnClick(int btn_id) {
                    if (btn_id == 1) {
                        updatePanCardDetails();
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

    public void updatePanCardDetails() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "updateAadharDetails");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("vAadharNum", Utils.getText(aadharCardNumBox));
        parameters.put("vAadharCity", Utils.getText(cityBox));
        parameters.put("iAadharStateId", iStateId);

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

        if (requestCode == ImageSourceDialog.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            boolean isStoragePermissionAvail = generalFunc.isStoragePermissionGranted();
            if (resultCode == verifyUsrAct.RESULT_OK) {
                // successfully captured the image
                // display it in image view

                final ArrayList<String[]> paramsList = new ArrayList<>();
                paramsList.add(Utils.generateImageParams("iMemberId", generalFunc.getMemberId()));
                paramsList.add(Utils.generateImageParams("type", isBackSidePressed == false ? "addAadharCardImage" : "addAadharCardBackImage"));
//                paramsList.add(Utils.generateImageParams("type", ""));

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
                                uploadImg.setLoadingMessage("Uploading your aadhar card's image...");
                                uploadImg.setResponseListener(AaadharFragment.this);
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

                paramsList.add(Utils.generateImageParams("type", isBackSidePressed == false ? "addAadharCardImage" : "addAadharCardBackImage"));

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
                                uploadImg.setLoadingMessage("Uploading your aadhar card's image...");
                                uploadImg.setResponseListener(AaadharFragment.this);
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
}
