package khl.mobile.bluecade.ui;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.model.GameHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends Activity {
	
	private TextView titel;
	private GameHandler handler;
	private ImageView launch;
	private Integer gameid;
	private String title;
	
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
