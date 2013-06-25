package org.projectcrawwl;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.StateController;

public class InGameState implements GameState{

	@Override
	public void onEnter() {
		
		System.out.println("Entering in-game state");
		
	}

	@Override
	public void onExit() {
		
		System.out.println("Exiting in-game state");
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
}
