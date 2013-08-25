package com.recursiveanomaly.ld27.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.recursiveanomaly.ld27.components.HealthComponent;
import com.recursiveanomaly.ld27.components.BodyComponent;
import com.recursiveanomaly.ld27.components.PlayerComponent;
import com.recursiveanomaly.ld27.map.BaseLevel;

public class LifecycleSystem extends EntityProcessingSystem 
{
	@Mapper ComponentMapper<HealthComponent> mapperHealthComponent;
	
	BaseLevel m_Level;

	@SuppressWarnings("unchecked")
	public LifecycleSystem(BaseLevel level)
	{
		super(Aspect.getAspectForAll(HealthComponent.class));
		m_Level = level;
	}

	@Override
	protected void process(Entity e) 
	{
		HealthComponent healthComponent = mapperHealthComponent.get(e);	
		
		if(!healthComponent.m_Special && healthComponent.m_CurrentHealth <= 0)
		{
			//you dead
			BodyComponent bodyComponent = e.getComponent(BodyComponent.class);
			if(bodyComponent != null)
			{				
				bodyComponent.RemoveFromPhysics();
			}
			world.deleteEntity(e);
		}
	}

}
