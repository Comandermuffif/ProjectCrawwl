package org.projectcrawwl.states;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.Main;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.StateController;
import org.projectcrawwl.objects.BasePlayer;

public class DeathState implements GameState {
	
	private BasePlayer p;

	@Override
	public void onEnter() {
		p = GameData.getPlayer();
		GameData.clearPlayer();
	}

	@Override
	public void onExit() {
		
	}

	@Override
	public void main(int delta) {
		
		GameData.render();
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, GameSettings.getScreenX(), GameSettings.getScreenY(), 0, -1, 1);
		
		GL11.glColor4d(0,0,0, .7);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(GameSettings.getScreenX(), 0);
		GL11.glVertex2d(GameSettings.getScreenX(), GameSettings.getScreenY());
		GL11.glVertex2d(0, GameSettings.getScreenY());
		GL11.glEnd();
		
		String s = "You had ";
		
		if(p.kills == 1){
			s += p.kills + " kill ";
		}else{
			s += p.kills + " kills ";
		}
		
		 s += "and reached level " + p.level;
		
		GameData.getFont().drawString(GameSettings.getScreenX()/2 - GameData.getFont().getWidth(s)/2, GameSettings.getScreenY()/2 - (GameData.getFont().getHeight(s)/2), s);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		

	}

	@Override
	public void mouseInput(ArrayList<Integer> a) {

	}

	@Override
	public void keyboardInput(ArrayList<Integer> a) {
		if(a.size() != 0){
			StateController.setGameState(Main.MAIN_MENU);
		}
	}

}
