package com.recursiveanomaly.ld27.components;

import com.artemis.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

public class SpineComponent extends Component 
{
	TextureAtlas m_Atlas;
	public Skeleton m_Skeleton;
	public AnimationState m_State;
	SkeletonJson m_SkeletonJson;
	SkeletonData m_SkeletonData;
	AnimationStateData m_AnimationStateData;
	
	public int m_Type;
	public float m_Rotation;
	
	public SpineComponent(String filePath, int type, float scale, float rotation)
	{
		m_Rotation = rotation;
		m_Type = type;
		
		m_Atlas = new TextureAtlas(Gdx.files.internal(filePath + ".atlas"));
		SkeletonJson m_SkeletonJson = new SkeletonJson(m_Atlas);
		m_SkeletonJson.setScale(scale);
		SkeletonData m_SkeletonData = m_SkeletonJson.readSkeletonData(Gdx.files.internal(filePath + ".json"));

		m_Skeleton = new Skeleton(m_SkeletonData);
		
		// Define mixing between animations.
		AnimationStateData m_AnimationStateData = new AnimationStateData(m_SkeletonData);
		if(m_Type == 1) // type 1 player
		{
			m_AnimationStateData.setMix("attackright", "idle", 0.4f);
			m_AnimationStateData.setMix("run", "attackright", 0.2f);
			m_AnimationStateData.setMix("idle", "attackright", 0.2f);
			m_AnimationStateData.setMix("attackright", "attackright", 0.4f);

			m_State = new AnimationState(m_AnimationStateData);
			m_State.setAnimation("idle", true);
			
			m_Skeleton.findSlot("sword").setAttachment(null);
		}
		else if(m_Type == 2) // type 2 enemy
		{
			m_AnimationStateData.setMix("idle", "run", 0.4f);
			m_AnimationStateData.setMix("run", "idle", 0.2f);
			m_State = new AnimationState(m_AnimationStateData);
			m_State.setAnimation("idle", true);
		}
		else if(m_Type == 3) // type 3 static torch
		{
			//what do
			m_State = new AnimationState(m_AnimationStateData);
			m_State.setAnimation("idle", true);
			m_State.setTime(MathUtils.random(m_State.getAnimation().getDuration()));
		}
		else if(m_Type == 4) // watch element
		{
			m_AnimationStateData.setMix("appear", "hide", 0.4f);
			m_AnimationStateData.setMix("appear", "tick", 0.4f);
			m_AnimationStateData.setMix("appear", "wind", 0.4f);
			
			m_AnimationStateData.setMix("hide", "appear", 0.4f);
			m_AnimationStateData.setMix("hide", "tick", 0.4f);
			m_AnimationStateData.setMix("hide", "wind", 0.4f);

			m_AnimationStateData.setMix("tick", "appear", 0.4f);
			m_AnimationStateData.setMix("tick", "hide", 0.4f);
			m_AnimationStateData.setMix("tick", "wind", 0.4f);
			
			m_AnimationStateData.setMix("wind", "appear", 0.4f);
			m_AnimationStateData.setMix("wind", "hide", 0.4f);
			m_AnimationStateData.setMix("wind", "appear", 0.4f);
			
			m_State = new AnimationState(m_AnimationStateData);
			m_State.setAnimation("hide", false);
		}
		else if(m_Type == 5) // vial element
		{
			m_AnimationStateData.setMix("idle", "break", 0.2f);
			m_State = new AnimationState(m_AnimationStateData);
			m_State.setAnimation("idle", true);
			m_State.setTime(MathUtils.random(m_State.getAnimation().getDuration()));
		}
	}
}
