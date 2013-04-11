package org.projectcrawwl.objects;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.projectcrawwl.weapons.BaseMeleeWeapon;
import org.projectcrawwl.weapons.SMG;
import org.projectcrawwl.weapons.Shotgun;
import org.projectcrawwl.weapons.SniperRifle;

public class Player extends BasePlayer {
	
	public Player(int tempX, int tempY){
		super(tempX,tempY);
		x = tempX;
		y = tempY;
		r = 25;
		facingAngle = 0;
		health = 100;
		
		inventory.addWeapon(new BaseMeleeWeapon(this, 25, 90, 10));
		inventory.addWeapon(new SniperRifle(this));
		inventory.addWeapon(new SMG(this));
		inventory.addWeapon(new Shotgun(this));
		
	}
	public Player(){
		super();
		x = 0;
		y = 0;
		r = 25;
		facingAngle = 0;
		health = 100;
	}
	
	//Draw everything here
	public void render(){
		super.render();
		
		GL11.glColor3d(1, 0, 0);
			
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
	      {
	    	GL11.glVertex2f(renderX,renderY);
	    	  
	        for (float angle=0; angle<=Math.PI*2; angle+=((Math.PI*2)/32) )
	        {
	        	GL11.glVertex2f( (r)*(float)Math.cos(angle) + renderX,
	        			(r)*(float)Math.sin(angle) + renderY);  
	        }
	          
	        GL11.glVertex2f(renderX + r,renderY);
	      }
	    GL11.glEnd();
	    
	    GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glColor3d(0, 0, 0);
		GL11.glVertex2d(Mouse.getX(), Mouse.getY());
		GL11.glVertex2f(renderX, renderY);
		GL11.glVertex2d(renderX + Math.sin(Math.toRadians(facingAngle))*r, renderY + Math.cos(Math.toRadians(facingAngle))*r);
		GL11.glEnd();
	}

	public void renderHUD(){
		super.renderHUD();
		GL11.glColor4d(.25,.25,.25,1.0f);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);{
			GL11.glVertex2d(5, 5);
			GL11.glVertex2d(230,5);
			GL11.glVertex2d(230,105);
			GL11.glVertex2d(5, 105);
		}
		GL11.glEnd();
		
		//g.setColor(new Color(255,255,255));
		//g.drawString("Kills: " + kills, 5, 25);
		//g.drawString("Weapon: " + inventory.getWeapon().getName(), 5, 45);
		
		GL11.glColor3f(1.0f,0,0);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);{
			GL11.glVertex2d(5, 85);
			GL11.glVertex2d(5+2*r,85);
			GL11.glVertex2d(5+2*r,85+.5*r);
			GL11.glVertex2d(5, 85+.5*r);
		}
		GL11.glEnd();
		
		
		GL11.glColor3f(0,1.0f,0);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);{
			GL11.glVertex2d(5, 85);
			GL11.glVertex2d(5+2*r*health/100,85);
			GL11.glVertex2d(5+2*r*health/100,85+.5*r);
			GL11.glVertex2d(5, 85+.5*r);
		}
		GL11.glEnd();
	}
	
	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		//Key and mouse input control
		
		int mouse_x = Mouse.getX();
		int mouse_y = Mouse.getY();
		
		facingAngle = (float) (Math.toDegrees(Math.atan2(renderX - mouse_x, renderY - mouse_y)) + 180);
		
		data.setMapXOffset((float) (settings.getScreenX()/2 - x - (mouse_x - settings.getScreenX()/2)*1));
		data.setMapYOffset((float) (settings.getScreenY()/2 - y - (mouse_y - settings.getScreenY()/2)*1));
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){speed = .5; moveAngle = 360-facingAngle;}else
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){speed = .5; moveAngle = 180-facingAngle;}else
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){speed = .5; moveAngle = 270-facingAngle;}else
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){speed = .5; moveAngle = 90-facingAngle;}else{
			speed = 0;
		}
			
		if(Keyboard.isKeyDown(Keyboard.KEY_1)){inventory.prevWeapon();}
		if(Keyboard.isKeyDown(Keyboard.KEY_2)){inventory.nextWeapon();}
				
		if(Mouse.isButtonDown(0)){
			inventory.getWeapon().fire();
		}
		
	}
}