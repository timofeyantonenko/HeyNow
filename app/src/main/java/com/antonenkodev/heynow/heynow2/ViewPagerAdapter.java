package com.antonenkodev.heynow.heynow2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by root on 09.05.15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter{
    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            InstagramFragment tabInsta = new InstagramFragment();
            return tabInsta;
        }
        else{
            VkFragment tabVk = new VkFragment();
            return tabVk;
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }


}
