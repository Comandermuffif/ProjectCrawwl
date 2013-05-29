package org.projectcrawwl.weapons;

import java.io.IOException;

import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.projectcrawwl.objects.BasePlayer;
public class Pistol extends BaseRangedWeapon{
	
	public Pistol(BasePlayer tempO){
		super(tempO);
		name = "Pistol";
		
		velocity = 3;
		
		spread = 600;
		spreadAngle = 4;
		
		damage = 10;
		
		coolDown = 500;
		currentCoolDown = coolDown;
		
		maxClip = 6;
		
		currentClip = maxClip;
		
		try {
			
			onFire = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/Wpn_riflelincolns_fire_2d.ogg"));
			//onFire = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/Wpn_pistol_44magnum_fire_2d.ogg"));
		} catch (IOException e) {e.printStackTrace();}
	}
}