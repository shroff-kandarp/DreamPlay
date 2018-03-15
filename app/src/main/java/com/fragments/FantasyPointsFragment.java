package com.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.dreamplay.FantasyPointsActivity;
import com.dreamplay.R;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FantasyPointsFragment extends Fragment {


    FantasyPointsActivity fantasyPointsAct;
    GeneralFunctions generalFunc;

    View view;
    MTextView descriptionTxtView;
    TableLayout table_main;

    int iFantasyPointsCategoryPositionId = 0;

    String iFantasyPointsCategoryId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fantasy_points, container, false);

        fantasyPointsAct = (FantasyPointsActivity) getActivity();
        generalFunc = fantasyPointsAct.generalFunc;
        iFantasyPointsCategoryPositionId = GeneralFunctions.parseInt(0, getArguments().getString("iFantasyPointsCategoryPositionId"));

        descriptionTxtView = (MTextView) view.findViewById(R.id.descriptionTxtView);
        table_main = (TableLayout) view.findViewById(R.id.table_main);

        String description_str = fantasyPointsAct.fantasyPointsCategoryList.get(iFantasyPointsCategoryPositionId).get("tDescription");
        iFantasyPointsCategoryId = fantasyPointsAct.fantasyPointsCategoryList.get(iFantasyPointsCategoryPositionId).get("iFantasyPointsCategoryId");
        descriptionTxtView.setText(Html.fromHtml(description_str));

        new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 2), Utils.dipToPixels(getActContext(), 2), getActContext().getResources().getColor(R.color.appThemeColor_1), table_main);
        setPoints();
        return view;
    }

    public void setPoints() {
        TableRow row0 = new TableRow(getActContext());
        row0.setBackgroundColor(getActContext().getResources().getColor(R.color.appThemeColor_1));
//        row0.setPadding(Utils.dipToPixels(getActContext(),5),Utils.dipToPixels(getActContext(),5),Utils.dipToPixels(getActContext(),5),Utils.dipToPixels(getActContext(),5));


        row0.setPadding(0,0,0,Utils.dipToPixels(getActContext(),5));


        MTextView tv0 = new MTextView(getActContext());
        tv0.setText("Type of Points");
        tv0.setTextColor(getActContext().getResources().getColor(R.color.appThemeColor_TXT_1));
        tv0.setWidth(TableRow.LayoutParams.MATCH_PARENT);
        tv0.setGravity(Gravity.CENTER);
        tv0.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv0.setHeight(Utils.dipToPixels(getActContext(), 50));
        row0.addView(tv0);
        for (int i = 0; i < fantasyPointsAct.matchCategoryList.size(); i++) {

            MTextView tv_tmp = new MTextView(getActContext());
            tv_tmp.setText(fantasyPointsAct.matchCategoryList.get(i).get("vName"));
            tv_tmp.setTextColor(getActContext().getResources().getColor(R.color.appThemeColor_TXT_1));
            tv_tmp.setWidth(TableRow.LayoutParams.MATCH_PARENT);
            tv_tmp.setGravity(Gravity.CENTER);
            tv_tmp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv_tmp.setHeight(Utils.dipToPixels(getActContext(), 50));
            row0.addView(tv_tmp);
        }

        table_main.addView(row0);


        for (int i = 0; i < fantasyPointsAct.pointsTypesList.size(); i++) {
            String iPointTypeId = fantasyPointsAct.pointsTypesList.get(i).get("iPointTypeId");
            TableRow pointsRow = new TableRow(getActContext());

            if(i%2 == 0){
                pointsRow.setBackgroundColor(getActContext().getResources().getColor(android.R.color.transparent));
            }else{
                pointsRow.setBackgroundColor(Color.parseColor("#E8E8E8"));
            }
            pointsRow.setPadding(0,0,0,Utils.dipToPixels(getActContext(),5));
            MTextView tv_tmp = new MTextView(getActContext());
            tv_tmp.setText(fantasyPointsAct.pointsTypesList.get(i).get("vName").trim());
            tv_tmp.setTextColor(Color.parseColor("#767676"));
            tv_tmp.setWidth(TableRow.LayoutParams.MATCH_PARENT);
            tv_tmp.setGravity(Gravity.CENTER);
            tv_tmp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv_tmp.setHeight(Utils.dipToPixels(getActContext(), 50));
            pointsRow.addView(tv_tmp);

            for (int j = 0; j < fantasyPointsAct.matchCategoryList.size(); j++) {
                String iMatchCategoryId = fantasyPointsAct.matchCategoryList.get(j).get("iMatchCategoryId");


                for (int k = 0; k < fantasyPointsAct.fantasyPointsList.size(); k++) {
                    String iFantasyPointsCategoryId_tmp = fantasyPointsAct.fantasyPointsList.get(k).get("iFantasyPointsCategoryId");
                    String iMatchCategoryId_tmp = fantasyPointsAct.fantasyPointsList.get(k).get("iMatchCategoryId");
                    String iPointTypeId_tmp = fantasyPointsAct.fantasyPointsList.get(k).get("iPointTypeId");
                    String dPoint_tmp = fantasyPointsAct.fantasyPointsList.get(k).get("dPoint");

                    if (iPointTypeId_tmp.equals(iPointTypeId) && iMatchCategoryId_tmp.equals(iMatchCategoryId) && iFantasyPointsCategoryId_tmp.equals(iFantasyPointsCategoryId)) {

                        MTextView tv_tmp_points = new MTextView(getActContext());
                        tv_tmp_points.setText(dPoint_tmp);
                        tv_tmp_points.setTextColor(Color.parseColor("#767676"));
                        tv_tmp_points.setWidth(TableRow.LayoutParams.MATCH_PARENT);
                        tv_tmp_points.setGravity(Gravity.CENTER);
                        tv_tmp_points.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        tv_tmp_points.setHeight(Utils.dipToPixels(getActContext(), 50));
                        pointsRow.addView(tv_tmp_points);
                    }
                }
            }


            table_main.addView(pointsRow);


            TableLayout.LayoutParams paramsTmpRow = (TableLayout.LayoutParams)pointsRow.getLayoutParams();
            paramsTmpRow.setMargins(Utils.dipToPixels(getActContext(),2),0,Utils.dipToPixels(getActContext(),2),0);

            pointsRow.setLayoutParams(paramsTmpRow);
        }
    }

    public Context getActContext() {
        return fantasyPointsAct.getActContext();
    }
}
