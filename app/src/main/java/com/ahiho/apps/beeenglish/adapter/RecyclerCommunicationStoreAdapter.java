package com.ahiho.apps.beeenglish.adapter;

/**
 * Created by Thep on 10/18/2015.
 */

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.Toast;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.model.CommunicationQueryObject;
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationObject;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.util.MyDownloadManager;
import com.ahiho.apps.beeenglish.util.MyFile;
import com.ahiho.apps.beeenglish.util.UtilSharedPreferences;
import com.ahiho.apps.beeenglish.util.deserialize.CommunicationDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;


public class RecyclerCommunicationStoreAdapter extends  RecyclerView.Adapter<RecyclerCommunicationStoreAdapter.DataObjectHolder> {
    private List<CommunicationQueryObject> mDataset;
    private List<CommunicationObject> mCommunicationObjects ;
    private Context mContext;
    private Activity mActivity;
    private UtilSharedPreferences mUtilSharedPreferences;
    private MyDownloadManager mDownloadManager;
    private List<Long> idDownload;


    public RecyclerCommunicationStoreAdapter(List<CommunicationQueryObject> dataset, Activity activity) {
        mDataset = dataset;
        mActivity = activity;
        mCommunicationObjects = new ArrayList<>();
        idDownload = new ArrayList<>();
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            mCommunicationObjects= realm.allObjects(CommunicationObject.class);
        }catch (Exception e){

        }finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvProgress, tvDownloaded;
        RelativeLayout rlProgressDownload, rlDownloadErr;
        ProgressBar pbDownload;
        ImageView ivIcon;
        private View view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
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
                .inflate(R.layout.row_communication_store, parent, false);
        mContext = view.getContext();
        mDownloadManager= new MyDownloadManager(mContext);
        mUtilSharedPreferences = UtilSharedPreferences.getInstanceSharedPreferences(mContext);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final CommunicationQueryObject object = mDataset.get(position);
        holder.tvName.setText(object.getName());
        holder.tvDescription.setText(object.getDescription());
        Picasso.with(mContext).load(object.getIcon()).into(holder.ivIcon, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                holder.ivIcon.setImageResource(R.drawable.ic_insert_photo_white_24dp);
                holder.ivIcon.setColorFilter(Color.GRAY);
            }
        });
        final String fileName = MyFile.getFileName(object.getLink());
        File file = new File(MyFile.APP_FOLDER + "/" + MyFile.DOWNLOADS_FOLDER + "/" + fileName);
        long id = mUtilSharedPreferences.getBookDownloadId(fileName);
        int status = mDownloadManager.statusDownloading(id);
        if (status == DownloadManager.STATUS_PENDING || status == DownloadManager.STATUS_RUNNING) {
            visibleDownloading(holder);
            showProgressDownloading(id, holder, position);
        } else {
            boolean isExist = false;
            for(CommunicationObject communicationObject:mCommunicationObjects){
                if(communicationObject.getId()==object.getId()){
                    isExist=true;
                    break;
                }
            }
            final Uri destinationUri = Uri.fromFile(file);
            if (isExist) {
                downloadSuccess(holder, position);
            } else {
                holder.tvDownloaded.setText(mContext.getString(R.string.not_download_file));
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (holder.rlProgressDownload.getVisibility() != View.VISIBLE) {
                            visibleDownloading(holder);
                            long currentId = mDownloadManager.downloadData(object.getLink(), object.getName(), destinationUri);
                            mUtilSharedPreferences.setBookDownloadId(fileName,currentId);
                            showProgressDownloading(currentId, holder, position);
                        }
                    }
                });
            }

        }

    }

    private void visibleDownloading(DataObjectHolder holder) {
        holder.rlDownloadErr.setVisibility(View.INVISIBLE);
        holder.tvDownloaded.setVisibility(View.INVISIBLE);
        holder.pbDownload.setIndeterminate(false);
        holder.rlProgressDownload.setVisibility(View.VISIBLE);
    }
    private void visibleExtracting(DataObjectHolder holder) {
        holder.rlDownloadErr.setVisibility(View.INVISIBLE);
        holder.tvDownloaded.setVisibility(View.INVISIBLE);
        holder.pbDownload.setIndeterminate(true);
        holder.rlProgressDownload.setVisibility(View.VISIBLE);
    }


    private void downloadSuccess(DataObjectHolder holder, int position) {
        holder.rlProgressDownload.setVisibility(View.INVISIBLE);
        holder.tvDownloaded.setText(mContext.getString(R.string.success_downloaded_file));
        holder.tvDownloaded.setVisibility(View.VISIBLE);
//        realm.commitTransaction();
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        try {
            notifyItemChanged(position);
        } catch (Exception e) {

        }

    }

    private void downloadFail(DataObjectHolder holder) {
        holder.rlProgressDownload.setVisibility(View.INVISIBLE);
        holder.rlDownloadErr.setVisibility(View.VISIBLE);
    }

    public CommunicationQueryObject getData(int index) {
        return mDataset.get(index);
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void showProgressDownloading(final long downloadId, final DataObjectHolder holder, final int position) {

        final Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(downloadId);
                Cursor cursor = mDownloadManager.getDownloadManager().query(q);
                cursor.moveToFirst();
                int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                final int dl_progress = (bytes_downloaded * 100 / bytes_total);
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                final int status = cursor.getInt(columnIndex);
                cursor.close();

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dl_progress >= 0) {
                            holder.pbDownload.setProgress(dl_progress);
                            holder.tvProgress.setText(dl_progress + "%");
                        } else {
                            holder.pbDownload.setProgress(0);
                            holder.tvProgress.setText(mContext.getString(R.string.pending_download_file));
                        }
                        switch (status) {
                            case DownloadManager.STATUS_FAILED:
                                downloadFail(holder);
                                myTimer.cancel();
                                break;
                            case DownloadManager.STATUS_PAUSED:
                                break;
                            case DownloadManager.STATUS_PENDING:
                                break;
                            case DownloadManager.STATUS_RUNNING:
                                break;
                            case DownloadManager.STATUS_SUCCESSFUL:
                                boolean isStart=true;
                                if(idDownload.size()>0){
                                    for(long l:idDownload){
                                        if(l==downloadId){
                                            isStart=false;
                                            break;
                                        }
                                    }
                                }
                                if(isStart) {
                                    idDownload.add(downloadId);
                                    new UnzipFile(downloadId, holder, position).execute();
                                }
                                myTimer.cancel();
                                break;
                        }
                    }
                });

            }

        }, 0, 10);
    }


    class UnzipFile extends AsyncTask<Void, Void, Boolean> {
        private String mUri;
        private DataObjectHolder holder;
        private int position;
        public UnzipFile(long id,DataObjectHolder holder,int position) {
            mUri = mDownloadManager.getStringUriFileDownload(id);
            this.holder=holder;
            this.position=position;
            visibleExtracting(holder);
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            String des = MyFile.APP_FOLDER + "/" + MyFile.DOWNLOADS_FOLDER + "/";
            return MyFile.unzipWithLib(mUri, des, "");


        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                File file = new File(mUri);
                file.delete();
                new ReadJson(mUri,holder,position).execute();
            } else {
                Toast.makeText(mContext, R.string.err_read_file, Toast.LENGTH_SHORT).show();

            }

            super.onPostExecute(result);
        }
    }

    class ReadJson extends AsyncTask<Void, Long, Boolean> {
        private String mUri;
        private DataObjectHolder holder;
        private int position;

        public ReadJson(String uri,DataObjectHolder holder,int position) {
            mUri = uri.replace(".zip", ".json");
            this.holder=holder;
            this.position=position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return readCommunication(mUri);
        }


        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                File file = new File(mUri);
                file.delete();
                downloadSuccess(holder,position);
            }else {
                downloadFail(holder);
            }
            super.onPostExecute(result);
        }
    }

    private boolean readCommunication(String mUri) {
        boolean result = true;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CommunicationObject.class, new CommunicationDeserializer())
                .create();
        JsonReader reader = null;
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            reader = new JsonReader(new FileReader(mUri));
            CommunicationObject data = gson.fromJson(reader, Identity.COMMUNICATION_TYPE);// contains the whole reviews list
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(data);
            realm.commitTransaction();


        } catch (Exception e) {

            return false;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }

        return result;
    }

}