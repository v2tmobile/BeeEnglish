package com.ahiho.apps.beeenglish.view;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.adapter.PaperGlobalAdapter;
import com.ahiho.apps.beeenglish.controller.RealmController;
import com.ahiho.apps.beeenglish.model.GlobalObject;
import com.ahiho.apps.beeenglish.model.realm_object.VocabularyObject;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.view.animation.VerticalFlipHorizontalTransformer;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class VocabularyActivity extends BaseActivity {
    private final String TAG="RESPONSE_Grammar";
    private TabLayout tabLayout;
    private PaperGlobalAdapter paperGlobalAdapter;
    private ViewPager vpHomePaper;
    private List<VocabularyObject> vocabularyObjects;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Intent intent = new Intent(vpHomePaper.getCurrentItem()+"_"+Identity.SEARCH_CHANGE_BROADCAST);
                    intent.putExtra(Identity.EXTRA_NEW_TEXT, newText);
                    sendBroadcast(intent);
                    return false;
                }
            });
        }
        return true;
    }

    private void init() {
        realm= RealmController.with(VocabularyActivity.this).getRealm();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        vpHomePaper = (ViewPager) findViewById(R.id.vpPaper);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        vpHomePaper.setPageTransformer(true, new VerticalFlipHorizontalTransformer());
        fillData();
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

    private void fillData() {
        try {
            vocabularyObjects = realm.where(VocabularyObject.class).findAll();
        }catch (Exception e){

        }
        Log.e("RESPONSE", vocabularyObjects.size()+"");
        if(vocabularyObjects !=null&& vocabularyObjects.size()>0) {
            List<GlobalObject> globalObjects= new ArrayList<>();
            for (VocabularyObject vocabularyObject: vocabularyObjects){
                globalObjects.add(new GlobalObject(vocabularyObject.getId(),vocabularyObject.getName()));
            }
            paperGlobalAdapter = new PaperGlobalAdapter(getSupportFragmentManager(), Identity.FUN_ID_VOCABULARY,globalObjects);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            vpHomePaper.setAdapter(paperGlobalAdapter);
            tabLayout.setupWithViewPager(vpHomePaper);
        }
    }

}
