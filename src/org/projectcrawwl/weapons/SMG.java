package org.projectcrawwl.weapons;

import java.io.IOException;

import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
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
		
		maxClip = 25;
		currentClip = 25;
		
		try {
			onFire = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/Wpn_rifleassault_fire_2d.ogg"));
		} catch (IOException e) {e.printStackTrace();}
	}
}
