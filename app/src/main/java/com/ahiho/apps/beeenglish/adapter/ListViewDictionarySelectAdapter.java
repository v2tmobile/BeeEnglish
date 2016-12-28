package com.ahiho.apps.beeenglish.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.model.realm_object.DictionaryObject;
import com.ahiho.apps.beeenglish.model.realm_object.WordObject;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Thep on 9/15/2015.
 */
public class ListViewDictionarySelectAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<DictionaryObject> mListItem;
    private ViewHoler mHoler;

    public ListViewDictionarySelectAdapter(Context context, List<DictionaryObject> lstMenu) {
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
            view = mInflater.inflate(R.layout.row_dictionary_select, null);
            mHoler = new ViewHoler();
            mHoler.tvDictionaryName = (TextView) view.findViewById(R.id.tvDictionaryName);
            mHoler.ivDictionaryIcon = (ImageView) view.findViewById(R.id.ivDictionaryIcon);
            mHoler.ivDictionaryDownload = (ImageView) view.findViewById(R.id.ivDictionaryDownload);
            view.setTag(mHoler);
        } else {
            mHoler = (ViewHoler) view.getTag();
        }
        final DictionaryObject dictionaryObject = mListItem.get(position);

        Picasso.with(mContext).load(dictionaryObject.getPicture()).into(mHoler.ivDictionaryIcon);
        mHoler.tvDictionaryName.setText(dictionaryObject.getName());
        WordObject wordObject =null;

        try{
            wordObject =dictionaryObject.getWordObjects().first();
        }catch (Exception e){

        }

        if(wordObject !=null) {
            mHoler.ivDictionaryDownload.setImageResource(R.drawable.ic_check_circle_white_48dp);
            mHoler.ivDictionaryDownload.setColorFilter(Color.parseColor("#4CAF50"));
        }else{
            mHoler.ivDictionaryDownload.setImageResource(R.drawable.ic_check_circle_white_48dp);
            mHoler.ivDictionaryDownload.setColorFilter(Color.parseColor("#b8b8b8"));
        }
        return view;
    }

    private class ViewHoler {
        ImageView ivDictionaryIcon, ivDictionaryDownload;
        TextView tvDictionaryName;
    }

}
