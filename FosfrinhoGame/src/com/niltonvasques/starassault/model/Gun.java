package com.niltonvasques.starassault.model;

import com.badlogic.gdx.Gdx;

public abstract class Gun {
	private static final String TAG = "[Gun]";
	private static boolean LOG = true;
	
	public static final int UNLIMITED_MUNITION = -1;
	
	private int shootsPerSecond;
	private Load load;
	private int damagePerShoot;
	
	public Gun(int shootPerSecond, int damagePerShoot) {
		this.shootsPerSecond = shootPerSecond;
		this.damagePerShoot = damagePerShoot;
	}
	
	public int getShootsPerSecond() {
		return shootsPerSecond;
	}
	public void setShootsPerSecond(int shootsPerSecond) {
		this.shootsPerSecond = shootsPerSecond;
	}
	
	public Load getLoad(){
		return load;
	}
	
	public void reload(Load load) {
		this.load = load;
	}
	
	public boolean shoot(){
		if(load != null && load.takeABullet()){
			return true;
		}else{
			if(LOG) Gdx.app.log(TAG,"Out of ammo! Please reload!");
			return false;
		}
	}

	public int getDamagePerShoot() {
		return damagePerShoot;
	}

	public void setDamagePerShoot(int damagePerShoot) {
		this.damagePerShoot = damagePerShoot;
	}


}
