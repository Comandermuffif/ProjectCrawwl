package org.projectcrawwl.weapons;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.projectile.BaseProjectile;
public class DuelPistols extends BaseRangedWeapon{

	private boolean left = false;
	
	public DuelPistols(BasePlayer tempO){
		super(tempO);
		name = "Duel Pistols";
		
		velocity = 3;
		spread = 1.5;
		
		damage = 10;
		
		coolDown = 500;
		currentCoolDown = coolDown;
		
		maxClip = 16;
		
		currentClip = maxClip;
		
		try {
			onFire = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/Wpn_pistol_44magnum_fire_2d.ogg"));
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void fire(){
		
		if(active == false && reloading == false){
			
			if(currentClip == 0){reloading = true;return;}
			
			
			active = true;
			currentCoolDown = coolDown;
			
			if(onFire != null){
				onFire.playAsSoundEffect(1.0f, 1.0f, false);
			}
			
			
			Random random = new Random();
			float temp = (float) (random.nextGaussian()*1);
			Math.atan2(Mouse.getY() - (owner.getRenderY() + Math.sin(Math.toRadians(owner.facingAngle + 45))*(40)), Mouse.getX() - (owner.renderX + Math.cos(Math.toRadians(owner.facingAngle + 30))*(40)));
			
			if(left){
				data.addProjectile(new BaseProjectile((float) (owner.x + Math.cos(Math.toRadians(owner.facingAngle + 30))*(40)),(float) (owner.y + Math.sin(Math.toRadians(owner.facingAngle + 45))*(40)),velocity,(float) Math.toDegrees(Math.atan2(Mouse.getY() - (owner.getRenderY() + Math.sin(Math.toRadians(owner.facingAngle + 45))*(40)), Mouse.getX() - (owner.renderX + Math.cos(Math.toRadians(owner.facingAngle + 30))*(40)))) + temp, damage, owner));
			}else{
				data.addProjectile(new BaseProjectile((float) (owner.x + Math.cos(Math.toRadians(owner.facingAngle - 30))*(40)),(float) (owner.y + Math.sin(Math.toRadians(owner.facingAngle - 45))*(40)),velocity,(float) Math.toDegrees(Math.atan2(Mouse.getY() - (owner.getRenderY() + Math.sin(Math.toRadians(owner.facingAngle - 45))*(40)), Mouse.getX() - (owner.renderX + Math.cos(Math.toRadians(owner.facingAngle - 30))*(40)))) + temp, damage, owner));
			}
			left = !left;
			
			currentClip -= 1;
		}
	}
}