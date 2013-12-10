package khl.mobile.bluecade.ui;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.controller.FragmentPager;
import khl.mobile.bluecade.model.GameHandler;
import khl.mobile.bluecade.model.GameInfo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends FragmentActivity {

	// TODO: make initialiseActivity (or alternative) that creates the game and
	// bluetooth handlers so it can pass them to the next Activity
	
	GameHandler gameHandler;
	ViewPager pager;
	ImageView previousButton;
	ImageView nextButton;

	ImageButton infobutton;
	ImageButton gamebutton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		gameHandler = new GameHandler();
		
		setContentView(R.layout.activity_main);
		pager = (ViewPager) findViewById(R.id.pager);
		previousButton = (ImageView) findViewById(R.id.previousButton);
		nextButton = (ImageView) findViewById(R.id.nextButton);
		
		nextButton = (ImageView) findViewById(R.id.nextButton);		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override public void onPageScrollStateChanged(int arg0) {}
			@Override public void onPageScrolled(int arg0, float arg1, int arg2) {updateButtonVisibility();}
			@Override public void onPageSelected(int arg0) {}});
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentPager pagerAdapter = new FragmentPager(fm);
		
		for(GameInfo i : gameHandler.getGamesInfo()){
			FragmentTemplate f = new FragmentTemplate();
			f.setTitle(i.getTitle());
			f.setId(i.getId());
			f.setImage(i.getImageResourceId());
			f.setContext(MainActivity.this);
			f.setInfoClass(InfoActivity.class);
			f.setGameClass(GameActivity.class);
			pagerAdapter.addItem(f);
		}		
		pager.setAdapter(pagerAdapter);
		pager.setCurrentItem(0);
		checkCurrentItem();
	}
	
	public void checkCurrentItem(){
		Bundle extras = getIntent().getExtras();
		  if (extras != null) {
		   String id= extras.getString("page");
		   if (id != null) {
		       pager.setCurrentItem(Integer.parseInt(id));
		   }
		  }
	}
	public void onPreviousButtonClicked(View v){
		pager.setCurrentItem(pager.getCurrentItem()-1);
		updateButtonVisibility();
	}	
	
	public void onNextButtonClicked(View v){
		pager.setCurrentItem(pager.getCurrentItem()+1);
		updateButtonVisibility();
	}
	
	public void onConfirmButtonClicked(View v){
		
	}
	
	private void updateButtonVisibility(){
		if(pager.getCurrentItem() >= pager.getAdapter().getCount()-1){
			nextButton.setVisibility(View.INVISIBLE);
		}else{
			nextButton.setVisibility(View.VISIBLE);
		}
		if(pager.getCurrentItem() == 0){
			previousButton.setVisibility(View.INVISIBLE);
		}else{
			previousButton.setVisibility(View.VISIBLE);
		}
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
