package org.projectcrawwl.weapons;

import org.projectcrawwl.objects.BasePlayer;

public class Fists extends BaseMeleeWeapon{

	public Fists(BasePlayer tempO) {
		super(tempO);
		name = "Fists";
		damage = 30;
		angle = 90;
		range = 50;
		coolDown = 150;
		
		createArea();
	}

}
