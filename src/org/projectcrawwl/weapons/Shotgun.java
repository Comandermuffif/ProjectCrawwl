package org.projectcrawwl.weapons;

import java.util.Random;

import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.projectile.BaseProjectile;

public class Shotgun extends BaseRangedWeapon{
	private int pellets;
	public Shotgun(BasePlayer tempO){
		super(tempO);
		name = "Shotgun";
		spread = 3;
		coolDown = 1000;
		damage = 25;
		currentCoolDown = coolDown;
		velocity = 3;
		pellets = 10;
		
	}
	public void fire(){
		if(active == false){
			active = true;
			currentCoolDown = coolDown;
			for(int x = 0; x < pellets; x++){
				Random random = new Random();
				float temp = (float) (random.nextGaussian()*spread);
				data.addProjectile(new BaseProjectile((float) (owner.x + Math.cos(Math.toRadians(owner.facingAngle))*(owner.r+5)),(float) (owner.y + Math.sin(Math.toRadians(owner.facingAngle))*(owner.r+5)),velocity,(float) owner.facingAngle + temp, damage, owner));
			}
		}
	}
}
