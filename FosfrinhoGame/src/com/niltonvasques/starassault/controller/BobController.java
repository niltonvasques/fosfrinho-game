package com.niltonvasques.starassault.controller;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.niltonvasques.starassault.FosfrinhoGame;
import com.niltonvasques.starassault.model.Block;
import com.niltonvasques.starassault.model.Bob;
import com.niltonvasques.starassault.model.Bob.State;
import com.niltonvasques.starassault.model.Door;
import com.niltonvasques.starassault.model.Gate;
import com.niltonvasques.starassault.model.Item;
import com.niltonvasques.starassault.model.Key;
import com.niltonvasques.starassault.model.Load;
import com.niltonvasques.starassault.model.Shoot;
import com.niltonvasques.starassault.model.World;
import com.niltonvasques.starassault.model.Zombie;
import com.niltonvasques.starassault.screen.ScoreScreen;
import com.niltonvasques.starassault.service.Assets;
import com.niltonvasques.starassault.service.net.Client;
import com.niltonvasques.starassault.service.net.Host;
import com.niltonvasques.starassault.service.net.Host.OnReceive;
import com.niltonvasques.starassault.service.net.Message;
import com.niltonvasques.starassault.service.net.Server;
import com.niltonvasques.starassault.util.CameraHelper;

public class BobController {
	private static final String TAG = "[BobController]";
	private static final boolean LOG = false;
	
	private Host host;
	
	enum Keys {
		LEFT, RIGHT, JUMP, FIRE
	}
	
	private final static long MAX_TIME_PRESS_JUMP 	= 150l;
	private final static float ACCELERATION 	= 20f;
	private final static float GRAVITY 			= -20f;
	private final static float DAMP				= 0.90f;
	
	private final static float DELAY_TO_NOTIFY_HOST = 0.1f;
	
	private float notifyStateTime = 0;
	
	private FosfrinhoGame game;
	private World 	world;
	private InputMultiplexer multiplexer;
	
	private Bob enemy;
	
	private long jumpPressedTime = 0;
	private boolean jumpingPressed = false;
	private Array<Shoot> bobShoots = new Array<Shoot>();
	private Array<Shoot> drawableShoots = new Array<Shoot>();
	
	private CameraHelper cameraHelper; 
	
	static Map<Keys, Boolean> keys = new HashMap<BobController.Keys, Boolean>();
	
	private Pool<Rectangle> rectPool = new Pool<Rectangle>(){
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};
	
	private Array<Block> collidable = new Array<Block>();
	private Array<Zombie> collidableZombies = new Array<Zombie>();
	private Array<Load> collidableLoads = new Array<Load>();
	private Array<Key> collidableKeys = new Array<Key>();
	private Array<Door> collidableDoors = new Array<Door>();
	
	private boolean grounded = true;

	public BobController() {
		init();
		game = ((FosfrinhoGame)Gdx.app.getApplicationListener());
	}
	
	public void init(){
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.JUMP, false);
		keys.put(Keys.FIRE, false);
		
		
		this.world = new World();
		this.multiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(multiplexer);
		this.cameraHelper = new CameraHelper();
		
		restartGame();
		
		if( ((FosfrinhoGame)Gdx.app.getApplicationListener()).isServer() ){
			host = new Server();
		}else{
			host = new Client();
		}
		
		host.setOnReceive(new OnReceive() {
			@Override
			public void onReceive(Message msg) {
				try{
					enemy = json.fromJson(Bob.class, msg.getContent());
					Gdx.app.log(TAG, "enemy object received");
				}catch (Exception e) {
					Gdx.app.error(TAG, e.getMessage()+" "+msg.getContent());
				}
			}
		});
		
