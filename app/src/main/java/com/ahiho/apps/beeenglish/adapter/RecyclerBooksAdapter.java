package com.ahiho.apps.beeenglish.adapter;

/**
 * Created by Thep on 10/18/2015.
 */

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
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
import com.ahiho.apps.beeenglish.my_interface.RealmRecyclerViewAdapter;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.util.MyDownloadManager;
import com.ahiho.apps.beeenglish.util.MyFile;
import com.ahiho.apps.beeenglish.util.UtilSharedPreferences;
import com.ahiho.apps.beeenglish.view.PDFReaderActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

import static android.content.Context.DOWNLOAD_SERVICE;


public class RecyclerBooksAdapter extends RealmRecyclerViewAdapter<BookObject> {
    private List<BookObject> mDataset;
    private Context mContext;
    private Activity mActivity;
    private Realm realm;
    private UtilSharedPreferences mUtilSharedPreferences;
    private MyDownloadManager mDownloadManager;


    public RecyclerBooksAdapter(List<BookObject> dataset, Activity activity) {
        mDataset = dataset;
        mActivity = activity;
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
        mDownloadManager= new MyDownloadManager(mContext);
        mUtilSharedPreferences = UtilSharedPreferences.getInstanceSharedPreferences(mContext);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        realm = RealmController.getInstance().getRealm();
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final BookObject bookObject = mDataset.get(position);
        final DataObjectHolder holder = (DataObjectHolder) viewHolder;
        holder.tvBookName.setText(bookObject.getName());
        holder.tvBookDescription.setText(bookObject.getDescription());
        Picasso.with(mContext).load(bookObject.getIconUri()).into(holder.ivBookPicture);
        final String bookUri = bookObject.getUri();
        final String fileName = MyFile.getFileName(bookUri);
        File file = new File(Environment.getExternalStorageDirectory() + "/" + MyFile.APP_FOLDER + "/" + MyFile.BOOK_FOLDER + "/" + fileName);
        long id = mUtilSharedPreferences.getBookDownloadId(fileName);
        int status = mDownloadManager.statusDownloading(id);
        if (status == DownloadManager.STATUS_PENDING || status == DownloadManager.STATUS_RUNNING) {
            visibleDownloading(holder);
            showProgressDownloading(id, holder, position);
        } else {
            final Uri destinationUri = Uri.fromFile(file);
            if (file.exists()) {
                downloadSuccess(holder, position, destinationUri);
            } else {
                holder.tvDownloaded.setText(mContext.getString(R.string.not_download_file));
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (holder.rlProgressDownload.getVisibility() != View.VISIBLE) {
                            visibleDownloading(holder);
                            long currentId = mDownloadManager.downloadData(bookUri, bookObject.getName(), destinationUri);
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
        holder.rlProgressDownload.setVisibility(View.VISIBLE);
    }


    private void downloadSuccess(DataObjectHolder holder, int position, Uri destinationUri) {
        holder.rlProgressDownload.setVisibility(View.INVISIBLE);
        holder.tvDownloaded.setText(mContext.getString(R.string.success_downloaded_file));
        holder.tvDownloaded.setVisibility(View.VISIBLE);
//        try {
//            realm.beginTransaction();
//
//        } catch (Exception e) {
//            realm.cancelTransaction();
//            realm.beginTransaction();
//        }
        final String bookUriString = destinationUri.toString();
//        bookObject.setUri(bookUriString);
//        realm.copyToRealmOrUpdate(bookObject);
//        realm.commitTransaction();
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPDFActivity(MyFile.convertUri2FileUri(bookUriString));
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

//    private long DownloadData(String filePath, String bookName, Uri destinationUri) {
//
//        long downloadReference;
//        Uri uri = Uri.parse(filePath);
//        // Create request for android download manager
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//        //Setting title of request
//        request.setTitle(mContext.getString(R.string.app_name) + " " + mContext.getString(R.string.download));
//
//        //Setting description of request
//        request.setDescription(mContext.getString(R.string.download_file) + " " + bookName);
//
//        //Set the local destination for the downloaded file to a path
//        //within the application's external files directory
////        request.setDestinationInExternalFilesDir(mContext,
////                Environment.DIRECTORY_DOWNLOADS, fileName);
////            Uri destinationUri = Uri.fromFile(fileDestionation);
//        request.setDestinationUri(destinationUri);
//        //Enqueue download and save into referenceId
//        downloadReference = MyDownloadManager.getDownloadManager(mContext).enqueue(request);
//        return downloadReference;
//    }



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
                                downloadSuccess(holder, position, mDownloadManager.getUriFileDownload(downloadId));
                                myTimer.cancel();
                                break;
                        }
                    }
                });

            }

        }, 0, 10);
    }

