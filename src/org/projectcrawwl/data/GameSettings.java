package org.projectcrawwl.data;

import java.io.Serializable;

public class GameSettings implements Serializable{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int screen_x;
	private int screen_y;
	
	private static GameSettings instance = null;

	public static GameSettings getInstance()
	{
		if(instance == null)
			instance = new GameSettings();

		return instance;
	}
	
	public int getScreenX(){
		return screen_x;
	}
	public int getScreenY(){
		return screen_y;
	}
	public void setScreenX(int tempX){
		screen_x = tempX;
	}
	public void setScreenY(int tempY){
		screen_y = tempY;
	}
}

