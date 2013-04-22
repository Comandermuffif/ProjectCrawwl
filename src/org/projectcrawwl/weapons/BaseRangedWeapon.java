package org.projectcrawwl.weapons;

import java.util.Random;

import org.projectcrawwl.data.GameData;
import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.projectile.BaseProjectile;


public class BaseRangedWeapon extends BaseWeapon{
	double spread;
	float velocity;
	public BaseRangedWeapon(BasePlayer tempO){
		super("BaseRangedWeapon",0);
		owner = tempO;
		damage = 0;
		velocity = 1;
		coolDown = 250;
		currentCoolDown = coolDown;
		spread = 0;
	}
	
	public void render(){
		super.render();
		//BEEP!
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
			Random random = new Random();
			data.addProjectile(new BaseProjectile((float) (owner.x + Math.cos(Math.toRadians(owner.facingAngle))*(owner.r + 5)),(float) (owner.y + Math.sin(Math.toRadians(owner.facingAngle))*(owner.r+5)),velocity,(float) ((float) owner.facingAngle + random.nextGaussian()*spread), damage, owner));

		}
	}
}
