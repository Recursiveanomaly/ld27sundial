package com.recursiveanomaly.ld27.components;

import com.artemis.Component;

public class PositionComponent extends Component 
{
	public float m_X;
	public float m_Y;
	
	public PositionComponent()
	{
		m_X = 0;
		m_Y = 0;
	}
	
	public PositionComponent(float x, float y)
	{
		m_X = x;
		m_Y = y;
	}
}
