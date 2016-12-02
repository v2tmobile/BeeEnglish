package com.ahiho.apps.beeenglish.adapter;

/**
 * Created by Thep on 10/18/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.controller.RealmController;
import com.ahiho.apps.beeenglish.model.BookObject;
import com.ahiho.apps.beeenglish.my_interface.OnCallbackDownload;
import com.ahiho.apps.beeenglish.my_interface.RealmRecyclerViewAdapter;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.util.MyFile;
import com.ahiho.apps.beeenglish.view.PDFReaderActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import io.realm.Realm;


public class RecyclerBooksAdapter extends RealmRecyclerViewAdapter<BookObject> {
    private List<BookObject> mDataset;
    private Context mContext;
    private Realm realm;

    public RecyclerBooksAdapter(List<BookObject> dataset) {
        mDataset = dataset;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView tvBookName, tvBookDescription, tvProgress, tvDownloaded;
        RelativeLayout rlProgressDownload, rlDownloadErr;
        ProgressBar pbDownload;
        ImageView ivBookPicture;
        private View view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivBookPicture = (ImageView) itemView.findViewById(R.id.ivBookPicture);
            tvBookName = (TextView) itemView.findViewById(R.id.tvBookBookName);
            tvBookDescription = (TextView) itemView.findViewById(R.id.tvBookDescription);
            tvProgress = (TextView) itemView.findViewById(R.id.tvProgress);
            tvDownloaded = (TextView) itemView.findViewById(R.id.tvDownloaded);
            rlProgressDownload = (RelativeLayout) itemView.findViewById(R.id.rlProgressDownload);
            rlDownloadErr = (RelativeLayout) itemView.findViewById(R.id.rlDownloadErr);
            pbDownload = (ProgressBar) itemView.findViewById(R.id.pbDownload);

        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_book, parent, false);
        mContext = view.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        realm = RealmController.getInstance().getRealm();
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final BookObject bookObject = mDataset.get(position);
        final String fileUri =MyFile.convertUri2FileUri(bookObject.getUri());
        final boolean fileExist = new File(fileUri).exists();
        final DataObjectHolder holder = (DataObjectHolder) viewHolder;
        final String uriContent = bookObject.getUri();
        holder.tvBookName.setText(bookObject.getName());
        holder.tvBookDescription.setText(bookObject.getDescription());
        Picasso.with(mContext).load(bookObject.getIconUri()).into(holder.ivBookPicture);
        if (fileExist) {
            holder.tvDownloaded.setText(mContext.getString(R.string.success_downloaded_file));
        } else {
            holder.tvDownloaded.setText(mContext.getString(R.string.not_download_file));

        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileExist) {
                    startPDFActivity(fileUri);
                } else {
                    if (holder.rlProgressDownload.getVisibility() != View.VISIBLE) {
                        holder.rlDownloadErr.setVisibility(View.INVISIBLE);
                        holder.tvDownloaded.setVisibility(View.INVISIBLE);
                        holder.rlProgressDownload.setVisibility(View.VISIBLE);
                        new DownloadFile(holder, bookObject, position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
        });
    }

    private void startPDFActivity(String uriFile) {
        Intent intent = new Intent(mContext, PDFReaderActivity.class);
        intent.putExtra(Identity.EXTRA_PDF_FILE_NAME, uriFile);
        mContext.startActivity(intent);
    }

    public void addItem(BookObject dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public BookObject getData(int index) {
        return mDataset.get(index);
    }

    public List<BookObject> getAllData() {
        return mDataset;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    class DownloadFile extends AsyncTask<Void, Integer, String> {
        private String mUriFile;
        private DataObjectHolder mHolder;
        private BookObject mBookObject;
        private String mUriImage;
        private int mPosition;

        public DownloadFile(DataObjectHolder holder, BookObject bookObject, int position) {
            mHolder = holder;
            mUriImage = "";
            mBookObject = bookObject;
            mPosition = position;

            mUriFile = mBookObject.getUri();
            mUriImage = mBookObject.getIconUri();
            mHolder.pbDownload.setProgress(0);
            mHolder.tvProgress.setText(0 + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            publishProgress(1);
            MyFile.downloadFile(mContext, mUriImage, MyFile.getFileName(mUriImage), new OnCallbackDownload() {
                @Override
                public void postProgress(float progress) {
                }

                @Override
                public void downloadSuccess(String uriFile) {
                    mUriImage = uriFile;
                }

                @Override
                public void downloadError(Exception exception) {

                }
            });

            MyFile.downloadFile(mContext, mUriFile, MyFile.getFileName(mUriFile), new OnCallbackDownload() {
                @Override
                public void postProgress(float progress) {
                    publishProgress((int) progress);
                }

                @Override
                public void downloadSuccess(String uriFile) {
                    mUriFile = uriFile;
                }

                @Override
                public void downloadError(Exception exception) {

                }
            });
            return mUriFile;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if(values[0]>0) {
                mHolder.pbDownload.setProgress(values[0]);
                mHolder.tvProgress.setText(values[0] + "%");
                super.onProgressUpdate(values);
            }
        }

        @Override
        protected void onPostExecute(String uriFile) {
            mHolder.rlProgressDownload.setVisibility(View.INVISIBLE);
            if (uriFile != null && !uriFile.isEmpty()) {
                mHolder.tvDownloaded.setText(mContext.getString(R.string.success_downloaded_file));
                mHolder.tvDownloaded.setVisibility(View.VISIBLE);
                realm.beginTransaction();
                mBookObject.setUri(uriFile);
                mBookObject.setIconUri(mUriImage);
                realm.copyToRealmOrUpdate(mBookObject);
                realm.commitTransaction();
                notifyItemChanged(mPosition);
            } else {
                mHolder.rlDownloadErr.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(uriFile);
        }
    }

}