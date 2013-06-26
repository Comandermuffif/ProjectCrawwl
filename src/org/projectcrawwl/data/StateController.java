package org.projectcrawwl.data;

import java.util.HashMap;

import org.projectcrawwl.GameState;

public class StateController {
	
	private static HashMap<GameState, GameState> states = new HashMap<GameState, GameState>();
	
	private static GameState currentState;
	
	public static void addGameState(GameState a){
		states.put(a,a);
	}
	
	public static void setGameState(GameState a){
		if(currentState != null){
			System.out.println("Leaving " + currentState.getName());
			currentState.onExit();
		}
		currentState = states.get(a);
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
