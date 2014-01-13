package com.niltonvasques.fosfrinho.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.util.BodyEditorLoader;

public class PhysicsManager {
	private static String TAG = "[PhysicsManager]";
	private static final float GRAVITY = -20f;
	private static final float WORLD_TO_BOX = 1f;
	private static final float BOX_WORLD_TO = 100f;

	public static PhysicsManager instance = new PhysicsManager();

	private Array<PhysicsComponent> physicsComponents;

	private Box2DDebugRenderer boxDebugRenderer;
	private OrthographicCamera camera;
	private boolean debug = false;
	private World world;
	private Array<Body> bodies = new Array<Body>();
	
	public interface SensorCollisionListener{
		public void onBeginContact();
		public void onEndContact();
	}

	private PhysicsManager() {
		physicsComponents = new Array<PhysicsComponent>();
		world = new World(new Vector2(0, GRAVITY), true);

		world.setContactListener(new ContactListener() {

			// global scope
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
				if (contact.getFixtureA() != null
						&& (contact.getFixtureA().getUserData()) != null) {
					if(contact.getFixtureA().getUserData() instanceof SensorCollisionListener){
						((SensorCollisionListener)contact.getFixtureA().getUserData()).onEndContact();
					}					
				}

				if (contact.getFixtureB() != null
						&& (contact.getFixtureB().getUserData()) != null) {
					if(contact.getFixtureB().getUserData() instanceof SensorCollisionListener){
						((SensorCollisionListener)contact.getFixtureB().getUserData()).onEndContact();
					}
				}

//				if (numFootContacts < 1) {
//					for (PhysicsComponent p : physicsComponents) {
//						p.receive(Message.FLYING);
//					}
//				}

			}

			@Override
			public void beginContact(Contact contact) {
				if (contact.getFixtureA() != null
						&& (contact.getFixtureA().getUserData()) != null) {
					if(contact.getFixtureA().getUserData() instanceof SensorCollisionListener){
						((SensorCollisionListener)contact.getFixtureA().getUserData()).onBeginContact();
					}					
				}

				if (contact.getFixtureB() != null
						&& (contact.getFixtureB().getUserData()) != null) {
					if(contact.getFixtureB().getUserData() instanceof SensorCollisionListener){
						((SensorCollisionListener)contact.getFixtureB().getUserData()).onBeginContact();
					}
				}

//				if (numFootContacts > 0) {
//					for (PhysicsComponent p : physicsComponents) {
//						p.receive(Message.GROUNDED);
//					}
//				}

			}
		});
	}

	private float ConvertToBox(float x) {
		return x * WORLD_TO_BOX;
	}


	public void enableDebug(OrthographicCamera cam) {
		boxDebugRenderer = new Box2DDebugRenderer();
		camera = cam;
		debug = true;
	}
	
	public Body registerStaticBody(GameObject g){
		
		Body body = CreateBody(world,
				new Vector2(g.getBounds().x, g.getBounds().y), 0f,BodyType.StaticBody);
		MakeRectFixture(body, g.getBounds().width,
				g.getBounds().height, 1f, 0f);
		body.setUserData(g);
		
		return body;
	}
	
	public Body registerDynamicBody(GameObject g){
		Body body = CreateBody(world,
				new Vector2(g.getBounds().x, g.getBounds().y), 0f,BodyType.DynamicBody);
		MakeRectFixture(body, g.getBounds().width,
				g.getBounds().height, 1f, 0f);
		body.setUserData(g);
		
		return body;
	}
	
	public Body registerDynamicBody(GameObject g,String path, String loaderName){
		
		if(path == null || loaderName == null){
			return registerDynamicBody(g);
		}
		
		Body body = createBodyFromBodyLoader(path, loaderName, g.getBounds().x, g.getBounds().y, g.getBounds().width);
		body.setUserData(g);
		
		return body;
	}

	public void registerCollisionComponent(PhysicsComponent value) {
		physicsComponents.add(value);
	}

