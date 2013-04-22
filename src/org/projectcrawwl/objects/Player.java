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
		
		this.createBoundingBox();
		
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
		/*
		GL11.glColor3d(.9,.46, 0);
		
		
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
	    GL11.glEnd();*/
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
		
		GL11.glColor4f(1.0f,0,0,1.0f);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);{
			GL11.glVertex2d(5, 85);
			GL11.glVertex2d(5+2*r,85);
			GL11.glVertex2d(5+2*r,85+.5*r);
			GL11.glVertex2d(5, 85+.5*r);
		}
		GL11.glEnd();
		
		
		GL11.glColor4f(0,1.0f,0,1.0f);
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
		//Key and mouse input control
		
		int mouse_x = Mouse.getX();
		int mouse_y = Mouse.getY();
		
		facingAngle = (float) (Math.toDegrees(Math.atan2(mouse_y - renderY, mouse_x - renderX)));
		
		data.setMapXOffset((float) (settings.getScreenX()/2 - x - (mouse_x - settings.getScreenX()/2)*1));
		data.setMapYOffset((float) (settings.getScreenY()/2 - y - (mouse_y - settings.getScreenY()/2)*1));
		
		float temp = 0;
		moveAngle = 0;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){speed = .5; moveAngle += 0; temp += 1;}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){speed = .5; moveAngle += 180; temp+=1;}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){speed = .5; moveAngle += 270;temp+=1;}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){speed = .5; moveAngle += 90;temp+=1;}
		
		if(temp != 0){
			
			if(Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_D)){
				moveAngle += 360;
			}
			
			
			moveAngle = moveAngle/temp;
		}else{
			speed = 0;
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
			}
		}
		
		Mouse.getEventButton();
		
		if(Mouse.isButtonDown(0)){
			inventory.getWeapon().fire();
		}

		super.update(delta);
	}
}