package khl.mobile.bluecade.ui;

import khl.mobile.bluecade.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class PictionaryMainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic_main);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.blue_cade, menu);
		return true;
	}
}