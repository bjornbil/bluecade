package khl.mobile.bluecade.model.bluetooth;

import java.io.IOException;
import java.util.UUID;

import khl.mobile.bluecade.ui.ConnectActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

public class AcceptThread implements Runnable {
    private BluetoothServerSocket mmServerSocket;
    private BluetoothSocket btConnectedSocket;
    private BluetoothAdapter mBluetoothAdapter;
    private static final String NAME = "Server";
	private static final String TAG = null;
	private static final String ACTION = "Bluetooth socket is connected";
	private ConnectActivity parent;
	private boolean connected;
	private int gameid;
 
    public AcceptThread(ConnectActivity parent) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
    	gameid = parent.getGameId();
    	this.parent = parent;
    	connected = false;
   
    }
    
    public BluetoothServerSocket getSocket(){
    	return mmServerSocket;
    }
    
    @Override
    public void run() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        try {
            Log.i(TAG, "getting socket from adapter");
            mmServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, ConnectActivity.YOUR_UUID);
            listen();

        }
        catch (IOException ex) {
            Log.e(TAG, "error while initializing");
        }
    }

    private void listen() {
        Log.i(TAG, "listening");
        btConnectedSocket = null;
        while (!connected) {
            try {
                btConnectedSocket = mmServerSocket.accept();
            }
            catch (IOException ex) {
                Log.e(TAG,"connection failed");
                connectionFailed();
            }

            if (btConnectedSocket != null) {
                broadcast();
                closeServerSocket();
            }
            else {
                Log.i(TAG,  "socket is null");
                connectionFailed();
            }
        }

    }

    private void broadcast() {
        try {
            Log.i(TAG, "connected? "+btConnectedSocket.isConnected());
            Intent intent = new Intent();
            intent.setAction(ACTION);
            intent.putExtra("state", btConnectedSocket.isConnected());
            intent.putExtra("id",gameid);
            parent.sendBroadcast(intent); 
            connected = true;
        }
        catch (RuntimeException runTimeEx) {

        }

        closeServerSocket();
    }


    private void connectionFailed () {

    }

    public void closeServerSocket() {
        try {
            mmServerSocket.close();
        }
        catch (IOException ex) {
            Log.e(TAG+":cancel", "error while closing server socket");
        }
    }
}