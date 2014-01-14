package com.niltonvasques.fosfrinho.gameobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.niltonvasques.fosfrinho.components.DefaultRenderCom;
import com.niltonvasques.fosfrinho.components.DefaultStaticPhysicsCom;
import com.niltonvasques.fosfrinho.components.bob.BobInputComponent;
import com.niltonvasques.fosfrinho.components.bob.BobMoveComponent;
import com.niltonvasques.fosfrinho.components.bob.BobPhysicsComponent;
import com.niltonvasques.fosfrinho.components.bob.BobRenderComponent;
import com.niltonvasques.fosfrinho.components.display.DisplayRenderComponent;
import com.niltonvasques.fosfrinho.components.enemy.ZombiePhysicsComponent;
import com.niltonvasques.fosfrinho.components.enemy.ZombieRenderComponent;
import com.niltonvasques.fosfrinho.gameobject.GameObject.Type;
import com.niltonvasques.fosfrinho.util.Assets;

public class GameObjectFactory {

	public static GameObject createBobGameObject(SpriteBatch batch, float x, float y){
		
		GameObject bob = new GameObject(Type.BOB, x, y, 0.5f, 0.5f);
		
		bob.addNotCollidableType(Type.ZOMBIE);
		
		bob.addComponent(new BobInputComponent());
		
		bob.addComponent(new BobPhysicsComponent(bob));
		
		bob.addComponent(new BobMoveComponent());
		
		bob.addComponent(new BobRenderComponent());		
		
		return bob;
	}
	
	public static GameObject createBlockGameObject(float x, float y){
		
		GameObject block = new GameObject(Type.BLOCK, x, y, 1f, 1f);
		
		block.addComponent(new DefaultRenderCom(Assets.instance.level.blockRegion));
		
		block.addComponent(new DefaultStaticPhysicsCom(block));
		
		return block;
	}
	
	
	public static GameObject createZombieGameObject(float x, float y){
		
		GameObject zombie = new GameObject(Type.ZOMBIE, x, y, 0.5f, 0.9f);
		
		zombie.addNotCollidableType(Type.ZOMBIE);
		
		zombie.addComponent(new ZombieRenderComponent());
		
		zombie.addComponent(new ZombiePhysicsComponent(zombie));
		
		return zombie;
	}
	
	public static GameObject createFpsDisplayGameObject(float x, float y){
		GameObject display = new GameObject(Type.DISPLAY, x, y, 0.5f, 0.9f);
		
		display.addComponent(new DisplayRenderComponent());
		
		return display;
	}
}
