package org.projectcrawwl.projectile;

//import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.objects.BasePlayer;

public class DumbMissle extends BaseProjectile{
	
	public BasePlayer target;
	
	public DumbMissle(float tempX, float tempY, float tempSpeed, float tempAngle, float tempD, BasePlayer tempO){
		super( tempX,  tempY,  tempSpeed,  tempAngle,  tempD, tempO);
		r = 5;
	}
	
	public void render(){
		super.render();
		
		GL11.glColor3d(255,0,0);
		
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);{
	    	GL11.glVertex2f(renderX, renderY);
	    	  
	        for (float angle=0; angle<=Math.PI*2; angle+=((Math.PI*2)/32) )
	        {
	        	GL11.glVertex2f( (r)*(float)Math.cos(angle) + getRenderX(),
	        			(r)*(float)Math.sin(angle) + getRenderY());  
	        }
	          
	        GL11.glVertex2f(renderX + r, renderY);
	    }
	    GL11.glEnd();
	}

	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		
		
		GameData data = GameData.getInstance();
		
		//ArrayList<BasePlayer> temp = data.getAllPlayers();
		
		BasePlayer tempT = data.getPlayer();
		double dist2 = -1;
		if(target == null){
			for(BasePlayer a : data.getAI()){
				double dist = Math.pow(Math.pow(a.getX() - getX(), 2) + Math.pow(a.getY() - getY(), 2), .5);
				if(tempT == data.getPlayer()){
					tempT = a;
					dist2 = dist;
				}
				if(dist < dist2){
					tempT = a;
					dist2 = dist;
				}
			}
		}
		
		if(target == null){
			target = tempT;
			System.out.println(target);
		}
		
		
		
		float tempA = (float) (270 - Math.toDegrees(Math.atan2(x - target.getX(), y - target.getY())));
		if(moveAngle < 0){
			moveAngle += 360;
		}
		if(moveAngle > 360){
			moveAngle -= 360;
		}
		if(tempA < 0){
			tempA += 360;
		}
		if(tempA > 360){
			tempA -= 360;
		}
		
		if(moveAngle != tempA){
			System.out.println(moveAngle + " : " + tempA);
			if(moveAngle < tempA){
				moveAngle += 2;
			}
			if(moveAngle > tempA){
				moveAngle -= 2;
			}
		}
		//angle = (float) (270 - Math.toDegrees(Math.atan2(x - target.getX(), y - target.getY())));
		
		
	}
}
