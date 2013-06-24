package org.projectcrawwl;

import java.util.ArrayList;

public class InGameState implements GameState{

	@Override
	public void onEnter() {
		
		if(Main.data.getPlayer() == null){
			Main.data.addPlayer();
		}
		
		Main.data.renderInit();
		
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
		Main.data.getPlayer().mouseInput(a);
	}

	@Override
	public void keyboardInput(ArrayList<Integer> a) {
		
		Main.data.getPlayer().keyboardInput(a);
		
	}
}
