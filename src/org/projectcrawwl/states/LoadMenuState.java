package org.projectcrawwl.states;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.projectcrawwl.Main;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.StateController;
import org.projectcrawwl.menu.Button;

public class LoadMenuState implements GameState{

	private ArrayList<Button> buttons = new ArrayList<Button>();
	
	@Override
	public String getName() {
		return "Load Menu";
	}

	@Override
	public void onEnter() {
		
		buttons.clear();
		
		buttons.add(new Button(GameSettings.getScreenX()/2 - 100, GameSettings.getScreenY()/2 + 160,200,100, "Load Save 1", 11));
		
		buttons.add(new Button(GameSettings.getScreenX()/2 - 100, GameSettings.getScreenY()/2 + 50,200,100, "Load Save 2", 12));
		
		buttons.add(new Button(GameSettings.getScreenX()/2 - 100, GameSettings.getScreenY()/2 - 60,200,100, "Load Save 3", 13));
		
		buttons.add(new Button(GameSettings.getScreenX()/2 - 100, GameSettings.getScreenY()/2 - 170,200,100, "Load Save 4", 14));
		
		buttons.add(new Button(GameSettings.getScreenX()/2 - 100, GameSettings.getScreenY()/2 - 280,200,100, "Back", 2));
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void main(int delta){
		
		GameData.render();
		
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
		GL11.glOrtho(-GameData.zoom, GameSettings.getScreenX()  + GameData.zoom, -GameData.zoom*(ratio), GameSettings.getScreenY() + GameData.zoom*(ratio), -1, 1);
		
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
				StateController.setGameState(Main.MAIN_MENU);
			}
		}
	}

}
