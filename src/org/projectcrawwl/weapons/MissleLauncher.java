package org.projectcrawwl.weapons;

import org.projectcrawwl.data.GameData;
import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.projectile.DumbMissle;

public class MissleLauncher extends BaseRangedWeapon{
	public MissleLauncher(BasePlayer tempO){
		super(tempO);
		name = "Missle Launcher";
		
		damage = 50;
		velocity = 1;
		spread = 0;
		coolDown = 250;
		currentCoolDown = coolDown;
	}
	public void fire(){
		if(active == false){
			active = true;
			currentCoolDown = coolDown;
			
			GameData.addProjectile(new DumbMissle((float) (owner.x + Math.cos(Math.toRadians(owner.facingAngle))*(owner.getFarthest())),(float) (owner.y + Math.sin(Math.toRadians(owner.facingAngle))*(owner.getFarthest())),velocity,(float) ((float) owner.facingAngle), damage, owner));
			
		}
	}
}
