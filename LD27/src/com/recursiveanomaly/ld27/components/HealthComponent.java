package com.recursiveanomaly.ld27.components;

import com.artemis.Component;

public class HealthComponent extends Component 
{
	public int m_MaxHealth;
	public int m_CurrentHealth;
	public boolean m_Spectral = false;
	public boolean m_Special = false;
	public long m_LastHitTime = 0;
	public long m_HitImmuneTime = 1000;
	
	public HealthComponent(int maxHealth, boolean spectral, boolean special)
	{
		m_MaxHealth = maxHealth;
		m_CurrentHealth = m_MaxHealth;
		m_Spectral = spectral;
		m_Special = special;
	}
	
	public void RemoveSpecial() 
	{
		m_Special = false;
		m_CurrentHealth = 0;
	}
}
