package khl.mobile.bluecade.ui.games;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.model.bluetooth.BluetoothHandler;
import khl.mobile.bluecade.model.bluetooth.packages.RPSPacket;
import khl.mobile.bluecade.model.bluetooth.packages.RPSPacket.Move;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class RPSMainActivity extends Activity implements Observer {

	private Move myMove;
	private Move oppMove;
	private TextView status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rps_main);
		BluetoothHandler.getInstance().addObserver(this);
		status = (TextView) findViewById(R.id.status);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.blue_cade, menu);
		return true;
	}

	public void rockClicked(View v) {
		setMyMove(RPSPacket.Move.ROCK);
	}

	public void paperClicked(View v) {
		setMyMove(RPSPacket.Move.PAPER);
	}

	public void scissorsClicked(View v) {
		setMyMove(RPSPacket.Move.SCISSORS);
	}

	private void setMyMove(Move move) {
		if (myMove == null)
			this.myMove = move;
		checkStatus();
	}

	private void setOppMove(Move move) {
		if (oppMove == null)
			this.oppMove = move;
		checkStatus();
	}

	private void checkStatus() {
		//Build the result string
		StringBuilder b = new StringBuilder();
		
		if(myMove != null)
			b.append("You picked " + myMove.toString().toLowerCase() + ".\n");
		
		if (oppMove != null && myMove != null) {
			b.append("Your opponent picked " + oppMove.toString().toLowerCase() + ".\n");
			// check winner
			int winner = 0; // 0=tie, 1=you win, 2=opp wins
			if (!oppMove.equals(myMove)) {
				if (oppMove == Move.ROCK && myMove == Move.PAPER) {
					winner = 1;
				}
				else if (oppMove == Move.PAPER && myMove == Move.SCISSORS) {
					winner = 1;
				}
				else if (oppMove == Move.SCISSORS && myMove == Move.ROCK) {
					winner = 1;
				}else {
					winner = 2;
				}
			}
			
			switch(winner){
			case 0:
				b.append("It's a draw.\n");
				break;
			case 1:
				if(new Random().nextDouble() < 0.02)
					b.append("Congratulations, you won!\n");
				else
					b.append("congration you done it\n");
				break;
			case 2:
				b.append("You lost..\n");
				break;
			}

			oppMove = null;
			myMove = null;
		} else if (oppMove == null) {
			// waiting for opponents move
			b.append("Waiting for your opponents move.\n");

		} else if (myMove == null) {
			// waiting for your move
			b.append("Waiting for your move.\n");
		}
		status.setText(b.toString());
	}

	@Override
	public void update(Observable obs, Object arg) {
		if (oppMove == null) {
			RPSPacket p = (RPSPacket) arg;
			setOppMove(p.getMove());
		}
	}

}
