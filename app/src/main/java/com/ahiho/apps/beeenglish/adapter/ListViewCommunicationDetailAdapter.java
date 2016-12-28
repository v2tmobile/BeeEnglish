package com.ahiho.apps.beeenglish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationSubSecondDetailObject;

import java.util.List;

/**
 * Created by Thep on 9/15/2015.
 */
public class ListViewCommunicationDetailAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<CommunicationSubSecondDetailObject> mListItem;
    private ViewHoler mHoler;

    public ListViewCommunicationDetailAdapter(Context context, List<CommunicationSubSecondDetailObject> lstMenu) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListItem = lstMenu;
    }

    @Override
    public int getCount() {
        return mListItem.size();
    }

    @Override
    public Object getItem(int position) {
        return mListItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        if (view == null) {
            view = mInflater.inflate(R.layout.row_communication_detail, null);
            mHoler = new ViewHoler();
            mHoler.tvName = (TextView) view.findViewById(R.id.tvName);
            view.setTag(mHoler);
        } else {
            mHoler = (ViewHoler) view.getTag();
        }
        final CommunicationSubSecondDetailObject object = mListItem.get(position);
       mHoler.tvName.setText(object.getName());
        return view;
    }

    private class ViewHoler {
        TextView tvName;
    }

}
