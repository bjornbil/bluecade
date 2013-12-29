package khl.mobile.bluecade.model.bluetooth.packages.pictionary;

import khl.mobile.bluecade.model.bluetooth.packages.Packet;

public class EndPacket extends Packet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum Reason{WIN, TIMER, QUIT};
	public Reason reason;
	
	public enum Result{NEWGAME, QUIT};
	public Result result;
	
	public EndPacket(Reason reason, Result result){
		this.reason = reason;
		this.result = result;
	}

}
