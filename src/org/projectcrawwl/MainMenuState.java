package org.projectcrawwl;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.StateController;
import org.projectcrawwl.menu.Button;

public class MainMenuState implements GameState {
	
	private ArrayList<Button> buttons = new ArrayList<Button>();

	@Override
	public void onEnter() {
		
		buttons.add(new Button(540,360,200,100, "Enter game", 1));
		
		buttons.add(new Button(540,250,200,100, "Close game", -1));
		
		System.out.println("Entering main menu state");
		
		Main.data.update(0);
	}

	@Override
	public void onExit() {
		System.out.println("Exiting main menu state");

	}

	@Override
	public void main(int delta) {
		GameData data = Main.data;
		
		Main.data.render();
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, GameSettings.getScreenX(), 0, GameSettings.getScreenY(), -1, 1);
		
		
		GL11.glColor4d(0,0,0, .7);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(GameSettings.getScreenX(), 0);
		GL11.glVertex2d(GameSettings.getScreenX(), GameSettings.getScreenY());
		GL11.glVertex2d(0, GameSettings.getScreenY());
		GL11.glEnd();
		
		float ratio = ((float) (GameSettings.getScreenY())/GameSettings.getScreenX());
		GL11.glLoadIdentity();
		GL11.glOrtho(-data.zoom, GameSettings.getScreenX()  + data.zoom, -data.zoom*(ratio), GameSettings.getScreenY() + data.zoom*(ratio), -1, 1);
		
		for(Button b : buttons){
			b.render();
		}
		
		
	}
	
	@Override
	public void mouseInput(ArrayList<Integer> a) {
		for(Button b : buttons){
			b.mouseInput(a);
		}
	}

	@Override
	public void keyboardInput(ArrayList<Integer> a) {
		for(Integer i : a){
			if(i == Keyboard.KEY_ESCAPE){
				StateController.setGameState(Main.IN_GAME);
			}
		}
	}

}
