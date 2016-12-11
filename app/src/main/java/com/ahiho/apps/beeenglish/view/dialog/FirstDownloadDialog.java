package com.ahiho.apps.beeenglish.view.dialog;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.controller.RealmController;
import com.ahiho.apps.beeenglish.model.DictionaryObject;
import com.ahiho.apps.beeenglish.model.WordObject;
import com.ahiho.apps.beeenglish.model.WordWithTypeObject;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.util.MyDownloadManager;
import com.ahiho.apps.beeenglish.util.MyFile;
import com.ahiho.apps.beeenglish.util.UtilSharedPreferences;
import com.ahiho.apps.beeenglish.view.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmList;

public class FirstDownloadDialog extends BaseActivity {

    private TextView tvFirstDownloadDescription, tvProgress, tvTotal;
    private LinearLayout llDownloadProgress, llErrorDownload;
    private ProgressBar pbProgress, pbTotal;
    private Button btOk, btCancel;
    private int maxDownload = 1;
    private int currentDownload = 0;
    private long currentDownloadId = -2;
    private long lastId = -1;
    private UtilSharedPreferences mUtilSharedPreferences;
    private Realm mRealm;
    private MyDownloadManager mDownloadManager;
    private final String TAG = "RESPONSE_FIRST";

    private BroadcastReceiver broadCastDownload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (downloadId == currentDownloadId) {
                if (mDownloadManager.statusDownloading(downloadId) == DownloadManager.STATUS_SUCCESSFUL) {
                    if (lastId != downloadId) {
                        lastId = downloadId;
                        currentDownload++;
                    }
                    if (currentDownload >= maxDownload) {
                        pbProgress.setProgress(100);
                        tvProgress.setText("100%");
                        new UnzipFile(downloadId).execute();
                        btOk.setVisibility(View.VISIBLE);
                        btOk.setText(getString(R.string.bt_complete));
                        btOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });
                    } else {
                        pbProgress.setProgress(0);
                        tvProgress.setText("0%");

                    }
                    pbTotal.setProgress(currentDownload);
                    tvTotal.setText(currentDownload + "/" + maxDownload);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_first_download);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(broadCastDownload, filter);
        init();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadCastDownload);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public void init() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.9);
        getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        mUtilSharedPreferences = UtilSharedPreferences.getInstanceSharedPreferences(FirstDownloadDialog.this);
        mDownloadManager = new MyDownloadManager(FirstDownloadDialog.this);
        mRealm = RealmController.with(FirstDownloadDialog.this).getRealm();

        final DictionaryObject dictionaryObject = mRealm.where(DictionaryObject.class).findFirst();
//        Intent intent = getIntent();
//        final DictionaryObject dictionaryObject = (DictionaryObject) intent.getSerializableExtra(Identity.EXTRA_DICTIONARY_OBJECT);

        tvFirstDownloadDescription = (TextView) findViewById(R.id.tvFirstDownloadDescription);
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        llDownloadProgress = (LinearLayout) findViewById(R.id.llDownloadProgress);
        llErrorDownload = (LinearLayout) findViewById(R.id.llErrorDownload);
        pbProgress = (ProgressBar) findViewById(R.id.pbProgress);
        pbTotal = (ProgressBar) findViewById(R.id.pbTotal);
        btOk = (Button) findViewById(R.id.btOk);
        btCancel = (Button) findViewById(R.id.btCancel);

        pbProgress.setMax(100);
        pbTotal.setMax(maxDownload);
        pbTotal.setProgress(currentDownload);


        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvFirstDownloadDescription.setVisibility(View.INVISIBLE);
                llDownloadProgress.setVisibility(View.VISIBLE);
                btOk.setVisibility(View.GONE);
                btCancel.setVisibility(View.GONE);
                String fileName = MyFile.getFileName(dictionaryObject.getContent());
                File file = new File(Environment.getExternalStorageDirectory() + "/" + MyFile.APP_FOLDER + "/" + MyFile.DICTIONARY_FOLDER + "/" + fileName);
                final Uri destinationUri = Uri.fromFile(file);
                currentDownloadId = mDownloadManager.downloadData(dictionaryObject.getContent(), dictionaryObject.getName(), destinationUri);
                mUtilSharedPreferences.setIdDictionaryDownload(String.valueOf(currentDownloadId), dictionaryObject.getId());
                showProgressDownloading(currentDownloadId);
