package com.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dreamplay.CreateTeamActivity;
import com.dreamplay.R;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shroff on 12-Mar-18.
 */

public class TeamPlayerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 2;
    public static final int TYPE_FOOTER = 3;
    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list;
    ArrayList<HashMap<String, String>> listOfAllPlayersData;
    Context mContext;
    boolean isFooterEnabled = false;
    View footerView;
    FooterViewHolder footerHolder;
    private OnItemClickListener mItemClickListener;

    public ArrayList<String> chosenPlayersList = new ArrayList<>();
    public int totalSelectedPlayers = 0;
    public int totalSizeForSelection = 0;

    public MTextView countSelectionTxtView;
    public MTextView countTotalPlayersTxtView;
    public MTextView countTotalCreditsInfoTxtView;
    CreateTeamActivity createTeamAct;

    public TeamPlayerListAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, boolean isFooterEnabled) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.isFooterEnabled = isFooterEnabled;
        createTeamAct = (CreateTeamActivity) mContext;
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setDataList(ArrayList<HashMap<String, String>> list) {
        this.list = list;
    }
    public void setAllPlayersList(ArrayList<HashMap<String, String>> listOfAllPlayersData) {
        this.listOfAllPlayersData = listOfAllPlayersData;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design_team_player_list, parent, false);
            return new ViewHolder(view);
        }

    }

    public boolean isPlayerSelectedAllowedForSelectedTeam(String iPlayerId) {
        int countOfTeam1 = 0;
        String team1 = "";

        int countOfTeam2 = 0;
        String team2 = "";
        Utils.printLog("countOfTeam","Size::"+chosenPlayersList.size()+"::"+chosenPlayersList.toString());
        for (int i = 0; i < chosenPlayersList.size(); i++) {
            String temp_player_id = chosenPlayersList.get(i);
            for (int j = 0; j < listOfAllPlayersData.size(); j++) {

                if(!temp_player_id.equals(listOfAllPlayersData.get(j).get("iPlayerId"))){
                    continue;
                }
                if(team1.equals("")){
                    team1 = listOfAllPlayersData.get(j).get("vTeamName");
                    countOfTeam1 = countOfTeam1 +1;
                }else if(team2.equals("") && !listOfAllPlayersData.get(j).get("vTeamName").equals(team1)){
                    team2 = listOfAllPlayersData.get(j).get("vTeamName");
                    countOfTeam2 = countOfTeam2 +1;
                }else{
                    String tempTeamName =  listOfAllPlayersData.get(j).get("vTeamName");

                    if(tempTeamName.equals(team1)){
                        countOfTeam1 = countOfTeam1 +1;
                    }else if(tempTeamName.equals(team2)){
                        countOfTeam2 = countOfTeam2 +1;
                    }

                }


            }
        }

        Utils.printLog("countOfTeam","countOfTeam1::"+countOfTeam1);
        Utils.printLog("countOfTeam","countOfTeam2::"+countOfTeam2);
        String selectedPlayerTeamName = "";

        for (int j = 0; j < listOfAllPlayersData.size(); j++) {

           String iPlayerId_tmp =  listOfAllPlayersData.get(j).get("iPlayerId");
           if(iPlayerId_tmp.equals(iPlayerId)){
               selectedPlayerTeamName =  listOfAllPlayersData.get(j).get("vTeamName");
           }
        }
        if((countOfTeam1 >= 7 && selectedPlayerTeamName.equals(team1)) || (countOfTeam2 >= 7 && selectedPlayerTeamName.equals(team2) )){
            return false;
        }

        return true;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof ViewHolder) {
            final HashMap<String, String> item = list.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;


            viewHolder.playerNameTxtView.setText(Html.fromHtml(item.get("vPlayerName")));

            viewHolder.playerTeamTxtView.setText(item.get("vTeamName"));
            viewHolder.pointsTxtView.setText(item.get("tPoints"));
            viewHolder.creditsTxtView.setText(item.get("tCredits"));
            if (!item.get("vImgName").equals("")) {
//                Picasso.with(mContext)
//                        .load(item.get("vImgName"))
//                        .placeholder(R.drawable.no_team_img)
//                        .into(viewHolder.playerImgView, null);
            }

            if (createTeamAct.selectedType.equals("Keeper")) {
//                viewHolder.playerImgView.setImageResource(R.mipmap.keeper_helmat);
                viewHolder.playerImgView.setImageResource(R.mipmap.ic_wk_hal);
            } else if (createTeamAct.selectedType.equals("BatsMan")) {
//                viewHolder.playerImgView.setImageResource(R.mipmap.cric_batsman);
                viewHolder.playerImgView.setImageResource(R.mipmap.ic_wk_hal);
            } else if (createTeamAct.selectedType.equals("AllRounder")) {
//                viewHolder.playerImgView.setImageResource(R.mipmap.cric_allrounder);
                viewHolder.playerImgView.setImageResource(R.mipmap.ic_wk_hal);
            } else if (createTeamAct.selectedType.equals("Bowlers")) {
//                viewHolder.playerImgView.setImageResource(R.mipmap.cric_bowler);
                viewHolder.playerImgView.setImageResource(R.mipmap.ic_bowl);
            } else {
//                viewHolder.playerImgView.setImageResource(R.mipmap.cric_allrounder);
                viewHolder.playerImgView.setImageResource(R.mipmap.ic_wk_hal);
            }

            new CreateRoundedView(mContext.getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(mContext, 8), Utils.dipToPixels(mContext, 2), Color.parseColor("#051e28"), viewHolder.selectionView);
            if (chosenPlayersList.contains(item.get("iPlayerId"))) {
                viewHolder.selectionView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.selectionView.setVisibility(View.GONE);
            }

            viewHolder.contentArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (chosenPlayersList.size() >= 11 && !chosenPlayersList.contains(item.get("iPlayerId"))) {
                        GeneralFunctions.showMessage(GeneralFunctions.getCurrentView((Activity) mContext), "More then 11 players selection are not allowed.");
                        return;
                    }

                    if(!chosenPlayersList.contains(item.get("iPlayerId")) && !isPlayerSelectedAllowedForSelectedTeam(item.get("iPlayerId"))){
                        GeneralFunctions.showMessage(GeneralFunctions.getCurrentView((Activity) mContext), "Maximum 7 players are allowed from same team.");
                        return;
                    }

                    if (chosenPlayersList.contains(item.get("iPlayerId"))) {
                        chosenPlayersList.remove(item.get("iPlayerId"));
                        totalSelectedPlayers = totalSelectedPlayers - 1;
                        viewHolder.selectionView.setVisibility(View.GONE);
                        createTeamAct.totalAvailCredit = createTeamAct.totalAvailCredit + GeneralFunctions.parseDouble(0.0, viewHolder.creditsTxtView.getText().toString());

                    } else {
                        if (totalSizeForSelection > totalSelectedPlayers) {
                            if(createTeamAct.totalAvailCredit < GeneralFunctions.parseDouble(0.0, viewHolder.creditsTxtView.getText().toString())){

                                GeneralFunctions.showMessage(GeneralFunctions.getCurrentView((Activity) mContext), "No more credits are available to select this player.");
                                return;
                            }
                            chosenPlayersList.add(item.get("iPlayerId"));
                            totalSelectedPlayers = totalSelectedPlayers + 1;
                            viewHolder.selectionView.setVisibility(View.VISIBLE);
                            createTeamAct.totalAvailCredit = createTeamAct.totalAvailCredit - GeneralFunctions.parseDouble(0.0, viewHolder.creditsTxtView.getText().toString());
                        } else {
                            if (totalSizeForSelection > 1) {

                                GeneralFunctions.showMessage(GeneralFunctions.getCurrentView((Activity) mContext), "More then " + totalSizeForSelection + " players selection are not allowed for this category.");
                            } else {

                                GeneralFunctions.showMessage(GeneralFunctions.getCurrentView((Activity) mContext), "More then " + totalSizeForSelection + " player selection is not allowed for this category.");
                            }
                        }
                    }
                    if (countSelectionTxtView != null) {
                        countSelectionTxtView.setText("" + totalSelectedPlayers);
                        if (totalSizeForSelection == totalSelectedPlayers) {
                            new CreateRoundedView(Color.parseColor("#32CD32"), Utils.dipToPixels(mContext, 15), Utils.dipToPixels(mContext, 0), mContext.getResources().getColor(R.color.appThemeColor_1), countSelectionTxtView);
//                            countSelectionTxtView.setBackgroundColor(Color.parseColor("#32CD32"));
                        } else {
                            new CreateRoundedView(Color.parseColor("#BBBBBB"), Utils.dipToPixels(mContext, 15), Utils.dipToPixels(mContext, 0), mContext.getResources().getColor(R.color.appThemeColor_1), countSelectionTxtView);

//                            countSelectionTxtView.setBackgroundColor(Color.parseColor("#BBBBBB"));
                        }
                    }

                    countTotalPlayersTxtView.setText("PLAYERS " + chosenPlayersList.size() + "/11");
                    createTeamAct.countTotalCreditsInfoTxtView.setText("CREDITS LEFT " + String.format("%.2f", createTeamAct.totalAvailCredit) + "/" + (int) createTeamAct.OrigAvailCredit);
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList(view, position);
                    }

                    notifyDataSetChanged();
                }
            });
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

        public MTextView playerNameTxtView;
        public MTextView pointsTxtView;
        public MTextView playerTeamTxtView;
        public MTextView creditsTxtView;
        public SelectableRoundedImageView playerImgView;
        public View contentArea;
        public View selectionView;

        public ViewHolder(View view) {
            super(view);

            playerNameTxtView = (MTextView) view.findViewById(R.id.playerNameTxtView);
            pointsTxtView = (MTextView) view.findViewById(R.id.pointsTxtView);
            playerTeamTxtView = (MTextView) view.findViewById(R.id.playerTeamTxtView);
            creditsTxtView = (MTextView) view.findViewById(R.id.creditsTxtView);
            playerImgView = (SelectableRoundedImageView) view.findViewById(R.id.playerImgView);
            selectionView = view.findViewById(R.id.selectionView);
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