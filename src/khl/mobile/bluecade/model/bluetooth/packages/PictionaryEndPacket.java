package khl.mobile.bluecade.model.bluetooth.packages;

public class PictionaryEndPacket extends Packet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum Reason{WIN, TIMER};
	public Reason reason;
	
	public PictionaryEndPacket(Reason r){
		this.reason = r;
	}

}
