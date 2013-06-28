package org.projectcrawwl.states;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.projectcrawwl.Main;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.StateController;
import org.projectcrawwl.weapons.BaseMeleeWeapon;

public class InventoryState implements GameState {

	@Override
	public void onEnter() {
		
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void main(int delta) {
		
		GameData.update(delta);
		GameData.render();
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, GameSettings.getScreenX(), 0, GameSettings.getScreenY(), -1, 1);
		
		
		GL11.glColor4d(0,0,0, .7);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(GameSettings.getScreenX(), 0);
		GL11.glVertex2d(GameSettings.getScreenX(), GameSettings.getScreenY());
		GL11.glVertex2d(0, GameSettings.getScreenY());
		GL11.glEnd();
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, GameSettings.getScreenX(), GameSettings.getScreenY(), 0, -1, 1);
		GameData.getFont().drawString(15, 15, "Inventory");
		GameData.getFont().drawString(115, 15, "Health: " + (int) GameData.getPlayer().getHealth() + "/" + 100);
		GameData.getFont().drawString(230, 15, "Bullets: " + GameData.getPlayer().getInventory().bullets);
		
		
		for(int i = 0; i < GameData.getPlayer().getInventory().getWeapons().size(); i ++){
			
			GL11.glLoadIdentity();
			GL11.glOrtho(0, GameSettings.getScreenX(), GameSettings.getScreenY(), 0, -1, 1);
			GameData.getFont().drawString(15, 50 + (25*i), 
					GameData.getPlayer().getInventory().getWeapons().get(i).name);
			
			GameData.getFont().drawString(115, 50 + (25*i), 
					"Damage: " + GameData.getPlayer().getInventory().getWeapons().get(i).damage);
			
			if(GameData.getPlayer().getInventory().getWeapons().get(i) instanceof BaseMeleeWeapon){
				GameData.getFont().drawString(230, 50 + (25*i), "Melee Weapon");
				
			}else{
				GameData.getFont().drawString(230, 50 + (25*i), 
						"Clip: " + GameData.getPlayer().getInventory().getWeapons().get(i).getClip().x + " / " + GameData.getPlayer().getInventory().getWeapons().get(i).getClip().y);
				
			}
			
			
			GL11.glDisable(GL11.GL_TEXTURE_2D);
				
		}
		
		GL11.glOrtho(0, GameSettings.getScreenX(), 0, GameSettings.getScreenY(), -1, 1);
		
		
		float ratio = ((float) (GameSettings.getScreenY())/GameSettings.getScreenX());
		GL11.glLoadIdentity();
		GL11.glOrtho(-GameData.zoom, GameSettings.getScreenX()  + GameData.zoom, -GameData.zoom*(ratio), GameSettings.getScreenY() + GameData.zoom*(ratio), -1, 1);
		

	}

	@Override
	public void mouseInput(ArrayList<Integer> a) {
		
	}

	@Override
	public void keyboardInput(ArrayList<Integer> a) {
		for(Integer i : a){
			if(i == Keyboard.KEY_I){
				StateController.setGameState(Main.IN_GAME);
			}
		}
	}

	@Override
	public String getName() {
		return "Inventory";
	}
}