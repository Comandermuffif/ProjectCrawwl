package org.projectcrawwl.data;

import java.util.ArrayList;

import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.weapons.BaseMeleeWeapon;
import org.projectcrawwl.weapons.BaseWeapon;

public class Inventory {
	private BasePlayer owner;
	private ArrayList<BaseWeapon> weapons = new ArrayList<BaseWeapon>();
	private int counter = 0;
	
	public Inventory(BasePlayer tempO){
		owner = tempO;
		weapons.add(new BaseMeleeWeapon(owner, 25, 90, 25));
	}
	
	public void render(){
		weapons.get(counter).render();
	}
	public void update(int delta){
		for(BaseWeapon a : weapons){
			a.update(delta);
		}
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
	public void mouseWheelMoved(int change) 
	{				
		System.out.print(change);
	}
}
