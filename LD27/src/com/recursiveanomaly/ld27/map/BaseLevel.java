package com.recursiveanomaly.ld27.map;

import java.util.ArrayList;
import java.util.List;

import box2dLight.RayHandler;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.esotericsoftware.spine.SkeletonJson;
import com.recursiveanomaly.ld27.components.BodyComponent;
import com.recursiveanomaly.ld27.components.DamageComponent;
import com.recursiveanomaly.ld27.components.HealthComponent;
import com.recursiveanomaly.ld27.components.PickUpComponent;
import com.recursiveanomaly.ld27.components.PlayerComponent;
import com.recursiveanomaly.ld27.components.PositionComponent;
import com.recursiveanomaly.ld27.components.RotationComponent;
import com.recursiveanomaly.ld27.components.SpineComponent;
import com.recursiveanomaly.ld27.entities.EntityHelper;
import com.recursiveanomaly.ld27.entities.IEntity;
import com.recursiveanomaly.ld27.entities.PlayerEntity;
import com.recursiveanomaly.ld27.screens.GameScreen;
import com.recursiveanomaly.ld27.screens.MainMenuScreen;
import com.recursiveanomaly.ld27.systems.CameraSystem;
import com.recursiveanomaly.ld27.systems.LifecycleSystem;
import com.recursiveanomaly.ld27.systems.PhysicsSystem;
import com.recursiveanomaly.ld27.systems.PlayerInputSystem;
import com.recursiveanomaly.ld27.systems.SpineRenderSystem;
import com.recursiveanomaly.ld27.systems.SteeringSystem;

public class BaseLevel 
{
	//tilemap stuff
	String m_MapFileName;
	TiledMapRenderer m_TiledMapRenderer;
	TiledMap m_TiledMap;
	
	public boolean m_Dead = false;
	
	Vector2 checkpointPos;
	
	//artemis entity system stuff
	public com.artemis.World m_ArtemisWorld;
	List<IEntity> m_Entities;
	public GameScreen m_GameScreen;
	SpineRenderSystem m_SpineRenderSystem;
	
	//physics stuff
	public com.badlogic.gdx.physics.box2d.World m_PhysicsWorld;
	public RayHandler m_RayHandler;
	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;

	Music m_BackgroundMusic;
	boolean m_ClockTicking = false;
	Sound m_ClockTick;
	Sound m_Powerup;
	Sound m_Crash;
	Sound m_Wind;

	//border stuff
	private Texture m_BorderTexture;
	private Sprite m_BorderSprite;
	private float m_FadeIn = 0f;
	
	public PlayerEntity m_PlayerEntity;
	public boolean m_SwordActivated = false;
	public float m_LightPowerDistance = 0;
	
	//HUD stuff
	private SpineComponent m_WatchSpine;
	int m_VialsBroken = 0;
	private SpineComponent m_VialOne;
	private SpineComponent m_VialTwo;
	private SpineComponent m_VialThree;
	
