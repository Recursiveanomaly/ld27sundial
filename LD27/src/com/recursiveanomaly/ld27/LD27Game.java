package com.recursiveanomaly.ld27;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.recursiveanomaly.ld27.screens.SplashScreen;

public class LD27Game extends Game
{
	public int m_Width = 800;
	public int m_Height = 600;

	public SpriteBatch m_Batch;
	public BitmapFont m_Font;
	
	@Override
	public void create() {		
		m_Batch = new SpriteBatch();
		m_Font = new BitmapFont();
		
		this.setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		m_Font.dispose();
		m_Batch.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
