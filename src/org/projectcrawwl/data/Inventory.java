package org.projectcrawwl.data;

import java.util.ArrayList;

import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.weapons.BaseWeapon;
import org.projectcrawwl.weapons.Fists;

public class Inventory {
	
	public static final ArrayList<String> ammo = new ArrayList<String>();
	
	public int bullets = 99;
	
	private BasePlayer owner;
	private ArrayList<BaseWeapon> weapons = new ArrayList<BaseWeapon>();
	private int counter = 0;
	
	public Inventory(BasePlayer tempO){
		owner = tempO;
		weapons.add(new Fists(owner));
	}
	
	public void render(){
		weapons.get(counter).render();
	}
	public void update(int delta){
		
		weapons.get(counter).update(delta);
	}
	
	public void setWeapon(int n){
		
		counter = n % (weapons.size()-1);
		
	}
	
	public void addWeapon(BaseWeapon tempW){
		weapons.add(tempW);
	}
	
	public BaseWeapon getWeapon(){
		return weapons.get(counter);
	}
	
	public void nextWeapon(){
		counter ++;
		if(counter >= weapons.size()){
			counter = 0;
		}
	}
	
	public void prevWeapon(){
		counter --;
		if(counter < 0){
			counter = weapons.size()-1;
		}
	}

	public ArrayList<BaseWeapon> getWeapons() {
		return weapons;
	}
}
