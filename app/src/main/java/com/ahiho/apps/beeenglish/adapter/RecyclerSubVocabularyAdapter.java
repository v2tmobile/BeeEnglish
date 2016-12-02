package com.ahiho.apps.beeenglish.adapter;

/**
 * Created by Thep on 10/18/2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.model.TestSubVocabularyObject;

import java.util.List;


public class RecyclerSubVocabularyAdapter extends RecyclerView
        .Adapter<RecyclerSubVocabularyAdapter
        .DataObjectHolder> {
    private List<TestSubVocabularyObject> mDataset;
    private Context mContext;
    private boolean mIsRecent;
    private int screenWidth;

    public RecyclerSubVocabularyAdapter(List<TestSubVocabularyObject> dataset, boolean isRecent) {
        mDataset = dataset;
        mIsRecent = isRecent;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView tvTextEx, tvNumEx, tvAnswer;
        RecyclerView rvEx;
        RadioGroup rgAnswer;
        RadioButton rbA, rbB, rbC, rbD;
        private View view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            view = itemView;
            rvEx = (RecyclerView) itemView.findViewById(R.id.rvEx);
            tvTextEx = (TextView) itemView.findViewById(R.id.tvTitleEx);
            tvAnswer = (TextView) itemView.findViewById(R.id.tvAnswer);
            rgAnswer = (RadioGroup) itemView.findViewById(R.id.rgAnswer);
            rbA = (RadioButton) itemView.findViewById(R.id.rbA);
            rbB = (RadioButton) itemView.findViewById(R.id.rbB);
            rbC = (RadioButton) itemView.findViewById(R.id.rbC);
            rbD = (RadioButton) itemView.findViewById(R.id.rbD);
            tvNumEx = (TextView) itemView.findViewById(R.id.tvNumEx);
        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_sub_test_vocabulary, parent, false);
        mContext = view.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        final TestSubVocabularyObject testSubVocabularyObject = mDataset.get(position);
        holder.tvNumEx.setText(position + ".");
        String question;
        switch (testSubVocabularyObject.getType()) {
            case 0:
                question = testSubVocabularyObject.getQuestion().replace("***",
                        "<u><font color=\"" + "#c5c5c5" + "\">....</u></font>");
                break;
            case 1:
                question = testSubVocabularyObject.getQuestion();
                holder.tvAnswer.setVisibility(View.VISIBLE);
                holder.tvAnswer.setText("<u><font color=\"" + "#c5c5c5" + "\">........</u></font>");
                break;
            default:
                String arr[] = testSubVocabularyObject.getQuestion().split(":::");
                question = arr[0].replace("***",
                        "<u><font color=\"" + "#c5c5c5" + "\">....</u></font>");
                arr = arr[0].split(";;;");
                holder.rgAnswer.setVisibility(View.VISIBLE);
                holder.rbA.setText(arr[0]);
                holder.rbB.setText(arr[1]);
                holder.rbC.setText(arr[2]);
                holder.rbD.setText(arr[3]);
                break;
        }
        holder.tvTextEx.setText(Html.fromHtml(question));
    }

    public void addItem(TestSubVocabularyObject dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public TestSubVocabularyObject getData(int index) {
        return mDataset.get(index);
    }

    public List<TestSubVocabularyObject> getAllData() {
        return mDataset;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}