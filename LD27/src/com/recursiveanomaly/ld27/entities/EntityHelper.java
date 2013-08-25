package com.recursiveanomaly.ld27.entities;

import box2dLight.RayHandler;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.recursiveanomaly.ld27.components.BodyComponent;
import com.recursiveanomaly.ld27.components.DamageComponent;
import com.recursiveanomaly.ld27.components.HealthComponent;
import com.recursiveanomaly.ld27.components.PickUpComponent;
import com.recursiveanomaly.ld27.components.PositionComponent;
import com.recursiveanomaly.ld27.components.RotationComponent;
import com.recursiveanomaly.ld27.components.SpineComponent;
import com.recursiveanomaly.ld27.components.SteeringComponent;
import com.recursiveanomaly.ld27.screens.GameScreen;

public class EntityHelper 
{
	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;

	public static void LoadObjects(GameScreen gameScreen, com.artemis.World m_ArtemisWorld, TiledMap m_TiledMap, com.badlogic.gdx.physics.box2d.World m_PhysicsWorld, RayHandler m_RayHandler) 
	{
		//add enemies from tiled map editor
		for(int i = 0; i < m_TiledMap.getLayers().getCount(); i++)
		{
			for(int j = 0; j < m_TiledMap.getLayers().get(i).getObjects().getCount(); j++)
			{
				if(m_TiledMap.getLayers().get(i).getObjects().get(j).getName().contentEquals("spider"))
				{
					//spawn a spider
					Entity newEntity = m_ArtemisWorld.createEntity();
					
					MapProperties props = m_TiledMap.getLayers().get(i).getObjects().get(j).getProperties();
					
					//add components
					Vector2 pos = new Vector2(0, 0);
					pos.x = props.get("x", int.class);
					pos.y = props.get("y", int.class);
					newEntity.addComponent(new PositionComponent(pos.x, pos.y));
					newEntity.addComponent(new RotationComponent());
					newEntity.addComponent(new SpineComponent("entities/spider", 2, 0.5f, 180));
					newEntity.addComponent(new BodyComponent(newEntity, m_PhysicsWorld, m_RayHandler, pos.scl(WORLD_TO_BOX), 32 * WORLD_TO_BOX, BodyType.DynamicBody, (short) 0, 0));
					newEntity.addComponent(new HealthComponent(3, false, false));
					newEntity.addComponent(new DamageComponent(1));
					newEntity.addComponent(new SteeringComponent());
					
					//add to world
					newEntity.addToWorld();
				}
				else if(m_TiledMap.getLayers().get(i).getObjects().get(j).getName().contentEquals("bossSpider"))
				{
					//spawn a spider
					Entity newEntity = m_ArtemisWorld.createEntity();
					
					MapProperties props = m_TiledMap.getLayers().get(i).getObjects().get(j).getProperties();
					
					//add components
					Vector2 pos = new Vector2(0, 0);
					pos.x = props.get("x", int.class);
					pos.y = props.get("y", int.class);
					newEntity.addComponent(new PositionComponent(pos.x, pos.y));
					newEntity.addComponent(new RotationComponent());
					newEntity.addComponent(new SpineComponent("entities/spider", 2, 1.5f, 180));
					newEntity.addComponent(new BodyComponent(newEntity, m_PhysicsWorld, m_RayHandler, pos.scl(WORLD_TO_BOX), 32 * WORLD_TO_BOX, BodyType.DynamicBody, (short) 0, 0));
					newEntity.addComponent(new HealthComponent(3, false, false));
					newEntity.addComponent(new DamageComponent(1));
					newEntity.addComponent(new SteeringComponent());
					
					//add to world
					newEntity.addToWorld();
				}
				else if(m_TiledMap.getLayers().get(i).getObjects().get(j).getName().contentEquals("ghost"))
				{
					Entity newEntity = m_ArtemisWorld.createEntity();
					
					MapProperties props = m_TiledMap.getLayers().get(i).getObjects().get(j).getProperties();
					
					//add components
					Vector2 pos = new Vector2(0, 0);
					pos.x = props.get("x", int.class);
					pos.y = props.get("y", int.class);
					newEntity.addComponent(new PositionComponent(pos.x, pos.y));
					newEntity.addComponent(new RotationComponent());
					newEntity.addComponent(new SpineComponent("entities/ghost", 2, 1.0f, 180));
					newEntity.addComponent(new BodyComponent(newEntity, m_PhysicsWorld, m_RayHandler, pos.scl(WORLD_TO_BOX), 32 * WORLD_TO_BOX, BodyType.DynamicBody, (short) 0, 0));
					newEntity.addComponent(new HealthComponent(1, true, false));
					newEntity.addComponent(new DamageComponent(1));
					newEntity.addComponent(new SteeringComponent());
					
					//add to world
					newEntity.addToWorld();
				}
				else if(m_TiledMap.getLayers().get(i).getObjects().get(j).getName().contentEquals("bossGhost"))
				{
					Entity newEntity = m_ArtemisWorld.createEntity();
					
					MapProperties props = m_TiledMap.getLayers().get(i).getObjects().get(j).getProperties();
					
					//add components
					Vector2 pos = new Vector2(0, 0);
					pos.x = props.get("x", int.class);
					pos.y = props.get("y", int.class);
					newEntity.addComponent(new PositionComponent(pos.x, pos.y));
					newEntity.addComponent(new RotationComponent());
					SpineComponent spineComponent = new SpineComponent("entities/ghost", 2, 3.0f, 180);
					spineComponent.m_Skeleton.getColor().set(1, 0.5f, 0.5f, 0.7f);
					newEntity.addComponent(spineComponent);
					newEntity.addComponent(new BodyComponent(newEntity, m_PhysicsWorld, m_RayHandler, pos.scl(WORLD_TO_BOX), 32 * WORLD_TO_BOX, BodyType.DynamicBody, (short) 0, 0));
					newEntity.addComponent(new HealthComponent(3, true, false));
					newEntity.addComponent(new DamageComponent(1));
					newEntity.addComponent(new SteeringComponent());
					
					//add to world
					newEntity.addToWorld();
				}
				else if(m_TiledMap.getLayers().get(i).getObjects().get(j).getName().contentEquals("fire"))
				{
					Entity newEntity = m_ArtemisWorld.createEntity();
					
					MapProperties props = m_TiledMap.getLayers().get(i).getObjects().get(j).getProperties();
					
					//add components
					Vector2 pos = new Vector2(0, 0);
					pos.x = props.get("x", int.class);
					pos.y = props.get("y", int.class);
					newEntity.addComponent(new PositionComponent(pos.x, pos.y));
					newEntity.addComponent(new RotationComponent());
					newEntity.addComponent(new SpineComponent("entities/fire", 3, 1.0f, 180));
					BodyComponent bodyComponent = new BodyComponent(newEntity, m_PhysicsWorld, m_RayHandler, pos.scl(WORLD_TO_BOX), 16 * WORLD_TO_BOX, BodyType.StaticBody, (short) 0, 4);
					newEntity.addComponent(bodyComponent);
					newEntity.addComponent(new PickUpComponent(3));	
					
					//add spectral sensor
					CircleShape circle = new CircleShape();
					circle.setRadius(128 * WORLD_TO_BOX);		
					// Create a fixture definition to apply our shape to
					FixtureDef fixtureDef = new FixtureDef();
					fixtureDef.shape = circle;
					fixtureDef.isSensor = true;
					Fixture fixture = bodyComponent.m_Body.createFixture(fixtureDef);
					fixture.setUserData(Boolean.TRUE);
					circle.dispose();
					
					//add to world
					newEntity.addToWorld();
				}
				else if(m_TiledMap.getLayers().get(i).getObjects().get(j).getName().contentEquals("watch") && !gameScreen.m_Settings.m_WatchFound)
				{
					Entity newEntity = m_ArtemisWorld.createEntity();
					
					MapProperties props = m_TiledMap.getLayers().get(i).getObjects().get(j).getProperties();
					
					//add components
					Vector2 pos = new Vector2(0, 0);
					pos.x = props.get("x", int.class);
					pos.y = props.get("y", int.class);
					newEntity.addComponent(new PositionComponent(pos.x, pos.y));
					newEntity.addComponent(new RotationComponent());
					newEntity.addComponent(new SpineComponent("map/watch", 3, 0.15f, 180));
					newEntity.addComponent(new BodyComponent(newEntity, m_PhysicsWorld, m_RayHandler, pos.scl(WORLD_TO_BOX), 16 * WORLD_TO_BOX, BodyType.StaticBody, (short) 0, 2));
					newEntity.addComponent(new PickUpComponent(1));
					newEntity.addComponent(new HealthComponent(1, false, true));
					
					//add to world
					newEntity.addToWorld();
				}
				else if(m_TiledMap.getLayers().get(i).getObjects().get(j).getName().contentEquals("light") && !gameScreen.m_Settings.m_WeaponLightFound)
				{
					Entity newEntity = m_ArtemisWorld.createEntity();
					
					MapProperties props = m_TiledMap.getLayers().get(i).getObjects().get(j).getProperties();
					
					//add components
					Vector2 pos = new Vector2(0, 0);
					pos.x = props.get("x", int.class);
					pos.y = props.get("y", int.class);
					newEntity.addComponent(new PositionComponent(pos.x, pos.y));
					newEntity.addComponent(new RotationComponent());
					newEntity.addComponent(new BodyComponent(newEntity, m_PhysicsWorld, m_RayHandler, pos.scl(WORLD_TO_BOX), 16 * WORLD_TO_BOX, BodyType.StaticBody, (short) 0, 3));
					newEntity.addComponent(new PickUpComponent(2));
					newEntity.addComponent(new HealthComponent(1, false, true));
					
					//add to world
					newEntity.addToWorld();
				}
				else if(m_TiledMap.getLayers().get(i).getObjects().get(j).getName().contentEquals("sword") && !gameScreen.m_Settings.m_WeaponFound)
				{
					Entity newEntity = m_ArtemisWorld.createEntity();
					
					MapProperties props = m_TiledMap.getLayers().get(i).getObjects().get(j).getProperties();
					
					//add components
					Vector2 pos = new Vector2(0, 0);
					pos.x = props.get("x", int.class);
					pos.y = props.get("y", int.class);
					newEntity.addComponent(new PositionComponent(pos.x, pos.y));
					newEntity.addComponent(new RotationComponent());
					newEntity.addComponent(new SpineComponent("map/sword", 3, 1.0f, 180));
					newEntity.addComponent(new BodyComponent(newEntity, m_PhysicsWorld, m_RayHandler, pos.scl(WORLD_TO_BOX), 16 * WORLD_TO_BOX, BodyType.StaticBody, (short) 0, 2));
					newEntity.addComponent(new PickUpComponent(4));
					newEntity.addComponent(new HealthComponent(1, false, true));
					
					//add to world
					newEntity.addToWorld();
				}
			}
		}
	}

}
