package com.ahiho.apps.beeenglish.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.adapter.RecyclerCategoryAdapter;
import com.ahiho.apps.beeenglish.controller.RealmController;
import com.ahiho.apps.beeenglish.model.CategoryObject;
import com.ahiho.apps.beeenglish.model.DictionaryObject;
import com.ahiho.apps.beeenglish.model.FunctionObject;
import com.ahiho.apps.beeenglish.model.ResponseData;
import com.ahiho.apps.beeenglish.model.WordObject;
import com.ahiho.apps.beeenglish.model.WordWithTypeObject;
import com.ahiho.apps.beeenglish.util.CircleTransform;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.util.MyConnection;
import com.ahiho.apps.beeenglish.util.MyDownloadManager;
import com.ahiho.apps.beeenglish.util.UtilSharedPreferences;
import com.ahiho.apps.beeenglish.util.UtilString;
import com.ahiho.apps.beeenglish.view.dialog.FirstDownloadDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnQueryTextListener {

    private NavigationView mNavigationView;
    private RelativeLayout llHeader;
    private ImageView ivSignOut;
    private ImageView ivAvatar;
    private TextView tvDisplayName;
    private TextView tvEmail;
    private RecyclerView rvUsually, rvRecent;
    private List<FunctionObject> functionObjectList;
    private UtilSharedPreferences mUtilSharedPreferences;
    private Realm mRealm;
    private boolean isLoaded = false;
    private MyDownloadManager downloadManager;

    private List<DictionaryObject> dictionaryObjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        grantPermissionReadWriteFile();
    }


    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        downloadManager = new MyDownloadManager(HomeActivity.this);
        mRealm = RealmController.with(this).getRealm();
        mUtilSharedPreferences = UtilSharedPreferences.getInstanceSharedPreferences(HomeActivity.this);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        llHeader = (RelativeLayout) mNavigationView.getHeaderView(0).findViewById(R.id.llHeader);
        ivSignOut = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.ivSignOut);
        ivAvatar = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.ivAvatar);
        tvDisplayName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tvDisplayName);
        tvEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tvEmail);


        rvUsually = (RecyclerView) findViewById(R.id.rvUsually);
        rvRecent = (RecyclerView) findViewById(R.id.rvRecent);

        rvUsually.setHasFixedSize(true);
        rvUsually.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvUsually = (RecyclerView) findViewById(R.id.rvUsually);

        rvRecent.setHasFixedSize(true);
        rvRecent.setLayoutManager(new GridLayoutManager(HomeActivity.this, 3));
