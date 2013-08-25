package com.recursiveanomaly.ld27.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.recursiveanomaly.ld27.components.BodyComponent;
import com.recursiveanomaly.ld27.components.PlayerComponent;
import com.recursiveanomaly.ld27.components.PositionComponent;
import com.recursiveanomaly.ld27.components.RotationComponent;
import com.recursiveanomaly.ld27.components.SpineComponent;
import com.recursiveanomaly.ld27.map.BaseLevel;

public class PlayerInputSystem extends EntityProcessingSystem 
{
	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;
	
	float m_AttackCooldown = 1.0f;
	float m_TimeSinceLastAttack = m_AttackCooldown;
	
	@Mapper ComponentMapper<PlayerComponent> mapperPlayerComponent;
	@Mapper ComponentMapper<PositionComponent> mapperPositionComponent;
	@Mapper ComponentMapper<RotationComponent> mapperRotationComponent;
	@Mapper ComponentMapper<BodyComponent> mapperBodyComponent;
	@Mapper ComponentMapper<SpineComponent> mapperSpineComponent;
	
	Music m_Steps;
	boolean m_StepsPlaying = false;
	Sound m_Attack;
	
	float m_Speed = 300.0f;

	float m_PreviousMouseX;
	float m_PreviousMouseY;
	
	BaseLevel m_Level;

	public PlayerInputSystem(BaseLevel level) 
	{
		super(Aspect.getAspectForAll(PlayerComponent.class, PositionComponent.class, RotationComponent.class, BodyComponent.class));

		m_Level = level;
		
		m_PreviousMouseX = Gdx.input.getX();
		m_PreviousMouseY = Gdx.input.getY();

		m_Steps = Gdx.audio.newMusic(Gdx.files.internal("music/steps.mp3"));
		m_Steps.setVolume(level.m_GameScreen.m_Settings.m_GameSoundVolume);
		m_Steps.setLooping(true);
		m_Attack = Gdx.audio.newSound(Gdx.files.internal("music/attack.mp3"));
	}
	
	@Override
	protected void process(Entity e) 
	{
		m_TimeSinceLastAttack += Gdx.graphics.getDeltaTime();
//		PositionComponent positionComponent = mapperPositionComponent.get(e);
		RotationComponent rotationComponent = mapperRotationComponent.get(e);
		BodyComponent bodyComponent = mapperBodyComponent.get(e);	
		SpineComponent spineComponent = mapperSpineComponent.get(e);	
		
		//move using WASD
		Vector2 moveVector = new Vector2(0, 0);
		
		if(Gdx.input.isKeyPressed(Keys.A))
		{
			moveVector.x -= 1;
		}
		
		if(Gdx.input.isKeyPressed(Keys.D))
		{
			moveVector.x += 1;
		}
		
		if(Gdx.input.isKeyPressed(Keys.W))
		{
			moveVector.y += 1;
		}
		
		if(Gdx.input.isKeyPressed(Keys.S))
		{
			moveVector.y -= 1;
		}

		if(m_Level.m_SwordActivated)
		{
			//if mouse click, then attack
			if(m_AttackCooldown < m_TimeSinceLastAttack 
					&& (Gdx.input.isButtonPressed(1) || Gdx.input.isButtonPressed(0) || Gdx.input.isKeyPressed(Keys.SPACE)) 
					&& !spineComponent.m_State.getAnimation().getName().contentEquals("attackright"))
			{
				m_Attack.play(m_Level.m_GameScreen.m_Settings.m_GameSoundVolume * 0.75f);
				m_TimeSinceLastAttack = 0;
				spineComponent.m_State.setAnimation("attackright", false);
				spineComponent.m_State.addAnimation("idle", true);
			}
		}
		
		if(moveVector.x != 0f || moveVector.y != 0f)
		{
			if(!m_StepsPlaying)
			{
				m_StepsPlaying = true;
				m_Steps.play();
			}
			moveVector.nor();
			moveVector.scl(m_Speed);

//			positionComponent.m_X += moveVector.x;
//			positionComponent.m_Y += moveVector.y;
			bodyComponent.m_Body.setLinearVelocity(moveVector.scl(WORLD_TO_BOX));
			
			float newRotDelta = (moveVector.angle() - 90) - rotationComponent.m_Rotation;

			if(newRotDelta < -180)
			{
				newRotDelta = 360 + newRotDelta;
			}
			else if(newRotDelta > 180)
			{
				newRotDelta = -360 + newRotDelta;
			}
			
			if(Math.abs(newRotDelta) > 5f)
			{
				rotationComponent.m_Rotation += newRotDelta / 6;
			}
			else
			{
				rotationComponent.m_Rotation += newRotDelta;
			}
		}
		else
		{
			bodyComponent.m_Body.setLinearVelocity(moveVector);

			if(m_StepsPlaying)
			{
				m_StepsPlaying = false;
				m_Steps.stop();
			}
		}
	}

}
