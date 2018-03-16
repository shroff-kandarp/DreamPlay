package com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dreamplay.ChooseCaptainActivity;
import com.dreamplay.R;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shroff on 13-Mar-18.
 */

public class TeamPListForCaptainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    public String selectedCaptainId = "";
    public String selectedViceCaptainId = "";

    ChooseCaptainActivity chooseCaptainAct;

    public TeamPListForCaptainAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, boolean isFooterEnabled) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.isFooterEnabled = isFooterEnabled;
        chooseCaptainAct = (ChooseCaptainActivity) mContext;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design_team_plist_captain, parent, false);
            return new ViewHolder(view);
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof ViewHolder) {
            final HashMap<String, String> item = list.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;


            viewHolder.playerNameTxtView.setText(Html.fromHtml(item.get("vPlayerName")));


            if (item.get("ePlayerType").equalsIgnoreCase("Wicketkeeper")) {
                viewHolder.playerImgView.setImageResource(R.mipmap.keeper_helmat);
            } else if (item.get("ePlayerType").equalsIgnoreCase("Batsman")) {
                viewHolder.playerImgView.setImageResource(R.mipmap.cric_batsman);
            } else if (item.get("ePlayerType").equalsIgnoreCase("Allrounder")) {
                viewHolder.playerImgView.setImageResource(R.mipmap.cric_allrounder);
            } else if (item.get("ePlayerType").equalsIgnoreCase("Bowler")) {
                viewHolder.playerImgView.setImageResource(R.mipmap.cric_bowler);
            } else {
                viewHolder.playerImgView.setImageResource(R.mipmap.cric_allrounder);
//                if (!item.get("vImgName").equals("")) {
//                Picasso.with(mContext)
//                        .load(item.get("vImgName"))
//                        .placeholder(R.drawable.no_team_img)
//                        .into(viewHolder.playerImgView, null);
//                }
            }


            if (selectedCaptainId.equalsIgnoreCase(item.get("iPlayerId"))) {

                new CreateRoundedView(Color.parseColor("#FDE7BD"), Utils.dipToPixels(mContext, 27), Utils.dipToPixels(mContext, 2), Color.parseColor("#FDE7BD"), viewHolder.captainTxtView);
            } else {

                new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(mContext, 27), Utils.dipToPixels(mContext, 2), Color.parseColor("#D4D4D4"), viewHolder.captainTxtView);
            }
            if (selectedViceCaptainId.equalsIgnoreCase(item.get("iPlayerId"))) {
                viewHolder.viceCaptainTxtView.setTextColor(mContext.getResources().getColor(R.color.appThemeColor_TXT_1));
                new CreateRoundedView(mContext.getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(mContext, 27), Utils.dipToPixels(mContext, 2), mContext.getResources().getColor(R.color.appThemeColor_1), viewHolder.viceCaptainTxtView);
            } else {
                viewHolder.viceCaptainTxtView.setTextColor(Color.parseColor("#272727"));

                new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(mContext, 27), Utils.dipToPixels(mContext, 2), Color.parseColor("#D4D4D4"), viewHolder.viceCaptainTxtView);
            }

            viewHolder.viceCaptainTxtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedViceCaptainId.equalsIgnoreCase(item.get("iPlayerId"))) {
                        selectedViceCaptainId = "";
                    } else if (selectedCaptainId.equals("")) {
                        selectedViceCaptainId = item.get("iPlayerId");
                    } else if (!selectedCaptainId.equals("") && !selectedCaptainId.equalsIgnoreCase(item.get("iPlayerId"))) {
                        selectedViceCaptainId = item.get("iPlayerId");
                    }
                    notifyDataSetChanged();
                }
            });
            viewHolder.captainTxtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (selectedCaptainId.equalsIgnoreCase(item.get("iPlayerId"))) {
                        selectedCaptainId = "";
                    } else if (selectedViceCaptainId.equals("")) {
                        selectedCaptainId = item.get("iPlayerId");
                    } else if (!selectedViceCaptainId.equals("") && !selectedViceCaptainId.equalsIgnoreCase(item.get("iPlayerId"))) {
                        selectedCaptainId = item.get("iPlayerId");
                    }

                    notifyDataSetChanged();
                }
            });

            viewHolder.contentArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList(view, position);
                    }
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

        public MTextView playerNameTxtView;
        public MTextView captainTxtView;
        public MTextView viceCaptainTxtView;
        public SelectableRoundedImageView playerImgView;
        public View contentArea;

        public ViewHolder(View view) {
            super(view);

            playerNameTxtView = (MTextView) view.findViewById(R.id.playerNameTxtView);
            captainTxtView = (MTextView) view.findViewById(R.id.captainTxtView);
            viceCaptainTxtView = (MTextView) view.findViewById(R.id.viceCaptainTxtView);
            playerImgView = (SelectableRoundedImageView) view.findViewById(R.id.playerImgView);
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