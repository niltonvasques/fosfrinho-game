package com.niltonvasques.starassault.util;

public class Constants {
	/**
	 * CAMERA_WIDTH is amount of units that camera has in your width.
	 */
	public static final float CAMERA_WIDTH = 10f;;
	
	/**
	 * CAMERA_HEIGHT is amount of units that camera has in your height.
	 */
	public static final float CAMERA_HEIGHT = 7f;
	
	/**
	 * CAMERA_HALF_HEIGHT is just a half of CAMERA_HEIGHT, and is are calculated before for avoid
	 * unnecessary computations at runtime. 
	 */
	public static final float CAMERA_HALF_HEIGHT = CAMERA_HEIGHT / 2f;
	
	/**
	 * CAMERA_HALF_WIDTH is just a half of CAMERA_WIDTH, and is are calculated before for avoid
	 * unnecessary computations at runtime. 
	 */
	public static final float CAMERA_HALF_WIDTH = CAMERA_WIDTH / 2f;
	
	/**
	 * 
	 */
	public static final String TEXTURE_ATLAS_PACK = "data/textures.pack";
	
}
