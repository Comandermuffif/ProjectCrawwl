package org.projectcrawwl.data;

public class GameSettings{	
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

