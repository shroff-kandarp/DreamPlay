package com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dreamplay.R;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.MTextView;

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

    public UserMatchTeamsAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, boolean isFooterEnabled) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.isFooterEnabled = isFooterEnabled;
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

                }
            });

            viewHolder.editTeamTxtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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

    private void openTeamPreview(){

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