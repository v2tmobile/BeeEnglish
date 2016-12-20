package com.ahiho.apps.beeenglish.view;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.adapter.ListViewDictionarySelectAdapter;
import com.ahiho.apps.beeenglish.adapter.RecyclerBooksAdapter;
import com.ahiho.apps.beeenglish.controller.RealmController;
import com.ahiho.apps.beeenglish.model.realm_object.DictionaryObject;
import com.ahiho.apps.beeenglish.model.ResponseData;
import com.ahiho.apps.beeenglish.model.realm_object.WordObject;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.util.MyConnection;
import com.ahiho.apps.beeenglish.util.MyDownloadManager;
import com.ahiho.apps.beeenglish.util.MyFile;
import com.ahiho.apps.beeenglish.util.UtilSharedPreferences;
import com.ahiho.apps.beeenglish.util.UtilString;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class DictionaryActivity extends BaseActivity {
    private AutoCompleteTextView etInput;
    private WebView etTranslate;
    private ImageButton btClearInput, btCopy;
    private Realm mRealm;
    private RelativeLayout rlNotFoundData;
    private CardView cvSelectDictionary;
    private TextView tvCurrentDictionary;
    private ListView lvDictionary;
    private UtilSharedPreferences mUtilSharedPreferences;
    private List<DictionaryObject> dictionaryObjects;
    private MyDownloadManager mDownloadManager;

    private int currentDictionary = -1;
    private final String TAG = "RESPONSE_DIC";

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            int status = mDownloadManager.statusDownloading(referenceId);
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                if(mUtilSharedPreferences.getIdDictionaryDownload(String.valueOf(referenceId))>0);
                    new UnzipFile(referenceId).execute();
            } else if (status == DownloadManager.STATUS_FAILED) {
                showDialogStatus(getString(R.string.err_title), getString(R.string.err_download_file_description), false);
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        init();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(downloadReceiver);

        super.onDestroy();
    }

    private void init() {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDownloadManager = new MyDownloadManager(DictionaryActivity.this);
        mRealm = RealmController.with(DictionaryActivity.this).getRealm();
        mUtilSharedPreferences = UtilSharedPreferences.getInstanceSharedPreferences(DictionaryActivity.this);
        cvSelectDictionary = (CardView) findViewById(R.id.cvSelectDictionary);
        tvCurrentDictionary = (TextView) findViewById(R.id.tvCurrentDictionary);
        etInput = (AutoCompleteTextView) findViewById(R.id.etInput);
        etTranslate = (WebView) findViewById(R.id.etTranslate);
        btClearInput = (ImageButton) findViewById(R.id.btClearInput);
        btCopy = (ImageButton) findViewById(R.id.btCopy);
        rlNotFoundData = (RelativeLayout) findViewById(R.id.rlNotFoundData);
        lvDictionary = (ListView) findViewById(R.id.lvDictionary);

        etTranslate.setBackgroundColor(Color.TRANSPARENT);
        etTranslate.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        setupETInput();

        btCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String translate = etTranslate.getText().toString();
//                if (!translate.isEmpty()) {
//                    final android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager)
//                            getSystemService(Context.CLIPBOARD_SERVICE);
//                    clipboardManager.setText(translate);
//                    showSnackBar(R.string.success_copied);
//                }
            }
        });
        new GetDictionary().execute();
    }

    private void loadDataTranslate() {

        DictionaryObject dictionaryObject=mRealm.where(DictionaryObject.class).findFirst();
        WordObject wordWithTypeObject = dictionaryObject.getWordObjects().first();
        if (wordWithTypeObject==null) {
//             DictionaryObject dictionaryObject = dictionaryObjects.get(0);
            showDialogDownload(dictionaryObject.getName(), dictionaryObject.getContent(), dictionaryObject.getId());
        }else{
//            int wordType= wordWithTypeObject.getType();
//            for(DictionaryObject object:dictionaryObjects){
//                if(object.getId()==wordType){
                    tvCurrentDictionary.setText(dictionaryObject.getName());
                    currentDictionary=dictionaryObject.getId();
//                    break;
//                }
//            }

        }
    }

    private void showDialogDownload(final String name, final String contentPath, final int dictionaryId) {
        final String fileName = MyFile.getFileName(contentPath);

        final File file = new File( MyFile.APP_FOLDER + "/" + MyFile.DOWNLOADS_FOLDER + "/" + fileName);

        final Dialog dialog = new Dialog(DictionaryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_download);


        TextView tvDictionaryName = (TextView) dialog.findViewById(R.id.tvDictionaryName);
        Button btOk = (Button) dialog.findViewById(R.id.btOk);
        Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
        tvDictionaryName.setText(name);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri destinationUri = Uri.fromFile(file);
                long currentId = mDownloadManager.downloadData(contentPath, name, destinationUri);
                mUtilSharedPreferences.setIdDictionaryDownload(String.valueOf(currentId), dictionaryId);
                dialog.dismiss();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setupETInput() {
        etInput.setSingleLine(true);
        etInput.setLines(4);
        etInput.setHorizontallyScrolling(false);
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                List<String> hintList = new ArrayList<>();
                List<WordObject> wordWithTypeObjectList=new ArrayList<WordObject>();
                mRealm.beginTransaction();

                try {
                    wordWithTypeObjectList = mRealm.where(DictionaryObject.class).equalTo("id", currentDictionary).findFirst().getWordObjects().where().beginsWith("word", input).findAll();
                }catch (Exception e){
                    wordWithTypeObjectList=new ArrayList<WordObject>();
                }
                mRealm.commitTransaction();

                for (int i = 0; i < 3; i++) {
                    try {
                        hintList.add(wordWithTypeObjectList.get(i).getWord());
                    } catch (Exception e) {

                    }
                }
                if (s.length() > 0) {
                    if (hintList.size() > 0) {
                        if (hintList.size() == 1) {
                            WordObject wordObject = wordWithTypeObjectList.get(0);
                            setTextTranslate(wordObject.getDescription());
                        } else {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(DictionaryActivity.this,
                                    android.R.layout.simple_dropdown_item_1line, hintList);
                            etInput.setAdapter(adapter);
                        }
                    }

                    mRealm.beginTransaction();
                    WordObject object = mRealm.where(DictionaryObject.class).equalTo("id", currentDictionary).findFirst().getWordObjects().where().equalTo("word", input).findFirst();
                    mRealm.commitTransaction();
                    String description = "";
                    try {
                        description = object.getDescription();
                    } catch (Exception e) {

                    }
                    if (object != null && !description.isEmpty()) {
                        setTextTranslate(description);
                    } else {
                        setTextTranslate(input);
                    }
                } else {
                    setTextTranslate("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text != null && !text.isEmpty()) {
                    btClearInput.setVisibility(View.VISIBLE);
                } else {
                    btClearInput.setVisibility(View.INVISIBLE);
                }
            }
        });
        btClearInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.setText("");
            }
        });
    }


