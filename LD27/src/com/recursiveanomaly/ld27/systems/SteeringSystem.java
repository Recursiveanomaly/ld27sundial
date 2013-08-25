package com.recursiveanomaly.ld27.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.recursiveanomaly.ld27.components.BodyComponent;
import com.recursiveanomaly.ld27.components.PositionComponent;
import com.recursiveanomaly.ld27.components.SteeringComponent;

public class SteeringSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<SteeringComponent> mapperSteeringComponent;
	@Mapper ComponentMapper<BodyComponent> mapperBodyComponent;
	@Mapper ComponentMapper<PositionComponent> mapperPositionComponent;
	
	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;
	
	float m_Speed = 100;

	@SuppressWarnings("unchecked")
	public SteeringSystem() {
		super(Aspect.getAspectForAll(SteeringComponent.class, BodyComponent.class, PositionComponent.class));
	}

	@Override
	protected void process(Entity e) 
	{
		SteeringComponent steeringComponent = mapperSteeringComponent.get(e);
		BodyComponent bodyComponent = mapperBodyComponent.get(e);
		PositionComponent positionComponent = mapperPositionComponent.get(e);

		if(steeringComponent.m_TimeLeft > 0)
		{
			steeringComponent.m_TimeLeft -= Gdx.graphics.getDeltaTime();
			if(steeringComponent.m_Chase)
			{
				Vector2 chaseVector = new Vector2(0,0);
				chaseVector.x = -(positionComponent.m_X * WORLD_TO_BOX) + steeringComponent.m_Target.getPosition().x;
				chaseVector.y = -(positionComponent.m_Y * WORLD_TO_BOX) + steeringComponent.m_Target.getPosition().y;
				chaseVector.nor();
				chaseVector.scl(WORLD_TO_BOX * m_Speed);
				bodyComponent.m_Body.setLinearVelocity(chaseVector);
			}
			else
			{
				Vector2 runAwayVector = new Vector2(0,0);
				runAwayVector.x = (positionComponent.m_X * WORLD_TO_BOX) - steeringComponent.m_Target.getPosition().x;
				runAwayVector.y = (positionComponent.m_Y * WORLD_TO_BOX) - steeringComponent.m_Target.getPosition().y;
				runAwayVector.nor();
				runAwayVector.scl(WORLD_TO_BOX * m_Speed);
				bodyComponent.m_Body.setLinearVelocity(runAwayVector);
			}
		}
	}

}
