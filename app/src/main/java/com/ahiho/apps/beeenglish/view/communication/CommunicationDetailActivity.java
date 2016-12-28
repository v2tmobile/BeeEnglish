package com.ahiho.apps.beeenglish.view.communication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.adapter.ListViewCommunicationDetailAdapter;
import com.ahiho.apps.beeenglish.adapter.RecyclerCommunicationSubSecondListAdapter;
import com.ahiho.apps.beeenglish.controller.RealmController;
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationObject;
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationSubSecondDetailObject;
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationSubSecondObject;
import com.ahiho.apps.beeenglish.util.Identity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class CommunicationDetailActivity extends AppCompatActivity {

    private ListView lvData;
    private RelativeLayout rlNotFoundData;
    private TextView tvDescription;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_detail);
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
        int subSecondId  = intent.getIntExtra(Identity.EXTRA_ID_SUB_SECOND,-1);
        String title  = intent.getStringExtra(Identity.EXTRA_TITLE);
        setTitle(title);

        realm= RealmController.with(this).getRealm();
        rlNotFoundData= (RelativeLayout) findViewById(R.id.rlNotFoundData);
        lvData= (ListView) findViewById(R.id.lvData);
        tvDescription= (TextView) findViewById(R.id.tvDescription);
        loadData(id,subId,subSecondId);
    }

    private void loadData(int id,int subId,int subSecondId) {

        List<CommunicationSubSecondDetailObject> objects = new ArrayList<>();
        try{
            objects = realm.where(CommunicationObject.class).equalTo("id",id).findFirst().getData()
                    .where().equalTo("id",subId).findFirst().getData()
                    .where().equalTo("id",subSecondId).findFirst().getData();
        }catch (Exception e){

        }
        if(objects.size()>0){
            lvData.setAdapter(new ListViewCommunicationDetailAdapter(CommunicationDetailActivity.this,objects));
            lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CommunicationSubSecondDetailObject detailObject = (CommunicationSubSecondDetailObject) parent.getAdapter().getItem(position);
                    tvDescription.setText(detailObject.getDescription());
                }
            });
        }else{
            lvData.setVisibility(View.GONE);
            rlNotFoundData.setVisibility(View.VISIBLE);
        }
    }
}
