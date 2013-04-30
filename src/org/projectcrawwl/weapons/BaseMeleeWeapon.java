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
	protected BaseMeleeWeapon(BasePlayer tempO){
		//BaseMeleeWeapon(this, 25, 90, 10)
		super("BaseMeleeWeapon", 0);
		range = 25;
		angle = 90;
		damage = 10;
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
				
				double bitches = Math.toDegrees(Math.atan2(b.y - owner.y, b.x - owner.x));
				
				double dist = Math.pow(Math.pow(b.x - owner.x, 2) + Math.pow(b.y - owner.y, 2), .5);
				
				if(bitches < owner.facingAngle + angle/2 && bitches > owner.facingAngle - angle/2 && dist <= range){
					//System.out.println("<HIT!>");
					b.damage(damage, owner);
				}
			}
		}
	}
}
