package khl.mobile.bluecade.model.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Observable;

import khl.mobile.bluecade.model.bluetooth.packages.Packet;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothHandler extends Observable{
	
	private static BluetoothHandler instance = null;
	
	private BluetoothSocket socket;
	private ListenThread thread;
	
    private BluetoothHandler() {
	    
    }
   
    public static BluetoothHandler getInstance() {
       if(instance == null) {
          instance = new BluetoothHandler();
       }
       return instance;
    }

	public void setSocket(BluetoothSocket socket){
		if(thread != null && thread.isAlive())
			thread.cancel();
		this.socket = socket;
		Log.v("Bluecade", "Socket connected: " + isConnected());
		try {
			thread = new ListenThread(socket.getInputStream());
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isConnected(){
		return socket != null && socket.isConnected();
	}
	
	public BluetoothDevice getDevice(){
		if(!isConnected())
			return null;
		return socket.getRemoteDevice();
	}
	
	public boolean sendPacket(Packet p) throws IOException{
		if(isConnected()){
			new ObjectOutputStream(socket.getOutputStream()).writeObject(p);
		}
		return false;
	}
	
	public void recievePacket(Packet p) {
		setChanged();
		notifyObservers(p);
	}

	public void connectionLost() {
		thread.cancel();
		thread = null;
	}
	 
	 class ListenThread extends Thread {
	     private ObjectInputStream inStream;
	     private boolean run;
	     
	     public ListenThread(InputStream in) throws StreamCorruptedException, IOException {
	         this.inStream = new ObjectInputStream(in);
	     }

	     public void cancel() {
			run = false;			
		}

		public void run() {
	    	 Object o;
	    	 
	         // Keep listening to the InputStream while connected
	         while (run) {
	             try {
	            	 o = inStream.readObject();
	                 
	                 if(o != null)
	                 synchronized (instance) {
	                	 instance.recievePacket((Packet)o);
	                	 o = null;
	                 }
	             } catch (IOException e) {
	                 synchronized (instance) {
	                	 instance.connectionLost();
	                 }
	                 break;
	             } catch (ClassNotFoundException e) {
					// Shouldn't happen because class is Object
					e.printStackTrace();
				}
	         }
	     }
	 }
	   
}
