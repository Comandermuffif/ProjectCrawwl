package org.projectcrawwl.weapons;

import org.projectcrawwl.objects.BasePlayer;

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
}
