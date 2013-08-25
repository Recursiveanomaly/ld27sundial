package com.recursiveanomaly.ld27.components;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;

public class BodyComponent extends Component 
{
	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;
	
//	final short CATEGORY_PLAYER = 0x0001;
//	final short CATEGORY_ENEMY = 0x0002;
//	final short CATEGORY_LIGHT = 0x0004; 
//	
//	final short MASK_PLAYER = CATEGORY_ENEMY | CATEGORY_LIGHT;
//	final short MASK_ENEMY = CATEGORY_PLAYER | CATEGORY_LIGHT;
//	final short MASK_LIGHT = -1;
	
	public Body m_Body;
	public Skeleton m_Skeleton = null;
	public String m_BoneName;
	
	private World m_World;
	private RayHandler m_RayHandler;
	private Entity m_Entity;
	public PointLight m_Light;
	
	public BodyComponent(Entity entity, World world, RayHandler rayHandler, Vector2 position, float radius, BodyType bodyType, short groupFilter, int pointLight)
	{
		m_Entity = entity;
		m_World = world;
		m_RayHandler = rayHandler;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(position);
		
		m_Body = world.createBody(bodyDef);
		m_Body.setUserData(m_Entity);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.5f;
		fixtureDef.filter.groupIndex = groupFilter;
		fixtureDef.restitution = 0.0f; // Make it bounce a little bit

		Fixture fixture = m_Body.createFixture(fixtureDef);
		fixture.setUserData(m_Entity);
		
		circle.dispose();

		if(pointLight == 1)
		{
			m_Light = new PointLight(rayHandler, 128, new Color(0.3f,0.3f,0.25f,1.0f), 450 * WORLD_TO_BOX, position.x, position.y);
			m_Light.attachToBody(m_Body, 0, 0);
			Filter contactFilter = new Filter();
			contactFilter.groupIndex = -1;
			m_Light.setContactFilter(contactFilter);
			m_Light.setSoftnessLenght(2.5f);
		}
		else if(pointLight == 2)
		{
			m_Light = new PointLight(rayHandler, 64, new Color(0.4f,0.2f,0.4f,.75f), 200 * WORLD_TO_BOX, position.x, position.y);
			m_Light.attachToBody(m_Body, 0, 0);
			m_Light.setSoftnessLenght(1.5f);			
		}
		else if(pointLight == 3)
		{
			m_Light = new PointLight(m_RayHandler, 64, new Color(0.4f,0.2f,0.2f,.75f), 15 * WORLD_TO_BOX, position.x, position.y);
			m_Light.attachToBody(m_Body, 0, 0);
			m_Light.setSoftnessLenght(1.5f);
		}
		else if(pointLight == 4)
		{
			m_Light = new PointLight(rayHandler, 128, new Color(0.1f,0.05f,0.05f,.75f), 400 * WORLD_TO_BOX, position.x, position.y);
			m_Light.attachToBody(m_Body, 0, 0);
			m_Light.setSoftnessLenght(2.5f);	
			m_Light.setStaticLight(true);
		}
	}
	
	public BodyComponent(Skeleton skeleton, String boneName, Entity entity, World world, RayHandler rayHandler, float radius, BodyType bodyType, short groupFilter, int pointLight)
	{
		m_Entity = entity;
		m_World = world;
		m_RayHandler = rayHandler;
		m_BoneName = boneName;
		m_Skeleton = skeleton;

		Vector2 position = new Vector2(0,0);
		Bone weaponBone = m_Skeleton.findBone(boneName);
		position.x = weaponBone.getWorldX();
		position.y = weaponBone.getWorldY();		
		position.scl(WORLD_TO_BOX);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(position);
		
		m_Body = world.createBody(bodyDef);
		m_Body.setUserData(m_Entity);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 1.0f; 
		fixtureDef.friction = 1.0f;
		fixtureDef.filter.groupIndex = groupFilter;
		fixtureDef.restitution = 0.0f; // Make it bounce a little bit

		Fixture fixture = m_Body.createFixture(fixtureDef);
		fixture.setUserData(m_Entity);

		if(pointLight == 1)
		{
			m_Light = new PointLight(rayHandler, 128, new Color(0.3f,0.3f,0.25f,1f), 450 * WORLD_TO_BOX, position.x, position.y);
			m_Light.attachToBody(m_Body, 0, 0);
			Filter contactFilter = new Filter();
			contactFilter.groupIndex = -1;
			m_Light.setContactFilter(contactFilter);
			m_Light.setSoftnessLenght(2.5f);
		}
		else if(pointLight == 2)
		{
			m_Light = new PointLight(rayHandler, 64, new Color(0.4f,0.2f,0.2f,1f), 200 * WORLD_TO_BOX, position.x, position.y);
			m_Light.attachToBody(m_Body, 0, 0);
			m_Light.setSoftnessLenght(1.5f);			
		}
		else if(pointLight == 3)
		{
			m_Light = new PointLight(m_RayHandler, 64, new Color(0.4f,0.2f,0.2f,1f), 15 * WORLD_TO_BOX, position.x, position.y);
			m_Light.attachToBody(m_Body, 0, 0);
			m_Light.setSoftnessLenght(1.5f);	
			m_Light.setActive(false);
		}

		circle.dispose();
	}
	
	public void RemoveFromPhysics()
	{
		m_World.destroyBody(m_Body);
		if(m_Light != null) m_Light.remove();
	}
}
