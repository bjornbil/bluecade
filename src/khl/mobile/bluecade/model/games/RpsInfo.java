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
		return "Herp derp";
	}

	@Override
	public Activity createGame() {
		return new RpsActivity();
	}

	@Override
	public int getImageResourceId() {
		return R.drawable.rps;
	}

}
