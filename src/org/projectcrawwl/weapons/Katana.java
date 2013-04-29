package org.projectcrawwl.weapons;

import org.projectcrawwl.objects.BasePlayer;

public class Katana extends BaseMeleeWeapon{

	public Katana(BasePlayer tempO){
		super(tempO);
		name = "Katana";
		damage = 51;
		angle = 10;
		range = 250;
		coolDown = 100;
	}

}