//        rvRecent.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
        loadDataRecent();
        loadDataUser();
        isLoaded = true;
        WordWithTypeObject wordObject=null;
        try {
            wordObject = mRealm.where(WordWithTypeObject.class).findFirst();
        }catch (Exception e){

        }
        if (wordObject == null) {
//            Toast.makeText(this, "aaaaaa", Toast.LENGTH_SHORT).show();
//            String contentPath ="https://www.google.com.tw/images/srpr/logo4w.png";
//            final String fileName = MyFile.getFileName(contentPath);
//            final File file = new File(Environment.getExternalStorageDirectory() + "/" + MyFile.APP_FOLDER + "/" + MyFile.DICTIONARY_FOLDER + "/" + fileName);
//            long currentId = downloadManager.downloadData(contentPath, "demo", Uri.fromFile(file));
//            mUtilSharedPreferences.setIdDictionaryDownload(String.valueOf(currentId), 100);
            new GetDictionary().execute();
        }
    }

    private void loadDataUser() {
        try {
            JSONObject jsonObject = new JSONObject(mUtilSharedPreferences.getUserData());

            tvDisplayName.setText(jsonObject.getString("email"));
            String uri = jsonObject.getString("avatar");
            if (uri != null && !uri.isEmpty() && !uri.equals("null")) {
                Picasso.with(HomeActivity.this).load(uri).transform(new CircleTransform()).error(R.drawable.default_avatar).into(ivAvatar);
            } else {
                Picasso.with(HomeActivity.this).load(R.drawable.default_avatar).transform(new CircleTransform()).into(ivAvatar);
            }
            ivSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUtilSharedPreferences.signOut();
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                    finish();
                }
            });
        } catch (JSONException e) {

        }
    }

    private void loadDataRecent() {
        functionObjectList = new ArrayList<>();
        functionObjectList.add(new FunctionObject(Identity.FUN_ID_DICTIONARY, R.drawable.ic_translate_white_48dp, getString(R.string.fun_dictionary)));
        functionObjectList.add(new FunctionObject(Identity.FUN_ID_SAMPLE, R.drawable.ic_format_size_white_48dp, getString(R.string.fun_sample)));
        functionObjectList.add(new FunctionObject(Identity.FUN_ID_GRAMMAR, R.drawable.ic_text_format_white_48dp, getString(R.string.fun_grammar)));
        functionObjectList.add(new FunctionObject(Identity.FUN_ID_VOCABULARY, R.drawable.ic_library_books_white_48dp, getString(R.string.fun_vocabulary)));
        functionObjectList.add(new FunctionObject(Identity.FUN_ID_SKILL, R.drawable.ic_speaker_notes_white_48dp, getString(R.string.fun_skill)));
        functionObjectList.add(new FunctionObject(Identity.FUN_ID_TEST, R.drawable.ic_spellcheck_white_48dp, getString(R.string.fun_test)));
        functionObjectList.add(new FunctionObject(Identity.FUN_ID_BOOK, R.drawable.ic_book_white_48dp, getString(R.string.fun_book)));
        functionObjectList.add(new FunctionObject(Identity.FUN_ID_COMMUNICATE, R.drawable.ic_swap_horiz_white_48dp, getString(R.string.fun_communicate)));
        rvRecent.setAdapter(new RecyclerCategoryAdapter(functionObjectList, true, mRealm));
        loadDataUsuallyList();
    }


    @Override
    protected void onResume() {
        if (isLoaded) {
            loadDataUsuallyList();
        }
        super.onResume();
    }

    private void loadDataUsuallyList() {
        mRealm.beginTransaction();
        List<CategoryObject> categoryObjects = mRealm.where(CategoryObject.class).findAllSorted("count", RealmResults.SORT_ORDER_DESCENDING);
        mRealm.commitTransaction();
        if (categoryObjects != null && categoryObjects.size() > 0) {
            List<FunctionObject> functionObjects = new ArrayList<>();
            int length = categoryObjects.size();
            if (length > 2) {
                length = 3;
            }
            for (int i = 0; i < length; i++) {
                int id = categoryObjects.get(i).getId();
                for (FunctionObject object : functionObjectList) {
                    if (object.getId() == id) {
                        functionObjects.add(object);
                        break;
                    }
                }
            }
            rvUsually.setAdapter(new RecyclerCategoryAdapter(functionObjects, false, mRealm));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_borrow:
                break;
            case R.id.nav_backup:
                break;
            case R.id.nav_restore:
                break;
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<FunctionObject> listSearch = new ArrayList<>();
        if (newText.isEmpty()) {
            listSearch = functionObjectList;
        } else {
            for (FunctionObject object : functionObjectList) {
                if (UtilString.compareStringSearch(object.getName(), newText)) {
                    listSearch.add(object);
                }
            }
        }
        rvRecent.setAdapter(new RecyclerCategoryAdapter(listSearch, true, mRealm));
        return false;
    }

    class GetDictionary extends AsyncTask<Void, Void, ResponseData> {

        @Override
        protected void onPreExecute() {
            showDialogLoading();
        }

        @Override
        protected ResponseData doInBackground(Void... params) {
            if (MyConnection.isOnline(HomeActivity.this)) {
                return MyConnection.getInstanceMyConnection(HomeActivity.this).getDictionary();
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
                            for (int i = 0; i < jsonArray.length(); i++) {
                                DictionaryObject dictionaryObject = new DictionaryObject(jsonArray.getJSONObject(i));
                                try {
                                    mRealm.beginTransaction();
                                    mRealm.copyToRealmOrUpdate(dictionaryObject);
                                    mRealm.commitTransaction();
                                } catch (Exception e) {

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
            mRealm.beginTransaction();
            dictionaryObjects = mRealm.allObjects(DictionaryObject.class);
            mRealm.commitTransaction();
            if (dictionaryObjects.size() > 0) {
                WordWithTypeObject wordObject=null;
                try {
                    wordObject = mRealm.where(WordWithTypeObject.class).findFirst();
                }catch (Exception e){

                }
                if (wordObject == null) {
                    Intent intent = new Intent(HomeActivity.this, FirstDownloadDialog.class);
                    startActivity(intent);
                }
            } else {

            }
            super.onPostExecute(responseData);
        }
    }

}
