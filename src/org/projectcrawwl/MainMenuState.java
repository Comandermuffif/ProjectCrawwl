package org.projectcrawwl;

import java.util.ArrayList;

import org.projectcrawwl.menu.Button;

public class MainMenuState implements GameState {
	
	private ArrayList<Button> buttons = new ArrayList<Button>();

	@Override
	public void onEnter() {
		
		Main.data.renderInit();
		
		buttons.add(new Button(0,360,200,100, "Push me", 1));
		
		System.out.println("Entering main menu state");
	}

	@Override
	public void onExit() {
		System.out.println("Exiting main menu state");

	}

	@Override
	public void main(int delta) {
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
		// TODO Auto-generated method stub

	}

}
