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
		
		spread = 300;
		spreadAngle = 6;
		
		damage = 75;
		
		coolDown = 50;
		currentCoolDown = coolDown;
		
		maxClip = 6;
		
		automatic = false;
		
		currentClip = maxClip;
		
		try {
			
			onFire = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/SoundFiles/Wpn_riflelincolns_fire_2d.ogg"));
			//onFire = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/Wpn_pistol_44magnum_fire_2d.ogg"));
		} catch (IOException e) {e.printStackTrace();}
	}
}