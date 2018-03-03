package com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dreamplay.R;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tarwindersingh on 01/03/18.
 */

public class FixturesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    long currentTimerValue = 0;
    public String PAGE_TYPE = "";

    public FixturesRecyclerAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, boolean isFooterEnabled) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.isFooterEnabled = isFooterEnabled;
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setCurrentTimerValue(long currentTimerValue) {
        this.currentTimerValue = currentTimerValue;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design_fixtures, parent, false);
            return new ViewHolder(view);
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof ViewHolder) {
            final HashMap<String, String> item = list.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;

            if (!item.get("tTeam1Logo").equals("")) {
                Picasso.with(mContext)
                        .load(item.get("tTeam1Logo"))
                        .placeholder(R.drawable.no_team_img)
                        .into(((ViewHolder) holder).leftImgView, null);
            }

            if (!item.get("tTeam2Logo").equals("")) {
                Picasso.with(mContext)
                        .load(item.get("tTeam2Logo"))
                        .placeholder(R.drawable.no_team_img)
                        .into(((ViewHolder) holder).rightImgView, null);
            }

            viewHolder.infoTxtView.setText(item.get("vMatchType"));
            viewHolder.leftTeamNameTxtView.setText(item.get("vTeam1"));
            viewHolder.rightTeamNameTxtView.setText(item.get("vTeam2"));
            viewHolder.dateInfoTxtView.setText(item.get("matchStartDate"));

            viewHolder.contentArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList(view, position);
                    }
                }
            });

            if (item.get("matchStartDateInMilli") != null && !item.get("matchStartDateInMilli").equals("")) {
                viewHolder.dateRemainsInfoTxtView.setVisibility(View.VISIBLE);
                long milliSecRemains = (GeneralFunctions.parseLong(0, item.get("matchStartDateInMilli")) - (currentTimerValue * 1000));
                long currMilliSecRemains = GeneralFunctions.parseLong(0, item.get("currentTimeInMilli"));
                milliSecRemains = milliSecRemains - currMilliSecRemains;
                if (milliSecRemains > 1) {
                    viewHolder.dateRemainsInfoTxtView.setText(Utils.getDurationBreakdown(milliSecRemains));
                } else {
                    viewHolder.dateRemainsInfoTxtView.setText("LIVE");
                }
            } else {
                viewHolder.dateRemainsInfoTxtView.setText(PAGE_TYPE.equals("LIVE") ? "In Progress" : "Completed");
                viewHolder.dateRemainsInfoTxtView.setVisibility(View.VISIBLE);
            }


            if ((position + 1) == list.size()) {
                viewHolder.seperatorView.setVisibility(View.GONE);
            } else {
                viewHolder.seperatorView.setVisibility(View.VISIBLE);
            }
        } else if (holder instanceof HeaderViewHolder) {
            final HashMap<String, String> item = list.get(position);
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headerTxtView.setText(Html.fromHtml(item.get("name")));
//            Utils.printLog("CCN","::"+item.get("name"));
        } else {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            this.footerHolder = footerHolder;
        }


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

        public MTextView infoTxtView;
        public MTextView leftTeamNameTxtView;
        public MTextView rightTeamNameTxtView;
        public MTextView dateInfoTxtView;
        public MTextView dateRemainsInfoTxtView;
        public ImageView leftImgView;
        public ImageView rightImgView;
        public View contentArea;
        public View seperatorView;

        public ViewHolder(View view) {
            super(view);

            infoTxtView = (MTextView) view.findViewById(R.id.infoTxtView);
            leftTeamNameTxtView = (MTextView) view.findViewById(R.id.leftTeamNameTxtView);
            rightTeamNameTxtView = (MTextView) view.findViewById(R.id.rightTeamNameTxtView);
            dateInfoTxtView = (MTextView) view.findViewById(R.id.dateInfoTxtView);
            dateRemainsInfoTxtView = (MTextView) view.findViewById(R.id.dateRemainsInfoTxtView);
            leftImgView = (ImageView) view.findViewById(R.id.leftImgView);
            rightImgView = (ImageView) view.findViewById(R.id.rightImgView);
            seperatorView = view.findViewById(R.id.seperatorView);
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
