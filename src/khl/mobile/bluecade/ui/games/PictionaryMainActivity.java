package khl.mobile.bluecade.ui.games;

import java.util.Observable;
import java.util.Observer;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.model.bluetooth.BluetoothHandler;
import khl.mobile.bluecade.ui.games.pictionary.DrawingPanel;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class PictionaryMainActivity extends Activity implements Observer{
	
	private DrawingPanel drawingPanel;
	private TextView status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic_main);
		BluetoothHandler.getInstance().addObserver(this);
		drawingPanel = (DrawingPanel) findViewById(R.id.drawingPanel);
		status = (TextView) findViewById(R.id.status);
		
		//TODO: 
		drawingPanel.setBehavior(DrawingPanel.BEHAVIOR_LOCAL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.blue_cade, menu);
		return true;
	}

	@Override
	public void update(Observable obs, Object arg) {
		// Warning: gets called outside of the UI thread, sync with the UI thread
		// arg is the Packet
		
	}
}