//    private void showProgressDownloading(final long downloadId, final RecyclerBooksAdapter.DataObjectHolder holder, final int position) {
//
//        final Timer myTimer = new Timer();
//        myTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                DownloadManager.Query q = new DownloadManager.Query();
//                q.setFilterById(downloadId);
//                Cursor cursor = mDownloadManager.getDownloadManager().query(q);
//                cursor.moveToFirst();
//                int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
//                int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
//                final int dl_progress = (bytes_downloaded * 100 / bytes_total);
//                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
//                final int status = cursor.getInt(columnIndex);
//                cursor.close();
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        if (dl_progress >= 0) {
////                            holder.pbDownload.setProgress(dl_progress);
////                            holder.tvProgress.setText(dl_progress + "%");
////                        } else {
////                            holder.pbDownload.setProgress(0);
////                            holder.tvProgress.setText(getString(R.string.pending_download_file));
////                        }
//                        switch (status) {
//                            case DownloadManager.STATUS_FAILED:
////                                downloadFail(holder);
//                                myTimer.cancel();
//                                break;
//                            case DownloadManager.STATUS_PAUSED:
//                                break;
//                            case DownloadManager.STATUS_PENDING:
//                                break;
//                            case DownloadManager.STATUS_RUNNING:
//                                break;
//                            case DownloadManager.STATUS_SUCCESSFUL:
////                                downloadSuccess(holder, position, getDownloadManager().getUriForDownloadedFile(downloadId));
//                                myTimer.cancel();
//                                break;
//                        }
//                    }
//                });
//
//            }
//
//        }, 0, 10);
//    }


    class GetDictionary extends AsyncTask<Void, Void, ResponseData> {

        @Override
        protected void onPreExecute() {
            showDialogLoading();
        }

        @Override
        protected ResponseData doInBackground(Void... params) {
            if (MyConnection.isOnline(DictionaryActivity.this)) {
                return MyConnection.getInstanceMyConnection(DictionaryActivity.this).getDictionary();
            } else {
                return null;
            }

        }

        @Override
        protected void onPostExecute(ResponseData responseData) {
            dismissDialog();
            if (responseData != null) {
                if (responseData.isResponseState()) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseData.getResponseData());
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            mRealm.beginTransaction();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                DictionaryObject dictionaryObject = new DictionaryObject(jsonArray.getJSONObject(i));
                                try {
                                    mRealm.copyToRealm(dictionaryObject);
                                } catch (Exception e) {

                                }
                            }
                            mRealm.commitTransaction();

                        } else {
                            showSnackBar(R.string.err_json_exception);
                        }
                    } catch (JSONException e) {
                        showSnackBar(R.string.err_json_exception);
                    }
                } else {
                    showSnackBar(responseData.getResponseData());
                }
            }
            mRealm.beginTransaction();
            dictionaryObjects = mRealm.allObjects(DictionaryObject.class);
            mRealm.commitTransaction();
            if (dictionaryObjects.size() > 0) {
                loadDataTranslate();
                lvDictionary.setAdapter(new ListViewDictionarySelectAdapter(DictionaryActivity.this, dictionaryObjects));
                cvSelectDictionary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lvDictionary.getVisibility() == View.VISIBLE) {
                            lvDictionary.setVisibility(View.GONE);

                        } else {
                            lvDictionary.setVisibility(View.VISIBLE);
                        }
                    }
                });
                lvDictionary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DictionaryObject dictionaryObject = (DictionaryObject) parent.getAdapter().getItem(position);
                        WordObject wordObject = null;
                        try {
                            wordObject = dictionaryObject.getWordObjects().first();
                        } catch (Exception e) {

                        }
                        if (wordObject == null) {
                            showDialogDownload(dictionaryObject.getName(), dictionaryObject.getContent(), dictionaryObject.getId());
                            lvDictionary.setVisibility(View.GONE);
                        } else {
                            tvCurrentDictionary.setText(dictionaryObject.getName());
                            lvDictionary.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                rlNotFoundData.setVisibility(View.VISIBLE);

            }
            super.onPostExecute(responseData);
        }
    }


    class UnzipFile extends AsyncTask<Void, Void, Boolean> {
        private String mUri;
        private long mId;

        public UnzipFile(long id) {
            mId = id;
            mUri = mDownloadManager.getStringUriFileDownload(id);
            showDialogLoading(R.string.extracting);

        }


        @Override
        protected Boolean doInBackground(Void... params) {
            String des = MyFile.APP_FOLDER + "/" + MyFile.DOWNLOADS_FOLDER + "/";
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
                Toast.makeText(DictionaryActivity.this, "false", Toast.LENGTH_SHORT).show();

            }

            super.onPostExecute(result);
        }
    }

    private void setTextTranslate(String input) {
        String text = new UtilString().htmlText(input);
        etTranslate.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
    }


    class ReadJson extends AsyncTask<Void, Void, Boolean> {
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
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .registerTypeAdapter(new TypeToken<RealmList<WordObject>>() {}.getType(), new TypeAdapter<RealmList<WordObject>>() {

                        @Override
                        public void write(JsonWriter out, RealmList<WordObject> value) throws IOException {
                            // Ignore
                        }

                        @Override
                        public RealmList<WordObject> read(JsonReader in) throws IOException {
                            RealmList<WordObject> list = new RealmList<WordObject>();
                            in.beginArray();
                            while (in.hasNext()) {
                                list.add(new WordObject(in.nextString(),in.nextString()));
                            }
                            in.endArray();
                            return list;
                        }
                    })
                    .create();
            JsonReader reader = null;
            Realm realm = null;
            try {
                realm = Realm.getDefaultInstance();
                reader = new JsonReader(new FileReader(mUri));
                List<WordObject> data = gson.fromJson(reader, Identity.WORD_TYPE);// contains the whole reviews list

                DictionaryObject dictionaryObject  = realm.where(DictionaryObject.class).equalTo("id",mId).findFirst();

                realm.beginTransaction();
//                for ( wordObject : data) {
//                    //add realm
//                    try {
////                        Log.e(TAG, idAutoIncrement + "");
//                        realm.copyToRealm(new WordWithTypeObject(idAutoIncrement, wordObject, id));
//                    } catch (Exception e) {
//
//                    }
//                    idAutoIncrement++;
//                }
                dictionaryObject.getWordObjects().addAll(data);
                realm.copyToRealmOrUpdate(dictionaryObject);
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
            try {
                File file = new File(mUri);
                file.delete();
            } catch (Exception e) {
            }
            if (result) {
                lvDictionary.setAdapter(new ListViewDictionarySelectAdapter(DictionaryActivity.this, dictionaryObjects));
            } else {
                showSnackBar(R.string.err_download_file);
            }
            super.onPostExecute(result);
        }
    }

}
