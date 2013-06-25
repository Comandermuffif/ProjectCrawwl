package org.projectcrawwl.weapons;

import java.awt.Point;

import org.projectcrawwl.objects.BasePlayer;

public class BaseWeapon {
	public String name;
	public double damage;
	public BasePlayer owner;
	
	public Boolean active = false;
	public float coolDown;
	public float currentCoolDown;
	
	protected boolean automatic = true;
	
	public BaseWeapon(String tempName, double tempD){
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
	public void reload(){
		
	}
	
	public boolean isAutomatic(){
		return automatic;
	}
}