//	public Body registerCollisionComponent(PhysicsComponent value,
//			boolean staticBody, String sensor) {
//		physicsComponents.add(value);
//		if (staticBody) {
//			Body body = CreateBody(world, new Vector2(value.getBounds().x,
//					value.getBounds().y), 0f, staticBody ? BodyType.StaticBody
//					: BodyType.DynamicBody);
//			MakeRectFixture(body, value.getBounds().width,
//					value.getBounds().height, 1f, 0f);
//			return body;
//		}
//
//		return createBody(value.getBounds().x, value.getBounds().y,
//				value.getBounds().width, value.getBounds().height, sensor);
//	}

	private Body createBodyFromBodyLoader(String path, String loaderName,
			float x, float y, float scale) {
		// 0. Create a loader for the file saved from the editor.
		FileHandle file = Gdx.files.internal(path);
		Gdx.app.log(TAG, file.readString());

		BodyEditorLoader loader = new BodyEditorLoader(file);

		// 1. Create a BodyDef, as usual.
		BodyDef bd = new BodyDef();
		bd.position.set(0, 0);
		bd.type = BodyType.DynamicBody;
		bd.fixedRotation = true;
		bd.position.set(ConvertToBox(x), ConvertToBox(y));

		// 2. Create a FixtureDef, as usual.
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		// fd.friction = 0.5f;
		// fd.restitution = 0f;

		// 3. Create a Body, as usual.
		Body body = world.createBody(bd);

		// 4. Create the body fixture automatically by using the loader.
		loader.attachFixture(body, loaderName, fd, scale);

//		// shape definition for main fixture
//		PolygonShape polygonShape = new PolygonShape();
//
//		// fixture definition
//		FixtureDef myFixtureDef = new FixtureDef();
//		myFixtureDef.shape = polygonShape;
//		myFixtureDef.density = 1;
//
//		// add foot sensor fixture
//		polygonShape.setAsBox(scale * 0.4f, scale * 0.1f, new Vector2(
//				scale * 0.5f, -0.02f), 0);
//		myFixtureDef.isSensor = true;
//		Fixture footSensorFixture = body.createFixture(myFixtureDef);
//		footSensorFixture.setUserData("Sensor");

		return body;
	}
	
	public void attachSensorToBody(Body body, float scale, SensorCollisionListener userData){
		PolygonShape polygonShape = new PolygonShape();
		// add foot sensor fixture
		polygonShape.setAsBox(scale * 0.4f, scale * 0.1f, new Vector2(
				scale * 0.5f, -0.02f), 0);
		
		FixtureDef myFixtureDef = new FixtureDef();
		myFixtureDef.shape = polygonShape;
		myFixtureDef.density = 1;
		myFixtureDef.isSensor = true;
		
		Fixture footSensorFixture = body.createFixture(myFixtureDef);
		footSensorFixture.setUserData(userData);
	}

	private Body CreateBody(World world, Vector2 pos, float angle, BodyType type) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = type;
		bodyDef.position.set(ConvertToBox(pos.x), ConvertToBox(pos.y));
		bodyDef.fixedRotation = true;

		return world.createBody(bodyDef);
	}

	private void MakeRectFixture(Body body, float width, float height,
			float density, float restitution) {
		PolygonShape bodyShape = new PolygonShape();

		float w = ConvertToBox(width / 2f);
		float h = ConvertToBox(height / 2f);
//		bodyShape.setAsBox(w, h);
		bodyShape.setAsBox(h, w, new Vector2(h, w), 0f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = restitution;
		fixtureDef.shape = bodyShape;
		body.createFixture(fixtureDef);

		bodyShape.dispose();

	}

	private Body createBody(float x, float y, float w, float h, String sensor) {
		// body definition
		BodyDef myBodyDef = new BodyDef();
		myBodyDef.type = BodyType.DynamicBody;
		myBodyDef.fixedRotation = true;
		myBodyDef.position.set(x, y);

		// create dynamic body
		Body body = world.createBody(myBodyDef);

		// shape definition for main fixture
		PolygonShape polygonShape = new PolygonShape();
//		polygonShape.setAsBox(w * 0.5f, h * 0.5f); // a 2x4 rectangle
		polygonShape.setAsBox(h * 0.5f, w * 0.5f, new Vector2(h, w), 0f);

		// fixture definition
		FixtureDef myFixtureDef = new FixtureDef();
		myFixtureDef.shape = polygonShape;
		myFixtureDef.density = 1;

		// add main fixture
		body.createFixture(myFixtureDef);

		// add foot sensor fixture
		polygonShape.setAsBox(w * 0.2f, h * 0.2f, new Vector2(0, -h * 0.5f), 0);
		myFixtureDef.isSensor = true;
		Fixture footSensorFixture = body.createFixture(myFixtureDef);
		footSensorFixture.setUserData(sensor);

		return body;
	}

	private void MakeCircleFixture(Body body, float radius, BodyDef.BodyType bodyType,
			float density, float restitution, Vector2 pos, float angle) {

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.restitution = restitution;
		fixtureDef.shape = new CircleShape();
		fixtureDef.shape.setRadius(ConvertToBox(radius));

		body.createFixture(fixtureDef);
		fixtureDef.shape.dispose();
	}

	private static final float BOX_STEP = 1 / 60f;
	private static final int BOX_VELOCITY_ITERATIONS = 6;
	private static final int BOX_POSITION_ITERATIONS = 2;
	private float accumulator;

	public void Update(float dt) {
		// accumulator+=dt;
		// while(accumulator>BOX_STEP){
		// world.step(BOX_STEP,BOX_VELOCITY_ITERATIONS,BOX_POSITION_ITERATIONS);
		// accumulator-=BOX_STEP;
		// }
		world.step(dt, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
		
		world.getBodies(bodies);
		
		for(Body body : bodies){
			if(body.getType() == BodyType.DynamicBody){
				GameObject obj = (GameObject)body.getUserData();
				obj.getBounds().x = body.getPosition().x;
				obj.getBounds().y = body.getPosition().y;
			}
		}

		if (debug)
			boxDebugRenderer.render(world, camera.combined);
	}

	public boolean isDebug() {
		return debug;
	}

}