package org.projectcrawwl.weapons;

import java.io.IOException;

import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.projectcrawwl.objects.BasePlayer;

public class Shotgun extends BaseRangedWeapon{
	public Shotgun(BasePlayer tempO){
		super(tempO);
		name = "Shotgun";
		spread = 3;
		coolDown = 1000;
		damage = 25;
		currentCoolDown = coolDown;
		velocity = 3;
		pellets = 10;
		
		maxClip = 8;
		currentClip = 8;
		
		try {
			onFire = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/Wpn_shotguncombat_fire_2d.ogg"));
		} catch (IOException e) {e.printStackTrace();}
		
	}
}
