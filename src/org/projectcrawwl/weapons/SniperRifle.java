package org.projectcrawwl.weapons;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.projectcrawwl.objects.BasePlayer;

public class SniperRifle extends BaseRangedWeapon{
	
	public Audio onFire;
	
	public SniperRifle(BasePlayer tempO){
		super(tempO);
		name = "Sniper Rifle";
		velocity = 5;
		damage = 90;
		spread = .1;
		coolDown = 250;
		currentCoolDown = coolDown;
		
		try {
			onFire = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/Wpn_riflelincolns_fire_2d.ogg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void fire(){
		onFire.playAsSoundEffect(1.0f, 1.0f, false);
		super.fire();
	}
	
}
