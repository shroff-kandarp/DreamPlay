package com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dreamplay.ContestDetailsActivity;
import com.dreamplay.ContestsActivity;
import com.dreamplay.CreateTeamActivity;
import com.dreamplay.JoinedContestActivity;
import com.dreamplay.R;
import com.dreamplay.UserMatchTeamsActivity;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shroff on 16-Mar-18.
 */

public class ContestListRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    ContestsActivity contestAct;
    JoinedContestActivity joinedContestAct;

    public ContestListRecycleAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, boolean isFooterEnabled) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.isFooterEnabled = isFooterEnabled;

        if (mContext instanceof JoinedContestActivity) {
            joinedContestAct = (JoinedContestActivity) mContext;
        } else {
            contestAct = (ContestsActivity) mContext;
        }
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_list, parent, false);
            this.footerView = v;
            return new FooterViewHolder(v);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contest_list_header_design, parent, false);
            return new HeaderViewHolder(v);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contest_list_design, parent, false);
            return new ViewHolder(view);
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof ViewHolder) {
            final HashMap<String, String> item = list.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;


            viewHolder.confirmedTxtView.setText(item.get("eConfirmed").equalsIgnoreCase("Yes") ? "C" : "U");
            viewHolder.singleEntryTxtView.setText(item.get("eMultipleTeamJoin").equalsIgnoreCase("Yes") ? "M" : "S");

            viewHolder.joinedTxtView.setText(item.get("ContestTeamCount") + "/" + item.get("vContestSize"));
            viewHolder.winnersTxtView.setText(item.get("tWinners"));
            viewHolder.winningsTxtView.setText("₹" + item.get("vWinningAmount"));

            viewHolder.entryFeesTxtView.setText("₹" + item.get("tEntryFees"));

            if (item.get("eUserJoined") != null && item.get("eUserJoined").equalsIgnoreCase("Yes")) {
                viewHolder.joinTxtView.setText("JOIN+");
            } else {
                viewHolder.joinTxtView.setText("JOIN");
            }
            new CreateRoundedView(mContext.getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(mContext, 15), Utils.dipToPixels(mContext, 0), Color.parseColor("#FFFFFF"), viewHolder.joinTxtView);
            new CreateRoundedView(mContext.getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(mContext, 20), Utils.dipToPixels(mContext, 0), Color.parseColor("#FFFFFF"), viewHolder.singleEntryTxtView);
            new CreateRoundedView(mContext.getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(mContext, 20), Utils.dipToPixels(mContext, 0), Color.parseColor("#FFFFFF"), viewHolder.confirmedTxtView);
            viewHolder.contentArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList(view, position);
                    }
                }
            });
            viewHolder.joinArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkEligibilityToJoin(item);
                }
            });

            if (contestAct != null && contestAct.PAGE_TYPE.equalsIgnoreCase("FIXTURES")) {
                viewHolder.joinArea.setVisibility(View.VISIBLE);
            } else if (contestAct != null) {
                viewHolder.joinArea.setVisibility(View.GONE);
            }

            if (joinedContestAct != null && joinedContestAct.PAGE_TYPE.equalsIgnoreCase("FIXTURES")) {
                viewHolder.joinArea.setVisibility(View.VISIBLE);
            } else if (joinedContestAct != null) {
                viewHolder.joinArea.setVisibility(View.GONE);
            }

            viewHolder.contentArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bn = new Bundle();
                    bn.putString("iMatchId", item.get("iMatchId"));
                    bn.putString("iConstestId", item.get("iConstestId"));
                    bn.putString("PAGE_TYPE", contestAct == null ? joinedContestAct.PAGE_TYPE : contestAct.PAGE_TYPE);
                    (new StartActProcess(mContext)).startActForResult(ContestDetailsActivity.class, bn, Utils.CONTEST_DETAILS_REQ_CODE);
                }
            });
        } else if (holder instanceof HeaderViewHolder) {
            final HashMap<String, String> item = list.get(position);
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
//            headerHolder.headerTxtView.setText(Html.fromHtml(item.get("name")));
//            Utils.printLog("CCN","::"+item.get("name"));

            new CreateRoundedView(mContext.getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(mContext, 10), Utils.dipToPixels(mContext, 0), Color.parseColor("#FFFFFF"), headerHolder.singleEntryTxtView);
            new CreateRoundedView(mContext.getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(mContext, 10), Utils.dipToPixels(mContext, 0), Color.parseColor("#FFFFFF"), headerHolder.confirmedTxtView);
            new CreateRoundedView(mContext.getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(mContext, 10), Utils.dipToPixels(mContext, 0), Color.parseColor("#FFFFFF"), headerHolder.unconfirmedTxtView);
            new CreateRoundedView(mContext.getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(mContext, 10), Utils.dipToPixels(mContext, 0), Color.parseColor("#FFFFFF"), headerHolder.multiEntryTxtView);

        } else {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            this.footerHolder = footerHolder;
        }


    }

    public void checkEligibilityToJoin(final HashMap<String, String> item) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "checkContestJoinOrTeamEligibility");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("iMatchId", item.get("iMatchId"));
        parameters.put("isFromJoin", "Yes");

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(parameters);
        exeWebServer.setLoaderConfig(mContext, true, generalFunc);

        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(final String responseString) {

                if (responseString != null && !responseString.equals("")) {
                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail) {

                        if (generalFunc.getJsonValue("isShowTeamSelection", responseString).equalsIgnoreCase("Yes")) {
                            Bundle bn = new Bundle();
                            bn.putString("iMatchId", item.get("iMatchId"));
                            bn.putString("isOpenForSelection", "Yes");
                            bn.putString("iConstestId", item.get("iConstestId"));
                            bn.putString("PAGE_TYPE", contestAct == null ? joinedContestAct.PAGE_TYPE : contestAct.PAGE_TYPE);
                            (new StartActProcess(mContext)).startActForResult(UserMatchTeamsActivity.class, bn,Utils.CHOOSE_TEAM_REQ_CODE);
                        } else {

                            Bundle bn = new Bundle();
                            bn.putString("iMatchId", item.get("iMatchId"));
                            bn.putString("iConstestId", item.get("iConstestId"));
                            bn.putString("PAGE_TYPE", contestAct == null ? joinedContestAct.PAGE_TYPE : contestAct.PAGE_TYPE);
                            (new StartActProcess(mContext)).startActForResult(CreateTeamActivity.class, bn, Utils.CREATE_TEAM_REQ_CODE);
                        }


                    } else {
                        generalFunc.showGeneralMessage("", generalFunc.getJsonValue(Utils.message_str, responseString));
                    }

                } else {
                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
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
        if (footerHolder != null)
            footerHolder.progressArea.setVisibility(View.VISIBLE);
    }

    public void removeFooterView() {
        Utils.printLog("Footer", "removed");
        if (footerHolder != null)
            footerHolder.progressArea.setVisibility(View.GONE);
//        footerHolder.progressArea.setPadding(0, -1 * footerView.getHeight(), 0, 0);
    }

    public interface OnItemClickListener {
        void onItemClickList(View v, int position);
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        public MTextView singleEntryTxtView;
        public MTextView confirmedTxtView;
        public MTextView winnersTxtView;
        public MTextView entryFeesTxtView;
        public MTextView joinedTxtView;
        public MTextView winningsTxtView;
        public MTextView joinTxtView;
        public AppCompatImageView menuIcon;
        public View joinArea;
        public View contentArea;

        public ViewHolder(View view) {
            super(view);

            singleEntryTxtView = (MTextView) view.findViewById(R.id.singleEntryTxtView);
            confirmedTxtView = (MTextView) view.findViewById(R.id.confirmedTxtView);
            winnersTxtView = (MTextView) view.findViewById(R.id.winnersTxtView);
            entryFeesTxtView = (MTextView) view.findViewById(R.id.entryFeesTxtView);
            joinedTxtView = (MTextView) view.findViewById(R.id.joinedTxtView);
            winningsTxtView = (MTextView) view.findViewById(R.id.winningsTxtView);
            joinTxtView = (MTextView) view.findViewById(R.id.joinTxtView);
            menuIcon = (AppCompatImageView) view.findViewById(R.id.menuIcon);
            joinArea = view.findViewById(R.id.joinArea);
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
        MTextView singleEntryTxtView;
        MTextView confirmedTxtView;
        MTextView multiEntryTxtView;
        MTextView unconfirmedTxtView;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            singleEntryTxtView = (MTextView) itemView.findViewById(R.id.singleEntryTxtView);
            multiEntryTxtView = (MTextView) itemView.findViewById(R.id.multiEntryTxtView);
            confirmedTxtView = (MTextView) itemView.findViewById(R.id.confirmedTxtView);
            unconfirmedTxtView = (MTextView) itemView.findViewById(R.id.unconfirmedTxtView);

        }
    }
}
