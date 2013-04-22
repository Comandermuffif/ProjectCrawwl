package org.projectcrawwl.weapons;

import org.projectcrawwl.objects.BasePlayer;
public class SMG extends BaseRangedWeapon{

	
	public SMG(BasePlayer tempO){
		super(tempO);
		name = "SMG";
		
		velocity = 3;
		spread = 1.5;
		
		damage = 10;
		
		coolDown = 60000/900;
		currentCoolDown = coolDown;
	}
}
