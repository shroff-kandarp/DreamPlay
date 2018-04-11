package com.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dreamplay.CreateTeamActivity;
import com.dreamplay.R;
import com.dreamplay.UserMatchTeamsActivity;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shroff on 18-Mar-18.
 */

public class UserMatchTeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 2;
    public static final int TYPE_FOOTER = 3;
    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list;
    Context mContext;
    boolean isFooterEnabled = false;
    View footerView;
    FooterViewHolder footerHolder;
    private OnItemClickListener mItemClickListener;

    UserMatchTeamsActivity userMatchTeamAct;

    public UserMatchTeamsAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, boolean isFooterEnabled) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.isFooterEnabled = isFooterEnabled;

        userMatchTeamAct = (UserMatchTeamsActivity) mContext;
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public void setDataList(ArrayList<HashMap<String, String>> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_list, parent, false);
            this.footerView = v;
            return new FooterViewHolder(v);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manu_header_design, parent, false);
            return new HeaderViewHolder(v);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design_match_team, parent, false);
            return new ViewHolder(view);
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof ViewHolder) {
            final HashMap<String, String> item = list.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;


            viewHolder.captainTxtView.setText(item.get("captainName"));
            viewHolder.viceCaptainTxtView.setText(item.get("viceCaptainName"));

            viewHolder.wkCountTxtView.setText(item.get("wkCount"));
            viewHolder.batCountTxtView.setText(item.get("batCount"));
            viewHolder.allCountTxtView.setText(item.get("allCount"));
            viewHolder.bowlCountTxtView.setText(item.get("bowlCount"));
            viewHolder.teamCountTxtView.setText("TEAM " + (position + 1));
//            viewHolder.contentArea.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if (mItemClickListener != null) {
//                        mItemClickListener.onItemClickList(view, position);
//                    }
//                }
//            });

            viewHolder.previewTeamTxtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openTeamPreview(generalFunc.getJsonArray(item.get("PlayerDetailsList")));
                }
            });

            viewHolder.editTeamTxtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bn = new Bundle();
                    bn.putString("iMatchId", item.get("iMatchId"));
                    bn.putString("iConstestId", item.get("iContestId"));
                    bn.putString("iUserTeamId", item.get("iUserTeamId"));
                    bn.putString("iJoinId", item.get("iJoinId"));
                    bn.putString("PAGE_TYPE",userMatchTeamAct.getIntent().getStringExtra("PAGE_TYPE"));
                    (new StartActProcess(mContext)).startActForResult(CreateTeamActivity.class, bn, Utils.CREATE_TEAM_REQ_CODE);
                }
            });
        } else if (holder instanceof HeaderViewHolder) {
            final HashMap<String, String> item = list.get(position);
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headerTxtView.setText(Html.fromHtml(item.get("vName")));
//            Utils.printLog("CCN","::"+item.get("name"));
        } else {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            this.footerHolder = footerHolder;
        }

    }

    private void openTeamPreview(JSONArray playerDetailsListArr) {
        if (playerDetailsListArr == null) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.cricket_team_design, null);

        MTextView closeTxtView = (MTextView) convertView.findViewById(R.id.closeTxtView);

        MTextView wicketKeeperCreditTxtView = (MTextView) convertView.findViewById(R.id.wicketKeeperCreditTxtView);
        MTextView payer1CreditTxtView = (MTextView) convertView.findViewById(R.id.payer1CreditTxtView);
        MTextView payer2CreditTxtView = (MTextView) convertView.findViewById(R.id.payer2CreditTxtView);
        MTextView payer3CreditTxtView = (MTextView) convertView.findViewById(R.id.payer3CreditTxtView);
        MTextView payer4CreditTxtView = (MTextView) convertView.findViewById(R.id.payer4CreditTxtView);
        MTextView payer5CreditTxtView = (MTextView) convertView.findViewById(R.id.payer5CreditTxtView);
        MTextView payer6CreditTxtView = (MTextView) convertView.findViewById(R.id.payer6CreditTxtView);
        MTextView payer7CreditTxtView = (MTextView) convertView.findViewById(R.id.payer7CreditTxtView);
        MTextView payer8CreditTxtView = (MTextView) convertView.findViewById(R.id.payer8CreditTxtView);
        MTextView payer9CreditTxtView = (MTextView) convertView.findViewById(R.id.payer9CreditTxtView);
        MTextView payer10CreditTxtView = (MTextView) convertView.findViewById(R.id.payer10CreditTxtView);

        ImageView wicketKeeperImgView = (ImageView) convertView.findViewById(R.id.wicketKeeperImgView);
        ImageView player1ImgView = (ImageView) convertView.findViewById(R.id.player1ImgView);
        ImageView player2ImgView = (ImageView) convertView.findViewById(R.id.player2ImgView);
        ImageView player3ImgView = (ImageView) convertView.findViewById(R.id.player3ImgView);
        ImageView player4ImgView = (ImageView) convertView.findViewById(R.id.player4ImgView);
        ImageView player5ImgView = (ImageView) convertView.findViewById(R.id.player5ImgView);
        ImageView player6ImgView = (ImageView) convertView.findViewById(R.id.player6ImgView);
        ImageView player7ImgView = (ImageView) convertView.findViewById(R.id.player7ImgView);
        ImageView player8ImgView = (ImageView) convertView.findViewById(R.id.player8ImgView);
        ImageView player9ImgView = (ImageView) convertView.findViewById(R.id.player9ImgView);
        ImageView player10ImgView = (ImageView) convertView.findViewById(R.id.player10ImgView);

        MTextView wicketKeeperTxtView = (MTextView) convertView.findViewById(R.id.wicketKeeperTxtView);
        MTextView player1TxtView = (MTextView) convertView.findViewById(R.id.player1TxtView);
        MTextView player2TxtView = (MTextView) convertView.findViewById(R.id.player2TxtView);
        MTextView player3TxtView = (MTextView) convertView.findViewById(R.id.player3TxtView);
        MTextView player4TxtView = (MTextView) convertView.findViewById(R.id.player4TxtView);
        MTextView player5TxtView = (MTextView) convertView.findViewById(R.id.player5TxtView);
        MTextView player6TxtView = (MTextView) convertView.findViewById(R.id.player6TxtView);
        MTextView player7TxtView = (MTextView) convertView.findViewById(R.id.player7TxtView);
        MTextView player8TxtView = (MTextView) convertView.findViewById(R.id.player8TxtView);
        MTextView player9TxtView = (MTextView) convertView.findViewById(R.id.player9TxtView);
        MTextView player10TxtView = (MTextView) convertView.findViewById(R.id.player10TxtView);

        for (int i = 0; i < playerDetailsListArr.length(); i++) {

            JSONObject obj_temp = generalFunc.getJsonObject(playerDetailsListArr, i);


            switch (i) {
                case 0:
                    wicketKeeperTxtView.setText(generalFunc.getJsonValue("vPlayerName", obj_temp));
                    wicketKeeperCreditTxtView.setText(generalFunc.getJsonValue("tCredits", obj_temp) + " CR");
                    break;
                case 1:
                    if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Batsman")) {
                        player1ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    } else if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Bowler")) {
                        player1ImgView.setImageResource(R.mipmap.ic_bowl);
                    } else {
                        player1ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    }
                    payer1CreditTxtView.setText(generalFunc.getJsonValue("tCredits", obj_temp) + " CR");
                    player1TxtView.setText(generalFunc.getJsonValue("vPlayerName", obj_temp));
                    break;
                case 2:
                    if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Batsman")) {
                        player2ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    } else if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Bowler")) {
                        player2ImgView.setImageResource(R.mipmap.ic_bowl);
                    } else {
                        player2ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    }
                    payer2CreditTxtView.setText(generalFunc.getJsonValue("tCredits", obj_temp) + " CR");
                    player2TxtView.setText(generalFunc.getJsonValue("vPlayerName", obj_temp));
                    break;
                case 3:
                    if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Batsman")) {
                        player3ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    } else if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Bowler")) {
                        player3ImgView.setImageResource(R.mipmap.ic_bowl);
                    } else {
                        player3ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    }
                    payer3CreditTxtView.setText(generalFunc.getJsonValue("tCredits", obj_temp) + " CR");
                    player3TxtView.setText(generalFunc.getJsonValue("vPlayerName", obj_temp));
                    break;
                case 4:
                    if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Batsman")) {
                        player4ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    } else if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Bowler")) {
                        player4ImgView.setImageResource(R.mipmap.ic_bowl);
                    } else {
                        player4ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    }
                    payer4CreditTxtView.setText(generalFunc.getJsonValue("tCredits", obj_temp) + " CR");
                    player4TxtView.setText(generalFunc.getJsonValue("vPlayerName", obj_temp));
                    break;
                case 5:
                    if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Batsman")) {
                        player5ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    } else if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Bowler")) {
                        player5ImgView.setImageResource(R.mipmap.ic_bowl);
                    } else {
                        player5ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    }
                    payer5CreditTxtView.setText(generalFunc.getJsonValue("tCredits", obj_temp) + " CR");
                    player5TxtView.setText(generalFunc.getJsonValue("vPlayerName", obj_temp));
                    break;
                case 6:
                    if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Batsman")) {
                        player6ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    } else if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Bowler")) {
                        player6ImgView.setImageResource(R.mipmap.ic_bowl);
                    } else {
                        player6ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    }
                    payer6CreditTxtView.setText(generalFunc.getJsonValue("tCredits", obj_temp) + " CR");
                    player6TxtView.setText(generalFunc.getJsonValue("vPlayerName", obj_temp));
                    break;
                case 7:
                    if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Batsman")) {
                        player7ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    } else if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Bowler")) {
                        player7ImgView.setImageResource(R.mipmap.ic_bowl);
                    } else {
                        player7ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    }
                    payer7CreditTxtView.setText(generalFunc.getJsonValue("tCredits", obj_temp) + " CR");
                    player7TxtView.setText(generalFunc.getJsonValue("vPlayerName", obj_temp));
                    break;
                case 8:
                    if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Batsman")) {
                        player8ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    } else if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Bowler")) {
                        player8ImgView.setImageResource(R.mipmap.ic_bowl);
                    } else {
                        player8ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    }
                    payer8CreditTxtView.setText(generalFunc.getJsonValue("tCredits", obj_temp) + " CR");
                    player8TxtView.setText(generalFunc.getJsonValue("vPlayerName", obj_temp));
                    break;
                case 9:
                    if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Batsman")) {
                        player9ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    } else if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Bowler")) {
                        player9ImgView.setImageResource(R.mipmap.ic_bowl);
                    } else {
                        player9ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    }
                    payer9CreditTxtView.setText(generalFunc.getJsonValue("tCredits", obj_temp) + " CR");
                    player9TxtView.setText(generalFunc.getJsonValue("vPlayerName", obj_temp));
                    break;
                case 10:
                    if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Batsman")) {
                        player10ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    } else if (generalFunc.getJsonValue("ePlayerType", obj_temp).equals("Bowler")) {
                        player10ImgView.setImageResource(R.mipmap.ic_bowl);
                    } else {
                        player10ImgView.setImageResource(R.mipmap.ic_wk_hal);
                    }
                    payer10CreditTxtView.setText(generalFunc.getJsonValue("tCredits", obj_temp) + " CR");
                    player10TxtView.setText(generalFunc.getJsonValue("vPlayerName", obj_temp));
                    break;
            }
        }
        Animation bottomUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up_anim);
        userMatchTeamAct.previewContainerView.startAnimation(bottomUp);
        userMatchTeamAct.previewContainerView.addView(convertView);
        userMatchTeamAct.previewContainerView.setVisibility(View.VISIBLE);

        closeTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation bottomDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_down_anim);
                bottomDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        userMatchTeamAct.previewContainerView.removeAllViews();
                        userMatchTeamAct.previewContainerView.requestLayout();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                userMatchTeamAct.previewContainerView.startAnimation(bottomDown);
                userMatchTeamAct.previewContainerView.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position) && isFooterEnabled == true) {
            return TYPE_FOOTER;
        } else if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position == list.size();
    }

    private boolean isPositionHeader(int position) {
        return list.get(position).get("TYPE").equals("" + TYPE_HEADER);
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (isFooterEnabled == true) {
            return list.size() + 1;
        } else {
            return list.size();
        }

    }

    public void addFooterView() {
        Utils.printLog("Footer", "added");
        this.isFooterEnabled = true;
        notifyDataSetChanged();
        if (footerHolder != null) footerHolder.progressArea.setVisibility(View.VISIBLE);
    }

    public void removeFooterView() {
        Utils.printLog("Footer", "removed");
        if (footerHolder != null) footerHolder.progressArea.setVisibility(View.GONE);
//        footerHolder.progressArea.setPadding(0, -1 * footerView.getHeight(), 0, 0);
    }

    public interface OnItemClickListener {
        void onItemClickList(View v, int position);
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        public MTextView bowlCountTxtView;
        public MTextView allCountTxtView;
        public MTextView batCountTxtView;
        public MTextView wkCountTxtView;
        public MTextView captainTxtView;
        public MTextView viceCaptainTxtView;
        public MTextView teamCountTxtView;
        public MTextView previewTeamTxtView;
        public MTextView editTeamTxtView;
        public View contentArea;

        public ViewHolder(View view) {
            super(view);

            bowlCountTxtView = (MTextView) view.findViewById(R.id.bowlCountTxtView);
            allCountTxtView = (MTextView) view.findViewById(R.id.allCountTxtView);
            batCountTxtView = (MTextView) view.findViewById(R.id.batCountTxtView);
            wkCountTxtView = (MTextView) view.findViewById(R.id.wkCountTxtView);
            captainTxtView = (MTextView) view.findViewById(R.id.captainTxtView);
            viceCaptainTxtView = (MTextView) view.findViewById(R.id.viceCaptainTxtView);
            teamCountTxtView = (MTextView) view.findViewById(R.id.teamCountTxtView);
            previewTeamTxtView = (MTextView) view.findViewById(R.id.previewTeamTxtView);
            editTeamTxtView = (MTextView) view.findViewById(R.id.editTeamTxtView);
            contentArea = view;
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout progressArea;

        public FooterViewHolder(View itemView) {
            super(itemView);

            progressArea = (LinearLayout) itemView;

        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        MTextView headerTxtView;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            headerTxtView = (MTextView) itemView.findViewById(R.id.headerTxtView);

        }
    }
}