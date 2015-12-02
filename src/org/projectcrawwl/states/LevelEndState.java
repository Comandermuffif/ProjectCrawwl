package org.projectcrawwl.states;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.projectcrawwl.Main;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.StateController;
import org.projectcrawwl.data.World;
import org.projectcrawwl.data.XMLHandler;

public class LevelEndState implements GameState {
	
	@Override
	public void onEnter() {
		
		GameData.render();
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, GameSettings.getScreenX(), GameSettings.getScreenY(), 0, -1, 1);
		
		
		GL11.glColor4d(0,0,0, .7);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(GameSettings.getScreenX(), 0);
		GL11.glVertex2d(GameSettings.getScreenX(), GameSettings.getScreenY());
		GL11.glVertex2d(0, GameSettings.getScreenY());
		GL11.glEnd();
		
		GameData.getFont().drawString(GameSettings.getScreenX()/2 - GameData.getFont().getWidth("Saving Game")/2, GameSettings.getScreenY()/2 - GameData.getFont().getHeight("Saving Game")/2, "Saving Game");
		
		float ratio = ((float) (GameSettings.getScreenY())/GameSettings.getScreenX());
		GL11.glLoadIdentity();
		GL11.glOrtho(-GameData.zoom, GameSettings.getScreenX()  + GameData.zoom, -GameData.zoom*(ratio), GameSettings.getScreenY() + GameData.zoom*(ratio), -1, 1);
		
		Display.update();
		XMLHandler.saveData();
	}

	@Override
	public void onExit() {
		
		GameData.clearData();
		World.generateWorld();
		GameData.loadPlayer();
		
		GameData.getPlayer().setX(World.getGoal().getWidth()/2);
		GameData.getPlayer().setY(World.getGoal().getHeight()/2);
    	
		for(int i = 0; i < (GameData.getPlayer().getLevel()+1)*(10); i ++){
			GameData.addZombie();
		}
		
		
    	XMLHandler.saveData();
	}

	@Override
	public void main(int delta) {
		GameData.render();
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, GameSettings.getScreenX(), GameSettings.getScreenY(), 0, -1, 1);
		
		
		GL11.glColor4d(0,0,0, .7);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(GameSettings.getScreenX(), 0);
		GL11.glVertex2d(GameSettings.getScreenX(), GameSettings.getScreenY());
		GL11.glVertex2d(0, GameSettings.getScreenY());
		GL11.glEnd();
		
		GameData.getFont().drawString(GameSettings.getScreenX()/2 - GameData.getFont().getWidth("Press Space to continue")/2, GameSettings.getScreenY()/2 - GameData.getFont().getHeight("Press Space to continue")/2, "Press Space to continue");
		
		float ratio = ((float) (GameSettings.getScreenY())/GameSettings.getScreenX());
		GL11.glLoadIdentity();
		GL11.glOrtho(-GameData.zoom, GameSettings.getScreenX()  + GameData.zoom, -GameData.zoom*(ratio), GameSettings.getScreenY() + GameData.zoom*(ratio), -1, 1);
		
	}

	@Override
	public void mouseInput(ArrayList<Integer> a) {
		
	}

	@Override
	public void keyboardInput(ArrayList<Integer> a) {
		for(Integer i : a){
			if(i == Keyboard.KEY_SPACE){
				StateController.setGameState(Main.IN_GAME);
			}
		}
	}
}
