package com.recursiveanomaly.ld27.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.recursiveanomaly.ld27.LD27Game;
import com.recursiveanomaly.ld27.map.GameSettings;

public class MainMenuScreen implements Screen {

	private OrthographicCamera m_Camera;
	private GameSettings m_GameSettings;

	private Texture m_TitleTexture;
	private Sprite m_TitleSprite;
	private Texture m_PlayTexture;
	private Sprite m_PlaySprite;
	
	float m_FadingIn = 0.2f;
	
	LD27Game m_Game;
	
	Music m_MenuMusic;
	
	public MainMenuScreen(LD27Game game)
	{
		m_Game = game;
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		m_Camera = new OrthographicCamera(1, h/w);

		//title
		m_TitleTexture = new Texture(Gdx.files.internal("menu/header.png"));
		m_TitleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);		
		TextureRegion titleRegion = new TextureRegion(m_TitleTexture, 0, 0, 512, 128);		
		m_TitleSprite = new Sprite(titleRegion);
		m_TitleSprite.setSize(0.9f, 0.9f * m_TitleSprite.getHeight() / m_TitleSprite.getWidth());
		m_TitleSprite.setOrigin(m_TitleSprite.getWidth()/2, m_TitleSprite.getHeight()/2);
		m_TitleSprite.setPosition(-m_TitleSprite.getWidth()/2, 0.125f);

		//play button
		m_PlayTexture = new Texture(Gdx.files.internal("menu/play.png"));
		m_PlayTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);		
		TextureRegion playRegion = new TextureRegion(m_PlayTexture, 0, 0, 512, 128);		
		m_PlaySprite = new Sprite(playRegion);
		m_PlaySprite.setSize(0.9f, 0.9f * m_PlaySprite.getHeight() / m_PlaySprite.getWidth());
		m_PlaySprite.setOrigin(m_PlaySprite.getWidth()/2, m_PlaySprite.getHeight()/2);
		m_PlaySprite.setPosition(-m_PlaySprite.getWidth()/2, -0.15f);

		m_GameSettings = new GameSettings();

		m_MenuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));
		m_MenuMusic.setVolume(m_GameSettings.m_BackgroundMusicVolume);
		m_MenuMusic.setLooping(true);
		m_MenuMusic.play();
	}
	
	@Override
	public void render(float delta)
	{		
//        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		m_Camera.update();
		m_Game.m_Batch.setProjectionMatrix(m_Camera.combined);
		
		m_Game.m_Batch.begin();
		m_TitleSprite.draw(m_Game.m_Batch);
		m_PlaySprite.draw(m_Game.m_Batch);
		m_Game.m_Batch.end();


//		if(Gdx.input.isKeyPressed(Keys.ESCAPE))
//		{
//			Gdx.app.exit();
//		}
		
		//skip to game with default settings for building

//		m_Game.setScreen(new GameScreen(m_Game, m_GameSettings));
//		dispose();
//		
		if(m_FadingIn > 0)
		{
			m_FadingIn -= Gdx.graphics.getDeltaTime();
		}
		else if(Gdx.input.isButtonPressed(0) || Gdx.input.isButtonPressed(1) || Gdx.input.isButtonPressed(2) || Gdx.input.isKeyPressed(Keys.ANY_KEY))
		{
			m_Game.setScreen(new GameScreen(m_Game, m_GameSettings));
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		m_MenuMusic.dispose();
		m_TitleTexture.dispose();
		m_PlayTexture.dispose();
	}

}
