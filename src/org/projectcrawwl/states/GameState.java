package org.projectcrawwl.states;

import java.util.ArrayList;

public interface GameState {
	public void onEnter();
	public void onExit();
	public void main(int delta);
	public int hashCode();
	public void mouseInput(ArrayList<Integer> a);
	public void keyboardInput(ArrayList<Integer> a);
}
