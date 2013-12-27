package khl.mobile.bluecade.ui;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import khl.mobile.bluecade.R;
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
import android.util.Log;
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
    protected static final UUID YOUR_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
    private static int DISCOVERY_REQUEST = 1;
	public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private BluetoothAdapter mBtAdapter;
    private BluetoothSocket socket;
    private ArrayAdapter<String> devicesArrayAdapter;
    private Button scanButton,discoverButton;
    private TextView title;
    private ListView devices;
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
        discoverButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		Intent disc;
        		disc = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        		startActivityForResult(disc, DISCOVERY_REQUEST);
        	}
        });

        devicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        devices = (ListView) findViewById(R.id.new_devices);
        devices.setAdapter(devicesArrayAdapter);
        devices.setOnItemClickListener(mDeviceClickListener);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
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
    
    @SuppressWarnings("unused")
	private void ensureDiscoverable() {
        if (mBtAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int index, long arg3) {
            mBtAdapter.cancelDiscovery();    
            
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length()-17);
            String name = info.substring(0, info.length() - 18);
            System.out.println(name);
            System.out.println(name.length());
            BluetoothDevice device = getDevice(name,address);
            System.out.print(device.getName());
            AsyncTask<Integer, Void, Void> connectTask =
            		new AsyncTask<Integer, Void, Void>() {
            		@Override
            		protected Void doInBackground(Integer...params) {
            		try {
            		BluetoothDevice device = availabledevices.get(params[0]);
            		socket = device.createRfcommSocketToServiceRecord(YOUR_UUID);
            		socket.connect();
            		} catch (IOException e) {
            		Log.d("BLUETOOTH_CLIENT", e.getMessage());
            		}
            		return null;
            		}
            		@Override
            		protected void onPostExecute(Void result) {
            		/*	INDIEN CONNECTIE WERKT KOMT DE ONDERSTAANDE CODE HIER TE STAAN, VOORLOPIG WORDT DEZE ALTIJD UITGEVOERD 
            		 */
            		}
            		};
            		connectTask.execute(index);           		
            // ONDERSTAANDE CODE
            Intent intent = new Intent(ConnectActivity.this,GameActivity.class);
       	    intent.putExtra("id",gameid);
       	    intent.putExtra("name", name);
       	    startActivity(intent);
        }
    };
    
    private BluetoothDevice getDevice(String name,String address){
    	BluetoothDevice d = null;
    	for (BluetoothDevice b : availabledevices){
    		if (b.getName().equals(name) && b.getAddress().equals(address)){
    			d = b;
    		}
    	}
    	return d;
    }
    

    
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
                	devicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                scanButton.setVisibility(View.VISIBLE);
            
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                if (devicesArrayAdapter.getCount() == 0) {
                	Toast.makeText(getApplicationContext(),
            				"No devices found",
            				Toast.LENGTH_LONG).show();
                	title.setText("Available devices");
                	scanButton.setVisibility(View.VISIBLE);
                	findViewById(R.id.title_new_devices).setVisibility(View.GONE);
                }
            }
            
        }
        
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
    data) {
    if (requestCode == DISCOVERY_REQUEST) {
    boolean isDiscoverable = resultCode > 0;
    if (isDiscoverable) {
    String name = "bluecadeserver";
    try {
    final BluetoothServerSocket btserver =
    mBtAdapter.listenUsingRfcommWithServiceRecord(name, YOUR_UUID);
    AsyncTask<Integer, Void, BluetoothSocket> acceptThread =
    new AsyncTask<Integer, Void, BluetoothSocket>() {
    @Override
    protected BluetoothSocket doInBackground(Integer...params) {
    try {
    socket = btserver.accept(params[0]*1000);
    return socket;
    } catch (IOException e) {
    Log.d("BLUETOOTH", e.getMessage());
    }
    return null;
    }
    @Override
    protected void onPostExecute(BluetoothSocket result) {
        
    }
    };
    acceptThread.execute(resultCode);
    } catch (IOException e) {
    Log.d("BLUETOOTH", e.getMessage());
    }
    }
    }
    }

}