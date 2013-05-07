package org.projectcrawwl.weapons;

import java.util.Random;

import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.projectile.BaseProjectile;

public class Shotgun extends BaseRangedWeapon{
	public Shotgun(BasePlayer tempO){
		super(tempO);
		name = "Shotgun";
		
		coolDown = 1000;
		currentCoolDown = coolDown;
	}
	public void fire(){
		if(active == false){
			active = true;
			currentCoolDown = coolDown;
			for(int x = 0; x < 10; x++){
				Random random = new Random();
				float temp = (float) (random.nextGaussian()*3);
				data.addProjectile(new BaseProjectile((float) (owner.x + Math.cos(Math.toRadians(owner.facingAngle))*(owner.r+5)),(float) (owner.y + Math.sin(Math.toRadians(owner.facingAngle))*(owner.r+5)),3,(float) owner.facingAngle + temp, 10, owner));
			}
		}
	}
}
