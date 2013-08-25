package com.recursiveanomaly.ld27.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.Bone;
import com.recursiveanomaly.ld27.components.BodyComponent;
import com.recursiveanomaly.ld27.components.PositionComponent;

public class PhysicsSystem extends EntityProcessingSystem 
{
	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;
	
	@Mapper ComponentMapper<PositionComponent> mapperPositionComponent;
	@Mapper ComponentMapper<BodyComponent> mapperBodyComponent;
	
	@SuppressWarnings("unchecked")
	public PhysicsSystem()
	{
		super(Aspect.getAspectForAll(PositionComponent.class, BodyComponent.class));
	}

	@Override
	protected void process(Entity e) 
	{
		PositionComponent positionComponent = mapperPositionComponent.get(e);	
		BodyComponent bodyComponent = mapperBodyComponent.get(e);	
		
		//if we have a skeleton bone we are bound to, move to bone
		if(bodyComponent.m_Skeleton != null)
		{
			Vector2 weaponPosition = new Vector2(0,0);
			Bone weaponBone = bodyComponent.m_Skeleton.findBone(bodyComponent.m_BoneName);
			weaponPosition.x = bodyComponent.m_Skeleton.getX() + weaponBone.getWorldX();
			weaponPosition.y = bodyComponent.m_Skeleton.getY() + weaponBone.getWorldY();
			float rotation = weaponBone.getWorldRotation();
			
			weaponPosition.scl(WORLD_TO_BOX);
			
			bodyComponent.m_Body.setTransform(weaponPosition.x, weaponPosition.y, rotation * MathUtils.degRad);
		}
		else
		{
			//else move entity to physics body
			bodyComponent.m_Body.setLinearDamping(1.5f);
			
			Vector2 pos = bodyComponent.m_Body.getPosition();
	
			positionComponent.m_X = pos.x * BOX_TO_WORLD;
			positionComponent.m_Y = pos.y * BOX_TO_WORLD;
		}
	}

}
