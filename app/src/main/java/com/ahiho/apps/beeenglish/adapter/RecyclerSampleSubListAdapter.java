package com.ahiho.apps.beeenglish.adapter;

/**
 * Created by Thep on 10/18/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.model.realm_object.SubDetailObject;
import com.ahiho.apps.beeenglish.model.realm_object.SubObject;
import com.ahiho.apps.beeenglish.my_interface.OnRecyclerViewItemClickListener;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.view.sample.DestailSampleActivity;

import java.util.List;


public class RecyclerSampleSubListAdapter extends RecyclerView
        .Adapter<RecyclerSampleSubListAdapter.DataObjectHolder> {
    private List<SubObject> mDataset;
    private Context mContext;


    public RecyclerSampleSubListAdapter(List<SubObject> dataset) {
        mDataset = dataset;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        CardView cvRow;
        TextView tvNumber, tvName;
        RelativeLayout rlIcon;
        ImageView ivIcon;
        RecyclerView rvExSub;
        private View view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            view = itemView;
            cvRow = (CardView) itemView.findViewById(R.id.cvRow);
            rlIcon = (RelativeLayout) itemView.findViewById(R.id.rlIcon);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            rvExSub = (RecyclerView) itemView.findViewById(R.id.rvExSub);


        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_sub_sample, parent, false);
        mContext = view.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }


    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final SubObject subObject = mDataset.get(position);
        holder.tvName.setText(subObject.getName());
        holder.rlIcon.setVisibility(View.VISIBLE);
        holder.tvNumber.setVisibility(View.VISIBLE);
        holder.tvNumber.setText(String.valueOf(position + 1));
        List<SubDetailObject> subDetailObjects = null;
        try {
            subDetailObjects = subObject.getData();
        } catch (Exception e) {

        }
        final List<SubDetailObject> finalSubDetailObjects = subDetailObjects;
        if (finalSubDetailObjects != null && finalSubDetailObjects.size() > 0) {
            if(finalSubDetailObjects.size()>1){
                holder.rvExSub.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                holder.rvExSub.setAdapter(new RecyclerSampleSubListDetailAdapter(finalSubDetailObjects));
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.rvExSub.getVisibility() == View.VISIBLE) {
                            holder.rvExSub.setVisibility(View.GONE);
                        } else {
                            holder.rvExSub.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }else {
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SubDetailObject subDetailObject = finalSubDetailObjects.get(0);
                        Intent intent = new Intent(mContext, DestailSampleActivity.class);
                        intent.putExtra(Identity.EXTRA_SAMPLE_DESCRIPTION, subDetailObject.getDetail());
                        intent.putExtra(Identity.EXTRA_SAMPLE_LINK, subDetailObject.getLink());
                        intent.putExtra(Identity.EXTRA_SAMPLE_NAME, subDetailObject.getName());
                        mContext.startActivity(intent);
                    }
                });

            }

        }


    }


    public void addItem(SubObject dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public SubObject getData(int index) {
        return mDataset.get(index);
    }

    public List<SubObject> getAllData() {
        return mDataset;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}