//                new ReadJson("/storage/sdcard0/bee_english/dictionary/en_vi.zip",1).execute();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showProgressDownloading(final long downloadId) {

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dl_progress >= 0) {
                            pbProgress.setProgress(dl_progress);
                            tvProgress.setText(dl_progress + "%");

                        }
                        switch (status) {
                            case DownloadManager.STATUS_FAILED:
                                llErrorDownload.setVisibility(View.VISIBLE);
                                btOk.setVisibility(View.VISIBLE);
                                btOk.setText(getString(R.string.bt_ok));
                                btOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });
                                myTimer.cancel();
                                break;
                            case DownloadManager.STATUS_PAUSED:
                                break;
                            case DownloadManager.STATUS_PENDING:
                                break;
                            case DownloadManager.STATUS_RUNNING:
                                break;
                            case DownloadManager.STATUS_SUCCESSFUL:
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
        private long mId;

        public UnzipFile(long id) {
            mId = id;
            mUri = mDownloadManager.getStringUriFileDownload(id);
            showDialogLoading();

        }


        @Override
        protected Boolean doInBackground(Void... params) {
            String des = Environment.getExternalStorageDirectory() + "/" + MyFile.APP_FOLDER + "/" + MyFile.DICTIONARY_FOLDER + "/";
            return MyFile.unzipWithLib(mUri, des, "");


        }

        @Override
        protected void onPostExecute(Boolean result) {
            dismissDialog();
            if (result) {
                File file = new File(mUri);
                file.delete();
                new ReadJson(mUri, mUtilSharedPreferences.getIdDictionaryDownload(String.valueOf(mId))).execute();
            } else {
                Toast.makeText(FirstDownloadDialog.this, "false", Toast.LENGTH_SHORT).show();

            }

            super.onPostExecute(result);
        }
    }

    class ReadJson extends AsyncTask<Void, Long, Boolean> {
        private String mUri;
        private int mId;

        public ReadJson(String uri, int id) {
            mUri = uri.replace(".zip", ".json");
            mId = id;
        }

        @Override
        protected void onPreExecute() {
            showDialogLoading(R.string.updating);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean result = true;
//            String s = MyFile.readFileText(mUri);
            Gson gson = new Gson();
            JsonReader reader = null;
            Realm realm = null;
            try {
                realm = Realm.getDefaultInstance();
                reader = new JsonReader(new FileReader(mUri));
                List<WordObject> data = gson.fromJson(reader, Identity.WORD_TYPE);// contains the whole reviews list
                realm.beginTransaction();
                long idAutoIncrement;
                try {
                    idAutoIncrement = realm.where(WordWithTypeObject.class).maximumInt("id") + 1;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    idAutoIncrement = 0;
                }
                if (idAutoIncrement < 0)
                    idAutoIncrement = 0;
                realm.commitTransaction();
                final int id  = realm.where(DictionaryObject.class).findFirst().getId();

                realm.beginTransaction();
                for (WordObject wordObject : data) {
                    //add realm
                    try {
//                        Log.e(TAG, idAutoIncrement + "");
                        realm.copyToRealm(new WordWithTypeObject(idAutoIncrement, wordObject, id));
                    } catch (Exception e) {

                    }
                    idAutoIncrement++;
                }
                realm.commitTransaction();


            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "");

                return false;
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }

            return result;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            dismissDialog();
            if (result) {
                File file = new File(mUri);
                file.delete();
            } else {
                showSnackBar(R.string.err_download_file);
            }
            super.onPostExecute(result);
        }
    }

}

