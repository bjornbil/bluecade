package khl.mobile.bluecade.ui;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.R.layout;
import khl.mobile.bluecade.R.menu;
import khl.mobile.bluecade.controller.FragmentPager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		FragmentManager fm = getSupportFragmentManager();
		FragmentPager pagerAdapter = new FragmentPager(fm);
		// Here you would declare which page to visit on creation
		pager.setAdapter(pagerAdapter);
		pager.setCurrentItem(0);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.blue_cade, menu);
		return true;
	}
	
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Closing Activity")
	        .setMessage("Are you sure you want to close BlueCade?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            finish();    
	        }

	    })
	    .setNegativeButton("No", null)
	    .show();
	}
}
