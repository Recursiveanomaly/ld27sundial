package com.recursiveanomaly.ld27.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.recursiveanomaly.ld27.components.PositionComponent;
import com.recursiveanomaly.ld27.components.RotationComponent;
import com.recursiveanomaly.ld27.components.SpineComponent;
import com.recursiveanomaly.ld27.screens.GameScreen;

public class SpineRenderSystem extends EntityProcessingSystem 
{
	@Mapper ComponentMapper<SpineComponent> mapperSpineComponent;
	@Mapper ComponentMapper<PositionComponent> mapperPositionComponent;
	@Mapper ComponentMapper<RotationComponent> mapperRotationComponent;
	
	GameScreen m_GameScreen;

	public SkeletonRenderer m_Renderer;
	
	public SpineRenderSystem(GameScreen gameScreen) 
	{
		super(Aspect.getAspectForAll(SpineComponent.class, PositionComponent.class, RotationComponent.class));
		
		m_GameScreen = gameScreen;

		m_Renderer = new SkeletonRenderer();
	}

	@Override
	protected void process(Entity e) 
	{
		SpineComponent spineComponent = mapperSpineComponent.get(e);
		PositionComponent positionComponent = mapperPositionComponent.get(e);		
		RotationComponent rotationComponent = mapperRotationComponent.get(e);

		
		if(spineComponent.m_Skeleton.getX() != positionComponent.m_X || spineComponent.m_Skeleton.getY() != positionComponent.m_Y)
		{
			//update animation
			if( (spineComponent.m_Type == 1 || spineComponent.m_Type == 2) && spineComponent.m_State.getAnimation() != null && spineComponent.m_State.getAnimation().getName().contentEquals("idle"))
			{
				spineComponent.m_State.setAnimation("run", true);
			}
			//update position/rotation
			spineComponent.m_Skeleton.getRootBone().setRotation(rotationComponent.m_Rotation + spineComponent.m_Rotation);
			spineComponent.m_Skeleton.setX(positionComponent.m_X);
			spineComponent.m_Skeleton.setY(positionComponent.m_Y);
		}
		else
		{
			//update animation
			if(spineComponent.m_State.getAnimation() != null && spineComponent.m_State.getAnimation().getName().contentEquals("run"))
			{
				spineComponent.m_State.setAnimation("idle", true);
			}
		}
		
		spineComponent.m_State.update(Gdx.graphics.getDeltaTime());
		spineComponent.m_State.apply(spineComponent.m_Skeleton);
		
		spineComponent.m_Skeleton.updateWorldTransform();			

		m_GameScreen.GetSpriteBatch().begin();
		m_Renderer.draw(m_GameScreen.GetSpriteBatch(), spineComponent.m_Skeleton);
		m_GameScreen.GetSpriteBatch().end();
	}
}
