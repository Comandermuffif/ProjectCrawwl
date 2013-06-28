package org.projectcrawwl.states;

import java.util.ArrayList;

import org.projectcrawwl.data.GameData;

public class PauseState implements GameState {

	@Override
	public String getName() {
		return "Paused";
	}

	@Override
	public void onEnter() {
		
	}

	@Override
	public void onExit() {

	}

	@Override
	public void main(int delta) {
		GameData.render();
		
		
		
	}

	@Override
	public void mouseInput(ArrayList<Integer> a) {

	}

	@Override
	public void keyboardInput(ArrayList<Integer> a) {

	}

}
