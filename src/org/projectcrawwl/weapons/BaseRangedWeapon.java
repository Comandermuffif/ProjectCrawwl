package org.projectcrawwl.weapons;

import java.awt.Point;
import java.util.Random;

import org.newdawn.slick.openal.Audio;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.projectile.BaseProjectile;


public class BaseRangedWeapon extends BaseWeapon{
	double spread;
	float velocity;
	
	int currentClip;
	int maxClip;
	
	int reloadTime;
	int currentReload;
	
	int pellets = 1;
	
	boolean reloading = false;
	
	Audio onFire = null;
	
	protected BaseRangedWeapon(BasePlayer tempO){
		super("BaseRangedWeapon",0);
		owner = tempO;
		damage = 0;
		velocity = 1;
		coolDown = 250;
		currentCoolDown = coolDown;
		spread = 0;
		
		maxClip = 30;
		
		currentClip = 0;
		
		reloadTime = 1000;
		currentReload = reloadTime;
	}
	
	public void render(){
		super.render();
		//BEEP!
	}
	
	public Point getClip(){
		return new Point(currentClip,maxClip);
	}
	
	public void update(int delta){
		
		if(reloading){
			currentReload -= delta;
		}
		
		if(currentReload <= 0){
			reloading = false;
			currentReload = reloadTime;
			currentClip = maxClip;
		}
		
		if(active){
			currentCoolDown -= delta;
		}
		if(currentCoolDown <= 0){
			active = false;
			currentCoolDown = coolDown;
		}
		//BOOP!
	}
	
	public boolean isReloading(){
		return reloading;
	}
	
	public void fire(){
		if(active == false && reloading == false){
			if(currentClip == 0){reloading = true;return;}
			
			if(onFire != null){
				onFire.playAsSoundEffect(1.0f, 1.0f, false);
			}
			
			active = true;
			currentCoolDown = coolDown;
			for(int x = 0; x < pellets; x++){
				GameData data = GameData.getInstance();
				Random random = new Random();
				data.addProjectile(new BaseProjectile((float) (owner.x + Math.cos(Math.toRadians(owner.facingAngle))*(owner.r + 5)),(float) (owner.y + Math.sin(Math.toRadians(owner.facingAngle))*(owner.r+5)),velocity,(float) ((float) owner.facingAngle + random.nextGaussian()*spread), damage, owner));
			}
						
			currentClip -= 1;
		}
	}
}
