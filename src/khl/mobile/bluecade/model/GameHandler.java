package khl.mobile.bluecade.model;

import java.util.ArrayList;
import java.util.List;

import khl.mobile.bluecade.model.games.*;

public class GameHandler {
	
	private List<GameInfo> games;
	
	public GameHandler(){
		games = new ArrayList<GameInfo>();
		games.add(new RpsInfo());
		games.add(new PictionaryInfo());		
	}
	
	public List<GameInfo> getGamesInfo(){
		return games;
	}

}
