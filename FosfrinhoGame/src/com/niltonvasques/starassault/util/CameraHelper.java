package com.niltonvasques.starassault.util;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.niltonvasques.starassault.view.View;

public class CameraHelper {
	
	private static final String TAG = CameraHelper.class.getName();
	
	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 10f;
	
	private final float MIN_Y = 0;
	
	private Vector2 position;
	private float zoom;
	private View target;
	private float viewportWidth;
	private float viewportHeight;
	
	public CameraHelper() {
		zoom = 1f;
		position = new Vector2();
		setViewport(Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
	}
	
	public void update(float deltaTime){
		if(!hasTarget()) return;
		
		position.x = target.getPositionX();
		position.y = target.getPositionY();
		float minY = position.y - Constants.CAMERA_HALF_HEIGHT;
		if(minY < MIN_Y){
			position.y += (Math.abs(MIN_Y - minY));
		}
	}
	
	public boolean hasTarget(){
		return target != null;
	}
	
	public boolean hasTarget(Sprite sprite){
		return hasTarget() && target.equals(sprite);
	}
	
	public void setPosition(float x, float y){
		position.set(x, y);
	}
	
	public Vector2 getPosition(){ return position; }
	
	public void setTarget(View target){
		this.target = target;
	}
	
	public View getTarget(){ return target; }
	
	public void setZoom(float zoom){
		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}
	
	public void addZoom(float amount){ setZoom(zoom + amount); }
	
	public float getZoom(){ return zoom; }
	
	public void applyTo(OrthographicCamera camera){
		camera.position.set(position.x, position.y, 0);
		camera.zoom = zoom;
		camera.update();
	}

	public void setViewport(float viewportWidth, float viewportHeight) {
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
	}

	public float getViewportWidth() {
		return viewportWidth;
	}


	public float getViewportHeight() {
		return viewportHeight;
	}

}
