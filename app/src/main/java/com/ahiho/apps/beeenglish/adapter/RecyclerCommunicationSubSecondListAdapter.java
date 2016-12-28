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
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationSubSecondObject;
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationSubSecondObject;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.view.communication.CommunicationDetailActivity;
import com.ahiho.apps.beeenglish.view.communication.CommunicationSubSecondActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecyclerCommunicationSubSecondListAdapter extends RecyclerView
        .Adapter<RecyclerCommunicationSubSecondListAdapter.DataObjectHolder> {
    private List<CommunicationSubSecondObject> mDataset;
    private Context mContext;
    private int mId;
    private int mSubId;

    public RecyclerCommunicationSubSecondListAdapter(List<CommunicationSubSecondObject> dataset,int id, int subId) {
        mDataset = dataset;
        mId=id;
        mSubId=subId;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView tvNumber, tvName,tvDescription;
        ImageView ivIcon;
        private View view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_communication_sub, parent, false);
        mContext = view.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }


    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final CommunicationSubSecondObject object = mDataset.get(position);
        holder.tvName.setText(object.getName());
        holder.tvDescription.setText(object.getDescription());
        Picasso.with(mContext).load(object.getIcon()).into(holder.ivIcon, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                holder.tvNumber.setText(String.valueOf(position + 1));
                holder.ivIcon.setVisibility(View.GONE);
                holder.tvNumber.setVisibility(View.VISIBLE);

            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommunicationDetailActivity.class);
                intent.putExtra(Identity.EXTRA_ID,mId);
                intent.putExtra(Identity.EXTRA_ID_SUB,mSubId);
                intent.putExtra(Identity.EXTRA_ID_SUB_SECOND,object.getId());
                intent.putExtra(Identity.EXTRA_TITLE,object.getName());
                mContext.startActivity(intent);
            }
        });

    }

    public CommunicationSubSecondObject getData(int index) {
        return mDataset.get(index);
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}