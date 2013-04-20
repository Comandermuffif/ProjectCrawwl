package org.projectcrawwl.weapons;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.objects.BasePlayer;

public class BaseMeleeWeapon extends BaseWeapon{
	public float range;
	public float angle;
	
	/**
	 * A generic Melee Weapon
	 * 
	 * @param tempR range
	 * @param tempA angle
	 * @param tempD damage
	 */
	public BaseMeleeWeapon(BasePlayer tempO, float tempR, float tempA, float tempD){
		super("BaseMeleeWeapon", tempD);
		range = tempR;
		angle = tempA;
		damage = tempD;
		owner = tempO;
		active = false;
		
		coolDown = 250;
		currentCoolDown = coolDown;
	}
	
	
	public void render(){
		super.render();
		if(active){
			GL11.glColor3d(255, 255, 0);
			
			GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		      {
		    	  GL11.glVertex2f(owner.getRenderX(), owner.getRenderY());
		    	  
		        for (double x= Math.toRadians(owner.facingAngle - angle/2); x<=Math.toRadians(owner.facingAngle + angle/2); x+=((Math.PI*2)/32) )
		        {
		        	GL11.glVertex2f( (owner.r + range)*(float)Math.cos(x) + owner.getRenderX(),
		        			(owner.r + range)*(float)Math.sin(x) + owner.getRenderY());  
		        }
		        GL11.glVertex2f( (owner.r + range)*(float)Math.cos(Math.toRadians(owner.facingAngle + angle/2)) + owner.getRenderX(),
	        			(owner.r + range)*(float)Math.sin(Math.toRadians(owner.facingAngle + angle/2)) + owner.getRenderY());  
		        //GL11.glVertex2f(owner.getRenderX()+range, owner.getRenderY());
		      }
		   GL11.glEnd();
		}
	}
	
	public void update(int delta){
		if(active){
			currentCoolDown -= delta;
		}
		if(currentCoolDown <= 0){
			active = false;
			currentCoolDown = coolDown;
		}
		//BOOP!
	}
	
	public void fire(){
		if(active == false){
			active = true;
			currentCoolDown = coolDown;
			
			GameData data = GameData.getInstance();
			ArrayList<BasePlayer> temp = data.getAllPlayers();
			
			for(BasePlayer b : temp){
				
				for(float x = owner.facingAngle - angle/2; x <= owner.facingAngle + angle/2; x +=1){
					double dist = Math.pow(Math.pow(b.getX() - owner.getX() - Math.cos(Math.toRadians(x))*(owner.r+range), 2) + Math.pow(b.getY() - owner.getY()- Math.sin(Math.toRadians(x))*(owner.r+range), 2), .5);
					if(dist < b.r && b != owner){
						//System.out.println("<HIT!>");
						b.damage(damage, owner);
						break;
					}
				}
			}
		}
	}
}
