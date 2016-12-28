package com.ahiho.apps.beeenglish.view.communication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.adapter.RecyclerBooksAdapter;
import com.ahiho.apps.beeenglish.adapter.RecyclerCommunicationStoreAdapter;
import com.ahiho.apps.beeenglish.controller.RealmController;
import com.ahiho.apps.beeenglish.model.BookObject;
import com.ahiho.apps.beeenglish.model.CommunicationQueryObject;
import com.ahiho.apps.beeenglish.model.ResponseData;
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationObject;
import com.ahiho.apps.beeenglish.util.MyConnection;
import com.ahiho.apps.beeenglish.view.BaseActivity;
import com.ahiho.apps.beeenglish.view.BooksActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class CommunicationStoreActivity extends BaseActivity {

    private ProgressDialog mDialog;
    private RelativeLayout rlNotFoundData;
    private RecyclerView rvCommunications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_store);
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

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvCommunications = (RecyclerView) findViewById(R.id.rvCommunications);
        rlNotFoundData = (RelativeLayout) findViewById(R.id.rlNotFoundData);
        rvCommunications.setHasFixedSize(true);
        rvCommunications.setLayoutManager(new LinearLayoutManager(CommunicationStoreActivity.this, LinearLayoutManager.VERTICAL, false));
        loadData();
    }

    private void loadData() {
        new GetCommunications().execute();

    }


    class GetCommunications extends AsyncTask<Void, Void, ResponseData> {

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(CommunicationStoreActivity.this, null,
                    getString(R.string.loading), true);
        }

        @Override
        protected ResponseData doInBackground(Void... params) {
            if (MyConnection.isOnline(CommunicationStoreActivity.this)) {
                return MyConnection.getInstanceMyConnection(CommunicationStoreActivity.this).getCommunications();
            } else {
                return null;
            }

        }

        @Override
        protected void onPostExecute(ResponseData responseData) {
            mDialog.dismiss();
            List<CommunicationQueryObject> communicationQueryObjects = new ArrayList<>();
            if (responseData != null) {
                if (responseData.isResponseState()) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseData.getResponseData());
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                CommunicationQueryObject object = new CommunicationQueryObject(jsonArray.getJSONObject(i));
                                communicationQueryObjects.add(object);
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
            if(communicationQueryObjects.size()>0) {
                rvCommunications.setVisibility(View.VISIBLE);
                rvCommunications.setAdapter(new RecyclerCommunicationStoreAdapter(communicationQueryObjects,CommunicationStoreActivity.this));
            }else{
                rlNotFoundData.setVisibility(View.VISIBLE);
                rvCommunications.setVisibility(View.GONE);
            }
            super.onPostExecute(responseData);
        }
    }
}
