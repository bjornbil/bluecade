package khl.mobile.bluecade.model.bluetooth.packages.pictionary;

import khl.mobile.bluecade.model.bluetooth.packages.Packet;

public class GuessPacket extends Packet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String guess;
	
	public GuessPacket(String guess){
		this.guess = guess;
	}

}
