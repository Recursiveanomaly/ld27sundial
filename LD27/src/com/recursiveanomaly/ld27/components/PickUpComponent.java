package com.recursiveanomaly.ld27.components;

import com.artemis.Component;

public class PickUpComponent extends Component 
{
	public int m_Type;
	//1 = watch
	//2 = sword light
	//3 = wind watch
	//4 = sword
	public PickUpComponent(int type)
	{
		m_Type = type;
	}
}
