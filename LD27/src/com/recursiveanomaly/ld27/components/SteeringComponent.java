package com.recursiveanomaly.ld27.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class SteeringComponent extends Component 
{
	public float m_ChaseTime = 5;
	public Body m_Target;
	public boolean m_Chase;
	public float m_TimeLeft = 0;
}
