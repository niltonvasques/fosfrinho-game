package com.niltonvasques.fosfrinho.gameobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.niltonvasques.fosfrinho.components.Component;
import com.niltonvasques.fosfrinho.components.PhysicsComponent;
import com.niltonvasques.fosfrinho.components.bob.BobInputComponent;
import com.niltonvasques.fosfrinho.components.bob.BobMoveComponent;
import com.niltonvasques.fosfrinho.components.bob.BobPhysicsComponent;
import com.niltonvasques.fosfrinho.components.bob.BobRenderComponent;
import com.niltonvasques.fosfrinho.components.level.BlockPhysicsComponent;
import com.niltonvasques.fosfrinho.components.level.BlockRenderComponent;

public class GameObjectFactory {

	public static GameObject createBobGameObject(SpriteBatch batch, float x, float y){
		
		GameObject bob = new GameObject(x, y, 0.5f, 0.5f);
		
		bob.addComponent(new BobInputComponent());
		
		bob.addComponent(new BobPhysicsComponent(bob));
		
		bob.addComponent(new BobMoveComponent());
		
		bob.addComponent(new BobRenderComponent());		
		
		return bob;
	}
	
	public static GameObject createBlockGameObject(SpriteBatch batch, float x, float y){
		
		GameObject block = new GameObject(x,y,1f,1f);
		
		block.addComponent(new BlockRenderComponent());
		
		block.addComponent(new BlockPhysicsComponent(block));
		
		return block;
	}
}
