package org.projectcrawwl.data;

public class GameSettings{	
	private static int screen_x;
	private static int screen_y;
	
	public static int getScreenX(){
		return screen_x;
	}
	public static int getScreenY(){
		return screen_y;
	}
	public static void setScreenX(int tempX){
		screen_x = tempX;
	}
	public static void setScreenY(int tempY){
		screen_y = tempY;
	}
}

