package com.recursiveanomaly.ld27.screens;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.recursiveanomaly.ld27.LD27Game;
import com.recursiveanomaly.ld27.map.BaseLevel;
import com.recursiveanomaly.ld27.map.GameSettings;

public class GameScreen implements Screen 
{	
	public LD27Game m_Game;
	
	public OrthographicCamera m_Camera;		
	BaseLevel m_CurrentLevel;
	
	public GameSettings m_Settings;
	
	public GameScreen(LD27Game game, GameSettings settings)
	{
		m_Game = game;
		m_Settings = settings;
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		m_Camera = new OrthographicCamera();
		m_Camera.setToOrtho(false, w, h);
		
		SetLevel(new BaseLevel(this));
	}
	
	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Gdx.app.log("fps", String.valueOf(Gdx.graphics.getFramesPerSecond()));
		
		m_Camera.update();
		m_Game.m_Batch.setProjectionMatrix(m_Camera.combined);
		
		if(m_CurrentLevel != null)
		{
			m_CurrentLevel.Update(delta);
		}
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))
		{
//			Gdx.app.exit();
			m_Game.setScreen(new MainMenuScreen(m_Game));
			m_CurrentLevel.dispose();
			dispose();
		}
		else if(m_CurrentLevel.m_Dead)
		{
			m_Game.setScreen(new GameScreen(m_Game, m_Settings));
			m_CurrentLevel.dispose();
			dispose();
		}
	}
	
	public SpriteBatch GetSpriteBatch()
	{
		return m_Game.m_Batch;
	}
	
	private void SetLevel(BaseLevel newLevel)
	{
		m_CurrentLevel = newLevel;
		m_CurrentLevel.Create();
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
	}

}
