package khl.mobile.bluecade.ui;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.model.GameHandler;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends Activity {
	
	private TextView titel;
	private GameHandler handler;
	private ImageView launch;
	private int gameid;
	private String title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_main);
		handler = new GameHandler();
		launch = (ImageView) findViewById(R.id.launchTemplate);
		Bundle extras = getIntent().getExtras();
		  if (extras != null) {
		   String id= extras.getString("id");
		   if (id != null) {
			   gameid = Integer.parseInt(id);
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
}
