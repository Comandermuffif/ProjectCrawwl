package org.projectcrawwl.data;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputController{
	public static void update(int delta){
		
		ArrayList<Integer> mouse = new ArrayList<Integer>();
		
		ArrayList<Integer> keyboard = new ArrayList<Integer>();
		
		while(Mouse.next()){
			if(Mouse.getEventButtonState()){
				mouse.add(Mouse.getEventButton());
			}
		}
		
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				keyboard.add(Keyboard.getEventKey());
			}
		}
		
		StateController.getGameState().mouseInput(mouse);
		StateController.getGameState().keyboardInput(keyboard);
		
	}
}
