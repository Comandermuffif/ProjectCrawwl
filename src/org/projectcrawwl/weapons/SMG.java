package org.projectcrawwl.weapons;

import java.util.Random;

import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.projectile.BaseProjectile;
public class SMG extends BaseRangedWeapon{

	
	public SMG(BasePlayer tempO){
		super(tempO);
		name = "SMG";
		
		
		coolDown = 60000/900;
		currentCoolDown = coolDown;
	}
	public void fire(){
		Random random = new Random();
		if(active == false){
			active = true;
			currentCoolDown = coolDown;
			data.addProjectile(new BaseProjectile((float) (owner.x + Math.cos(Math.toRadians(owner.facingAngle))*(owner.r+5)),(float) (owner.y + Math.sin(Math.toRadians(owner.facingAngle))*(owner.r+5)),3,(float) ((float) owner.facingAngle + random.nextGaussian()*1.5), 10, owner));
		}
	}
}
