package khl.mobile.bluecade.ui.games.pictionary;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingPanel extends View implements MotionEventListener{
	
	private Canvas drawCanvas;
	private	Bitmap canvasBitmap;
	private	Path drawPath = new Path();
	private Paint drawPaint = new Paint();
	
	//Default behavior is Local, acts as expected, not used for multiplayer games
	//Sender behavior also sends out events to its listeners, don't forget to register
	// the multiplayer handler as a listener
	//Reciever behavior ignores local touches, don't forget to register this
	// DrawingPanel as a listener of the multiplayer handler
	public enum Behavior {DISABLED, LOCAL, SENDER, RECIEVER};
	
	private Behavior behavior = DrawingPanel.Behavior.DISABLED;
	private List<MotionEventListener> listeners = new ArrayList<MotionEventListener>();
	
	//Because the drawing panel might be a different size on the screen of your opponent
	// we have to scale the coordinates of incoming events.
	// Only used with RECIEVER behavior
	private double hScale = 1;
	private double vScale = 1;
	
	public DrawingPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		setColor(Color.BLACK);
		drawPaint.setAntiAlias(true);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
	}
	
	public DrawingPanel(Context context, AttributeSet attrs, Behavior behavior) {
		this(context, attrs);
		setBehavior(behavior);
	}
	
	public void setBehavior(Behavior behavior){
		this.behavior = behavior;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		clear();
		drawCanvas = new Canvas(canvasBitmap);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(canvasBitmap, 0, 0, null);
		canvas.drawPath(drawPath, drawPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if(this.behavior.equals(DrawingPanel.Behavior.DISABLED) || this.behavior.equals(DrawingPanel.Behavior.RECIEVER)) return false;
		boolean changed = (e.getAction() == MotionEvent.ACTION_DOWN ||
				e.getAction() == MotionEvent.ACTION_MOVE ||
				e.getAction() == MotionEvent.ACTION_UP);
		if(changed){
			if(this.behavior.equals(DrawingPanel.Behavior.SENDER) ){
				for(MotionEventListener l : this.listeners){
					l.update(e);
				}
			}
			this.update(e);
		}
		
		return changed;
	}
	
	public void setColor(int color){
		drawPaint.setColor(color);
		drawPaint.setStrokeWidth(20);
	}
	
	public void eraseTool(){
		drawPaint.setColor(Color.WHITE);
		drawPaint.setStrokeWidth(40);
	}
	
	public void clear(){
		canvasBitmap.eraseColor(Color.WHITE);
		invalidate();
	}
	
	public void addMotionEventListener(MotionEventListener l){
		this.listeners.add(l);
	}	
	
	public void removeMotionEventListener(MotionEventListener l){
		this.listeners.remove(l);
	}

	@Override
	public void update(MotionEvent e) {
		if(this.behavior.equals(DrawingPanel.Behavior.DISABLED)) return;
		switch(e.getAction()){
		case MotionEvent.ACTION_DOWN:
			drawPath.moveTo(e.getX(), e.getY());
			break;
		case MotionEvent.ACTION_MOVE:
			drawPath.lineTo(e.getX(), e.getY());
			break;
		case MotionEvent.ACTION_UP:
			drawCanvas.drawPath(drawPath, drawPaint);
			drawPath.reset();
			break;
		}
		invalidate();
	}

	public double getHorizontalScale() {
		return hScale;
	}

	public void setHorizontalScale(double hScale) {
		this.hScale = hScale;
	}

	public double getVerticalScale() {
		return vScale;
	}

	public void setVerticalScale(double vScale) {
		this.vScale = vScale;
	}
}
