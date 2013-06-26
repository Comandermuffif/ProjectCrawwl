package org.projectcrawwl;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.StateController;

public class InGameState implements GameState{

	private int mouseX = -1;
	private int mouseY = -1;
	
	@Override
	public void onEnter() {
		
		if(mouseX == -1){
			mouseX = (int) (GameSettings.getScreenX()*(.5));
		}
		
		if(mouseY == -1){
			mouseY = (int) (GameSettings.getScreenY()*(.5));
		}
		
		Mouse.setCursorPosition(mouseX, mouseY);
		
	}

	@Override
	public void onExit() {
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();
		
		System.out.println(mouseX + " " + mouseY);
	}

	@Override
	public void main(int delta) {
		GameData.update(delta);
		
		GameData.render();
	}
	
	@Override
	public void mouseInput(ArrayList<Integer> a) {
		
		if(GameData.getPlayer() != null){
			GameData.getPlayer().mouseInput(a);
		}
	}

	@Override
	public void keyboardInput(ArrayList<Integer> a) {
		
		for(Integer i : a){
			if(i == Keyboard.KEY_ESCAPE){
				StateController.setGameState(Main.MAIN_MENU);
			}
		}
		
		if(GameData.getPlayer() != null){
			GameData.getPlayer().keyboardInput(a);
		}
	}

	@Override
	public String getName() {
		return "In-Game";
	}
}
