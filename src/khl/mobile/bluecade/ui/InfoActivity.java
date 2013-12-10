package khl.mobile.bluecade.ui;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.model.GameHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class InfoActivity extends Activity implements OnClickListener{
	
	ImageButton goback;
	GameHandler handler;
	String info, title;
	TextView gametitle, gameinfo;
	Integer gameid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_info);
		handler = new GameHandler();
		Bundle bundle = getIntent().getExtras();
		  if (bundle != null) {
		   	   gameid = bundle.getInt("id");
			   info = handler.getGamesInfo().get(gameid).getInstructions();
			   title = handler.getGamesInfo().get(gameid).getTitle();
		   }
		goback = (ImageButton) findViewById(R.id.imageButton1);
		goback.setOnClickListener(this);
		gametitle = (TextView) findViewById(R.id.textView2);
		gameinfo = (TextView) findViewById(R.id.textView1);
		gametitle.setText(title);
		gameinfo.setText(info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.blue_cade, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		i.setClass(InfoActivity.this,MainActivity.class);
		i.putExtra("page", gameid);
        i.addCategory(Intent.CATEGORY_HOME); 
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		startActivity(i);
	}
	
	@Override
	public void onBackPressed() {
		goback.performClick();
	}


}
