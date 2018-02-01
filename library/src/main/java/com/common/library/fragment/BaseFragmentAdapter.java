package com.common.library.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuruibin on 2018/2/1.
 */
public class BaseFragmentAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private List<BaseFragment> fragmentList;
    private BaseFragment currentFragment;


    public BaseFragmentAdapter(FragmentManager manager, List<BaseFragment> fragmentList) {
        super(manager);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentFragment = (BaseFragment) object;
        super.setPrimaryItem(container, position, object);
    }

    public List<BaseFragment> getListFragments() {
        return fragmentList;
    }

    public void setListFragments(ArrayList<BaseFragment> listFragments) {
        this.fragmentList = listFragments;
    }

    public void addFragments(BaseFragment fragment) {
        fragmentList.add(fragment);
    }

    public void addFragments(int position, BaseFragment fragment) {
        fragmentList.add(position, fragment);
    }

    public void delAllFragments() {
        for (int i = fragmentList.size(); i > 0; --i) {
            destroyItem(null, i, fragmentList.get(i - 1));
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).getFragmentTitle();
    }

    public BaseFragment getCurrentFragment() {
        return currentFragment;
    }

}
