package org.projectcrawwl.weapons;

import java.awt.Point;

import org.projectcrawwl.data.GameData;
import org.projectcrawwl.objects.BasePlayer;

public class BaseWeapon {
	public String name;
	public float damage;
	public BasePlayer owner;
	
	GameData data = GameData.getInstance();
	
	public Boolean active = false;
	public float coolDown;
	public float currentCoolDown;
	
	public BaseWeapon(String tempName, float tempD){
		name = tempName;
		damage = tempD;
	}
	
	public void fire(){
		System.out.println("PEW PEW");
	}
	public void render(){
		//BEEP!
	}
	public void update(int delta){
		//BOOP!
	}
	public String getName(){
		return name;
	}
	public Point getClip(){
		return new Point(0,0);
	}
	public boolean isReloading(){
		return false;
	}
}
