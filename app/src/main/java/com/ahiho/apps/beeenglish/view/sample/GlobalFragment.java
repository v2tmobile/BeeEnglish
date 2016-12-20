package com.ahiho.apps.beeenglish.view.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.adapter.RecyclerCategoryAdapter;
import com.ahiho.apps.beeenglish.adapter.RecyclerSampleSubListAdapter;
import com.ahiho.apps.beeenglish.model.FunctionObject;
import com.ahiho.apps.beeenglish.model.realm_object.GrammarObject;
import com.ahiho.apps.beeenglish.model.realm_object.SampleObject;
import com.ahiho.apps.beeenglish.model.realm_object.SubDetailObject;
import com.ahiho.apps.beeenglish.model.realm_object.SubObject;
import com.ahiho.apps.beeenglish.model.realm_object.VocabularyObject;
import com.ahiho.apps.beeenglish.my_interface.OnRecyclerViewItemClickListener;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.util.UtilString;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;


/**
 * Created by Thep on 1/28/2016.
 */
public class GlobalFragment extends Fragment {

    public static final GlobalFragment newInstance(int position,int idFunc, int idObject) {
        GlobalFragment borrowFragment = new GlobalFragment();
        Bundle args = new Bundle();
        args.putInt(Identity.EXTRA_FUN_ID, idFunc);
        args.putInt(Identity.EXTRA_GLOBAL_OBJECT_ID, idObject);
        args.putInt(Identity.EXTRA_POSITION, position);
        borrowFragment.setArguments(args);
        return borrowFragment;
    }


    private RecyclerView rvSampleSub;
    private Context mContext;
    private int type;
    private TextView tvNoData;
    private ProgressBar pbLoading;
    private RecyclerSampleSubListAdapter subListAdapter;
    private Realm realm;
    private List<SubObject> subObjects;

    private BroadcastReceiver broadCastReload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(subObjects!=null&&subObjects.size()>0) {
                String newText = intent.getStringExtra(Identity.EXTRA_NEW_TEXT);
                List<SubObject> listSearch = new ArrayList<>();
                if (newText.isEmpty()) {
                    listSearch = subObjects;
                } else {
                    for (SubObject object : subObjects) {
                        if (UtilString.compareStringSearch(object.getName(), newText)) {
                            listSearch.add(object);
                        }
                    }
                }
                rvSampleSub.setAdapter(new RecyclerSampleSubListAdapter(listSearch));
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sample, container, false);
        tvNoData = (TextView) rootView.findViewById(R.id.tvNoData);
        rvSampleSub = (RecyclerView) rootView.findViewById(R.id.rvSampleSub);

//        rvPayment.setHasFixedSize(true);
        rvSampleSub.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        realm = Realm.getDefaultInstance();
        int idFun = getArguments().getInt(Identity.EXTRA_FUN_ID);
        int idObject = getArguments().getInt(Identity.EXTRA_GLOBAL_OBJECT_ID);
        int position = getArguments().getInt(Identity.EXTRA_POSITION);

        subObjects = new RealmList<>();
        try {
            switch (idFun) {
                case Identity.FUN_ID_SAMPLE:
                    SampleObject sampleObject = realm.where(SampleObject.class).equalTo("id", idObject).findFirst();
                    subObjects = sampleObject.getData();
                    break;
                case Identity.FUN_ID_GRAMMAR:
                    GrammarObject grammarObject = realm.where(GrammarObject.class).equalTo("id", idObject).findFirst();
                    subObjects = grammarObject.getData();
                    break;
                case Identity.FUN_ID_VOCABULARY:
                    VocabularyObject vocabularyObject = realm.where(VocabularyObject.class).equalTo("id", idObject).findFirst();
                    subObjects = vocabularyObject.getData();
                    break;
            }
        } catch (Exception e) {

        }
        if (subObjects.size() > 0) {
            setupList();
        }
        mContext = rootView.getContext();
        mContext.registerReceiver(broadCastReload, new IntentFilter(position+"_"+Identity.SEARCH_CHANGE_BROADCAST));

        return rootView;
    }

    private void setupList() {
        subListAdapter = new RecyclerSampleSubListAdapter(subObjects);
        rvSampleSub.setAdapter(subListAdapter);
    }

    @Override
    public void onDestroy() {
        mContext.unregisterReceiver(broadCastReload);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


}
