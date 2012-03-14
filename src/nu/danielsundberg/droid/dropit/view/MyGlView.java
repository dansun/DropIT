package nu.danielsundberg.droid.dropit.view;

import nu.danielsundberg.droid.dropit.DropItEngine;
import nu.danielsundberg.droid.dropit.rendering.MyRenderer;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MyGlView implements OnTouchListener {

	public MyRenderer renderer;
	public DropItEngine engine;
	public MyGlView(Context context) {
		renderer = new MyRenderer(this);
	}
	
	public void setParent(DropItEngine parent) {
		this.engine = parent;
	}

	float oldX=0;
	float oldY =0;
	double cameraRotationX = 1;
	double cameraRotationY = 1;
	private final float TOUCH_SCALE = 0.00002f;

	public double getCameraRotationX() {
		return cameraRotationX;
	}

	public double getCameraRotationY() {
		return cameraRotationY;
	}



	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		//Calculate the change
		float dx = x - oldX;
		float dy = y - oldY;

		//Rotate around the axis otherwise  
		cameraRotationX += dy*TOUCH_SCALE;
		cameraRotationY += dx*TOUCH_SCALE;

		oldX = x;
		oldY = y;

		return true;
	}



}

