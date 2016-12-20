package com.ahiho.apps.beeenglish.adapter;

/**
 * Created by Thep on 10/18/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.model.realm_object.SubDetailObject;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.view.sample.DestailSampleActivity;

import java.util.List;


public class RecyclerSampleSubListDetailAdapter extends RecyclerView
.Adapter<RecyclerSampleSubListDetailAdapter.DataObjectHolder> {
    private List<SubDetailObject> mDataset;
    private int max=0;
    private Context mContext;


    public RecyclerSampleSubListDetailAdapter(List<SubDetailObject> dataset) {
        mDataset = dataset;
        max=mDataset.size()-1;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView ivBar;
        private View view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivBar = (ImageView) itemView.findViewById(R.id.ivBar);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_detail_sample, parent, false);
        mContext = view.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }



    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final SubDetailObject subDetailObject = mDataset.get(position);
        holder.tvName.setText(subDetailObject.getName());
        if(position==max){
            holder.ivBar.setImageResource(R.drawable.sub_bar);
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DestailSampleActivity.class);
                intent.putExtra(Identity.EXTRA_SAMPLE_DESCRIPTION, subDetailObject.getDetail());
                intent.putExtra(Identity.EXTRA_SAMPLE_LINK, subDetailObject.getLink());
                intent.putExtra(Identity.EXTRA_SAMPLE_NAME, subDetailObject.getName());
                mContext.startActivity(intent);
            }
        });

    }

    public SubDetailObject getData(int index) {
        return mDataset.get(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}