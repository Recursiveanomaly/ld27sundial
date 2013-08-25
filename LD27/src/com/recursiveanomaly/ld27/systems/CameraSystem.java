package com.recursiveanomaly.ld27.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.recursiveanomaly.ld27.components.PlayerComponent;
import com.recursiveanomaly.ld27.components.PositionComponent;
import com.recursiveanomaly.ld27.components.RotationComponent;
import com.recursiveanomaly.ld27.screens.GameScreen;

public class CameraSystem extends EntityProcessingSystem 
{
	@Mapper ComponentMapper<PlayerComponent> mapperPlayerComponent;
	@Mapper ComponentMapper<PositionComponent> mapperPositionComponent;
	@Mapper ComponentMapper<RotationComponent> mapperRotationComponent;
	
	Camera m_Camera;
	
	float m_PreviousRotation = 0;
	
	public CameraSystem(Camera camera) 
	{
		super(Aspect.getAspectForAll(PlayerComponent.class, PositionComponent.class, RotationComponent.class));
		m_Camera = camera;
	}

	@Override
	protected void process(Entity e) 
	{
		PositionComponent positionComponent = mapperPositionComponent.get(e);	
		RotationComponent rotationComponent = mapperRotationComponent.get(e);	

		m_Camera.position.x = positionComponent.m_X + 32;
		m_Camera.position.y = positionComponent.m_Y + 32;
	}
}
