package org.projectcrawwl.weapons;

import org.projectcrawwl.data.GameData;
import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.projectile.DumbMissle;

public class MissleLauncher extends BaseRangedWeapon{
	public MissleLauncher(BasePlayer tempO){
		super(tempO);
		name = "Missle Launcher";
		
		coolDown = 250;
		currentCoolDown = coolDown;
	}
	public void fire(){
		if(active == false){
			active = true;
			currentCoolDown = coolDown;
			GameData data = GameData.getInstance();
			data.addProjectile(new DumbMissle((float) (owner.x + Math.cos(Math.toRadians(90 - owner.facingAngle))*(owner.r+5)),(float) (owner.y + Math.sin(Math.toRadians(90 - owner.facingAngle))*(owner.r+5)),1,(float) owner.facingAngle, 50, owner));
		}
	}
}
