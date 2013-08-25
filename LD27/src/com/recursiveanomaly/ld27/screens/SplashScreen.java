package com.recursiveanomaly.ld27.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class SplashScreen implements Screen {

	private Texture m_Texture;
	private Sprite m_Sprite;
	private OrthographicCamera m_Camera;

	Music m_MenuMusic;
	
	LD27Game m_Game;
	
	public SplashScreen(LD27Game game)
	{
		m_Game = game;
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		m_Camera = new OrthographicCamera(1, h/w);

		m_Texture = new Texture(Gdx.files.internal("menu/splash.png"));
		m_Texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(m_Texture, 0, 0, 512, 512);
		
		m_Sprite = new Sprite(region);
		m_Sprite.setSize(0.9f, 0.9f * m_Sprite.getHeight() / m_Sprite.getWidth());
		m_Sprite.setOrigin(m_Sprite.getWidth()/2, m_Sprite.getHeight()/2);
		m_Sprite.setPosition(-m_Sprite.getWidth()/2, -m_Sprite.getHeight()/2);

		m_MenuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));
		m_MenuMusic.setVolume(0.15f);
		m_MenuMusic.setLooping(true);
		m_MenuMusic.play();
	}
	
	@Override
	public void render(float delta) 
	{
		if(CheckForInput())
		{
			//go to main menu
			m_Game.setScreen(new GameScreen(m_Game, new GameSettings()));
			dispose();
			return;
		}

        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		m_Camera.update();
		m_Game.m_Batch.setProjectionMatrix(m_Camera.combined);
		
		m_Game.m_Batch.begin();
		m_Sprite.draw(m_Game.m_Batch);
		m_Game.m_Batch.end();

//		//skip to menu for easy building
//		m_Game.setScreen(new MainMenuScreen(m_Game));
//		dispose();
	}

	private boolean CheckForInput() {
		// TODO Auto-generated method stub
		if(Gdx.input.isKeyPressed(Keys.ANY_KEY)) return true;
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) return true;
		if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) return true;
		return false;
	}

	@Override
	public void resize(int width, int height) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {

		m_MenuMusic.dispose();
		m_Texture.dispose();		
	}
}
