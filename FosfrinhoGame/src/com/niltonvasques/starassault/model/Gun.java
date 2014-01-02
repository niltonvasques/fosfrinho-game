package com.niltonvasques.starassault.model;

import com.badlogic.gdx.Gdx;

public abstract class Gun {
	private static final String TAG = "[Gun]";
	private static boolean LOG = true;
	
	public static final int UNLIMITED_MUNITION = -1;
	
	private int shootsPerSecond;
	private int munition;
	private int maxMunition;
	private int loads;
	private int damagePerShoot;
	
	public Gun(int shootPerSecond, int munition, int maxMunition, int damagePerShoot) {
		this.shootsPerSecond = shootPerSecond;
		this.maxMunition = maxMunition;
		this.munition = munition;
		this.damagePerShoot = damagePerShoot;
	}
	
	public int getShootsPerSecond() {
		return shootsPerSecond;
	}
	public void setShootsPerSecond(int shootsPerSecond) {
		this.shootsPerSecond = shootsPerSecond;
	}
	public int getMunition() {
		return munition;
	}
	public void setMunition(int munition) {
		this.munition = munition;
	}
	public int getMaxMunition() {
		return maxMunition;
	}
	public void setMaxMunition(int maxMunition) {
		this.maxMunition = maxMunition;
	}
	public int getLoads() {
		return loads;
	}
	public void setLoads(int loads) {
		this.loads = loads;
	}
	
	public boolean shoot(){
		if(munition > 0 || munition == UNLIMITED_MUNITION){
			munition--;
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
