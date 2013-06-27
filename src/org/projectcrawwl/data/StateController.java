package org.projectcrawwl.data;

import org.projectcrawwl.states.GameState;

public class StateController {
	
	private static GameState currentState;
	
	public static void setGameState(GameState a){
		if(currentState != null){
			System.out.println("Leaving " + currentState.getName());
			currentState.onExit();
		}
		currentState = a;
		System.out.println("Entering " + currentState.getName());
		currentState.onEnter();
	}
	
	public static void update(int delta){
		InputController.update(delta);
		currentState.main(delta);
	}
	
	public static GameState getGameState(){
		return currentState;
	}
}
