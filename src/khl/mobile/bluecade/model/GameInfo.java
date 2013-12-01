package khl.mobile.bluecade.model;

import android.app.Activity;

public interface GameInfo {
	
	public String getTitle();
	public int getImageResourceId();
	public String getInstructions();
	public Activity createGame();

}
