package khl.mobile.bluecade.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.model.bluetooth.AcceptThread;
import khl.mobile.bluecade.model.bluetooth.BluetoothHandler;
import khl.mobile.bluecade.model.bluetooth.ConnectThread;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// Example used from http://luugiathuy.com/2011/02/android-java-bluetooth/

public class ConnectActivity extends Activity {
	protected static final UUID YOUR_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static int DISCOVERY_REQUEST = 1;
	public static String EXTRA_DEVICE_ADDRESS = "device_address";
	private BluetoothAdapter mBtAdapter;
	private BluetoothSocket socket;
	private BluetoothDevice mDevice;
	private ArrayAdapter<String> devicesArrayAdapter, pairedDevicesArrayAdapter;
	private Button scanButton, discoverButton;
	private TextView title;
	private ListView paireddevices,devices;
	private int gameid;
	private ArrayList<BluetoothDevice> availabledevices = new ArrayList<BluetoothDevice>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		gameid = bundle.getInt("id");
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_connect);
		setResult(Activity.RESULT_CANCELED);
		title = (TextView) findViewById(R.id.title_new_devices);
		scanButton = (Button) findViewById(R.id.button_scan);
		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doDiscovery();
				v.setVisibility(View.GONE);
			}
		});
		discoverButton = (Button) findViewById(R.id.button_disc);
		discoverButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent disc;
				disc = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				startActivityForResult(disc, DISCOVERY_REQUEST);
			}
		});
		


		devicesArrayAdapter = new ArrayAdapter<String>(this,R.layout.device_name);
		pairedDevicesArrayAdapter = new ArrayAdapter<String>(this,R.layout.device_name);

		devices = (ListView) findViewById(R.id.new_devices);
		devices.setAdapter(devicesArrayAdapter);
		devices.setOnItemClickListener(mDeviceClickListener);
		
		paireddevices = (ListView) findViewById(R.id.paired_devices);
		paireddevices.setAdapter(pairedDevicesArrayAdapter);
		paireddevices.setOnItemClickListener(mDeviceClickListener);
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);
		
		registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
		 // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        toastMaster(pairedDevices.size() + " paired devices");
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            pairedDevicesArrayAdapter.add(noDevices);
        }

		
	}
	
	public void toastMaster(String textToDisplay){
		 Toast myMessage = Toast.makeText(getApplicationContext(), 
		 textToDisplay,
		 Toast.LENGTH_LONG);
		 myMessage.setGravity(Gravity.CENTER, 0, 0);
		 myMessage.show();
		}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}

		this.unregisterReceiver(mReceiver);
	}

	/**
	 * Start device discover with the BluetoothAdapter
	 */
	private void doDiscovery() {
		title.setText("Scanning...");
		findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		mBtAdapter.startDiscovery();
	}

	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int index, long arg3) {
			mBtAdapter.cancelDiscovery();
			
			// String info = ((TextView) v).getText().toString();
			// String address = info.substring(info.length() - 17);
			// final String name = info.substring(0, info.length() - 18);

			AsyncTask<Integer, Void, Void> connectTask = new AsyncTask<Integer, Void, Void>() {
				@Override
				protected Void doInBackground(Integer... params) {
						BluetoothDevice device = availabledevices
								.get(params[0]);
						mDevice = device;
						ConnectThread t = new ConnectThread(device);
						try {
							new Thread(t).start();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						return null;
				}
				
				@Override
				protected void onPostExecute(Void result) {
					Intent intent = new Intent(ConnectActivity.this, GameActivity.class);
					intent.putExtra("id", gameid);
					startActivity(intent);
				}
			};
			title.setText("Connecting...");
			findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
			connectTask.execute(index);
		}
	};

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {

				title.setText("Available devices");
				findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getBondState() != BluetoothDevice.BOND_BONDED && !availabledevices.contains(device)) {
					availabledevices.add(device);
					devicesArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
				}
				scanButton.setVisibility(View.VISIBLE);

			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				if (devicesArrayAdapter.getCount() == 0) {
					toastMaster("No devices found");
					title.setText("Available devices");
					scanButton.setVisibility(View.VISIBLE);
					findViewById(R.id.title_new_devices).setVisibility(
							View.GONE);
				}
			}
			else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
				toastMaster("Connected!");
			}

		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	if (requestCode == DISCOVERY_REQUEST) {
			boolean isDiscoverable = resultCode > 0;
			if (isDiscoverable) {
				String name = "bluecadeserver";
				AsyncTask<Integer, Void, BluetoothSocket> acceptThread = new AsyncTask<Integer, Void, BluetoothSocket>() {
					@Override
					protected BluetoothSocket doInBackground(
							Integer... params) {
						try {
							AcceptThread t = new AcceptThread(); 
							socket = t.getServerSocket().accept(params[0] * 1000);
							return socket;
						} catch (IOException e) {
							Log.d("BLUETOOTH", e.getMessage());
						}
						return null;
					}

					@Override
					protected void onPostExecute(BluetoothSocket result) {
						BluetoothHandler.getInstance().setSocket(result);
						Intent i = new Intent(ConnectActivity.this,GameActivity.class);
						i.putExtra("id",gameid);
						startActivity(i);
					}
				};
				acceptThread.execute(resultCode);
			}
		}
	}
	

}