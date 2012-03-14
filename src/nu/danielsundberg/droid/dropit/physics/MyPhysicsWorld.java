package nu.danielsundberg.droid.dropit.physics;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import nu.danielsundberg.droid.dropit.DropItEngine;
import nu.danielsundberg.droid.dropit.DropItEngine.PlaceHolderObject;
import nu.danielsundberg.droid.dropit.rendering.object.MyRenderableObject;
import nu.danielsundberg.droid.dropit.rendering.object.TexturedBox;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class MyPhysicsWorld implements ContactListener {
	public float timestep = 1/30f;
	public int velocityIterations = 10;
	public int positionIterations = 10;
	public World world;
	private boolean pause = false;
	
	protected List<MyRenderableObject> myObjects = new CopyOnWriteArrayList<MyRenderableObject>();

	private DropItEngine engine;
	
	
	public void setParent(DropItEngine parent) {
		this.engine = parent;
	}

	public void create() {
		Vector2 gravity = new Vector2((float) 0.0, (float) -9.82);
		world = new World(gravity, pause);
		world.setContactListener(this);
        initBounds();
	}

	private void initBounds() {
		initBottom();
		initTop();
		initLeft();
		initRight();
	}

	private void initBottom() {
		PolygonShape shapeDef = new PolygonShape();
		shapeDef.setAsBox((float) 150.0f, (float) 1f);
        
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shapeDef;
        fixture.density = 1f;
        fixture.friction = 0.3f;
        fixture.restitution = 0f;
         
        BodyDef bodyDef = new BodyDef();  
        bodyDef.position.set(new Vector2((float) 0.0, (float) -80.0));
        bodyDef.allowSleep = true;
        
        Body body = world.createBody(bodyDef);
        body.createFixture(fixture);
        body.setType(BodyDef.BodyType.StaticBody);

        body.setActive(true);
	}
	
	private void initTop() {
		PolygonShape shapeDef = new PolygonShape();
		shapeDef.setAsBox((float) 150.0f, (float) 1f);
        
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shapeDef;
        fixture.density = 1f;
        fixture.friction = 0.3f;
        fixture.restitution = 0f;
         
        BodyDef bodyDef = new BodyDef();  
        bodyDef.position.set(new Vector2((float) 0.0, (float) 170.0));
        bodyDef.allowSleep = true;
        
        Body body = world.createBody(bodyDef);
        body.createFixture(fixture);
        body.setType(BodyDef.BodyType.StaticBody);
        body.setActive(true);		
	}
	
	private void initLeft() {
		PolygonShape shapeDef = new PolygonShape();
		shapeDef.setAsBox((float) 1.0f, (float) 200f);
        
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shapeDef;
        fixture.density = 1f;
        fixture.friction = 0.3f;
        fixture.restitution = 0f;
         
        BodyDef bodyDef = new BodyDef();  
        bodyDef.position.set(new Vector2((float) -1.0, (float) 0.0));
        bodyDef.allowSleep = true;
        
        Body body = world.createBody(bodyDef);
        body.createFixture(fixture);
        body.setType(BodyDef.BodyType.StaticBody);

        body.setActive(true);
	}
	
	private void initRight() {
		PolygonShape shapeDef = new PolygonShape();
		shapeDef.setAsBox((float) 1.0f, (float) 200f);
        
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shapeDef;
        fixture.density = 1f;
        fixture.friction = 0.3f;
        fixture.restitution = 0f;
         
        BodyDef bodyDef = new BodyDef();  
        bodyDef.position.set(new Vector2((float) 150.0, (float) 0.0));
        bodyDef.allowSleep = true;
        
        Body body = world.createBody(bodyDef);
        body.createFixture(fixture);
        body.setType(BodyDef.BodyType.StaticBody);

        body.setActive(true);
	}

	public synchronized void addObject(MyRenderableObject object) {
		myObjects.add(object);  
	}
	public synchronized void removeObject(MyRenderableObject object) {
		myObjects.remove(object);
	}

	public void update() {
		for(PlaceHolderObject tmp : engine.toBeAdded){
			
			PolygonShape lineShapeDef = new PolygonShape();
			lineShapeDef.setAsBox(3f, 3f, new Vector2(0f, 0f), 0f);
	    	
	    	FixtureDef fixture = new FixtureDef();
	    	fixture.shape = lineShapeDef;
	    	fixture.density = 1f;
	    	fixture.friction = 0.3f;
	    	fixture.restitution = 0f;
	    	
			BodyDef newline = new BodyDef(); 	
			newline.position.set(new Vector2(tmp.sx, tmp.sy));    	
	    	
			
			newline.allowSleep = true;
	    	
	    	Body newBody = world.createBody(newline);
	    	newBody.setType(BodyDef.BodyType.DynamicBody);
	    	newBody.createFixture(fixture);
	    	newBody.setActive(true);
	    	
	    	MyRenderableObject box = new TexturedBox(tmp.sx, tmp.sy, newline.angle, tmp.texture, tmp.u1, tmp.v1, tmp.u2, tmp.v2);
	    	
	    	engine.actors.add(new MyPhysicsActor(box, newBody));
	    	addObject(box);
	    	engine.toBeAdded.remove(tmp);
		}
		
		world.step(timestep, velocityIterations, positionIterations);		
	}

	public void pause(boolean b) {
		this.pause = b;
	}

	@Override
	public void beginContact(Contact c) {
		float max = c.getFixtureA().getBody().getLinearVelocity().len()+c.getFixtureB().getBody().getLinearVelocity().len();
		float result = c.getFixtureA().getBody().getLinearVelocity().add(c.getFixtureB().getBody().getLinearVelocity()).len();
		float impact = (max/result)-1;
		if(Math.random()>=Math.random()){
			engine.bounce.play(impact);
		} else if(Math.random()>=Math.random()) {
			engine.bounce2.play(impact);
		} else {
			engine.bounce3.play(impact);
		}
	}

	@Override
	public void endContact(Contact arg0) {}

	public void updateGravity(float f, float g) {
		world.setGravity(new Vector2(f,g));
	}    

}
