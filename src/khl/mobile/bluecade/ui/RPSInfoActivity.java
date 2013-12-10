package khl.mobile.bluecade.ui;

import khl.mobile.bluecade.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class RPSInfoActivity extends Activity implements OnClickListener {
	
	ImageButton goback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rps_info);
		goback = (ImageButton) findViewById(R.id.imageButton1);
		goback.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.blue_cade, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		i.setClass(RPSInfoActivity.this,MainActivity.class);
		i.putExtra("ID", "0");
		startActivity(i);
	}
}
