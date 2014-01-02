package com.niltonvasques.starassault.controller;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.niltonvasques.starassault.model.Block;
import com.niltonvasques.starassault.model.Bob;
import com.niltonvasques.starassault.model.Zombie;
import com.niltonvasques.starassault.model.Bob.State;
import com.niltonvasques.starassault.model.Shoot;
import com.niltonvasques.starassault.model.World;

public class BobController {
	private static final String TAG = "[BobController]";
	private static final boolean LOG = false;
	
	enum Keys {
		LEFT, RIGHT, JUMP, FIRE
	}
	
	private final static long MAX_TIME_PRESS_JUMP 	= 150l;
	private final static float ACCELERATION 	= 20f;
	private final static float GRAVITY 			= -20f;
	private final static float DAMP				= 0.90f;
	
	private static final float WIDTH = 10f;
	
	private World 	world;
	private Bob 	bob;
	private InputMultiplexer multiplexer;
	
	private long jumpPressedTime = 0;
	private boolean jumpingPressed = false;
	private Array<Shoot> bobShoots = new Array<Shoot>();
	private Array<Shoot> drawableShoots = new Array<Shoot>();
	private Sound shootSound;
	private Sound stepSound;

	public Array<Shoot> getBobShoots() {
		return bobShoots;
	}

