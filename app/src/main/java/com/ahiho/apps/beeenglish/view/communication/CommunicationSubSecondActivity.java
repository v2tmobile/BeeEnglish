package com.ahiho.apps.beeenglish.view.communication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.adapter.RecyclerCommunicationSubListAdapter;
import com.ahiho.apps.beeenglish.adapter.RecyclerCommunicationSubSecondListAdapter;
import com.ahiho.apps.beeenglish.controller.RealmController;
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationObject;
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationSubObject;
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationSubSecondObject;
import com.ahiho.apps.beeenglish.util.Identity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class CommunicationSubSecondActivity extends AppCompatActivity {

    private RecyclerView rvData;
    private RelativeLayout rlNotFoundData;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
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
        Intent intent = getIntent();
        int id  = intent.getIntExtra(Identity.EXTRA_ID,-1);
        int subId  = intent.getIntExtra(Identity.EXTRA_ID_SUB,-1);
        String title  = intent.getStringExtra(Identity.EXTRA_TITLE);
        setTitle(title);

        realm= RealmController.with(this).getRealm();
        rvData= (RecyclerView) findViewById(R.id.rvData);
        rlNotFoundData= (RelativeLayout) findViewById(R.id.rlNotFoundData);
        rvData.setHasFixedSize(true);
        rvData.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        loadData(id,subId);
    }

    private void loadData(int id,int subId) {

        List<CommunicationSubSecondObject> objects = new ArrayList<>();
        try{
            objects = realm.where(CommunicationObject.class).equalTo("id",id).findFirst().getData()
                    .where().equalTo("id",subId).findFirst().getData();
        }catch (Exception e){

        }
        if(objects.size()>0){
            rvData.setAdapter(new RecyclerCommunicationSubSecondListAdapter(objects,id,subId));
        }else{
            rvData.setVisibility(View.GONE);
            rlNotFoundData.setVisibility(View.VISIBLE);
        }
    }
}

