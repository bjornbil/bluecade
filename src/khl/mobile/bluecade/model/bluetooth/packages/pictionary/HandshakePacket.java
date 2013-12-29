package khl.mobile.bluecade.model.bluetooth.packages.pictionary;

import java.util.Random;

import khl.mobile.bluecade.model.bluetooth.packages.Packet;

public class HandshakePacket extends Packet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Device with the highest dice roll gets to draw
	private int diceRoll;
	private int panelHeight;
	private int panelWidth;
	
	public HandshakePacket(int panelWidth, int panelHeight){
		diceRoll = new Random().nextInt();
		this.panelHeight = panelHeight;
		this.panelWidth = panelWidth;
	}

	public int getDiceRoll() {
		return diceRoll;
	}

	public int getScreenHeight() {
		return panelHeight;
	}

	public int getScreenWidth() {
		return panelWidth;
	}
	
	

}
