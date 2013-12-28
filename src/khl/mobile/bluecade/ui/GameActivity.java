package khl.mobile.bluecade.ui;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.model.GameHandler;
import khl.mobile.bluecade.model.bluetooth.BluetoothHandler;
import khl.mobile.bluecade.ui.games.PictionaryMainActivity;
import khl.mobile.bluecade.ui.games.RPSMainActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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

public class GameActivity extends Activity {
	
	private TextView titel, connected;
	private GameHandler handler;
	private ImageView launch;
	private Integer gameid;
	private String title;
	private Switch btonoff;
	private ImageButton connect, startgame;
	private BluetoothAdapter btadapter;
	private String connectedto = "";
	
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
		if (BluetoothHandler.getInstance().getDevice() != null)
		   connectedto = BluetoothHandler.getInstance().getDevice().getName();
		   
		launch.setImageResource(handler.getGamesInfo().get(gameid).getLaunchImageId());
		titel = (TextView) findViewById(R.id.textView1);
		titel.setText(title);
		connected = (TextView) findViewById(R.id.textView2);
		connected.setText(Html.fromHtml(connected.getText() + " " + connectedto));
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
			i.putExtra("id",gameid);
			startActivity(i);
			}
			else {
			Toast.makeText(getApplicationContext(),
				"Please make sure your bluetooth connection is turned on",
				Toast.LENGTH_LONG).show();
			}
		}
		});
		startgame = (ImageButton) findViewById(R.id.launchTemplate);
		startgame.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				if (btadapter.isEnabled()){
					if (BluetoothHandler.getInstance().getDevice() != null){
					if (gameid == 0){
						Intent i = new Intent(GameActivity.this,RPSMainActivity.class);
						i.putExtra("id",gameid);
						startActivity(i);
					}
					else if (gameid == 1){
						Intent i = new Intent(GameActivity.this,PictionaryMainActivity.class);
						i.putExtra("id",gameid);
						startActivity(i);
					}
					}
					else {
						Toast.makeText(getApplicationContext(),
								"Please make sure you have an opponent connected",
								Toast.LENGTH_LONG).show();
					}
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
