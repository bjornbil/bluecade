package khl.mobile.bluecade.controller;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentPager extends FragmentPagerAdapter {

	private List<Fragment> fragments;

	public FragmentPager(FragmentManager fm) {
		super(fm);
		fragments = new ArrayList<Fragment>();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}
	
	public void addItem(Fragment f){
		fragments.add(f);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}