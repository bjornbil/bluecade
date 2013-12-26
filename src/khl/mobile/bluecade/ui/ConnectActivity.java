package khl.mobile.bluecade.ui;

import java.util.ArrayList;

import khl.mobile.bluecade.R;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ConnectActivity extends Activity {
	private ListView devicelist;
	private ArrayList<BluetoothDevice> foundDevices;
	private Button searchButton;
	private ArrayAdapter<BluetoothDevice> aa;
	private BluetoothAdapter bluetooth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		foundDevices = new ArrayList<BluetoothDevice>();
		devicelist = (ListView) findViewById(R.id.listView1);
		searchButton = (Button) findViewById(R.id.button1);
		searchButton.setOnClickListener(new OnClickListener(){
			public void onClick(View e){
				searchDevices();
			}
		});
		setupListView();	
	}
	
	private void setupListView() {
		aa = new ArrayAdapter<BluetoothDevice>(this,
		android.R.layout.simple_list_item_1,foundDevices);
		devicelist.setAdapter(aa);
	}
	
	private void searchDevices(){
		BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent intent) {
				BluetoothDevice remoteDevice;
				remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (bluetooth.getBondedDevices().contains(remoteDevice)) {
				foundDevices.add(remoteDevice);
				aa.notifyDataSetChanged();
				}
			}
			};
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.blue_cade, menu);
		return true;
	}
}
