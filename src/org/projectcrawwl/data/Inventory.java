package org.projectcrawwl.data;

import java.util.ArrayList;

import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.weapons.BaseWeapon;
import org.projectcrawwl.weapons.Fists;

public class Inventory {
	private BasePlayer owner;
	private ArrayList<BaseWeapon> weapons = new ArrayList<BaseWeapon>();
	private int counter = 0;
	
	BaseWeapon fists;
	
	public Inventory(BasePlayer tempO){
		owner = tempO;
		fists = new Fists(owner);
	}
	
	public void render(){
		if(weapons.size() == 0){
			fists.render();
		}else{
			weapons.get(counter).render();
		}
		
	}
	public void update(int delta){
		for(BaseWeapon a : weapons){
			a.update(delta);
		}
	}
	
	public void setWeapon(int n){
		
		counter = n % (weapons.size()-1);
		
		//counter = n;
	}
	
	public void addWeapon(BaseWeapon tempW){
		weapons.add(tempW);
	}
	
	public BaseWeapon getWeapon(){
		if(weapons.size() == 0){
			return fists;
		}
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
}
