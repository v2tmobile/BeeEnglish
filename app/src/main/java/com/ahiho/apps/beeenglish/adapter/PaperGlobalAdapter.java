package com.ahiho.apps.beeenglish.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ahiho.apps.beeenglish.model.GlobalObject;
import com.ahiho.apps.beeenglish.view.sample.GlobalFragment;

import java.util.List;

/**
 * Created by Thep on 1/28/2016.
 */
public class PaperGlobalAdapter extends FragmentStatePagerAdapter {
    private List<GlobalObject> globalObjects;
    private int funcId;

    public PaperGlobalAdapter(FragmentManager fm, int funcId,List<GlobalObject> globalObjects) {
        super(fm);
        this.globalObjects =globalObjects;
        this.funcId=funcId;
    }

    @Override
    public Fragment getItem(int position) {
        return GlobalFragment.newInstance(position,funcId,globalObjects.get(position).getId());
    }

    @Override
    public int getCount() {
        return globalObjects.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return globalObjects.get(position).getName();
    }
}
