package com.ahiho.apps.beeenglish.view;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.adapter.RecyclerBooksAdapter;
import com.ahiho.apps.beeenglish.controller.RealmController;
import com.ahiho.apps.beeenglish.model.BookObject;
import com.ahiho.apps.beeenglish.model.ResponseData;
import com.ahiho.apps.beeenglish.my_interface.OnCallbackSnackBar;
import com.ahiho.apps.beeenglish.util.MyConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class BooksActivity extends BaseActivity {

    private ProgressDialog mDialog;
    private RelativeLayout rlNotFoundData;
    private RecyclerView rvBooks;
    private Realm realm;
    private List<BookObject> bookObjects;
    private boolean isValid=false;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(
                    "android.net.conn.CONNECTIVITY_CHANGE")) {
                if (bookObjects.size()<=0&&isValid) {
                    if (MyConnection.isOnline(BooksActivity.this)) {
                        loadData();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bookObjects=new ArrayList<>();
        rvBooks = (RecyclerView) findViewById(R.id.rvBooks);
        rlNotFoundData = (RelativeLayout) findViewById(R.id.rlNotFoundData);
        rvBooks.setHasFixedSize(true);
        rvBooks.setLayoutManager(new LinearLayoutManager(BooksActivity.this, LinearLayoutManager.VERTICAL, false));
        realm = RealmController.with(this).getRealm();
        loadData();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(this.mBatInfoReceiver,
                filter);
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(mBatInfoReceiver);
        super.onDestroy();
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

    private void loadData() {
        new GetBooks().execute();

    }


    class GetBooks extends AsyncTask<Void, Void, ResponseData> {

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(BooksActivity.this, null,
                    getString(R.string.loading), true);
        }

        @Override
        protected ResponseData doInBackground(Void... params) {
            if (MyConnection.isOnline(BooksActivity.this)) {
                return MyConnection.getInstanceMyConnection(BooksActivity.this).getBooks();
            } else {
                return null;
            }

        }

        @Override
        protected void onPostExecute(ResponseData responseData) {
            mDialog.dismiss();
            if (responseData != null) {
                if (responseData.isResponseState()) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseData.getResponseData());
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                BookObject book = new BookObject(jsonArray.getJSONObject(i));
                                try{
                                    realm.beginTransaction();
                                    realm.copyToRealmOrUpdate(book);
                                    realm.commitTransaction();
                                }catch (Exception e){

                                }
                            }
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
             bookObjects = realm.allObjects(BookObject.class);
            if(bookObjects.size()>0) {
                rvBooks.setVisibility(View.VISIBLE);
                rvBooks.setAdapter(new RecyclerBooksAdapter(bookObjects,BooksActivity.this));
            }else{
                rlNotFoundData.setVisibility(View.VISIBLE);
                rvBooks.setVisibility(View.GONE);
            }
            isValid=true;
            super.onPostExecute(responseData);
        }
    }


}