	static Map<Keys, Boolean> keys = new HashMap<BobController.Keys, Boolean>();
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.JUMP, false);
		keys.put(Keys.FIRE, false);
	};
	
	private Pool<Rectangle> rectPool = new Pool<Rectangle>(){
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};
	
	private Array<Block> collidable = new Array<Block>();
	private Array<Zombie> collidableZombies = new Array<Zombie>();
	
	private boolean grounded = true;

	public BobController(World world) {
		shootSound = Gdx.audio.newSound(Gdx.files.internal("data/shoot.wav"));
		stepSound = Gdx.audio.newSound(Gdx.files.internal("data/step.mp3"));
		
		this.world = world;
		this.bob = world.getBob();
		this.multiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(multiplexer);
		
	}

	// ** Key presses and touches **************** //

	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	}

	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
	}

	public void jumpPressed() {
		keys.get(keys.put(Keys.JUMP, true));
	}

	public void firePressed() {
		keys.get(keys.put(Keys.FIRE, true));
	}

	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
	}

	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
	}

	public void jumpReleased() {
		keys.get(keys.put(Keys.JUMP, false));
		jumpingPressed = false;
	}

	public void fireReleased() {
		keys.get(keys.put(Keys.FIRE, false));
	}

	/** The main update method **/
    public void update(float delta) {
    	
    		if(world.isGameOver()) return;
            // Processing the input - setting the states of Bob
            processInput();
            
            // If Bob is grounded then reset the state to IDLE
            if (grounded && bob.getState().equals(State.JUMPING)) {
                    bob.setState(State.IDLE);
            }
            
            // Setting initial vertical acceleration
            bob.getAcceleration().y = GRAVITY;
            
            // Convert acceleration to frame time
            bob.getAcceleration().scl(delta);
            
            // apply acceleration to change velocity
            bob.getVelocity().add(bob.getAcceleration().x, bob.getAcceleration().y);

            // checking collisions with the surrounding blocks depending on Bob's velocity
            checkZombiesCollisionWithBlocks(delta);
            checkBobCollisionWithBlocksAndZombies(delta);
            checkShootsCollisionWithBlocks(delta);
            

            // apply damping to halt Bob nicely
            bob.getVelocity().x *= DAMP;
            
            // ensure terminal velocity is not exceeded
            if (bob.getVelocity().x > Bob.SPEED) {
                    bob.getVelocity().x = Bob.SPEED;
            }
            if (bob.getVelocity().x < -Bob.SPEED) {
                    bob.getVelocity().x = -Bob.SPEED;
            }
            
            for(Zombie zombie: world.getLevel().getZombies()){
            	zombie.updateZombie(delta);
            }
            
            if(bob.getState().equals(State.WALKING)){
            	stepStateTime += delta;
            	if(stepStateTime > 1/3f){
            		stepStateTime = 0;
            		stepSound.play();
            	}
            }
            
            // simply updates the state time
            bob.update(delta);

    }
	
    /** Collision checking **/
    private void checkBobCollisionWithBlocksAndZombies(float delta) {
            // scale velocity to frame units
            bob.getVelocity().scl(delta);
            
            // Obtain the rectangle from the pool instead of instantiating it
            Rectangle bobRect = rectPool.obtain();
            // set the rectangle to bob's bounding box
            bobRect.set(bob.getBounds().x, bob.getBounds().y, bob.getBounds().width, bob.getBounds().height);
            
            // we first check the movement on the horizontal X axis
            int startX, endX;
            int startY = (int) bob.getBounds().y;
            int endY = (int) (bob.getBounds().y + bob.getBounds().height);
            // if Bob is heading left then we check if he collides with the block on his left
            // we check the block on his right otherwise
            if (bob.getVelocity().x < 0) {
                    startX = endX = (int) Math.floor(bob.getBounds().x + bob.getVelocity().x);
            } else {
                    startX = endX = (int) Math.floor(bob.getBounds().x + bob.getBounds().width + bob.getVelocity().x);
            }

            // get the block(s) bob can collide with
            populateCollidableBlocks(startX, startY, endX, endY);
            
            // get the block(s) bob can collide with
            populateCollidableZombies(startX, startY, endX, endY);

            // simulate bob's movement on the X
            bobRect.x += bob.getVelocity().x;
            
            // clear collision boxes in world
            world.getCollisionRects().clear();
            
            

            
            // if bob collides, make his horizontal velocity 0
            for (Block block : collidable) {
                    if (block == null) continue;
                    if (bobRect.overlaps(block.getBounds())) {
                            bob.getVelocity().x = 0;
                            world.getCollisionRects().add(block.getBounds());
                            break;
                    }
            }
            
            if(!bob.isDamaged()){
	            // if bob collides, make his horizontal velocity 0
	            for (Zombie zombie : collidableZombies) {
	            	if (bobRect.overlaps(zombie.getBounds())) {
	            		bob.getVelocity().x = 0;
	            		applyBobDamage();
	            		break;
	            	}
	            }
            }

            // reset the x position of the collision box
            bobRect.x = bob.getPosition().x;
            
            // the same thing but on the vertical Y axis
            startX = (int) bob.getBounds().x;
            endX = (int) (bob.getBounds().x + bob.getBounds().width);
            if (bob.getVelocity().y < 0) {
                    startY = endY = (int) Math.floor(bob.getBounds().y + bob.getVelocity().y);
            } else {
                    startY = endY = (int) Math.floor(bob.getBounds().y + bob.getBounds().height + bob.getVelocity().y);
            }
            
            populateCollidableBlocks(startX, startY, endX, endY);
            
            populateCollidableZombies(startX, startY, endX, endY);
            
            bobRect.y += bob.getVelocity().y;
            
            for (Block block : collidable) {
                    if (block == null) continue;
                    if (bobRect.overlaps(block.getBounds())) {
                            if (bob.getVelocity().y < 0) {
                                    grounded = true;
                            }
                            bob.getVelocity().y = 0;
                            world.getCollisionRects().add(block.getBounds());
                            break;
                    }
            }
            if(!bob.isDamaged()){
	            for (Zombie zombie : collidableZombies) {
	                if (bobRect.overlaps(zombie.getBounds())) {
	                	bob.getVelocity().y = 0;
	                	applyBobDamage();
	                    break;
	                }
	            }
            }
            // reset the collision box's position on Y
            bobRect.y = bob.getPosition().y;
            
            // update Bob's position
            bob.getPosition().add(bob.getVelocity());
            bob.getBounds().x = bob.getPosition().x;
            bob.getBounds().y = bob.getPosition().y;
            
            // un-scale velocity (not in frame time)
            bob.getVelocity().scl(1 / delta);
            
    }

	private void applyBobDamage() {
		if(bob.isDamaged()) return;
//		bob.getPosition().x += 0.2f;
		bob.decreaseHp();
		if(bob.getHp()  <= 0){
			world.setGameOver(true);
		}else{
			bob.setDamaged(true);
		}
	}
    
    
    
    /** Collision checking **/
    private void checkShootsCollisionWithBlocks(float delta) {
    	
    		for(Shoot shoot: bobShoots){
    			boolean collide = false;
    			float speed = shoot.getVelocity().x;
    			
	            // scale velocity to frame units
	            shoot.getVelocity().scl(delta);
	            
	            // Obtain the rectangle from the pool instead of instantiating it
	            Rectangle shootRect = rectPool.obtain();
	            // set the rectangle to bob's bounding box
	            shootRect.set(shoot.getBounds().x, shoot.getBounds().y, shoot.getBounds().width, shoot.getBounds().height);
	            
	            // we first check the movement on the horizontal X axis
	            int startX, endX;
	            int startY = (int) shoot.getBounds().y;
	            int endY = (int) (shoot.getBounds().y + shoot.getBounds().height);
	            // if Bob is heading left then we check if he collides with the block on his left
	            // we check the block on his right otherwise
	            if (shoot.getVelocity().x < 0) {
	                    startX = endX = (int) Math.floor(shoot.getBounds().x + shoot.getVelocity().x);
	            } else {
	                    startX = endX = (int) Math.floor(shoot.getBounds().x + shoot.getBounds().width + shoot.getVelocity().x);
	            }
	
	            // get the block(s) shoot can collide with
	            populateCollidableBlocks(startX, startY, endX, endY);
	            
	            populateCollidableZombies(startX, startY, endX, endY);
	
	            // simulate shoot's movement on the X
	            shootRect.x += shoot.getVelocity().x;
	            
	            // clear collision boxes in world
	            world.getCollisionRects().clear();
	            
	            // if shoot collides, make his horizontal velocity 0
	            for (Block block : collidable) {
	                    if (block == null) continue;
	                    if (shootRect.overlaps(block.getBounds())) {
	                    		collide = true;
	                    		bobShoots.removeValue(shoot, true);
	                            world.getCollisionRects().add(block.getBounds());
	                            break;
	                    }
	            }
	            
	         // if bob collides, make his horizontal velocity 0
	            for (Zombie zombie : collidableZombies) {
	            	if (shootRect.overlaps(zombie.getBounds())) {
	            		collide = true;
                		bobShoots.removeValue(shoot, true);
                		zombie.decreaseHp();
                		if(zombie.getHp() == 0){
                			world.getLevel().getZombies().removeValue(zombie, true);
                		}
	            		break;
	            	}
	            }
	            
	            if(collide) continue;
	
	            // reset the x position of the collision box
	            shootRect.x = shoot.getPosition().x;
	            
	            // the same thing but on the vertical Y axis
	            startX = (int) shoot.getBounds().x;
	            endX = (int) (shoot.getBounds().x + shoot.getBounds().width);
	            if (shoot.getVelocity().y < 0) {
	                    startY = endY = (int) Math.floor(shoot.getBounds().y + shoot.getVelocity().y);
	            } else {
	                    startY = endY = (int) Math.floor(shoot.getBounds().y + shoot.getBounds().height + shoot.getVelocity().y);
	            }
	            
	            populateCollidableBlocks(startX, startY, endX, endY);
	            
	            populateCollidableZombies(startX, startY, endX, endY);
	            
	            shootRect.y += shoot.getVelocity().y;
	            
	            for (Block block : collidable) {
	                    if (block == null) continue;
	                    if (shootRect.overlaps(block.getBounds())) {
	                    	collide = true;
                    		bobShoots.removeValue(shoot, true);
                            world.getCollisionRects().add(block.getBounds());
                            break;
	                    }
	            }
	            
		         // if bob collides, make his horizontal velocity 0
	            for (Zombie zombie : collidableZombies) {
	            	if (shootRect.overlaps(zombie.getBounds())) {
	            		collide = true;
                		bobShoots.removeValue(shoot, true);
                		zombie.decreaseHp();
                		if(zombie.getHp() == 0){
                			world.getLevel().getZombies().removeValue(zombie, true);
                		}
	            		break;
	            	}
	            }
	            
	            if(collide) continue;
	            
	            // reset the collision box's position on Y
	            shootRect.y = shoot.getPosition().y;
	            
	            // update shoot's position
	            shoot.getPosition().add(shoot.getVelocity());
	            shoot.getBounds().x = shoot.getPosition().x;
	            shoot.getBounds().y = shoot.getPosition().y;
	            
	            // un-scale velocity (not in frame time)
	            shoot.getVelocity().scl(1 / delta);
    		}
            
    }
    
    /** Collision checking **/
    private void checkZombiesCollisionWithBlocks(float delta) {
    	
    		for(Zombie zombie: world.getLevel().getZombies()){
    			
	            // scale velocity to frame units
	            zombie.getVelocity().scl(delta);
	            
	            // Obtain the rectangle from the pool instead of instantiating it
	            Rectangle zombieRect = rectPool.obtain();
	            // set the rectangle to bob's bounding box
	            zombieRect.set(zombie.getBounds().x, zombie.getBounds().y, zombie.getBounds().width, zombie.getBounds().height);
	            
	            // we first check the movement on the horizontal X axis
	            int startX, endX;
	            int startY = (int) zombie.getBounds().y;
	            int endY = (int) (zombie.getBounds().y + zombie.getBounds().height);
	            // if Bob is heading left then we check if he collides with the block on his left
	            // we check the block on his right otherwise
	            if (zombie.getVelocity().x < 0) {
	                    startX = endX = (int) Math.floor(zombie.getBounds().x + zombie.getVelocity().x);
	            } else {
	                    startX = endX = (int) Math.floor(zombie.getBounds().x + zombie.getBounds().width + zombie.getVelocity().x);
	            }
	
	            // get the block(s) zombie can collide with
	            populateCollidableBlocks(startX, startY, endX, endY);
	            
	            // simulate zombie's movement on the X
	            zombieRect.x += zombie.getVelocity().x;
	            
	            // clear collision boxes in world
	            world.getCollisionRects().clear();
	            
	            // if zombie collides, make his horizontal velocity 0
	            for (Block block : collidable) {
	                    if (block == null) continue;
	                    if (zombieRect.overlaps(block.getBounds())) {
	                    	if (zombie.getVelocity().y < 0) {
	                    	}
                    		zombie.reveserXDirection();
                            world.getCollisionRects().add(block.getBounds());
                            break;
	                    }
	            }
	            
	            
	            // reset the x position of the collision box
	            zombieRect.x = zombie.getPosition().x;
	            
	            // the same thing but on the vertical Y axis
	            startX = (int) zombie.getBounds().x;
	            endX = (int) (zombie.getBounds().x + zombie.getBounds().width);
	            if (zombie.getVelocity().y < 0) {
	                    startY = endY = (int) Math.floor(zombie.getBounds().y + zombie.getVelocity().y);
	            } else {
	                    startY = endY = (int) Math.floor(zombie.getBounds().y + zombie.getBounds().height + zombie.getVelocity().y);
	            }
	            
	            populateCollidableBlocks(startX, startY, endX, endY);
	            
	            zombieRect.y += zombie.getVelocity().y;
	            
	            for (Block block : collidable) {
	                    if (block == null) continue;
	                    if (zombieRect.overlaps(block.getBounds())) {
	                    	zombie.reverseYDirection();
                            world.getCollisionRects().add(block.getBounds());
                            break;
	                    }
	            }
	            
	            // reset the collision box's position on Y
	            zombieRect.y = zombie.getPosition().y;
	            
	            // update zombie's position
	            zombie.getPosition().add(zombie.getVelocity());
	            zombie.getBounds().x = zombie.getPosition().x;
	            zombie.getBounds().y = zombie.getPosition().y;
	            
	            // un-scale velocity (not in frame time)
	            zombie.getVelocity().scl(1 / delta);
    		}
            
    }
	

    /** populate the collidable array with the blocks found in the enclosing coordinates **/
    private void populateCollidableBlocks(int startX, int startY, int endX, int endY) {
            collidable.clear();
            for (int x = startX; x <= endX; x++) {
                    for (int y = startY; y <= endY; y++) {
                            if (x >= 0 && x < world.getLevel().getWidth() && y >=0 && y < world.getLevel().getHeight()) {
                                    collidable.add(world.getLevel().get(x, y));
                            }
                    }
            }
    }
    
    /** populate the collidable array with the blocks found in the enclosing coordinates **/
    private void populateCollidableZombies(int startX, int startY, int endX, int endY) {
    	
    	Rectangle screenDrawableArea = new Rectangle(startX, startY, endX, endY);
        collidableZombies.clear();
        for(Zombie zombie: world.getLevel().getZombies()){
        	if(zombie.getBounds().overlaps(screenDrawableArea)){
        		collidableZombies.add(zombie);
        	}
        }
        
    }
    
    public Array<Shoot> getDrawableShoots(float width, float height){
    	drawableShoots.clear();
    	float x1,x2,y1,y2;
    	
    	x1 = bob.getBounds().x - width;
    	x2 = bob.getBounds().x + width;
    	
    	y1 = bob.getBounds().y - height;
    	y2 = bob.getBounds().y + height;
    	
    	Rectangle screenDrawableArea = new Rectangle(x1, y1, x2, y2);
    	
    	for(Shoot shoot: bobShoots){
    		if(shoot.getBounds().overlaps(screenDrawableArea)){
    			drawableShoots.add(shoot);
    		}
    	}    	
    	
    	return drawableShoots;    	
    }

    private float stepStateTime = 0f;
    /** Change Bob's state and parameters based on input controls **/
    private boolean processInput() {
            if (keys.get(Keys.JUMP)) {
                    if (!bob.getState().equals(State.JUMPING)) {
                            jumpingPressed = true;
                            jumpPressedTime = System.currentTimeMillis();
                            bob.setState(State.JUMPING);
                            bob.getVelocity().y = Bob.JUMP_VELOCITY;
                            grounded = false;
                    } else {
                            if (jumpingPressed && ((System.currentTimeMillis() - jumpPressedTime) >= MAX_TIME_PRESS_JUMP)) {
                                    jumpingPressed = false;
                            } else {
                                    if (jumpingPressed) {
                                            bob.getVelocity().y = Bob.JUMP_VELOCITY;
                                    }
                            }
                    }
            }
            if (keys.get(Keys.LEFT)) {
                    // left is pressed
                    bob.setFacingLeft(true);
                    if (!bob.getState().equals(State.JUMPING)) {
                            bob.setState(State.WALKING);
                    }
                    bob.getAcceleration().x = -ACCELERATION;
                    
            } else if (keys.get(Keys.RIGHT)) {
                    // left is pressed
                    bob.setFacingLeft(false);
                    if (!bob.getState().equals(State.JUMPING)) {
                            bob.setState(State.WALKING);
                    }
                    bob.getAcceleration().x = ACCELERATION;
                    
            } else {
                    if (!bob.getState().equals(State.JUMPING)) {
                            bob.setState(State.IDLE);
                    }
                    bob.getAcceleration().x = 0;
                    
            }           
            
            checkIfBobFire();
            return false;
    }

	private void checkIfBobFire() {
		long time = TimeUtils.millis();
		if(keys.get(Keys.FIRE) && (time - Shoot.lastShoot) >= (1000/bob.getGun().getShootsPerSecond())){
			if(bob.getGun().shoot()){
				shootSound.play();
				Shoot.lastShoot = time;
				Vector2 sPos = bob.getPosition().cpy();
				sPos.y += Bob.SIZE/2;
				Shoot s = new Shoot(sPos);
				if(bob.isFacingLeft())
					s.getVelocity().x = -Shoot.SPEED;
				else{
					s.getPosition().x += bob.getBounds().width;
					s.getVelocity().x = Shoot.SPEED;
				}
				bobShoots.add(s);
				if(LOG) Gdx.app.log(TAG, "BOB FIRED");
			}
		}
	}
	
	public void registerInputProcessor(InputProcessor processor){
		multiplexer.addProcessor(processor);
	}

	public void restartGame() {
		bobShoots.clear();
		world.clear();
	}
}
