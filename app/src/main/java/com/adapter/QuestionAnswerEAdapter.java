package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.dreamplay.R;
import com.general.files.GeneralFunctions;
import com.view.MTextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 17-05-2016.
 */
public class QuestionAnswerEAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private GeneralFunctions generalFunc;

    private HashMap<String, List<String>> _listDataChild;

    private int currentExpandedPosition =-1;
    ExpandableListView expListView;

    public QuestionAnswerEAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData, ExpandableListView expListView) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.expListView = expListView;
        generalFunc = new GeneralFunctions(context);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public String wrapHtml(Context context, String html) {
        return context.getString(R.string.html, html);
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.help_answers_list_item, null);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.questions_answer_list_header,
                    null);
        }

        ((MTextView) convertView.findViewById(R.id.questionTxt)).setText(headerTitle);

        View dataArea = convertView.findViewById(R.id.dataArea);
        final ImageView indicatorImg = (ImageView) convertView.findViewById(R.id.indicatorImg);

        final WebView webView = (WebView) convertView.findViewById(R.id.webView);
        // WebView webView = new WebView(_context);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setVerticalScrollBarEnabled(false);
        webView.setLongClickable(false);
        webView.setHapticFeedbackEnabled(false);
        //  container.removeAllViewsInLayout();
        // container.addView(webView);
        final String childText = (String) getChild(groupPosition, 0);
        webView.loadDataWithBaseURL(null, generalFunc.wrapHtml(webView.getContext(), childText),
                "text/html", "UTF-8", null);


        if (isExpanded) {
            currentExpandedPosition = groupPosition;
            indicatorImg.setImageResource(R.mipmap.ic_collapse);
            webView.setVisibility(View.VISIBLE);
        } else {
            indicatorImg.setImageResource(R.mipmap.ic_ellapse);
            webView.setVisibility(View.GONE);
        }

        dataArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(webView.getVisibility() == View.VISIBLE){
                    expListView.collapseGroup(groupPosition);
                }else{
                    expListView.expandGroup(groupPosition);
                }
            }
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
