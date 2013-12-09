package khl.mobile.bluecade.model.games;

import khl.mobile.bluecade.R;
import khl.mobile.bluecade.model.GameInfo;
import android.app.Activity;

public class PictionaryInfo implements GameInfo {

	@Override
	public String getTitle() {
		return "Pictionary";
	}

	@Override
	public String getInstructions() {
		return "With your bluetooth connection on, you will be able to play Pictionary with any of your bluetooth opponents running the BlueCade application. One player gets an object to draw and the opponent bluetooth player has to guess the object. When he guesses wrong, you can draw another object. When he guesses right, the roles get switched.";
	}

	@Override
	public Activity createGame() {
		return null;
	}
	
	@Override
	public int getImageResourceId() {
		return R.drawable.pictionary;
	}

	@Override
	public int getId() {
		return 1;
	}

	@Override
	public int getLaunchImageId() {
		return R.drawable.pictionarylaunch;
	}


}
