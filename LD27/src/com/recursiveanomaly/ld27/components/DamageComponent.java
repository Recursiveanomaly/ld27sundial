package com.recursiveanomaly.ld27.components;

import com.artemis.Component;


public class DamageComponent extends Component 
{
	public int m_Damage;
	public boolean m_LightDamage;
	public DamageComponent(int damage)
	{
		m_Damage = damage;
		m_LightDamage = false;
	}
}
