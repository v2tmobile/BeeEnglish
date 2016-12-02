package com.ahiho.apps.beeenglish.adapter;

/**
 * Created by Thep on 10/18/2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.model.TestVocabularyObject;

import java.util.List;


public class RecyclerVocabularyAdapter extends RecyclerView
        .Adapter<RecyclerVocabularyAdapter
        .DataObjectHolder> {
    private List<TestVocabularyObject> mDataset;
    private Context mContext;
    private boolean mIsRecent;
    private int screenWidth;

    public RecyclerVocabularyAdapter(List<TestVocabularyObject> dataset, boolean isRecent) {
        mDataset = dataset;
        mIsRecent = isRecent;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView tvTitleEx;
        RecyclerView rvEx;
        private View view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            view = itemView;
            rvEx = (RecyclerView) itemView.findViewById(R.id.rvEx);
            tvTitleEx = (TextView) itemView.findViewById(R.id.tvTitleEx);
        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_test_vocabulary, parent, false);
        mContext = view.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        final TestVocabularyObject testVocabularyObject = mDataset.get(position);
        holder.tvTitleEx.setText(position + ". " + testVocabularyObject.getTitle());
    }

    public void addItem(TestVocabularyObject dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public TestVocabularyObject getData(int index) {
        return mDataset.get(index);
    }

    public List<TestVocabularyObject> getAllData() {
        return mDataset;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}