package org.projectcrawwl.weapons;

import org.projectcrawwl.objects.BasePlayer;

public class SniperRifle extends BaseRangedWeapon{
	public SniperRifle(BasePlayer tempO){
		super(tempO);
		name = "Sniper Rifle";
		velocity = 5;
		damage = 90;
		spread = .1;
		coolDown = 250;
		currentCoolDown = coolDown;
	}
}
