package khl.mobile.bluecade.model.bluetooth.packages;

public class RPSPacket extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum Move{ROCK, PAPER, SCISSORS};
	
	private Move move;
	
	public RPSPacket(Move move){
		this.move = move;
	}
	
	public Move getMove(){
		return move;
	}

}
