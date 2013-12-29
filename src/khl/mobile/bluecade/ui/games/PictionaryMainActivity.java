package khl.mobile.bluecade.ui.games;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.model.bluetooth.BluetoothHandler;
import khl.mobile.bluecade.model.bluetooth.packages.pictionary.*;
import khl.mobile.bluecade.model.games.pictionary.WordList;
import khl.mobile.bluecade.ui.games.pictionary.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PictionaryMainActivity extends Activity implements Observer, MotionEventListener{
	
	private DrawingPanel drawingPanel;
	private TextView status;
	private EditText guessText;
	private Button guessButton;
	
	private HandshakePacket myHandshake;
	private boolean handshakeDone;
	
	private enum Role {DRAW, GUESS};
	private Role role;
	
	private String wordToDraw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic_main);
		BluetoothHandler.getInstance().addObserver(this);
		drawingPanel = (DrawingPanel) findViewById(R.id.drawingPanel);
		status = (TextView) findViewById(R.id.status);
		guessText = (EditText) findViewById(R.id.guessText);
		guessButton = (Button) findViewById(R.id.guessButton);

		drawingPanel.setBehavior(DrawingPanel.Behavior.DISABLED);
		myHandshake = new HandshakePacket(drawingPanel.getWidth(), drawingPanel.getHeight());
		
		AsyncTask<Integer, Void, Void> handshakeTask = new AsyncTask<Integer, Void, Void>() {
			@Override
			protected Void doInBackground(Integer... params) {
				sendHandshake();
				try {
					this.wait(1000);
				} catch (InterruptedException e) {
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
	
	public void makeGuess(View v){
		guessText.getText().toString();
	}
	
	public void setRole(Role r){
		drawingPanel.clear();
		if(r.equals(PictionaryMainActivity.Role.DRAW)){
			drawingPanel.setBehavior(DrawingPanel.Behavior.SENDER);
			drawingPanel.removeMotionEventListener(this);
			status.setVisibility(View.VISIBLE);
			guessText.setVisibility(View.GONE);
			guessButton.setVisibility(View.GONE);
			
			wordToDraw = WordList.random();
			guessText.setText("You are drawing a\n " + wordToDraw);
		}else{
			drawingPanel.setBehavior(DrawingPanel.Behavior.RECIEVER);
			drawingPanel.addMotionEventListener(this);
			status.setVisibility(View.GONE);
			guessText.setVisibility(View.VISIBLE);
			guessButton.setVisibility(View.VISIBLE);
		}
	}
	
	public void switchRoles(){
		if(role.equals(PictionaryMainActivity.Role.DRAW)){
			setRole(PictionaryMainActivity.Role.GUESS);
		}else{
			setRole(PictionaryMainActivity.Role.DRAW);
		}
	}

	@Override
	public void update(Observable obs, Object arg) {
		// Warning: gets called outside of the UI thread, sync with the UI thread
		// arg is the Packet
		if(arg instanceof HandshakePacket){
			HandshakePacket p = (HandshakePacket)arg;
			//Device with the highest dice roll gets to draw
			if(p.getDiceRoll() < myHandshake.getDiceRoll()){
				setRole(PictionaryMainActivity.Role.GUESS);
			}else{
				setRole(PictionaryMainActivity.Role.DRAW);
			}
			drawingPanel.setHorizontalScale(myHandshake.getScreenWidth() / p.getScreenWidth());
			drawingPanel.setVerticalScale(myHandshake.getScreenHeight() / p.getScreenHeight());
			
			//tell other device that the handshake was successful
			try {
				BluetoothHandler.getInstance().sendPacket(new HandshakeAckPacket());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(!handshakeDone && arg instanceof HandshakeAckPacket){
			handshakeDone = true;
		}else if(handshakeDone && arg instanceof DrawPacket && role.equals(role.GUESS)){
			//you are guessing so the recieved packet must be a drawPacket
			DrawPacket p = (DrawPacket)arg;
			drawingPanel.update(DrawPacket.packetToMotionEvent(p));
		}else if(handshakeDone && arg instanceof GuessPacket && role.equals(role.DRAW)){
			//you are drawing so the recieved packet must be a guessPacket
			GuessPacket p = (GuessPacket)arg;
			if(WordList.equalsLenient(wordToDraw, p.guess)){
				try {
					BluetoothHandler.getInstance().sendPacket(new EndPacket(EndPacket.Reason.WIN, EndPacket.Result.NEWGAME));
				} catch (IOException e) {
					e.printStackTrace();
				}
				Toast.makeText(getApplicationContext(), "Your opponent found the word! Starting a new game.", Toast.LENGTH_LONG).show();
				switchRoles();
			}
		}else if(handshakeDone && arg instanceof EndPacket){
			EndPacket p = (EndPacket)arg;
			if(p.result.equals(EndPacket.Result.NEWGAME)){
				if(p.reason.equals(EndPacket.Reason.WIN))
				Toast.makeText(getApplicationContext(), "You guessed right! Starting a new game.", Toast.LENGTH_LONG).show();
				switchRoles();
			}else if(p.result.equals(EndPacket.Result.QUIT))
				Toast.makeText(getApplicationContext(), "The other player quit the game.", Toast.LENGTH_LONG).show();
				this.finish();
		}

	}

	@Override
	public void update(MotionEvent e) {
		try {
			BluetoothHandler.getInstance().sendPacket(DrawPacket.motionEventToPacket(e));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Quitting game")
	        .setMessage("Are you sure you want to exit the game?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	try {
					BluetoothHandler.getInstance().sendPacket(new EndPacket(EndPacket.Reason.QUIT, EndPacket.Result.NEWGAME));
				} catch (IOException e) {
					e.printStackTrace();
				}
	            finish(); 
	        }

	    })
	    .setNegativeButton("No", null)
	    .show();
	}
}
