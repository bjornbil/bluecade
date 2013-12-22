package khl.mobile.bluecade.ui;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.model.GameHandler;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class GameActivity extends Activity {
	
	private TextView titel;
	private GameHandler handler;
	private ImageView launch;
	private Integer gameid;
	private String title;
	private Switch btonoff;
	private ImageButton connect;
	private BluetoothAdapter btadapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_main);
		handler = new GameHandler();
		launch = (ImageView) findViewById(R.id.launchTemplate);
		Bundle bundle = getIntent().getExtras();
		  if (bundle != null) {
		  {
			   gameid = (Integer) bundle.get("id");
			   title = handler.getGamesInfo().get(gameid).getTitle();
		   }
		}
		launch.setImageResource(handler.getGamesInfo().get(gameid).getLaunchImageId());
		titel = (TextView) findViewById(R.id.textView1);
		titel.setText(title);
		btonoff = (Switch) findViewById(R.id.switch1);
		btonoff.setTextOff("Off");
		btonoff.setTextOn("On");
		btadapter = BluetoothAdapter.getDefaultAdapter();
		if (btadapter.isEnabled()){
			btonoff.toggle();
		}
		btonoff.setOnCheckedChangeListener(new OnCheckedChangeListener(){
   			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (btadapter.isEnabled()){
					btadapter.disable();
				}	
				else {
					btadapter.enable();
				}
   			}
			});
		connect = (ImageButton) findViewById(R.id.imageButton1);
		connect.setOnClickListener(new OnClickListener(){
		public void onClick(View view) {
			if (btadapter.isEnabled()){
			Intent i = new Intent(GameActivity.this,ConnectActivity.class);
			startActivity(i);
			}
			else {
			Toast.makeText(getApplicationContext(),
				"Please make sure your bluetooth connection is turned on",
				Toast.LENGTH_LONG).show();
			}
		}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.blue_cade, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent();
		i.setClass(GameActivity.this,MainActivity.class);
		i.putExtra("page", gameid);
        i.addCategory(Intent.CATEGORY_HOME); 
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		startActivity(i);
	}
}
