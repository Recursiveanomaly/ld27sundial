package com.recursiveanomaly.ld27.entities;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.attachments.Attachment;
import com.recursiveanomaly.ld27.components.BodyComponent;
import com.recursiveanomaly.ld27.components.DamageComponent;
import com.recursiveanomaly.ld27.components.HealthComponent;
import com.recursiveanomaly.ld27.components.PlayerComponent;
import com.recursiveanomaly.ld27.components.PositionComponent;
import com.recursiveanomaly.ld27.components.RotationComponent;
import com.recursiveanomaly.ld27.components.SpineComponent;
import com.recursiveanomaly.ld27.map.BaseLevel;

public class PlayerEntity implements IEntity
{
	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;
	
	public Entity m_Entity;
	public Entity m_WeaponEntity;
	
	public Fixture m_LightDistanceSensor;
	
	@Override
	public void AddToWorld(BaseLevel level) 
	{
		//create entity
		m_Entity = level.m_ArtemisWorld.createEntity();
		
		//add components
		Vector2 pos = new Vector2((16 * 64) / 2 - 32, (128 * 64) - 512);
		m_Entity.addComponent(new PositionComponent(pos.x, pos.y));
		m_Entity.addComponent(new RotationComponent());
		SpineComponent spineComponent = new SpineComponent("entities/player", 1, 0.5f, 0);
		m_Entity.addComponent(spineComponent);
		m_Entity.addComponent(new PlayerComponent());
		m_Entity.addComponent(new HealthComponent(4, false, false));
		
		BodyComponent bodyComponent = new BodyComponent(m_Entity, level.m_PhysicsWorld, level.m_RayHandler, pos.scl(WORLD_TO_BOX), 32 * WORLD_TO_BOX, BodyType.DynamicBody, (short) -1, 1);
		
		//create spectral sensor
		CircleShape spectralCircle = new CircleShape();
		spectralCircle.setRadius(0 * WORLD_TO_BOX);		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureSpectralDef = new FixtureDef();
		fixtureSpectralDef.shape = spectralCircle;
		fixtureSpectralDef.isSensor = true;
		m_LightDistanceSensor = bodyComponent.m_Body.createFixture(fixtureSpectralDef);
		m_LightDistanceSensor.setUserData(Boolean.TRUE);
		spectralCircle.dispose();
		
		//create chase sensor
		CircleShape chaseCircle = new CircleShape();
		chaseCircle.setRadius(200 * WORLD_TO_BOX);		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureChaseDef = new FixtureDef();
		fixtureChaseDef.shape = chaseCircle;
		fixtureChaseDef.isSensor = true;
		Fixture chaseFixture = bodyComponent.m_Body.createFixture(fixtureChaseDef);
		chaseFixture.setUserData(Boolean.FALSE);
		chaseCircle.dispose();
		
		m_Entity.addComponent(bodyComponent);	
		
		//add to world
		m_Entity.addToWorld();
		
		//weapon component
		m_WeaponEntity = level.m_ArtemisWorld.createEntity();
		m_WeaponEntity.addComponent(new PositionComponent(pos.x, pos.y));
		m_WeaponEntity.addComponent(new RotationComponent());
		m_WeaponEntity.addComponent(new BodyComponent(spineComponent.m_Skeleton, "swordTip", m_WeaponEntity, level.m_PhysicsWorld, level.m_RayHandler, 4 * WORLD_TO_BOX, BodyType.KinematicBody, (short) -1, 3));
		m_WeaponEntity.addComponent(new DamageComponent(0));
		m_WeaponEntity.addToWorld();
	}

	@Override
	public void DeleteFromWorld() {
		// TODO Auto-generated method stub
		m_Entity.deleteFromWorld();
		m_WeaponEntity.deleteFromWorld();
	}

	@Override
	public void ChangedIntWorld() {
		// TODO Auto-generated method stub
		m_Entity.changedInWorld();
		m_WeaponEntity.changedInWorld();
	}

	@Override
	public Entity GetEntity() {
		// TODO Auto-generated method stub
		return m_Entity;
	}
	
	public void ActivateWeapon()
	{
		SpineComponent spineComponent = m_Entity.getComponent(SpineComponent.class);

		if(spineComponent != null)
		{
			spineComponent.m_Skeleton.findSlot("sword").setAttachment(spineComponent.m_Skeleton.getAttachment("sword", "sword"));
		}
		
		BodyComponent bodyComponent = m_WeaponEntity.getComponent(BodyComponent.class);
		
		DamageComponent damageComponent = m_WeaponEntity.getComponent(DamageComponent.class);
		if(damageComponent != null)
		{
			damageComponent.m_Damage = 1;
		}
	}
	
	public void ActivateWeaponLight()
	{
		BodyComponent bodyComponent = m_WeaponEntity.getComponent(BodyComponent.class);
		bodyComponent.m_Light.setActive(true);
		
		DamageComponent damageComponent = m_WeaponEntity.getComponent(DamageComponent.class);
		if(damageComponent != null)
		{
			damageComponent.m_LightDamage = true;
		}
	}
}
