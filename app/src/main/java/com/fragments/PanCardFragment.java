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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_CANCELED;

/**
 * A simple {@link Fragment} subclass.
 */
public class PanCardFragment extends Fragment implements UploadImage.SetResponseListener {


    View view;

    View containerView;
    MaterialEditText nameBox;
    MaterialEditText panCardNumBox;
    MaterialEditText dobBox;
    MaterialEditText stateBox;
    boolean isDOBSelected = false;
    String iStateId = "";
    android.support.v7.app.AlertDialog list_state;
    ArrayList<String> items_txt_state = new ArrayList<String>();
    ArrayList<String> items_state_ids = new ArrayList<String>();

    ProgressBar loadingBar;

    MTextView noDataTxt;


    View photoUploadArea;
    View imgAdd;

    ImageView panCardImgView;

    MButton btn_type2;
    MButton uploadPanImageBtn;

    GeneralFunctions generalFunc;
    VerifyUserActivity verifyUsrAct;
    boolean isAllInfoEditable = true;

    boolean isImageUploadEnable = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pan_card, container, false);

        verifyUsrAct = (VerifyUserActivity) getActivity();
        generalFunc = verifyUsrAct.generalFunc;
        noDataTxt = (MTextView) view.findViewById(R.id.noDataTxt);
        nameBox = (MaterialEditText) view.findViewById(R.id.nameBox);
        dobBox = (MaterialEditText) view.findViewById(R.id.dobBox);
        stateBox = (MaterialEditText) view.findViewById(R.id.stateBox);
        panCardNumBox = (MaterialEditText) view.findViewById(R.id.panCardNumBox);
        loadingBar = (ProgressBar) view.findViewById(R.id.loadingBar);
        panCardImgView = (ImageView) view.findViewById(R.id.panCardImgView);
        imgAdd = view.findViewById(R.id.imgAdd);
        photoUploadArea = view.findViewById(R.id.photoUploadArea);
        containerView = view.findViewById(R.id.containerView);

        btn_type2 = ((MaterialRippleLayout) view.findViewById(R.id.btn_type2)).getChildView();
        uploadPanImageBtn = ((MaterialRippleLayout) view.findViewById(R.id.uploadPanImageBtn)).getChildView();

        btn_type2.setId(Utils.generateViewId());
        uploadPanImageBtn.setId(Utils.generateViewId());

        photoUploadArea.setOnClickListener(new setOnClickList());
        dobBox.setOnClickListener(new setOnClickList());
        btn_type2.setOnClickListener(new setOnClickList());
        uploadPanImageBtn.setOnClickListener(new setOnClickList());

        new CreateRoundedView(getActContext().getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(getActContext(), 25), 0, getActContext().getResources().getColor(R.color.appThemeColor_1), imgAdd);

        setLabels();
        removeInput();

        getUserData();
        return view;
    }

    public void setLabels() {

        nameBox.setBothText("Name", "Enter name");
        dobBox.setBothText("DOB", "Enter date of birth");

        stateBox.setBothText("State", "Select your state");
        panCardNumBox.setBothText("Pan Number", "Enter pan card number");
        btn_type2.setText("Submit");
        uploadPanImageBtn.setText("Upload Pan Card's Image");
    }

    public void removeInput() {

        Utils.removeInput(stateBox);
        stateBox.setOnTouchListener(new SetOnTouchList());
        stateBox.setOnClickListener(new setOnClickList());

        Utils.removeInput(dobBox);
        dobBox.setOnTouchListener(new SetOnTouchList());
        dobBox.setOnClickListener(new setOnClickList());
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

                            String vPanCardName = generalFunc.getJsonValue("vPanCardName", obj_msg);
                            String tPanDOB = generalFunc.getJsonValue("tPanDOB", obj_msg);
                            String vPanState = generalFunc.getJsonValue("vPanState", obj_msg);
                            String vState = generalFunc.getJsonValue("vState", obj_msg);
                            String vPanCardNum = generalFunc.getJsonValue("vPanCardNum", obj_msg);
                            final String vPanImage = generalFunc.getJsonValue("vPanImage", obj_msg);

                            if (vPanCardName.equals("")) {
                                nameBox.setText(generalFunc.getJsonValue("vName", obj_msg));
                            } else {

                                nameBox.setText(vPanCardName);
                            }
                            panCardNumBox.setText(vPanCardNum);
                            dobBox.setText(tPanDOB.equals("") ? generalFunc.getJsonValue("dDOB", obj_msg) : tPanDOB);

                            if (!dobBox.getText().toString().equals("") || !tPanDOB.equals("")) {
                                isDOBSelected = true;
                            }
                            if (!vPanState.equals("")) {
                                stateBox.setText(vPanState);
                                iStateId = generalFunc.getJsonValue("iPanStateId", obj_msg);
                            } else if (!vState.equals("")) {
                                stateBox.setText(vPanState.equals("") ? generalFunc.getJsonValue("vState", obj_msg) : vPanState);
                                iStateId = generalFunc.getJsonValue("iStateId", obj_msg);
                            }

                            if (!tPanDOB.equals("") && !vPanState.equals("") && !vState.equals("") && !vPanCardNum.equals("") && !vPanCardName.equals("")) {
                                stateBox.setOnClickListener(null);
                                dobBox.setOnClickListener(null);
                                nameBox.setEnabled(false);
                                panCardNumBox.setEnabled(false);

                                isAllInfoEditable = false;

                                nameBox.getLabelFocusAnimator().start();
                                panCardNumBox.getLabelFocusAnimator().start();
                                dobBox.getLabelFocusAnimator().start();
                                stateBox.getLabelFocusAnimator().start();
                            }

                            if (!vPanImage.equals("")) {
                                isImageUploadEnable = false;

                                Picasso.with(getActContext())
                                        .load(vPanImage)
                                        .into(panCardImgView, null);

                                photoUploadArea.setVisibility(View.GONE);
                                panCardImgView.setVisibility(View.VISIBLE);

                                panCardImgView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        (new StartActProcess(getActContext())).openURL(vPanImage);
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
            if (i == dobBox.getId()) {
                chooseDate();
            } else if (i == stateBox.getId()) {
                if (list_state != null) {
                    list_state.show();
                }
            } else if (i == btn_type2.getId()) {
                if (isAllInfoEditable == false) {
                    generalFunc.showGeneralMessage("", "You can't edit information once its added.");
                    return;
                }
                checkData();
            } else if (i == uploadPanImageBtn.getId()) {
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

    public void chooseDate() {

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        String day = dayOfMonth < 10 ? ("0" + dayOfMonth) : ("" + dayOfMonth);
                        String month = monthOfYear < 10 ? ("0" + monthOfYear) : ("" + monthOfYear);

                        dobBox.setText("" + year + "-" + month + "-" + day);
                        isDOBSelected = true;
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMaxDate(now);
        dpd.setTitle("Select Date of birth");
        dpd.show(verifyUsrAct.getFragmentManager(), "Datepickerdialog");
    }

    public void checkData() {

        boolean isPanCardNumEntered = Utils.checkText(panCardNumBox) ? (Utils.getText(panCardNumBox).toString().length() != 8 ? Utils.setErrorFields(panCardNumBox, "Pan card number must be 8 character long.") : true) : Utils.setErrorFields(panCardNumBox, "Required");

        if (iStateId.equals("") || isDOBSelected == false || Utils.checkText(nameBox) == false || isPanCardNumEntered == false) {

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
        parameters.put("type", "updatePanCardDetails");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("vPanCardName", Utils.getText(nameBox));
        parameters.put("vPanCardNum", Utils.getText(panCardNumBox));
        parameters.put("tPanDOB", Utils.getText(dobBox));
        parameters.put("iPanStateId", iStateId);

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
                paramsList.add(Utils.generateImageParams("type", "addPanCardImage"));

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
                                uploadImg.setLoadingMessage("Uploading your pan card's image...");
                                uploadImg.setResponseListener(PanCardFragment.this);
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

                paramsList.add(Utils.generateImageParams("type", "addPanCardImage"));

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
                                uploadImg.setLoadingMessage("Uploading your pan card's image...");
                                uploadImg.setResponseListener(PanCardFragment.this);
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
