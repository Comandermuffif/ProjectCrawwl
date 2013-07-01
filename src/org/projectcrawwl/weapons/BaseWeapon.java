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

	public String toXML() {
		String data = "";
		
		data += "\t\t<BaseWeapon>\n";
		{
			data += "\t\t\t<name>" + name + "</name>\n";
			data += "\t\t\t<damage>" + damage + "</damage>\n";
			data += "\t\t\t<active>" + active + "</active>\n";
			
			data += "\t\t\t<coolDown>" + coolDown + "</coolDown>\n";
			data += "\t\t\t<currentCoolDown>" + currentCoolDown + "</currentCoolDown>\n";
			
			data += "\t\t\t<automatic>" + automatic + "</automatic>\n";
		}
		data += "\t\t</BaseWeapon>\n";
		
		return data;
	}
}
