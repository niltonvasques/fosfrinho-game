package com.niltonvasques.fosfrinho.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.niltonvasques.fosfrinho.components.Message;
import com.niltonvasques.fosfrinho.components.PhysicsComponent;

public class PhysicsManager {
	private static String TAG = "[PhysicsManager]";
	private static final float GRAVITY = -20f;

	public static PhysicsManager instance = new PhysicsManager();

	private Array<PhysicsComponent> physicsComponents;

	private Box2DDebugRenderer boxDebugRenderer;
	private OrthographicCamera camera;
	private boolean debug = false;

	private PhysicsManager() {
		physicsComponents = new Array<PhysicsComponent>();
		world = new World(new Vector2(0, GRAVITY), true);

		world.setContactListener(new ContactListener() {

			//global scope
			int numFootContacts;
			  
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub

			}

			@Override
			public void endContact(Contact contact) {
				if(contact.getFixtureA() != null && ((String)contact.getFixtureA().getUserData()) == "Sensor"){
					Gdx.app.log(TAG, "begin Collide");
					numFootContacts--;
				}
				
				if(contact.getFixtureB() != null && ((String)contact.getFixtureB().getUserData()) == "Sensor"){
					Gdx.app.log(TAG, "begin Collide");
					numFootContacts--;
				}
				
				if(numFootContacts < 1){
					for(PhysicsComponent p : physicsComponents){
						p.receive(Message.FLYING);
					}
				}

			}

			@Override
			public void beginContact(Contact contact) {
				if(contact.getFixtureA() != null && ((String)contact.getFixtureA().getUserData()) == "Sensor"){
					Gdx.app.log(TAG, "begin Collide");
					numFootContacts++;
					
				}
				
				if(contact.getFixtureB() != null && ((String)contact.getFixtureB().getUserData()) == "Sensor"){
					Gdx.app.log(TAG, "begin Collide");
					numFootContacts++;
					
				}
				
				if(numFootContacts > 0){
					for(PhysicsComponent p : physicsComponents){
						p.receive(Message.GROUNDED);
					}
				}

			}
		});
	}

	public void enableDebug(OrthographicCamera cam) {
		boxDebugRenderer = new Box2DDebugRenderer();
		camera = cam;
		debug = true;
	}

	public Body registerCollisionComponent(PhysicsComponent value,	boolean staticBody) {
		physicsComponents.add(value);
		
		Body body = CreateBody(world,
				new Vector2(value.getBounds().x, value.getBounds().y), 0f,
				staticBody ? BodyType.StaticBody : BodyType.DynamicBody);
		MakeRectFixture(body, value.getBounds().width,
				value.getBounds().height, 1f, 0f);
		return body;
		
	}
	
	public Body registerCollisionComponent(PhysicsComponent value,	boolean staticBody, String sensor) {
		physicsComponents.add(value);
		if(staticBody){
			Body body = CreateBody(world,
					new Vector2(value.getBounds().x, value.getBounds().y), 0f,
					staticBody ? BodyType.StaticBody : BodyType.DynamicBody);
			MakeRectFixture(body, value.getBounds().width,
					value.getBounds().height, 1f, 0f);
			return body;
		}
		
		return createBody(value.getBounds().x, value.getBounds().y, value.getBounds().width, value.getBounds().height,sensor);
	}


	public boolean checkCollision(PhysicsComponent value) {
		for (PhysicsComponent c : physicsComponents) {
			if (value != c && value.getBounds().overlaps(c.getBounds())) {
				return true;
			}
		}
		return false;
	}

	// static final float WORLD_TO_BOX=0.01f;
	// static final float BOX_WORLD_TO=100f;

	static final float WORLD_TO_BOX = 1f;
	static final float BOX_WORLD_TO = 100f;

	float ConvertToBox(float x) {
		return x * WORLD_TO_BOX;
	}

	private World world;

	private Body CreateBody(World world, Vector2 pos, float angle, BodyType type) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = type;
		bodyDef.position.set(ConvertToBox(pos.x), ConvertToBox(pos.y));
		bodyDef.fixedRotation = true;
		return world.createBody(bodyDef);
	}

	private void MakeRectFixture(Body body,float width,float height,
			float density,float restitution){
		PolygonShape bodyShape = new PolygonShape();

		float w=ConvertToBox(width/2f);
		float h=ConvertToBox(height/2f);
		bodyShape.setAsBox(w,h);

		FixtureDef fixtureDef=new FixtureDef();
		fixtureDef.density=density;
		fixtureDef.friction = 0f;
		fixtureDef.restitution=restitution;
		fixtureDef.shape=bodyShape;
		body.createFixture(fixtureDef);
		
		bodyShape.dispose();
		
		
	}
	
	public Body createBody(float x, float y, float w, float h, String sensor){
		//body definition
	      BodyDef myBodyDef = new BodyDef();
	      myBodyDef.type = BodyType.DynamicBody;
	      myBodyDef.fixedRotation = true;
	      myBodyDef.position.set(x, y);
	      
	      //create dynamic body
	      Body body = world.createBody(myBodyDef);
	  
	      //shape definition for main fixture
	      PolygonShape polygonShape = new PolygonShape();
	      polygonShape.setAsBox(w*0.5f, h*0.5f); //a 2x4 rectangle
	  
	      //fixture definition
	      FixtureDef myFixtureDef = new FixtureDef();
	      myFixtureDef.shape = polygonShape;
	      myFixtureDef.density = 1;
	      
	      //add main fixture
	      body.createFixture(myFixtureDef);
	  
	      //add foot sensor fixture
	      polygonShape.setAsBox(w*0.2f, h*0.2f, new Vector2(0,-h*0.5f), 0);
	      myFixtureDef.isSensor = true;
	      Fixture footSensorFixture = body.createFixture(myFixtureDef);
	      footSensorFixture.setUserData(sensor);
	      
	      return body;
	}

	void MakeCircleFixture(Body body, float radius, BodyDef.BodyType bodyType,
			float density, float restitution, Vector2 pos, float angle) {

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.restitution = restitution;
		fixtureDef.shape = new CircleShape();
		fixtureDef.shape.setRadius(ConvertToBox(radius));

		body.createFixture(fixtureDef);
		fixtureDef.shape.dispose();
	}

	static final float BOX_STEP = 1 / 60f;
	static final int BOX_VELOCITY_ITERATIONS = 6;
	static final int BOX_POSITION_ITERATIONS = 2;
	float accumulator;

	public void Update(float dt) {
		// accumulator+=dt;
		// while(accumulator>BOX_STEP){
		// world.step(BOX_STEP,BOX_VELOCITY_ITERATIONS,BOX_POSITION_ITERATIONS);
		// accumulator-=BOX_STEP;
		// }
		world.step(dt, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);

		if (debug)
			boxDebugRenderer.render(world, camera.combined);
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}