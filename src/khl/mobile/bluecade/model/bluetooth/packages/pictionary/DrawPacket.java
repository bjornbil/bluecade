package khl.mobile.bluecade.model.bluetooth.packages.pictionary;

import khl.mobile.bluecade.model.bluetooth.packages.Packet;
import android.os.SystemClock;
import android.view.MotionEvent;

public class DrawPacket extends Packet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int action;
	public float x;
	public float y;
	
	public static MotionEvent packetToMotionEvent(DrawPacket p){
		return MotionEvent.obtain(1, SystemClock.uptimeMillis(), p.action, p.x, p.y, 0);
	}
	
	public static DrawPacket motionEventToPacket(MotionEvent e){
		DrawPacket p = new DrawPacket();
		p.action = e.getAction();
		p.x = e.getX();
		p.y = e.getY();
		return p;
	}

}