//    private void DownloadStatus(final Cursor cursor, final DataObjectHolder holder) {
//        //column for download  status
//        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
//        int status = cursor.getInt(columnIndex);
//        //column for reason code if the download failed or paused
//        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
//        int reason = cursor.getInt(columnReason);
//        //get the download filename
//        int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
//        String filename = cursor.getString(filenameIndex);
//
//        String statusText = "";
//        String reasonText = "";
//
//        switch (status) {
//            case DownloadManager.STATUS_FAILED:
//                statusText = "STATUS_FAILED";
//                switch (reason) {
//                    case DownloadManager.ERROR_CANNOT_RESUME:
//                        reasonText = "ERROR_CANNOT_RESUME";
//                        break;
//                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
//                        reasonText = "ERROR_DEVICE_NOT_FOUND";
//                        break;
//                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
//                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
//                        break;
//                    case DownloadManager.ERROR_FILE_ERROR:
//                        reasonText = "ERROR_FILE_ERROR";
//                        break;
//                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
//                        reasonText = "ERROR_HTTP_DATA_ERROR";
//                        break;
//                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
//                        reasonText = "ERROR_INSUFFICIENT_SPACE";
//                        break;
//                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
//                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
//                        break;
//                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
//                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
//                        break;
//                    case DownloadManager.ERROR_UNKNOWN:
//                        reasonText = "ERROR_UNKNOWN";
//                        break;
//                }
//                break;
//            case DownloadManager.STATUS_PAUSED:
//                statusText = "STATUS_PAUSED";
//                switch (reason) {
//                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
//                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
//                        break;
//                    case DownloadManager.PAUSED_UNKNOWN:
//                        reasonText = "PAUSED_UNKNOWN";
//                        break;
//                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
//                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
//                        break;
//                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
//                        reasonText = "PAUSED_WAITING_TO_RETRY";
//                        break;
//                }
//                break;
//            case DownloadManager.STATUS_PENDING:
//                statusText = "STATUS_PENDING";
//                break;
//            case DownloadManager.STATUS_RUNNING:
//                statusText = "STATUS_RUNNING";
//                break;
//            case DownloadManager.STATUS_SUCCESSFUL:
//                statusText = "STATUS_SUCCESSFUL";
//                reasonText = "Filename:\n" + filename;
//                break;
//
//        }
//
//
////        Toast toast = Toast.makeText(mContext,
////                "Image Download Status:" + "\n" + statusText + "\n" +
////                        reasonText,
////                Toast.LENGTH_LONG);
////        toast.setGravity(Gravity.TOP, 25, 400);
////        toast.show();
//
//        // Make a delay of 3 seconds so that next toast (Music Status) will not merge with this one.
//        int bytes_downloaded = cursor.getInt(cursor
//                .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
//        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
//        final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
//        holder.pbDownload.setProgress(dl_progress);
//        holder.tvProgress.setText(dl_progress + "%");
////        final Handler handler = new Handler();
////        handler.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////
////                Log.e("AAAA","AA"+dl_progress);
////            }
////        }, 1000);
//
//    }


}