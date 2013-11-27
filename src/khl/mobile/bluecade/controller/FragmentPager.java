package khl.mobile.bluecade.controller;

import khl.mobile.bluecade.ui.MainPIC;
import khl.mobile.bluecade.ui.MainRPS;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

  public class FragmentPager extends FragmentPagerAdapter {

final int PAGE_COUNT = 2;
public FragmentPager(FragmentManager fm) {
    super(fm);
    // TODO Auto-generated constructor stub
}

@Override
public Fragment getItem(int position) {
	switch (position) {
    case 0:
        return new MainRPS();
    case 1:
        // Calling a Fragment without sending arguments
        return new MainPIC();
    default:
        return null;
    }

}

@Override
public int getCount() {
    // TODO Auto-generated method stub
    return PAGE_COUNT;
}

}