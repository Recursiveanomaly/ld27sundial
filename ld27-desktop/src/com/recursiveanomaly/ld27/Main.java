package com.recursiveanomaly.ld27;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) 
	{
		LD27Game game = new LD27Game();
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "LD27";
		cfg.useGL20 = false;
		cfg.width = game.m_Width;
		cfg.height = game.m_Height;
		
		new LwjglApplication(game, cfg);
	}
}
