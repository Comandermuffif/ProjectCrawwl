package org.projectcrawwl.objects;

import java.awt.Font;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.World;

public class Chest extends GameObject {
	
	private UnicodeFont font;
	
	private static ArrayList<Chest> chests = new ArrayList<Chest>();
	
	/**
	 * Empty constructor
	 */
	@SuppressWarnings("unchecked")
	public Chest(int x, int y){
		super(x,y);
		chests.add(this);
		this.x = x;
		this.y = y;
		
		Font awFont = new Font("Times New Roman", Font.BOLD, 24);
		font = new UnicodeFont(awFont, 12, true, false);
		font.addAsciiGlyphs();
		font.addGlyphs(400, 600);
		font.getEffects().add(new ColorEffect(new java.awt.Color(255,0,0)));
		
		try {
			font.loadGlyphs();
		}catch(SlickException e){e.printStackTrace();}
		
		addPoint(-50, -25);
		addPoint(-50, 25);
		addPoint(50, 25);
		addPoint(50, -25);
		updateLines();
		
		isReady = true;
	}
	
	public void render(){
		
		super.render();
		
		float ratio = ((float) (GameSettings.getScreenY())/GameSettings.getScreenX());
		
		{
			GL11.glLoadIdentity();
			GL11.glOrtho(-GameData.zoom, GameSettings.getScreenX()  + GameData.zoom, GameSettings.getScreenY() + GameData.zoom*(ratio), -GameData.zoom*(ratio), -1, 1);
			
		}
		
		
		
		Chest c = null;
		
		if(GameData.getPlayer() != null){
			for(Chest a : chests){
				if(c == null){
					c = a;
					continue;
				}
				if(a.getCenter().distance(GameData.getPlayer().getCenter()) < c.getCenter().distance(GameData.getPlayer().getCenter())){
					c = a;
				}
			}
		}	
		
		
		if(c == this && c.getCenter().distance(GameData.getPlayer().getCenter()) < 200){
			font.drawString(x + World.getMapXOffset(),GameSettings.getScreenY() - (y - 10 + World.getMapYOffset()), "Press E");
		}
		
		

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		{
			GL11.glLoadIdentity();
			GL11.glOrtho(-GameData.zoom, GameSettings.getScreenX()  + GameData.zoom, -GameData.zoom*(ratio), GameSettings.getScreenY() + GameData.zoom*(ratio), -1, 1);
			
		}
		
	}
}
