package org.projectcrawwl.projectile;

import org.projectcrawwl.data.GameData;
import org.projectcrawwl.objects.BasePlayer;

public class DumbMissle extends Bullet{
	
	public BasePlayer target;
	/**
	 * 
	 * @param tempX - X location
	 * @param tempY - Y location
	 * @param tempSpeed - speed
	 * @param tempAngle - angle
	 * @param tempD - Damage
	 * @param tempO - owner
	 */
	public DumbMissle(float tempX, float tempY, float tempSpeed, float tempAngle, double tempD, BasePlayer tempO){
		super( tempX,  tempY,  tempSpeed,  tempAngle,  tempD, tempO);
		facingAngle = tempAngle;
		turnSpeed = .2;
		
	}
	
	public void render(){
		super.render();
	}

	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		
		moveAngle = facingAngle;
		
		if(target == null){
			double dist = 0;
			for(BasePlayer player : GameData.getPlayers()){
				if(dist == 0){
					dist = Math.pow(Math.pow(player.x - x, 2) + Math.pow(player.y - y, 2), .5);
					target = player;
				}else{
					double tempD = Math.pow(Math.pow(player.x - x, 2) + Math.pow(player.y - y, 2), .5);
					if(tempD <  dist){
						dist = tempD;
						target = player;
					}
				}
			}
		}else{
			tempFacing = (float) Math.toDegrees(Math.atan2(target.y - y, target.x - x));
		}
		
	}
}
