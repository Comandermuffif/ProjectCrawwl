package org.projectcrawwl;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.projectcrawwl.data.StateController;
import org.projectcrawwl.data.World;

public class InGameState implements GameState{

	@Override
	public void onEnter() {
		
		World.generateWorld();
		
		if(Main.data.getPlayer() == null){
			Main.data.addPlayer();
		}
		
		System.out.println("Entering in-game state");
		
	}

	@Override
	public void onExit() {
		
		System.out.println("Exiting in-game state");
	}

	@Override
	public void main(int delta) {
		Main.data.update(delta);
		
		Main.data.render();
	}
	
	@Override
	public void mouseInput(ArrayList<Integer> a) {
		
		if(Main.data.getPlayer() != null){
			Main.data.getPlayer().mouseInput(a);
		}
	}

	@Override
	public void keyboardInput(ArrayList<Integer> a) {
		
		for(Integer i : a){
			if(i == Keyboard.KEY_ESCAPE){
				StateController.setGameState(Main.MAIN_MENU);
			}
		}
		
		if(Main.data.getPlayer() != null){
			Main.data.getPlayer().keyboardInput(a);
		}
	}
}