	public BaseLevel(GameScreen gameScreen)
	{
		m_GameScreen = gameScreen;
		m_Entities = new ArrayList<IEntity>();

		m_BackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/background.mp3"));
		m_BackgroundMusic.setVolume(gameScreen.m_Settings.m_BackgroundMusicVolume * 0.75f);
		m_BackgroundMusic.setLooping(true);
		m_BackgroundMusic.play();
		
		m_ClockTick = Gdx.audio.newSound(Gdx.files.internal("music/tick.mp3"));
		m_Powerup = Gdx.audio.newSound(Gdx.files.internal("music/powerup.wav"));
		m_Crash = Gdx.audio.newSound(Gdx.files.internal("music/crash.mp3"));
		m_Wind = Gdx.audio.newSound(Gdx.files.internal("music/wind.mp3"));

		//title
		m_BorderTexture = new Texture(Gdx.files.internal("map/border.png"));
		m_BorderTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);		
		TextureRegion borderRegion = new TextureRegion(m_BorderTexture, 0, 0, 800, 600);		
		m_BorderSprite = new Sprite(borderRegion);
		m_BorderSprite.setColor(1, 1, 1, 0.5f);
	}
	
	public void Create()
	{
		//set tilemap file name
		m_MapFileName = "map/test.tmx";
		
		//set up the tilemap
		m_TiledMap = new TmxMapLoader().load(m_MapFileName);
		m_TiledMapRenderer = new OrthogonalTiledMapRenderer(m_TiledMap, 1f);
		
		//set up the box2d physics
		m_PhysicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
		
		//attach contact listener to physics world
		m_PhysicsWorld.setContactListener(new GameContactListener(this));

		//set up box2dlights
		m_RayHandler = new RayHandler(m_PhysicsWorld);
		m_RayHandler.setAmbientLight(0.0f);
		
		//add tilemap collision boxes to physics world
		for(int i = 0; i < m_TiledMap.getLayers().getCount(); i++)
		{
			if(m_TiledMap.getLayers().get(i) instanceof TiledMapTileLayer)
			{
				TiledMapTileLayer layer = (TiledMapTileLayer) m_TiledMap.getLayers().get(i);
				for(int j = 0; j < layer.getWidth(); j++)
				{
					for(int k = 0; k < layer.getHeight(); k++)
					{
						Cell cell = layer.getCell(j, k);
						if(cell != null)
						{
							TiledMapTile tile = cell.getTile();
							if(tile != null)
							{
								MapProperties props = tile.getProperties();
								if(props.containsKey("blocked"))
								{
									float worldX = j * 64 + 32;
									float worldY = k * 64 + 32;
									BodyDef bodyDef = new BodyDef();
									bodyDef.type = BodyType.StaticBody;
									bodyDef.position.set(new Vector2(worldX * WORLD_TO_BOX, worldY * WORLD_TO_BOX));
									
									Body newBody = m_PhysicsWorld.createBody(bodyDef);
									
									PolygonShape boxShape = new PolygonShape();
									boxShape.setAsBox(32 * WORLD_TO_BOX, 32 * WORLD_TO_BOX);
									
									// Create a fixture definition to apply our shape to
									FixtureDef fixtureDef = new FixtureDef();
									fixtureDef.shape = boxShape;
				
									newBody.createFixture(fixtureDef);
									
									boxShape.dispose();
									
								}
			
								if(props.containsKey("rotate"))
								{
									//flip some random tiles to break pattern
									if(k * j % 3 == 0) layer.getCell(j, k).setFlipVertically(true);
									if(k * j % 2 == 0) 
										layer.getCell(j, k).setFlipHorizontally(true);
								}
							}
						}
					}
				}
			}
		}
		
		//set up artemis entity system
		m_ArtemisWorld = new com.artemis.World();
		
		//add the passive systems
		m_SpineRenderSystem = new SpineRenderSystem(m_GameScreen);
		m_ArtemisWorld.setSystem(m_SpineRenderSystem, true);

		//add the systems
		m_ArtemisWorld.setSystem(new PlayerInputSystem(this));
		m_ArtemisWorld.setSystem(new CameraSystem(m_GameScreen.m_Camera));
		m_ArtemisWorld.setSystem(new PhysicsSystem());
		m_ArtemisWorld.setSystem(new LifecycleSystem(this));
		m_ArtemisWorld.setSystem(new SteeringSystem());
		
		//init the world
		m_ArtemisWorld.initialize();
		EntityHelper.LoadObjects(m_GameScreen, m_ArtemisWorld, m_TiledMap, m_PhysicsWorld, m_RayHandler);
		
		//add player to entity list
		m_PlayerEntity = new PlayerEntity();
		m_PlayerEntity.AddToWorld(this);
		checkpointPos = new Vector2(0,0);
		checkpointPos.x = m_PlayerEntity.m_Entity.getComponent(PositionComponent.class).m_X;
		checkpointPos.y = m_PlayerEntity.m_Entity.getComponent(PositionComponent.class).m_Y;
		
		//hud items are special cases since they needs to draw on top of border	
		m_VialOne = new SpineComponent("map/vial", 5, 0.75f, -25);	
		m_VialTwo = new SpineComponent("map/vial", 5, 0.65f, -5);	
		m_VialThree = new SpineComponent("map/vial", 5, 0.75f, -50);	
		m_WatchSpine = new SpineComponent("map/watch", 4, 0.75f, 0);

		if(m_GameScreen.m_Settings.m_WatchFound)
		{
			ActivateWatch();
			BodyComponent bodyComponent = m_PlayerEntity.m_Entity.getComponent(BodyComponent.class);
			bodyComponent.m_Body.setTransform(621 * WORLD_TO_BOX, 5961 * WORLD_TO_BOX, 0);		
		}
		if(m_GameScreen.m_Settings.m_WeaponFound)
		{
			ActivateSword();
			BodyComponent bodyComponent = m_PlayerEntity.m_Entity.getComponent(BodyComponent.class);
			bodyComponent.m_Body.setTransform(1965 * WORLD_TO_BOX, 4842 * WORLD_TO_BOX, 0);		
		}
		if(m_GameScreen.m_Settings.m_WeaponLightFound) 
		{
			ActivateSwordLight();
			BodyComponent bodyComponent = m_PlayerEntity.m_Entity.getComponent(BodyComponent.class);
			bodyComponent.m_Body.setTransform(6390 * WORLD_TO_BOX, 6462 * WORLD_TO_BOX, 0);		
		}

	}
	
	public void Update(float delta)
	{
		if(m_FadeIn < 1)
		{
			m_FadeIn += delta / 10;
			m_BackgroundMusic.setVolume(m_GameScreen.m_Settings.m_BackgroundMusicVolume * m_FadeIn);
		}
		//update and render the tilemap
		m_TiledMapRenderer.setView(m_GameScreen.m_Camera);
		m_TiledMapRenderer.render();

		//update physics system
		m_PhysicsWorld.step(1f/60f, 6, 2);
		
		//update and render the artemis entity system
		m_ArtemisWorld.setDelta(delta);
		m_ArtemisWorld.process();
		
		m_SpineRenderSystem.process();
		
		m_GameScreen.m_Camera.update();
		m_GameScreen.GetSpriteBatch().setProjectionMatrix(m_GameScreen.m_Camera.combined);		
		
		//update the player's light distance from watch
		float distance = 225;

		if(!m_WatchSpine.m_State.getAnimation().getName().contentEquals("tick"))
		{
			m_ClockTicking = false;
		}
			
		if(m_WatchSpine.m_State.getAnimation().getName().contentEquals("tick"))
		{
			if(!m_ClockTicking)
			{
				m_ClockTick.play(m_GameScreen.m_Settings.m_GameSoundVolume / 2);
				m_ClockTicking = true;
			}
			float time = m_WatchSpine.m_State.getTime();
			if(time < 10.0f)
			{
				float timePercentageComplete = (10 - time) / 10.0f;
				distance += (timePercentageComplete * 300);
				m_LightPowerDistance = timePercentageComplete * 300;
			}
		} 
		else if(m_WatchSpine.m_State.getAnimation().getName().contentEquals("wind"))
		{
			float time = m_WatchSpine.m_State.getTime();
			float timePercentageComplete = time / m_WatchSpine.m_State.getAnimation().getDuration();
			distance += (timePercentageComplete * 300);
			m_LightPowerDistance = timePercentageComplete * 300;
		}
		
		//update the players light distance sensor
		m_PlayerEntity.m_LightDistanceSensor.getShape().setRadius((m_LightPowerDistance * WORLD_TO_BOX) / 2);
		
		if(m_PlayerEntity.m_Entity != null)
		{
			BodyComponent bodyComponent = m_PlayerEntity.m_Entity.getComponent(BodyComponent.class);
			if(bodyComponent != null)
			{
				bodyComponent.m_Light.setDistance(distance * WORLD_TO_BOX);
			}
		}
		
		//render the lighting
		m_RayHandler.setCombinedMatrix(m_GameScreen.m_Camera.combined.scl(BOX_TO_WORLD));
		m_RayHandler.updateAndRender();

		//draw border
		m_BorderSprite.setColor(1, 1, 1, m_FadeIn * 0.5f);
		m_BorderSprite.setPosition(m_GameScreen.m_Camera.position.x - 400, m_GameScreen.m_Camera.position.y - 300);
		m_GameScreen.GetSpriteBatch().begin();
		m_BorderSprite.draw(m_GameScreen.GetSpriteBatch());
		m_GameScreen.GetSpriteBatch().end();
		
		//draw the vials
		m_VialOne.m_Skeleton.getColor().set(1, 1, 1, m_FadeIn * 0.75f);
		m_VialOne.m_Skeleton.setX(m_GameScreen.m_Camera.position.x - 375);
		m_VialOne.m_Skeleton.setY(m_GameScreen.m_Camera.position.y - 275);
		m_VialOne.m_Skeleton.getRootBone().setRotation(m_VialOne.m_Rotation);
		m_VialOne.m_State.update(Gdx.graphics.getDeltaTime());
		m_VialOne.m_State.apply(m_VialOne.m_Skeleton);		
		m_VialOne.m_Skeleton.updateWorldTransform();

		m_VialTwo.m_Skeleton.getColor().set(1, 1, 1, m_FadeIn * 0.75f);
		m_VialTwo.m_Skeleton.setX(m_GameScreen.m_Camera.position.x - 305);
		m_VialTwo.m_Skeleton.setY(m_GameScreen.m_Camera.position.y - 275);
		m_VialTwo.m_Skeleton.getRootBone().setRotation(m_VialTwo.m_Rotation);
		m_VialTwo.m_State.update(Gdx.graphics.getDeltaTime());
		m_VialTwo.m_State.apply(m_VialTwo.m_Skeleton);
		m_VialTwo.m_Skeleton.updateWorldTransform();

		m_VialThree.m_Skeleton.getColor().set(1, 1, 1, m_FadeIn * 0.75f);
		m_VialThree.m_Skeleton.setX(m_GameScreen.m_Camera.position.x - 390);
		m_VialThree.m_Skeleton.setY(m_GameScreen.m_Camera.position.y - 205);
		m_VialThree.m_Skeleton.getRootBone().setRotation(m_VialThree.m_Rotation);
		m_VialThree.m_State.update(Gdx.graphics.getDeltaTime());
		m_VialThree.m_State.apply(m_VialThree.m_Skeleton);
		m_VialThree.m_Skeleton.updateWorldTransform();
		
		//draw watch last
		m_WatchSpine.m_Skeleton.setX(m_GameScreen.m_Camera.position.x + 300);
		m_WatchSpine.m_Skeleton.setY(m_GameScreen.m_Camera.position.y + 200);
		m_WatchSpine.m_State.update(Gdx.graphics.getDeltaTime());
		m_WatchSpine.m_State.apply(m_WatchSpine.m_Skeleton);		
		m_WatchSpine.m_Skeleton.updateWorldTransform();
		m_GameScreen.GetSpriteBatch().begin();
		m_SpineRenderSystem.m_Renderer.draw(m_GameScreen.GetSpriteBatch(), m_VialOne.m_Skeleton);
		m_SpineRenderSystem.m_Renderer.draw(m_GameScreen.GetSpriteBatch(), m_VialTwo.m_Skeleton);
		m_SpineRenderSystem.m_Renderer.draw(m_GameScreen.GetSpriteBatch(), m_VialThree.m_Skeleton);
		m_SpineRenderSystem.m_Renderer.draw(m_GameScreen.GetSpriteBatch(), m_WatchSpine.m_Skeleton);
		m_GameScreen.GetSpriteBatch().end();
	}
	
	public void dispose()
	{
		m_BorderTexture.dispose();
		m_BackgroundMusic.stop();
		m_BackgroundMusic.dispose();
		m_RayHandler.dispose();
		m_PhysicsWorld.dispose();
	}
	
	public void ActivateWatch()
	{
		if(m_WatchSpine.m_State.getAnimation().getName().contentEquals("hide"))
		{
			//m_Powerup.play(m_GameScreen.m_Settings.m_GameSoundVolume);
			m_WatchSpine.m_State.setAnimation("appear", false);
			m_WatchSpine.m_State.addAnimation("tick", false);
		}

		m_GameScreen.m_Settings.m_WatchFound = true;
	}

	public void ActivateSword() 
	{
		m_SwordActivated = true;
		m_PlayerEntity.ActivateWeapon();
		
		m_GameScreen.m_Settings.m_WeaponFound = true;
	}

	public void ActivateSwordLight() 
	{
		m_PlayerEntity.ActivateWeaponLight();
		m_GameScreen.m_Settings.m_WeaponLightFound = true;
	}

	public void WindWatch() 
	{
		if(m_WatchSpine.m_State.getAnimation().getName().contentEquals("tick") && m_WatchSpine.m_State.getTime() >= 6)
		{
			m_Wind.play(m_GameScreen.m_Settings.m_GameSoundVolume);
			m_WatchSpine.m_State.setAnimation("wind", false);
			m_WatchSpine.m_State.addAnimation("tick", false);
		}
	}

	public void BreakVials(int m_Damage) 
	{
		for(int i = 0; i < m_Damage; i++)
		{
			if(m_VialsBroken < 3) m_Crash.play(m_GameScreen.m_Settings.m_GameSoundVolume);
			if(m_VialsBroken == 0) m_VialOne.m_State.setAnimation("break", false);
			else if(m_VialsBroken == 1) m_VialTwo.m_State.setAnimation("break", false);
			else if(m_VialsBroken == 2) m_VialThree.m_State.setAnimation("break", false);
			else if(m_VialsBroken == 3)
			{
				m_Dead = true;
			}
			m_VialsBroken++;
		}
	}
}
