package com.ahiho.apps.beeenglish.view.communication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.adapter.RecyclerCommunicationListAdapter;
import com.ahiho.apps.beeenglish.controller.RealmController;
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationObject;
import com.ahiho.apps.beeenglish.view.BaseActivity;
import com.ahiho.apps.beeenglish.view.BooksActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class CommunicationActivity extends BaseActivity {

    private RecyclerView rvData;
    private RelativeLayout rlNotFoundData;
    private Realm realm;
    private boolean isLoadFirst=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
        init();
    }

    @Override
    protected void onResume() {
        if(isLoadFirst){
            loadData();
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_communication, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_download_communication:
                startActivity(new Intent(CommunicationActivity.this,CommunicationStoreActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        realm= RealmController.with(this).getRealm();
        rvData= (RecyclerView) findViewById(R.id.rvData);
        rlNotFoundData= (RelativeLayout) findViewById(R.id.rlNotFoundData);
        rvData.setHasFixedSize(true);
        rvData.setLayoutManager(new LinearLayoutManager(CommunicationActivity.this, LinearLayoutManager.VERTICAL, false));
        loadData();
        isLoadFirst=true;
    }

    private void loadData() {
        List<CommunicationObject> communicationObjects = new ArrayList<>();
        try{
            communicationObjects = realm.allObjects(CommunicationObject.class);
        }catch (Exception e){

        }
        if(communicationObjects.size()>0){
            rvData.setAdapter(new RecyclerCommunicationListAdapter(communicationObjects));
        }else{
            rvData.setVisibility(View.GONE);
            rlNotFoundData.setVisibility(View.VISIBLE);
        }
    }

}
