package com.niltonvasques.fosfrinho.gameobject;

import com.niltonvasques.fosfrinho.components.DefaultRenderCom;
import com.niltonvasques.fosfrinho.components.DefaultStaticPhysicsCom;
import com.niltonvasques.fosfrinho.components.bob.BobGunCom;
import com.niltonvasques.fosfrinho.components.bob.BobHealthComponent;
import com.niltonvasques.fosfrinho.components.bob.BobInputCom;
import com.niltonvasques.fosfrinho.components.bob.BobMoveCom;
import com.niltonvasques.fosfrinho.components.bob.BobPhysicsCom;
import com.niltonvasques.fosfrinho.components.bob.BobRenderCom;
import com.niltonvasques.fosfrinho.components.bob.gun.ShootPhysicsCom;
import com.niltonvasques.fosfrinho.components.bob.gun.ShootRenderCom;
import com.niltonvasques.fosfrinho.components.display.DisplayRenderComponent;
import com.niltonvasques.fosfrinho.components.enemy.ZombiePhysicsCom;
import com.niltonvasques.fosfrinho.components.enemy.ZombieRenderCom;
import com.niltonvasques.fosfrinho.gameobject.GameObject.Type;
import com.niltonvasques.fosfrinho.util.Assets;

public class GameObjectFactory {

	public static GameObject createBobGameObject(float x, float y){
		
		GameObject bob = new GameObject(Type.BOB, x, y, 0.5f, 0.5f);
		
//		bob.addNotCollidableType(Type.ZOMBIE);
		
		bob.addComponent(new BobInputCom(bob));
		
		bob.addComponent(new BobPhysicsCom(bob));
		
		bob.addComponent(new BobHealthComponent(bob));
		
		bob.addComponent(new BobMoveCom(bob));
		
		bob.addComponent(new BobRenderCom(bob));
		
		bob.addComponent(new BobGunCom(bob));
		
		return bob;
	}
	
	public static GameObject createBlockGameObject(float x, float y){
		
		GameObject block = new GameObject(Type.BLOCK, x, y, 1f, 1f);
		
		block.addComponent(new DefaultRenderCom(block,Assets.instance.level.blockRegion));
		
		block.addComponent(new DefaultStaticPhysicsCom(block));
		
		return block;
	}
	
	
	public static GameObject createZombieGameObject(float x, float y){
		
		GameObject zombie = new GameObject(Type.ZOMBIE, x, y, 0.5f, 0.9f);
		
		zombie.addNotCollidableType(Type.ZOMBIE);
		
		zombie.addComponent(new ZombieRenderCom(zombie));
		
		zombie.addComponent(new ZombiePhysicsCom(zombie));
		
		return zombie;
	}
	
	public static GameObject createFpsDisplayGameObject(float x, float y){
		GameObject display = new GameObject(Type.DISPLAY, x, y, 0.5f, 0.9f);
		
		display.addComponent(new DisplayRenderComponent(display));
		
		return display;
	}
	
	public final static float SHOOT_SIZE = 0.1f; // half a unit
	public static GameObject createShootGameObject(float x, float y, boolean left){
		
		GameObject shoot = new GameObject(Type.SHOOT, x, y, SHOOT_SIZE, SHOOT_SIZE);
		
		shoot.addNotCollidableType(Type.SHOOT);
		shoot.addNotCollidableType(Type.BOB);
		
		shoot.addComponent(new ShootPhysicsCom(shoot,left));
		
		shoot.addComponent(new ShootRenderCom(shoot));
		
		return shoot;
		
	}
}
