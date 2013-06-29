package org.projectcrawwl.data;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputController{
	
	private static ArrayList<Integer> mouse = new ArrayList<Integer>();
	
	private static ArrayList<Integer> keyboard = new ArrayList<Integer>();
	
	public static void update(int delta){
		
		mouse.clear();
		keyboard.clear();
		
		
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
