package org.projectcrawwl.data;

import java.util.ArrayList;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;
import org.projectcrawwl.objects.*;
import org.projectcrawwl.projectile.BaseProjectile;

public class GameData 
{	
	private ArrayList<BaseProjectile> projectiles = new ArrayList<BaseProjectile>();//Projectiles
	private ArrayList<BaseProjectile> tempProjectiles = new ArrayList<BaseProjectile>();//Projectiles to be removed

	private BasePlayer player;

	private ArrayList<BasePlayer> allPlayers = new ArrayList<BasePlayer>();
	
	private ArrayList<BasePlayer> addPlayers = new ArrayList<BasePlayer>();
	
	private ArrayList<BasePlayer> removePlayers = new ArrayList<BasePlayer>();
	
	private static GameData instance = null;
	
	private int ups = 0;
	
	private World world = World.getInstance();
	
	public int getUPS(){
		return ups;
	}
	
	public void setUPS(int temp){
		ups = temp;
	}
	
	public float getMapXOffset(){
		return world.getMapXOffset();
	}
	public float getMapYOffset(){
		return world.getMapYOffset();
	}
	
	public float getGridX(){
		return world.getGridX();
	}
	public float getGridY(){
		return world.getGridY();
	}
	
	public void setMapXOffset(float temp){
		world.setMapXOffset(temp);
	}
	public void setMapYOffset(float temp){
		world.setMapYOffset(temp);
	}
	
	public float getMapX(){
		return world.getMapX();
	}
	public float getMapY(){
		return world.getMapY();
	}
	
	public static GameData getInstance()
	{
		if(instance == null)
			instance = new GameData();
		return instance;
	}
	public BasePlayer getPlayer(){
		return player;
	}
	public ArrayList<BasePlayer> getAI(){
		ArrayList<BasePlayer> temp = new ArrayList<BasePlayer>();
		for(BasePlayer a : allPlayers){
			if(a instanceof org.projectcrawwl.objects.Zombie){
				temp.add(a);
			}
		}
		return temp;
	}
	public void setPlayer(Player tempPlayer){
		player = tempPlayer;
		addPlayers.add(player);
	}
	
	public void addPlayer(){
		int tempX = (int) (Math.random() * (world.getMapX() / world.getGridX()));
		int tempY = (int) (Math.random() * (world.getMapY() / world.getGridY()));
		
		player = new Player((int) (tempX * world.getGridX() + world.getGridX()/2), (int) (tempY*world.getGridY() + world.getGridY()/2));
		addPlayers.add(player);
	}
	
	public void addZombie(){
		int tempX = (int) (Math.random() * (world.getMapX() / world.getGridX()));
		int tempY = (int) (Math.random() * (world.getMapY() / world.getGridY()));
		
		addPlayers.add(new Zombie((int) (tempX * world.getGridX() + world.getGridX()/2), (int) (tempY*world.getGridY() + world.getGridY()/2)));
	}
	
	public void addZombie(BasePlayer temp){
		addPlayers.add(temp);
	}
	public void removeObject(BasePlayer gameObject) {
		removePlayers.add(gameObject);
	}
	public void addProjectile(BaseProjectile temp){
		projectiles.add(temp);
	}
	public void removeProjectile(BaseProjectile temp){
		tempProjectiles.add(temp);
	}
	public ArrayList<BaseProjectile> getProjectiles(){
		return projectiles;
	}
	public ArrayList<BasePlayer> getAllPlayers(){
		return allPlayers;
	}
	
	public void addFriendly(){
		int tempX = (int) (Math.random() * (world.getMapX() / world.getGridX()));
		int tempY = (int) (Math.random() * (world.getMapY() / world.getGridY()));
		
		addPlayers.add(new Friendly((int) (tempX * world.getGridX() + world.getGridX()/2), (int) (tempY*world.getGridY() + world.getGridY()/2)));
	}
	
	public ArrayList<BasePlayer> getFriendlies(){
		ArrayList<BasePlayer> temp = new ArrayList<BasePlayer>();
		for(BasePlayer a : allPlayers){
			if(a instanceof org.projectcrawwl.objects.Friendly){
				temp.add(a);
			}
			if(a instanceof org.projectcrawwl.objects.Player){
				temp.add(a);
			}
		}
		return temp;
	}
	
	public void render(){
		world.renderBackground();
		
		for(BasePlayer a : allPlayers){
			a.render();
		}
		
		ListIterator<BaseProjectile> q = projectiles.listIterator();
		
		while(q.hasNext()){
			BaseProjectile temp = q.next();
			
			temp.render();
		}
		
		
		player.render();
		
		//world.renderLights();
		player.renderHUD();
	}
	public void update(int delta){
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){world.setMapXOffset(world.getMapXOffset() - delta);}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){world.setMapXOffset(world.getMapXOffset() + delta);}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){world.setMapYOffset(world.getMapYOffset() + delta);}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){world.setMapYOffset(world.getMapYOffset() - delta);}
		
		while(Keyboard.next()){
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE){
					addZombie();
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_G){
					addFriendly();
				}
			}
		}
		
		
		//Removes projectiles that are off screen
		for(GameObject a : tempProjectiles){
			projectiles.remove(a);
		}
		tempProjectiles.clear();
		
		for(BasePlayer a : removePlayers){
			allPlayers.remove(a);
		}
		removePlayers.clear();
		
		
		for(BasePlayer a : addPlayers){
			allPlayers.add(a);
		}
		addPlayers.clear();
	}
}

