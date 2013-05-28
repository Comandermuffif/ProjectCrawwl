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
		
		spread = 1;
		spreadCooldown = 1.5;
		
		damage = 10;
		
		coolDown = 1000/11;
		currentCoolDown = coolDown;
		
		reloadTime = 2200;
		
		maxClip = 25;
		currentClip = 25;
		
		try {
			onFire = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/Wpn_45_smg_2d_01.ogg"));
		} catch (IOException e) {e.printStackTrace();}
	}
}
