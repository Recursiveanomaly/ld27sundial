package com.recursiveanomaly.ld27.map;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.TimeUtils;
import com.recursiveanomaly.ld27.components.BodyComponent;
import com.recursiveanomaly.ld27.components.DamageComponent;
import com.recursiveanomaly.ld27.components.HealthComponent;
import com.recursiveanomaly.ld27.components.PickUpComponent;
import com.recursiveanomaly.ld27.components.PlayerComponent;
import com.recursiveanomaly.ld27.components.PositionComponent;
import com.recursiveanomaly.ld27.components.SteeringComponent;

public class GameContactListener implements ContactListener 
{
	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;
	
	Sound m_Ghost;
	
	BaseLevel m_Level;
	public GameContactListener(BaseLevel level)
	{
		m_Level = level;
		m_Ghost = Gdx.audio.newSound(Gdx.files.internal("music/ghost.mp3"));
	}
	
	@Override
	public void beginContact(Contact contact) 
	{
		// TODO Auto-generated method stub
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		float m_CollisionForceScale = 15f;
		
		if(fixtureA.getUserData() instanceof Entity && fixtureB.getUserData() instanceof Entity)
		{
			Entity objA = (Entity) fixtureA.getUserData();
			Entity objB = (Entity) fixtureB.getUserData();
			
			HealthComponent healthComponentA = objA.getComponent(HealthComponent.class);
			HealthComponent healthComponentB = objB.getComponent(HealthComponent.class);

			DamageComponent damageComponentA = objA.getComponent(DamageComponent.class);
			DamageComponent damageComponentB = objB.getComponent(DamageComponent.class);
			
			//check for damages
			if(healthComponentA != null && damageComponentB != null 
					&& (!healthComponentA.m_Spectral || damageComponentB.m_LightDamage)
					&& healthComponentA.m_LastHitTime < TimeUtils.millis() - healthComponentA.m_HitImmuneTime)
			{
				healthComponentA.m_CurrentHealth -= damageComponentB.m_Damage;
				healthComponentA.m_LastHitTime = TimeUtils.millis();
				BodyComponent bodyComponentA = objA.getComponent(BodyComponent.class);
				if(bodyComponentA != null)
				{
					PositionComponent positionComponentA = objA.getComponent(PositionComponent.class);
					SteeringComponent steeringComponentA = objA.getComponent(SteeringComponent.class);
					PositionComponent positionComponentB = objB.getComponent(PositionComponent.class);
					
					if(positionComponentA != null && positionComponentB != null)
					{
						Vector2 force = new Vector2(0,0);
						force.x = positionComponentA.m_X - positionComponentB.m_X;
						force.y = positionComponentA.m_Y - positionComponentB.m_Y;
						force.nor();
						force.scl(m_CollisionForceScale);
						bodyComponentA.m_Body.applyForceToCenter(force, true);
					}
					if(steeringComponentA != null)
					{
						steeringComponentA.m_TimeLeft = steeringComponentA.m_ChaseTime / 5;
						steeringComponentA.m_Chase = false;
					}
					
					PlayerComponent playerComponentA = objA.getComponent(PlayerComponent.class);
					if(playerComponentA != null)
					{
						m_Level.BreakVials(damageComponentB.m_Damage);
					}
				}
			}
			
			//check for reverse damages
			if(healthComponentB != null && damageComponentA != null 
					&& (!healthComponentB.m_Spectral || damageComponentA.m_LightDamage)
					&& healthComponentB.m_LastHitTime < TimeUtils.millis() - healthComponentB.m_HitImmuneTime)
			{
				healthComponentB.m_CurrentHealth -= damageComponentA.m_Damage;
				healthComponentB.m_LastHitTime = TimeUtils.millis();
				BodyComponent bodyComponentB = objB.getComponent(BodyComponent.class);
				if(bodyComponentB != null)
				{
					PositionComponent positionComponentA = objA.getComponent(PositionComponent.class);
					PositionComponent positionComponentB = objB.getComponent(PositionComponent.class);
					SteeringComponent steeringComponentB = objB.getComponent(SteeringComponent.class);
					if(positionComponentA != null && positionComponentB != null)
					{
						Vector2 force = new Vector2(0,0);
						force.x = -positionComponentA.m_X + positionComponentB.m_X;
						force.y = -positionComponentA.m_Y + positionComponentB.m_Y;
						force.nor();
						force.scl(m_CollisionForceScale);
						bodyComponentB.m_Body.applyForceToCenter(force, true);
					}
					if(steeringComponentB != null)
					{
						steeringComponentB.m_TimeLeft = steeringComponentB.m_ChaseTime / 5;
						steeringComponentB.m_Chase = false;
					}

					PlayerComponent playerComponentB = objB.getComponent(PlayerComponent.class);
					if(playerComponentB != null)
					{
						m_Level.BreakVials(damageComponentA.m_Damage);
					}
				}
			}
			
			//check for good stuff
			PickUpComponent pickUpComponentA = objA.getComponent(PickUpComponent.class);
			if(pickUpComponentA != null)
			{
				PlayerComponent playerComponentB = objB.getComponent(PlayerComponent.class);
				if(playerComponentB != null)
				{
					//woot woot
					if(pickUpComponentA.m_Type == 1)
					{
						m_Level.ActivateWatch();
						if(healthComponentA != null) healthComponentA.RemoveSpecial();
						return;
					}
					else if(pickUpComponentA.m_Type == 2)
					{
						m_Level.ActivateSwordLight();
						if(healthComponentA != null) healthComponentA.RemoveSpecial();
						return;
					}
					else if(pickUpComponentA.m_Type == 3)
					{
						m_Level.WindWatch();
					}
					else if(pickUpComponentA.m_Type == 4)
					{
						m_Level.ActivateSword();
						if(healthComponentA != null) healthComponentA.RemoveSpecial();
						return;
					}
					
				}
			}
			//check for reverse good stuff
			PickUpComponent pickUpComponentB = objB.getComponent(PickUpComponent.class);
			if(pickUpComponentB != null)
			{
				PlayerComponent playerComponentA = objA.getComponent(PlayerComponent.class);
				if(playerComponentA != null)
				{
					//woot woot
					if(pickUpComponentB.m_Type == 1)
					{
						m_Level.ActivateWatch();
						if(healthComponentB != null) healthComponentB.RemoveSpecial();
						return;
					}
					else if(pickUpComponentB.m_Type == 2)
					{
						m_Level.ActivateSwordLight();
						if(healthComponentB != null) healthComponentB.RemoveSpecial();
						return;
					}
					else if(pickUpComponentA.m_Type == 3)
					{
						m_Level.WindWatch();
					}
				}
			}
		}
		//check for light hitting spectral
		if(fixtureA.isSensor() && fixtureA.getUserData() instanceof Boolean && fixtureB.getUserData() instanceof Entity)
		{
			Boolean isSpectralSensor = (Boolean)fixtureA.getUserData();
			Entity objB = (Entity) fixtureB.getUserData();
			HealthComponent healthComponentB = objB.getComponent(HealthComponent.class);
			
			if(isSpectralSensor && healthComponentB != null && healthComponentB.m_Spectral)
			{
				SteeringComponent steeringComponentB = objB.getComponent(SteeringComponent.class);
				if(steeringComponentB != null)
				{
					steeringComponentB.m_Target = fixtureA.getBody();
					steeringComponentB.m_TimeLeft = steeringComponentB.m_ChaseTime;
					steeringComponentB.m_Chase = false;
				}
			}
			else if(!isSpectralSensor)
			{
				SteeringComponent steeringComponentB = objB.getComponent(SteeringComponent.class);
				if(steeringComponentB != null && steeringComponentB.m_TimeLeft <= 0)
				{
					if(healthComponentB.m_Spectral) m_Ghost.play(m_Level.m_GameScreen.m_Settings.m_GameSoundVolume);
					steeringComponentB.m_Target = fixtureA.getBody();
					steeringComponentB.m_TimeLeft = steeringComponentB.m_ChaseTime;
					steeringComponentB.m_Chase = true;
				}
			}
		}
		//reverse check for light hitting spectral
		if(fixtureB.isSensor() && fixtureB.getUserData() instanceof Boolean && fixtureA.getUserData() instanceof Entity)
		{
			Boolean isSpectralSensor = (Boolean)fixtureB.getUserData();
			Entity objA = (Entity) fixtureA.getUserData();
			HealthComponent healthComponentA = objA.getComponent(HealthComponent.class);
			if(isSpectralSensor && healthComponentA != null && healthComponentA.m_Spectral)
			{
				SteeringComponent steeringComponentA = objA.getComponent(SteeringComponent.class);
				if(steeringComponentA != null)
				{
					steeringComponentA.m_Target = fixtureB.getBody();
					steeringComponentA.m_TimeLeft = steeringComponentA.m_ChaseTime;
					steeringComponentA.m_Chase = false;
				}
			}
			else if(!isSpectralSensor)
			{
				SteeringComponent steeringComponentA = objA.getComponent(SteeringComponent.class);
				if(steeringComponentA != null && steeringComponentA.m_TimeLeft <= 0)
				{
					if(healthComponentA.m_Spectral) m_Ghost.play(m_Level.m_GameScreen.m_Settings.m_GameSoundVolume);
					steeringComponentA.m_Target = fixtureB.getBody();
					steeringComponentA.m_TimeLeft = steeringComponentA.m_ChaseTime;
					steeringComponentA.m_Chase = true;
				}
			}
		}
		
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
