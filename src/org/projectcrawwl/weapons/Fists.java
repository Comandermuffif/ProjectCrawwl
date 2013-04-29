package org.projectcrawwl.weapons;

import org.projectcrawwl.objects.BasePlayer;

public class Fists extends BaseMeleeWeapon{

	public Fists(BasePlayer tempO) {
		super(tempO);
		name = "fists";
		damage = 30;
		angle = 90;
		range = 25;
		coolDown = 150;
	}

}