		host.asyncStart();
		
	}
	
	
	private Json json = new Json();
	
	private void notifyHost(){
		host.send(new Message(json.toJson(getWorld().getBob())));
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
    	
    	
    		if(world.isGameOver()){
    			Gdx.app.log(TAG, "Game Over");
    			return;
    		}
    		
    		// clear collision boxes in world
    		world.getCollisionRects().clear();
            // Processing the input - setting the states of Bob
            processInput();
            
            // If Bob is grounded then reset the state to IDLE
            if (grounded && world.getBob().getState().equals(State.JUMPING)) {
                    world.getBob().setState(State.IDLE);
            }
            
            for(Zombie zombie: world.getLevel().getZombies()){
            	zombie.getAcceleration().y = GRAVITY;
            	zombie.getAcceleration().scl(delta);
            	zombie.getVelocity().add(zombie.getAcceleration());
            }
            
            // Setting initial vertical acceleration
            world.getBob().getAcceleration().y = GRAVITY;
            
            // Convert acceleration to frame time
            world.getBob().getAcceleration().scl(delta);
            
            // apply acceleration to change velocity
            world.getBob().getVelocity().add(world.getBob().getAcceleration().x, world.getBob().getAcceleration().y);

            // checking collisions with the surrounding blocks depending on world.getBob()'s velocity
            checkZombiesCollisionWithBlocks(delta);
            checkBobCollisionWithObjects(delta);
            checkShootsCollisionWithBlocks(delta);
            

            // apply damping to halt Bob nicely
            world.getBob().getVelocity().x *= DAMP;
            
            // ensure terminal velocity is not exceeded
            if (world.getBob().getVelocity().x > Bob.SPEED) {
                    world.getBob().getVelocity().x = Bob.SPEED;
            }
            if (world.getBob().getVelocity().x < -Bob.SPEED) {
                    world.getBob().getVelocity().x = -Bob.SPEED;
            }
            
            if(world.getBob().getState().equals(State.WALKING)){
            	stepStateTime += delta;
            	if(stepStateTime > 1/3f){
            		stepStateTime = 0;
            		Assets.instance.sound.step.play();
            	}
            }
            
            // simply updates the state time
            world.getBob().update(delta);
            
            for(int i = (int)(world.getBob().getBounds().x - 6f); i <= (int)(world.getBob().getBounds().x + 6f); i++){
            	for(int j = (int)(world.getBob().getBounds().y - 6f); j <= (int)(world.getBob().getBounds().y + 6f); j++){
                	Load load = world.getLevel().getLoad(i, j);
                	if(load != null){
                		load.update(delta);
                	}
                }
            }
            
            notifyStateTime += delta;
            if(notifyStateTime >= DELAY_TO_NOTIFY_HOST){
            	notifyHost();
            	notifyStateTime = 0f;
            }
    }
	
    public CameraHelper getCameraHelper() {
		return cameraHelper;
	}

	public void setCameraHelper(CameraHelper cameraHelper) {
		this.cameraHelper = cameraHelper;
	}

	public Array<Shoot> getBobShoots() {
		return bobShoots;
	}

	/** Collision checking **/
    private void checkBobCollisionWithObjects(float delta) {
            // scale velocity to frame units
            world.getBob().getVelocity().scl(delta);
            
            // Obtain the rectangle from the pool instead of instantiating it
            Rectangle bobRect = rectPool.obtain();
            // set the rectangle to bob's bounding box
            bobRect.set(world.getBob().getBounds().x, world.getBob().getBounds().y, world.getBob().getBounds().width, world.getBob().getBounds().height);
            
            // we first check the movement on the horizontal X axis
            int startX, endX;
            int startY = (int) world.getBob().getBounds().y;
            int endY = (int) (world.getBob().getBounds().y + world.getBob().getBounds().height);
            // if Bob is heading left then we check if he collides with the block on his left
            // we check the block on his right otherwise
            if (world.getBob().getVelocity().x < 0) {
                    startX = endX = (int) Math.floor(world.getBob().getBounds().x + world.getBob().getVelocity().x);
            } else {
                    startX = endX = (int) Math.floor(world.getBob().getBounds().x + world.getBob().getBounds().width + world.getBob().getVelocity().x);
            }

            // get the block(s) bob can collide with
            populateCollidableObjects(startX, startY, endX, endY);
            
            // get the block(s) bob can collide with
            populateCollidableZombies(startX, startY, endX, endY);
            
            //get the loads that bob can collide with
            populateColectableItems(startX, startY, endX, endY);

            // simulate bob's movement on the X
            bobRect.x += world.getBob().getVelocity().x;
            
            // if bob collides, make his horizontal velocity 0
            for (Block block : collidable) {
                    if (block == null) continue;
                    if (bobRect.overlaps(block.getBounds())) {
                            world.getBob().getVelocity().x = 0;
                            world.getCollisionRects().add(block.getBounds());
                            break;
                    }
            }
            
            // if bob collides, make his horizontal velocity 0
            for (Door door : collidableDoors) {
                    if (door == null) continue;
                    if (bobRect.overlaps(door.getBounds())) {
                    	if(door instanceof Gate){
                    		levelClear();
                    	}else{
                            world.getBob().getVelocity().x = 0;
                            world.getCollisionRects().add(door.getBounds());
                            for(Item key : world.getBob().getBag().getItems()){
                            	if(key instanceof Key && door.open((Key)key) ){
                            		world.getLevel().getDoors()[(int)door.getBounds().x][(int)door.getBounds().y] = null;
                            	}
                            }
                            if(!door.isOpen()) Gdx.app.log(TAG, "You need of the correct key to open this door!");
                            break;
                    	}
                    }
            }
            
            // if bob collides, make his horizontal velocity 0
            for (Load load : collidableLoads) {
                    if (load == null) continue;
                    float portionWidth = load.getBounds().width/3;
                    if ((load.getBounds().x+portionWidth) < world.getBob().getBounds().x + world.getBob().getBounds().width && 
                    		(load.getBounds().x + load.getBounds().width-portionWidth) > world.getBob().getBounds().x && 
                    		load.getBounds().y < world.getBob().getBounds().y + world.getBob().getBounds().height &&
                    		load.getBounds().y + load.getBounds().height > world.getBob().getBounds().y) {
                    	Assets.instance.sound.load.play();
                    	world.getBob().getGun().reload(load);
                    	world.getLevel().getLoads()[(int)load.getBounds().x][(int)load.getBounds().y] = null;
                            break;
                    }
            }
            
         // Check if bob collide with a key
            for (Key key : collidableKeys) {
                    if (key == null) continue;
                    float portionWidth = key.getBounds().width/3;
                    if ((key.getBounds().x+portionWidth) < world.getBob().getBounds().x + world.getBob().getBounds().width && 
                    		(key.getBounds().x + key.getBounds().width-portionWidth) > world.getBob().getBounds().x && 
                    		key.getBounds().y < world.getBob().getBounds().y + world.getBob().getBounds().height &&
                    		key.getBounds().y + key.getBounds().height > world.getBob().getBounds().y) {
                    	world.getBob().getBag().addItem(key);
                    	world.getLevel().getKeys()[(int)key.getBounds().x][(int)key.getBounds().y] = null;
                            break;
                    }
            }
            
            if(!world.getBob().isDamaged()){
	            // if bob collides, make his horizontal velocity 0
	            for (Zombie zombie : collidableZombies) {
	            	if (zombie.isAlive() && bobRect.overlaps(zombie.getBounds())) {
	            		world.getBob().getVelocity().x = 0;
	            		applyBobDamage();
	            		break;
	            	}
	            }
            }

            // reset the x position of the collision box
            bobRect.x = world.getBob().getPosition().x;
            
            // the same thing but on the vertical Y axis
            startX = (int) world.getBob().getBounds().x;
            endX = (int) (world.getBob().getBounds().x + world.getBob().getBounds().width);
            if (world.getBob().getVelocity().y < 0) {
                    startY = endY = (int) Math.floor(world.getBob().getBounds().y + world.getBob().getVelocity().y);
            } else {
                    startY = endY = (int) Math.floor(world.getBob().getBounds().y + world.getBob().getBounds().height + world.getBob().getVelocity().y);
            }
            
            populateCollidableObjects(startX, startY, endX, endY);
            
            populateCollidableZombies(startX, startY, endX, endY);
            
            bobRect.y += world.getBob().getVelocity().y;
            
            for (Block block : collidable) {
                    if (block == null) continue;
                    if (bobRect.overlaps(block.getBounds())) {
                            if (world.getBob().getVelocity().y < 0) {
                                    grounded = true;
                            }
                            world.getBob().getVelocity().y = 0;
                            world.getCollisionRects().add(block.getBounds());
                            break;
                    }
            }
            if(!world.getBob().isDamaged()){
	            for (Zombie zombie : collidableZombies) {
	                if (zombie.isAlive() && bobRect.overlaps(zombie.getBounds())) {
	                	world.getBob().getVelocity().y = 0;
	                	applyBobDamage();
	                    break;
	                }
	            }
            }
            // reset the collision box's position on Y
            bobRect.y = world.getBob().getPosition().y;
            
            // update Bob's position
            world.getBob().getPosition().add(world.getBob().getVelocity());
            world.getBob().getBounds().x = world.getBob().getPosition().x;
            world.getBob().getBounds().y = world.getBob().getPosition().y;
            
            // un-scale velocity (not in frame time)
            world.getBob().getVelocity().scl(1 / delta);
            
    }

	private void levelClear() {
		Assets.instance.music.levelMusic.stop();
		Gdx.app.log(TAG, "Level cleared!");
		world.getLevel().setLevelClearTime(TimeUtils.millis() - world.getLevel().getLevelClearTime());
		game.setScoreTime(world.getLevel().getLevelClearTime());
		
		game.setScreen(new ScoreScreen());
	}

	private void applyBobDamage() {
		if(world.getBob().isDamaged()) return;
//		bob.getPosition().x += 0.2f;
		world.getBob().decreaseHp();
		if(world.getBob().getHp()  <= 0){
			world.setGameOver(true);
			Assets.instance.music.levelMusic.stop();
		}else{
			world.getBob().setDamaged(true);
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
	            populateCollidableObjects(startX, startY, endX, endY);
	            
	            populateCollidableZombies(startX, startY, endX, endY);
	
	            // simulate shoot's movement on the X
	            shootRect.x += shoot.getVelocity().x;
	            
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
	            for (Door door : collidableDoors) {
	                    if (door == null) continue;
	                    if (shootRect.overlaps(door.getBounds())) {
	                            collide = true;
	                            bobShoots.removeValue(shoot, true);
	                            world.getCollisionRects().add(door.getBounds());
	                            break;
	                    }
	            }
	            
	         // if bob collides, make his horizontal velocity 0
	            for (Zombie zombie : collidableZombies) {
	            	if (zombie.isAlive() && shootRect.overlaps(zombie.getBounds())) {
	            		if(shoot.getVelocity().x > 0 && !zombie.isFacingLeft()
	            				|| shoot.getVelocity().x < 0 && zombie.isFacingLeft()){
	            			zombie.reveserXDirection();
	            		}
	            		collide = true;
                		bobShoots.removeValue(shoot, true);
                		zombie.decreaseHp();
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
	            
	            populateCollidableObjects(startX, startY, endX, endY);
	            
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
	            for (Door door : collidableDoors) {
	                    if (door == null) continue;
	                    if (shootRect.overlaps(door.getBounds())) {
	                            collide = true;
	                            bobShoots.removeValue(shoot, true);
	                            world.getCollisionRects().add(door.getBounds());
	                            break;
	                    }
	            }
	            
		         // if bob collides, make his horizontal velocity 0
	            for (Zombie zombie : collidableZombies) {
	            	if (zombie.isAlive() && shootRect.overlaps(zombie.getBounds())) {
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
	            populateCollidableObjects(startX, startY, endX, endY);
	            
	            // simulate zombie's movement on the X
	            zombieRect.x += zombie.getVelocity().x;
	            
	            
	            // if zombie collides, make his horizontal velocity 0
	            for (Block block : collidable) {
	                    if (block == null) continue;
	                    if (zombieRect.overlaps(block.getBounds())) {
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
	            
	            populateCollidableObjects(startX, startY, endX, endY);
	            
	            zombieRect.y += zombie.getVelocity().y;
	            
	            for (Block block : collidable) {
	                    if (block == null) continue;
	                    if (zombieRect.overlaps(block.getBounds())) {
	                    	if (zombie.getVelocity().y < 0) {
	                    		zombie.getVelocity().y = 0;
	                    		zombie.getPosition().y = block.getBounds().y + block.getBounds().height;
	                    		
	                    	}
//	                    	zombie.reverseYDirection();
                            world.getCollisionRects().add(block.getBounds());
                            break;
	                    }
	            }
	            
	            // reset the collision box's position on Y
	            zombieRect.y = zombie.getPosition().y;
	            
	            // un-scale velocity (not in frame time)
	            zombie.getVelocity().scl(1 / delta);
	            
	            zombie.updateZombie(delta);
//	            zombie.getPosition().add(zombie.getVelocity());
	            zombie.getBounds().x = zombie.getPosition().x;
	            zombie.getBounds().y = zombie.getPosition().y;
	            
	            //If zombie died, remove it from level
	            if(zombie.getState() == Zombie.State.DIED){
	            	world.getLevel().getZombies().removeValue(zombie, true);
	            }
	            
    		}
            
    }
	

    /** populate the collidable array with the blocks found in the enclosing coordinates **/
    private void populateCollidableObjects(int startX, int startY, int endX, int endY) {
            collidable.clear();
            collidableDoors.clear();
            for (int x = startX; x <= endX; x++) {
                    for (int y = startY; y <= endY; y++) {
                            if (x >= 0 && x < world.getLevel().getWidth() && y >=0 && y < world.getLevel().getHeight()) {
                            		Block block = world.getLevel().get(x, y);
                                    Door door = world.getLevel().getDoor(x, y);
                                    if(door != null) collidableDoors.add(door);
                                    if(block != null) collidable.add(block);
                            }
                    }
            }
    }
    
    /** populate the collidable array with the blocks found in the enclosing coordinates **/
    private void populateColectableItems(int startX, int startY, int endX, int endY) {
    		collidableLoads.clear();
    		collidableKeys.clear();
            for (int x = startX; x <= endX; x++) {
                    for (int y = startY; y <= endY; y++) {
                            if (x >= 0 && x < world.getLevel().getWidth() && y >=0 && y < world.getLevel().getHeight()) {
                        		Load load = world.getLevel().getLoad(x, y);
                                Key key = world.getLevel().getKey(x, y);
                                if(load != null) collidableLoads.add(load);
                                if(key != null) collidableKeys.add(key);
                            }
                    }
            }
    }
    
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
    	
    	x1 = world.getBob().getBounds().x - width;
    	x2 = world.getBob().getBounds().x + width;
    	
    	y1 = world.getBob().getBounds().y - height;
    	y2 = world.getBob().getBounds().y + height;
    	
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
                    if (!world.getBob().getState().equals(State.JUMPING)) {
                            jumpingPressed = true;
                            jumpPressedTime = System.currentTimeMillis();
                            world.getBob().setState(State.JUMPING);
                            world.getBob().getVelocity().y = Bob.JUMP_VELOCITY;
                            grounded = false;
                    } else {
                            if (jumpingPressed && ((System.currentTimeMillis() - jumpPressedTime) >= MAX_TIME_PRESS_JUMP)) {
                                    jumpingPressed = false;
                            } else {
                                    if (jumpingPressed) {
                                            world.getBob().getVelocity().y = Bob.JUMP_VELOCITY;
                                    }
                            }
                    }
            }
            if (keys.get(Keys.LEFT)) {
                    // left is pressed
                    world.getBob().setFacingLeft(true);
                    if (!world.getBob().getState().equals(State.JUMPING)) {
                            world.getBob().setState(State.WALKING);
                    }
                    world.getBob().getAcceleration().x = -ACCELERATION;
                    
            } else if (keys.get(Keys.RIGHT)) {
                    // left is pressed
                    world.getBob().setFacingLeft(false);
                    if (!world.getBob().getState().equals(State.JUMPING)) {
                            world.getBob().setState(State.WALKING);
                    }
                    world.getBob().getAcceleration().x = ACCELERATION;
                    
            } else {
                    if (!world.getBob().getState().equals(State.JUMPING)) {
                            world.getBob().setState(State.IDLE);
                    }
                    world.getBob().getAcceleration().x = 0;
                    
            }           
            
            checkIfBobFire();
            return false;
    }

	private void checkIfBobFire() {
		long time = TimeUtils.millis();
		if(keys.get(Keys.FIRE) && (time - Shoot.lastShoot) >= (1000/world.getBob().getGun().getShootsPerSecond())){
			if(world.getBob().getGun().shoot()){
				Assets.instance.sound.shoot.play();
				Shoot.lastShoot = time;
				Vector2 sPos = world.getBob().getPosition().cpy();
				sPos.y += Bob.SIZE/2;
				Shoot s = new Shoot(sPos);
				if(world.getBob().isFacingLeft())
					s.getVelocity().x = -Shoot.SPEED;
				else{
					s.getPosition().x += world.getBob().getBounds().width;
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
		world.getLevel().setLevelClearTime(TimeUtils.millis());
		Assets.instance.music.levelMusic.play();
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Bob getEnemy() {
		return enemy;
	}
	
}
