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
		return "Herp Derp";
	}

	@Override
	public Activity createGame() {
		return null;
	}

	@Override
	public int getImageResourceId() {
		return R.drawable.pictionary;
	}

}
