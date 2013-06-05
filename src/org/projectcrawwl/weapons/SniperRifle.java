package org.projectcrawwl.weapons;

import java.io.IOException;

import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.projectcrawwl.objects.BasePlayer;

public class SniperRifle extends BaseRangedWeapon{
	
	public SniperRifle(BasePlayer tempO){
		super(tempO);
		name = "Sniper Rifle";
		velocity = 5;
		damage = 110;
		coolDown = (float) (1000/5);
		
		reloadTime = 3000;
		
		currentCoolDown = coolDown;
		maxClip = 10;
		currentClip = maxClip;
		
		spread = 750;
		spreadAngle = 7.5;
		
		automatic = false;
		
		try {
			onFire = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/Wpn_riflesniper_fire_2d.ogg"));
		} catch (IOException e) {e.printStackTrace();}
	}	
}
