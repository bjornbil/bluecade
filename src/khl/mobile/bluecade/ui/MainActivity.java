package khl.mobile.bluecade.ui;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.controller.FragmentPager;
import khl.mobile.bluecade.model.GameHandler;
import khl.mobile.bluecade.model.GameInfo;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
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
	
	private GameHandler gameHandler;
	private ViewPager pager;
	private ImageView previousButton;
	private ImageView nextButton;
	private ImageButton infobutton;
	private ImageButton startgamebutton;
	private int lastfragmentid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		gameHandler = new GameHandler();
		
		setContentView(R.layout.activity_main);
		pager = (ViewPager) findViewById(R.id.pager);
		previousButton = (ImageView) findViewById(R.id.previousButton);
		nextButton = (ImageView) findViewById(R.id.nextButton);		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override public void onPageScrollStateChanged(int arg0) {}
			@Override public void onPageScrolled(int arg0, float arg1, int arg2) {
				updateButtonVisibility();
				lastfragmentid = pager.getCurrentItem();
				}
			@Override public void onPageSelected(int arg0) {}});
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentPager pagerAdapter = new FragmentPager(fm);
		
		for(GameInfo i : gameHandler.getGamesInfo()){
			FragmentTemplate f = new FragmentTemplate();
			f.setTitle(i.getTitle());
			f.setId(i.getId());
			f.setImage(i.getImageResourceId());
			pagerAdapter.addItem(f);
		}		
		pager.setAdapter(pagerAdapter);
		checkCurrentItem();
		infobutton = (ImageButton) findViewById(R.id.imageButton1);
		infobutton.setOnClickListener(new View.OnClickListener(){
    		@Override
    		public void onClick(View arg0) {
    			Intent i=new Intent();
    			i.setClass(MainActivity.this,InfoActivity.class);
    	        i.putExtra("id",lastfragmentid);      
    			startActivity(i);
    		}
    	});
		startgamebutton = (ImageButton) findViewById(R.id.imageButton2);
		startgamebutton.setOnClickListener(new View.OnClickListener(){
    		@Override
    		public void onClick(View arg0) {
    			Intent i=new Intent();
    			i.setClass(MainActivity.this,GameActivity.class);
    	        i.putExtra("id",lastfragmentid);  
    			startActivity(i);
    		}
    	});
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();    
		if (!mBluetoothAdapter.isEnabled()) {
		        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		        startActivity(enableBtIntent);
		}
	}
	
	public void checkCurrentItem(){
		Bundle extras = getIntent().getExtras();
		  if (extras != null) {
		   Integer id = extras.getInt("page");
		   if (id != null) {
		       pager.setCurrentItem(id);
		       lastfragmentid = pager.getCurrentItem();
		   }
		  }
		  else {
			  pager.setCurrentItem(0);
		  }
	}
	public void onPreviousButtonClicked(View v){
		pager.setCurrentItem(pager.getCurrentItem()-1);
		updateButtonVisibility();
		lastfragmentid = pager.getCurrentItem();
	}	
	
	public void onNextButtonClicked(View v){
		pager.setCurrentItem(pager.getCurrentItem()+1);
		updateButtonVisibility();
		lastfragmentid = pager.getCurrentItem();
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
