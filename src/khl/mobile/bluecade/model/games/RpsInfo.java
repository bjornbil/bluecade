package khl.mobile.bluecade.model.games;

import android.app.Activity;
import khl.mobile.bluecade.R;
import khl.mobile.bluecade.model.GameInfo;
import khl.mobile.bluecade.model.games.rps.RpsActivity;

public class RpsInfo implements GameInfo {

	@Override
	public String getTitle() {
		return "Rock, Paper, Scissors";
	}

	@Override
	public String getInstructions() {
		return "With your bluetooth connection on, you will be able to play Rock, Paper, Scissors on the street against any opponent running the BlueCade application. You simply press Rock, Paper or Scissors and wait for your opponent to react. Rock will win against Scissors, Paper will win against Rock and Scissors will win against Paper.";
	}
	
	@Override
	public Activity createGame() {
		return new RpsActivity();
	}

	@Override
	public int getImageResourceId() {
		return R.drawable.rps;
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public int getLaunchImageId() {
		return R.drawable.rpslaunch;
	}



}
