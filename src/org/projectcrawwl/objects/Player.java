package org.projectcrawwl.objects;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.projectcrawwl.weapons.*;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Player extends BasePlayer {
	
	private Texture texture;
	
	public Player(int tempX, int tempY){
		super(tempX,tempY);
		x = tempX;
		y = tempY;
		r = 25;
		facingAngle = 0;
		health = 100;
		turnSpeed = .1;//.25;
		
		inventory.addWeapon(new Katana(this));
		inventory.addWeapon(new SniperRifle(this));
		inventory.addWeapon(new SMG(this));
		inventory.addWeapon(new Shotgun(this));
		inventory.addWeapon(new DuelPistols(this));
		//inventory.addWeapon(new LaserRifle(this));
		//inventory.addWeapon(new MissleLauncher(this));
		
		this.createBoundingBox();
		
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/player 2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
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
	
		float ratio = ((float) (settings.getScreenY())/settings.getScreenX());
		
		GL11.glLoadIdentity();
		GL11.glOrtho(-data.zoom, settings.getScreenX()  + data.zoom, -data.zoom*(ratio), settings.getScreenY() + data.zoom*(ratio), -1, 1);
		
		/*
		Color.white.bind();
		texture.bind();
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0,1);
			GL11.glVertex2f(renderX - 25,renderY - 25);
			GL11.glTexCoord2f(0,0);
			GL11.glVertex2f(renderX + 25, renderY - 25);
			GL11.glTexCoord2f(1,0);
			GL11.glVertex2f(renderX + 25, renderY + 25);
			GL11.glTexCoord2f(1,1);
			GL11.glVertex2f(renderX - 25, renderY + 25);
			
		GL11.glEnd();*/
		
		
		
	}

	public void renderHUD(){
		
		super.renderHUD();
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, settings.getScreenX(), 0, settings.getScreenY(), -1, 1);
		
		GL11.glColor3d(0,0,0);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(250, 0);
		GL11.glVertex2d(250, 100);
		GL11.glVertex2d(0, 100);
		GL11.glEnd();
		
		
		//Re scale view so text is right side up
		GL11.glLoadIdentity();
		GL11.glOrtho(0, settings.getScreenX(), settings.getScreenY(), 0, -1, 1);
		
		
		data.getFont().drawString(20, settings.getScreenY() - 80, "Weapon: " + inventory.getWeapon().getName());
		if(inventory.getWeapon().isReloading()){
			data.getFont().drawString(20, settings.getScreenY() - 60, "Clip: Reloading...");
		}else{
			data.getFont().drawString(20, settings.getScreenY() - 60, "Clip: " + inventory.getWeapon().getClip().x + "/" + inventory.getWeapon().getClip().y);
		}
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		//Reset to zoom
		
		float ratio = ((float) (settings.getScreenY())/settings.getScreenX());
		
		GL11.glLoadIdentity();
		
    	GL11.glAlphaFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	
		GL11.glOrtho(-data.zoom, settings.getScreenX()  + data.zoom, -data.zoom*(ratio), settings.getScreenY() + data.zoom*(ratio), -1, 1);
	}
	
	//Do all calculations here
	public void update(int delta){
		
		//updateViewCone();
		
		//Key and mouse input control
		
		int mouse_x = Mouse.getX();
		int mouse_y = Mouse.getY();
		
		tempFacing = (float) (Math.toDegrees(Math.atan2(mouse_y - renderY, mouse_x - renderX)));
		
		data.setMapXOffset((float) (settings.getScreenX()/2 - x - (mouse_x - settings.getScreenX()/2)*1));
		data.setMapYOffset((float) (settings.getScreenY()/2 - y - (mouse_y - settings.getScreenY()/2)*1));
		
		float temp = 0;
		moveAngle = 0;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){speed = .5; moveAngle += 0; temp += 1;}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){speed = .5; moveAngle += 180; temp+=1;}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){speed = .5; moveAngle += 270;temp+=1;}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){speed = .5; moveAngle += 90;temp+=1;}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_L)){}
		if(Keyboard.isKeyDown(Keyboard.KEY_J)){}
		if(Keyboard.isKeyDown(Keyboard.KEY_K)){}
		if(Keyboard.isKeyDown(Keyboard.KEY_I)){}
		
		
		
		if(temp != 0){
			
			if(Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_D)){
				moveAngle += 360;
			}
			
			
			moveAngle = moveAngle/temp;
		}else{
			speed = 0;
		}
		
		
		data.zoom -= Mouse.getDWheel();
		
		
		if(data.zoom < 0){
			data.zoom = 0;
		}
		
		while(Keyboard.next()){
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_1){
					inventory.prevWeapon();
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_2){
					inventory.nextWeapon();
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE){
					data.addZombie();
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_G){
					data.addFriendly();
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD0){
					data.zoom = 0;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_R){
					inventory.getWeapon().reload();
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD1){
					for(int i = 0; i < 10; i ++){
						data.addZombie();
					}
				}
				
			}
		}
		
		Mouse.getEventButton();
		
		if(Mouse.isButtonDown(0)){
			inventory.getWeapon().fire();
		}

		super.update(delta);
	}
}