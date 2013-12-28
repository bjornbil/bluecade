package khl.mobile.bluecade.ui.games;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.model.bluetooth.BluetoothHandler;
import khl.mobile.bluecade.model.bluetooth.packages.PictionaryDrawPacket;
import khl.mobile.bluecade.model.bluetooth.packages.PictionaryGuessPacket;
import khl.mobile.bluecade.model.bluetooth.packages.PictionaryHandshakePacket;
import khl.mobile.bluecade.ui.games.pictionary.DrawingPanel;
import khl.mobile.bluecade.ui.games.pictionary.MotionEventListener;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;

public class PictionaryMainActivity extends Activity implements Observer, MotionEventListener{
	
	private DrawingPanel drawingPanel;
	private TextView status;
	private PictionaryHandshakePacket myHandshake;
	private boolean handshakeDone;
	private boolean role; //false = you draw, true = you guess

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic_main);
		BluetoothHandler.getInstance().addObserver(this);
		drawingPanel = (DrawingPanel) findViewById(R.id.drawingPanel);
		status = (TextView) findViewById(R.id.status);

		drawingPanel.setBehavior(DrawingPanel.Behavior.DISABLED);
		myHandshake = new PictionaryHandshakePacket(drawingPanel.getWidth(), drawingPanel.getHeight());
		
		AsyncTask<Integer, Void, Void> handshakeTask = new AsyncTask<Integer, Void, Void>() {
			@Override
			protected Void doInBackground(Integer... params) {
				sendHandshake();
				try {
					this.wait(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!handshakeDone){
					this.execute();
				}
				return null;
			}

		};
		handshakeTask.execute();
	}
	
	public void sendHandshake(){
		try {
			BluetoothHandler.getInstance().sendPacket(myHandshake);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.blue_cade, menu);
		return true;
	}
	
	public void switchRoles(){
		if(role){
			drawingPanel.setBehavior(DrawingPanel.Behavior.SENDER);
			drawingPanel.removeMotionEventListener(this);
		}else{
			drawingPanel.setBehavior(DrawingPanel.Behavior.RECIEVER);
			drawingPanel.addMotionEventListener(this);
		}
		role = !role;
	}

	@Override
	public void update(Observable obs, Object arg) {
		// Warning: gets called outside of the UI thread, sync with the UI thread
		// arg is the Packet
		if(arg instanceof PictionaryHandshakePacket){
			PictionaryHandshakePacket p = (PictionaryHandshakePacket)arg;
			//Device with the highest dice roll gets to draw
			if(p.getDiceRoll() < myHandshake.getDiceRoll()){
				role = true;
				drawingPanel.setBehavior(DrawingPanel.Behavior.RECIEVER);
			}else{
				drawingPanel.setBehavior(DrawingPanel.Behavior.SENDER);
				drawingPanel.addMotionEventListener(this);
			}
			drawingPanel.setHorizontalScale(myHandshake.getScreenWidth() / p.getScreenWidth());
			drawingPanel.setVerticalScale(myHandshake.getScreenHeight() / p.getScreenHeight());
		}else if(handshakeDone && arg instanceof PictionaryDrawPacket && role){
			PictionaryDrawPacket p = (PictionaryDrawPacket)arg;
			drawingPanel.update(PictionaryDrawPacket.packetToMotionEvent(p));
		}else if(handshakeDone && arg instanceof PictionaryGuessPacket && !role){
			PictionaryGuessPacket p = (PictionaryGuessPacket)arg;
		}
	}

	@Override
	public void update(MotionEvent e) {
		try {
			BluetoothHandler.getInstance().sendPacket(PictionaryDrawPacket.motionEventToPacket(e));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
}
