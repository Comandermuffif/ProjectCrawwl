package org.projectcrawwl.objects;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.projectcrawwl.Main;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.StateController;
import org.projectcrawwl.data.World;
import org.projectcrawwl.weapons.*;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Player extends BasePlayer {
	
	private Texture texture;
	
	boolean anti = false;
	
	public Player(String filename){
		
		super();
		
		try {
			
			if(!filename.split("\\.")[1].equalsIgnoreCase("Player")){
				System.out.println("Error loading player: " + filename);
				return;
			}
			
			BufferedReader wordStream = new BufferedReader(new FileReader(filename));
			
			String l;
			
			while((l = wordStream.readLine()) != null){
				
				String[] s = l.split("=");
				
				for(int i = 0; i < s.length; i ++){
					s[i] = s[i].trim();
				}
				
				////////////////////////////////////////
				
				if(s[0].equalsIgnoreCase("x")){
					x = Integer.parseInt(s[1]);
				}
				if(s[0].equalsIgnoreCase("y")){
					y = Integer.parseInt(s[1]);
				}
				if(s[0].equalsIgnoreCase("health")){
					health = Integer.parseInt(s[1]);
				}
				if(s[0].equalsIgnoreCase("turnSpeed")){
					turnSpeed = Double.parseDouble(s[1]);
				}
				if(s[0].equalsIgnoreCase("level")){
					level = Integer.parseInt(s[1]);
				}
				
			}
			
			wordStream.close();
			
		}catch(IOException e){e.printStackTrace();}
		
	}
	
	public Player(int tempX, int tempY){
		super(tempX,tempY);
		x = tempX;
		y = tempY;
		facingAngle = 0;
		health = 100;
		turnSpeed = .25;
		
		inventory.addWeapon(new Katana(this));
		inventory.addWeapon(new BaseRangedWeapon(this, "res/Weapons/Ranged/SniperRifle.RangedWeapon"));
		inventory.addWeapon(new BaseRangedWeapon(this, "res/Weapons/Ranged/SMG.RangedWeapon"));
		inventory.addWeapon(new BaseRangedWeapon(this, "res/Weapons/Ranged/Shotgun.RangedWeapon"));
		inventory.addWeapon(new BaseRangedWeapon(this, "res/Weapons/Ranged/Pistol.RangedWeapon"));
		
		this.createBoundingBox();
		
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/player 2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Player() {
		super();
	}

	@Override
	public void createBoundingBox(){
		
		addPoint(25,25);
		addPoint(25,-25);
		addPoint(-25,-25);
		addPoint(-25,25);
		
		updateLines();
	}
	
	
	//Draw everything here
	public void render(){
		if(!isReady){return;}
		super.render();
		
		
		if(anti){
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
			
		}else{
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
		}
		
	
//		float ratio = ((float) (settings.getScreenY())/settings.getScreenX());
//		
//		GL11.glLoadIdentity();
//		GL11.glOrtho(-data.zoom, settings.getScreenX()  + data.zoom, -data.zoom*(ratio), settings.getScreenY() + data.zoom*(ratio), -1, 1);
		
	}

	public void renderHUD(){
		
		super.renderHUD();
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, GameSettings.getScreenX(), 0, GameSettings.getScreenY(), -1, 1);
		
		GL11.glColor3d(0,0,0);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(250, 0);
		GL11.glVertex2d(250, 100);
		GL11.glVertex2d(0, 100);
		GL11.glEnd();
		
		//Re scale view so text is right side up
		GL11.glLoadIdentity();
		GL11.glOrtho(0, GameSettings.getScreenX(), GameSettings.getScreenY(), 0, -1, 1);
		
		GameData.getFont().drawString(20, GameSettings.getScreenY() - 100, "Level: " + getLevel());
		GameData.getFont().drawString(20, GameSettings.getScreenY() - 80, "Weapon: " + inventory.getWeapon().getName());
		if(!(inventory.getWeapon() instanceof BaseMeleeWeapon)){
			if(inventory.getWeapon().isReloading()){
				GameData.getFont().drawString(20, GameSettings.getScreenY() - 60, "Clip: " + "--" + "/" + String.format("%1$02d", inventory.getWeapon().getClip().y) +"/"+String.format("%1$02d", inventory.bullets));
			}else{
				GameData.getFont().drawString(20, GameSettings.getScreenY() - 60, "Clip: " + String.format("%1$02d", inventory.getWeapon().getClip().x) + "/" + String.format("%1$02d", inventory.getWeapon().getClip().y) +"/"+String.format("%1$02d", inventory.bullets));
				
			}
		}
		GameData.getFont().drawString(20, GameSettings.getScreenY() - 40, "Heath: " + health);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		//Reset to zoom
		
		float ratio = ((float) (GameSettings.getScreenY())/GameSettings.getScreenX());
		
		GL11.glLoadIdentity();
		
    	GL11.glAlphaFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	
		GL11.glOrtho(-GameData.zoom, GameSettings.getScreenX()  + GameData.zoom, -GameData.zoom*(ratio), GameSettings.getScreenY() + GameData.zoom*(ratio), -1, 1);
	}
	
	//Do all calculations here
	public void update(int delta){
		
		//Key and mouse input control
		
		int mouse_x = Mouse.getX();
		int mouse_y = Mouse.getY();
		
		tempFacing = (float) (Math.toDegrees(Math.atan2(mouse_y - renderY, mouse_x - renderX)));
		
		if(StateController.getGameState() == Main.IN_GAME){
			GameData.setMapXOffset((float) (GameSettings.getScreenX()/2 - x - (mouse_x - GameSettings.getScreenX()/2)*1));
			GameData.setMapYOffset((float) (GameSettings.getScreenY()/2 - y - (mouse_y - GameSettings.getScreenY()/2)*1));
		}
		
		float temp = 0;
		moveAngle = 0;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){speed = .5; moveAngle += 0; temp += 1;}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){speed = .5; moveAngle += 180; temp+=1;}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){speed = .5; moveAngle += 270;temp+=1;}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){speed = .5; moveAngle += 90;temp+=1;}
		
		speedMult = 1;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			speedMult = 1.5;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
			speedMult = .5;
		}
		
		if(temp != 0){
			
			if(Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_D)){
				moveAngle += 360;
			}
			
			
			moveAngle = moveAngle/temp;
		}else{
			speed = 0;
		}
		
		
		GameData.zoom -= Mouse.getDWheel();
		
		if(GameData.zoom < 0){
			GameData.zoom = 0;
		}
		
		if(GameData.zoom > 360){
			//data.zoom = 360;
		}
		
		if(Mouse.isButtonDown(0) && inventory.getWeapon().isAutomatic()){
			inventory.getWeapon().fire();
		}

		super.update(delta);
	}
	
	@Override
	public void mouseInput(ArrayList<Integer> a){
		for(Integer i : a){
			if(i == 0 && !inventory.getWeapon().isAutomatic()){
				inventory.getWeapon().fire();
			}
		}
	}
	
	@Override
	public void keyboardInput(ArrayList<Integer> a){
		for(Integer i : a){
			if (i == Keyboard.KEY_1){
				inventory.prevWeapon();
			}
			if (i == Keyboard.KEY_2){
				inventory.nextWeapon();
			}
			if (i == Keyboard.KEY_Z){
				GameData.addZombie();
			}
			if (i == Keyboard.KEY_G){
				GameData.addFriendly();
			}
			if (i == Keyboard.KEY_NUMPAD0){
				GameData.zoom = 0;
			}
			if (i == Keyboard.KEY_R){
				inventory.getWeapon().reload();
			}
			
			if (i == Keyboard.KEY_NUMPAD1){
				for(int ii = 0; ii < 10; ii ++){
					GameData.addZombie();
				}
			}
			if(i == Keyboard.KEY_Q){
				anti = !anti;
			}
			
			if(i == Keyboard.KEY_3){
				World.clearHulls();
			}
			
			if(i == Keyboard.KEY_I){
				StateController.setGameState(Main.INVENTORY);
			}
		}
	}
	
	@Override
	public String toXML(){
		String data = "";
		
		data += "<Player>\n";
		{
			
				data += "\t<x>" + x + "</x>\n";
				data += "\t<y>" + y + "</y>\n";
				
				data += "\t<facingAngle>" + facingAngle + "</facingAngle>\n";
				
				data += "\t<moveAngle>" + moveAngle + "</moveAngle>\n";
				
				data += "\t<speed>" + speed + "</speed>\n";
				
				data += "\t<turnSpeed>" + turnSpeed + "</turnSpeed>\n";
				
				data += "\t<health>" + health + "</health>\n";
				
				data += "\t<level>" + level + "</level>\n";
				
				data += "\t<kills>" + kills + "</kills>\n";
				
				data += "\t<boundingBox>\n";
				{
					for(Point p : this.getPoints()){
						data += "\t\t<point>\n";
						{
							data += "\t\t\t\t<pX>" + p.x + "</pX>\n";
							data += "\t\t\t<pY>" + p.y + "</pY>\n";
						}
						data += "\t\t</point>\n";
					}
				}
				data += "\t</boundingBox>\n";
				
			
			
			data += "\t<Inventory>\n";
			{
				for(BaseWeapon w : inventory.getWeapons()){
					data += w.toXML();
				}
			}
			data += "\t</Inventory>\n";
			
		}
		data += "</Player>\n";
		
		return data;
	}
}