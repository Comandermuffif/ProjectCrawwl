package org.projectcrawwl.weapons;

import java.io.IOException;

import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.projectcrawwl.objects.BasePlayer;

public class Shotgun extends BaseRangedWeapon{
	public Shotgun(BasePlayer tempO){
		super(tempO);
		name = "Combat Shotgun";
		
		coolDown = (float) (1000/1.5);
		
		reloadTime = 3300;
		
		damage = 25;
		currentCoolDown = coolDown;
		velocity = 3;
		pellets = 9;
		
		cone = 9;
		
		spread = 700;
		spreadAngle = 8;
		
		maxClip = 12;
		currentClip = maxClip;
		
		automatic = false;
		
		try {
			onFire = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/Wpn_shotguncombat_fire_2d.ogg"));
		} catch (IOException e) {e.printStackTrace();}
		
	}
}
