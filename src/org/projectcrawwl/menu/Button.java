package org.projectcrawwl.menu;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.projectcrawwl.Main;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.StateController;
import org.projectcrawwl.data.World;

public class Button {
	
	private int x;
	private int y;
	
	private int width;
	private int height;
	
	private String name;
	
	private Color color = new Color(128,128,128);
	
	private int ID = 0;
	
	public Button(int x, int y, int w, int h, String s){
		this.x = x;
		this.y = y;
		
		this.width = w;
		this.height = h;
		
		this.name = s;
	}
	
	public Button(int x, int y, int w, int h, String s, int id){
		this.x = x;
		this.y = y;
		
		this.width = w;
		this.height = h;
		
		this.name = s;
		
		this.ID = id;
	}
	
	public void mouseInput(ArrayList<Integer> a){
		for(Integer i : a){
			if(i == 0){
				if(Mouse.getX() >= x && Mouse.getX() <= x + width && Mouse.getY() >= y && Mouse.getY() <= y + height){
					onPress();
				}
			}
		}
	}
	
	public void render(){
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, GameSettings.getScreenX(), 0, GameSettings.getScreenY(), -1, 1);
		
		GL11.glColor3d((double)(color.getRed())/255, (double)(color.getBlue())/255, (double)(color.getGreen())/255);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x + width, y);
		GL11.glVertex2d(x + width, y + height);
		GL11.glVertex2d(x, y + height);
		GL11.glEnd();
		
		GL11.glColor3d(0, 0, 0);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x + width, y);
		GL11.glVertex2d(x + width, y + height);
		GL11.glVertex2d(x, y + height);
		GL11.glEnd();
		
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, GameSettings.getScreenX(), GameSettings.getScreenY(), 0, -1, 1);
		GameData.getFont().drawString(x + width/2 - GameData.getFont().getWidth(name)/2, GameSettings.getScreenY() - (y + height/2 + GameData.getFont().getHeight(name)/2), name);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		
		
		float ratio = ((float) (GameSettings.getScreenY())/GameSettings.getScreenX());
		GL11.glLoadIdentity();
		GL11.glOrtho(-GameData.zoom, GameSettings.getScreenX()  + GameData.zoom, -GameData.zoom*(ratio), GameSettings.getScreenY() + GameData.zoom*(ratio), -1, 1);
		
	}
	
	private void onPress(){
		
		if(ID == -1){
			 AL.destroy();
	    	Display.destroy();
	        System.exit(0);
		}
		
		if(ID == 0){
			color = new Color((int)(Math.random()*255),(int) (Math.random()*255),(int) (Math.random()*255));
		}
		
		if(ID == 1){
			StateController.setGameState(Main.IN_GAME);
		}
		if(ID == 2){
			StateController.setGameState(Main.MAIN_MENU);
		}
		if(ID == 3){
			World.generateWorld();
		}
		if(ID == 4){
			StateController.setGameState(Main.LOAD_MENU);
		}
		
		
		
	}
}
