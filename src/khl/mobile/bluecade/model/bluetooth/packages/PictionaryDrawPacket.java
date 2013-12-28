package khl.mobile.bluecade.model.bluetooth.packages;

import android.os.SystemClock;
import android.view.MotionEvent;

public class PictionaryDrawPacket extends Packet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int action;
	public float x;
	public float y;
	
	public static MotionEvent packetToMotionEvent(PictionaryDrawPacket p){
		return MotionEvent.obtain(1, SystemClock.uptimeMillis(), p.action, p.x, p.y, 0);
	}
	
	public static PictionaryDrawPacket motionEventToPacket(MotionEvent e){
		PictionaryDrawPacket p = new PictionaryDrawPacket();
		p.action = e.getAction();
		p.x = e.getX();
		p.y = e.getY();
		return p;
	}

}
