package org.projectcrawwl.weapons;

import org.projectcrawwl.data.GameData;
import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.projectile.BaseProjectile;

public class BaseRangedWeapon extends BaseWeapon{
	public BaseRangedWeapon(BasePlayer tempO){
		super("BaseRangedWeapon",0);
		owner = tempO;
		
		coolDown = 250;
		currentCoolDown = coolDown;
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
			data.addProjectile(new BaseProjectile((float) (owner.x + Math.cos(Math.toRadians(owner.facingAngle))*owner.r),(float) (owner.y + Math.sin(Math.toRadians(owner.facingAngle))*owner.r),1,(float) owner.facingAngle, -1, owner));

		}
	}
}
