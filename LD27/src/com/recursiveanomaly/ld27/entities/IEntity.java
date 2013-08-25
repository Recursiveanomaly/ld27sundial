package com.recursiveanomaly.ld27.entities;

import com.artemis.Entity;
import com.recursiveanomaly.ld27.map.BaseLevel;

public interface IEntity 
{
	public void AddToWorld(BaseLevel level);
	public void DeleteFromWorld();
	public void ChangedIntWorld();
	public Entity GetEntity();
}
