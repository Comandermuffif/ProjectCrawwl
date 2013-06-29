package org.projectcrawwl.states;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.projectcrawwl.Main;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.StateController;

public class InGameState implements GameState{
	
	@Override
	public void onEnter() {
		
	}

	@Override
	public void onExit() {
		
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
				StateController.setGameState(Main.PAUSE_MENU);
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
