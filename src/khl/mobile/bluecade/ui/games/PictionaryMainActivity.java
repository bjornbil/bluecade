package khl.mobile.bluecade.ui.games;

import java.util.Observable;
import java.util.Observer;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.model.bluetooth.BluetoothHandler;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class PictionaryMainActivity extends Activity implements Observer{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic_main);
		BluetoothHandler.getInstance().addObserver(this);
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
