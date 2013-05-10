package org.projectcrawwl.data;

import java.awt.Font;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.projectcrawwl.objects.*;
import org.projectcrawwl.projectile.Bullet;
import org.projectcrawwl.projectile.Particle;

public class GameData 
{	
	private ArrayList<Bullet> allProjectiles = new ArrayList<Bullet>();//Projectiles
	private ArrayList<Bullet> removeProjectiles = new ArrayList<Bullet>();//Projectiles to be removed
	private ArrayList<Bullet> addProjectiles = new ArrayList<Bullet>();//Projectiles to be removed
	private Object projectileLock = new Object();

	private BasePlayer player;

	private ArrayList<BasePlayer> allPlayers = new ArrayList<BasePlayer>();
	private ArrayList<BasePlayer> addPlayers = new ArrayList<BasePlayer>();
	private ArrayList<BasePlayer> removePlayers = new ArrayList<BasePlayer>();
	private Object playerLock = new Object();
	
	private ArrayList<GameObject> allParticles = new ArrayList<GameObject>();
	private ArrayList<GameObject> addParticles = new ArrayList<GameObject>();
	private ArrayList<GameObject> removeParticles = new ArrayList<GameObject>();
	private Object particleLock = new Object();
	
	private ArrayList<GameObject> corpses = new ArrayList<GameObject>();
	
	private static GameData instance = null;
	
	private int ups = 0;
	
	private World world = World.getInstance();
	
	public float zoom = 0;
	
	private UnicodeFont font;
	
	public void renderInit(){
		
		System.out.println("Initializing Render Data");
		
		Font awFont = new Font("Times New Roman", Font.BOLD, 24);
		font = new UnicodeFont(awFont, 12, true, false);
		font.addAsciiGlyphs();
		font.addGlyphs(400, 600);
		font.getEffects().add(new ColorEffect(new java.awt.Color(255,255,255)));
		
		try {
			font.loadGlyphs();
		}catch(SlickException e){e.printStackTrace();}
	}
	
	public UnicodeFont getFont(){
		return font;
	}
	
	public int getUPS(){
		return ups;
	}
	
	public ArrayList<GameObject> getCorpses(){
		return corpses;
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
		int tempX = (int) (Math.random() * world.getMapX());
		int tempY = (int) (Math.random() * world.getMapY());
		
		player = new Player(tempX, tempY);
		
		boolean flag = true;
		
		for(ConvexHull k : world.getHulls()){
			
			if(k.getPolygon().contains(player.x, player.y)){
				flag = false;
				break;
			}
			
			for(Line2D.Float q : k.getLines()){
				for(Line2D.Float bound : player.boundingBox()){
					
					Line2D.Float temp = new Line2D.Float();
					
					temp.x1 = (float) (bound.x1*Math.cos(Math.toRadians(player.facingAngle)) - bound.y1*Math.sin(Math.toRadians(player.facingAngle)) + player.x);
					temp.y1 = (float) (bound.x1*Math.sin(Math.toRadians(player.facingAngle)) + bound.y1*Math.cos(Math.toRadians(player.facingAngle)) + player.y);
					temp.x2 = (float) (bound.x2*Math.cos(Math.toRadians(player.facingAngle)) - bound.y2*Math.sin(Math.toRadians(player.facingAngle)) + player.x);
					temp.y2 = (float) (bound.x2*Math.sin(Math.toRadians(player.facingAngle)) + bound.y2*Math.cos(Math.toRadians(player.facingAngle)) + player.y);
							
							
					//Line2D.Float temp = new Line2D.Float(bound.x1 + tempx, bound.y1 + tempy, bound.x2 + tempx, bound.y2 + tempy);
					if(temp.intersectsLine(q)){
						flag = false;
						break;
					}
				}
				if(!flag){break;}
			}
			if(!flag){break;}
		}
		if(flag){
			
			addPlayers.add(player);
			
		}else{
			addPlayer();
		}
	}
	
	public void addZombie(){
		int tempX = (int) (Math.random() * world.getMapX());
		int tempY = (int) (Math.random() * world.getMapY());
		
		BasePlayer zombie = new Zombie(tempX, tempY);
		
		boolean flag = true;
		
		for(ConvexHull k : world.getHulls()){
			
			if(k.getPolygon().contains(zombie.x, zombie.y)){
				flag = false;
				break;
			}
			
			for(Line2D.Float q : k.getLines()){
				for(Line2D.Float bound : zombie.boundingBox()){
					
					Line2D.Float temp = new Line2D.Float();
					
					temp.x1 = (float) (bound.x1*Math.cos(Math.toRadians(zombie.facingAngle)) - bound.y1*Math.sin(Math.toRadians(zombie.facingAngle)) + zombie.x);
					temp.y1 = (float) (bound.x1*Math.sin(Math.toRadians(zombie.facingAngle)) + bound.y1*Math.cos(Math.toRadians(zombie.facingAngle)) + zombie.y);
					temp.x2 = (float) (bound.x2*Math.cos(Math.toRadians(zombie.facingAngle)) - bound.y2*Math.sin(Math.toRadians(zombie.facingAngle)) + zombie.x);
					temp.y2 = (float) (bound.x2*Math.sin(Math.toRadians(zombie.facingAngle)) + bound.y2*Math.cos(Math.toRadians(zombie.facingAngle)) + zombie.y);
							
							
					//Line2D.Float temp = new Line2D.Float(bound.x1 + tempx, bound.y1 + tempy, bound.x2 + tempx, bound.y2 + tempy);
					if(temp.intersectsLine(q)){
						flag = false;
						break;
					}
				}
				if(!flag){break;}
			}
			if(!flag){break;}
		}
		if(flag){
			
			addPlayers.add(zombie);
		}else{
			addZombie();
		}
	}
	
	public void addZombie(BasePlayer temp){
		addPlayers.add(temp);
	}
	public void removeObject(BasePlayer gameObject) {
		removePlayers.add(gameObject);
	}
	public void addProjectile(Bullet temp){
		addProjectiles.add(temp);
	}
	public void removeProjectile(Bullet temp){
		removeProjectiles.add(temp);
	}
	
	public void addParticle(Particle p){
		addParticles.add(p);
	}
	
	public void removeParticle(Particle p){
		removeParticles.add(p);
	}
	
	public ArrayList<Bullet> getProjectiles(){
		return allProjectiles;
	}
	public ArrayList<BasePlayer> getAllPlayers(){
		return allPlayers;
	}
	
	public void addFriendly(){
		int tempX = (int) (Math.random() * world.getMapX());
		int tempY = (int) (Math.random() * world.getMapY());
		
		BasePlayer friendly = new Friendly(tempX, tempY);
		
		boolean flag = true;
		
		for(ConvexHull k : world.getHulls()){
			
			if(k.getPolygon().contains(friendly.x, friendly.y)){
				flag = false;
				break;
			}
			
			for(Line2D.Float q : k.getLines()){
				for(Line2D.Float bound : friendly.boundingBox()){
					
					Line2D.Float temp = new Line2D.Float();
					
					temp.x1 = (float) (bound.x1*Math.cos(Math.toRadians(friendly.facingAngle)) - bound.y1*Math.sin(Math.toRadians(friendly.facingAngle)) + friendly.x);
					temp.y1 = (float) (bound.x1*Math.sin(Math.toRadians(friendly.facingAngle)) + bound.y1*Math.cos(Math.toRadians(friendly.facingAngle)) + friendly.y);
					temp.x2 = (float) (bound.x2*Math.cos(Math.toRadians(friendly.facingAngle)) - bound.y2*Math.sin(Math.toRadians(friendly.facingAngle)) + friendly.x);
					temp.y2 = (float) (bound.x2*Math.sin(Math.toRadians(friendly.facingAngle)) + bound.y2*Math.cos(Math.toRadians(friendly.facingAngle)) + friendly.y);
							
							
					//Line2D.Float temp = new Line2D.Float(bound.x1 + tempx, bound.y1 + tempy, bound.x2 + tempx, bound.y2 + tempy);
					if(temp.intersectsLine(q)){
						flag = false;
						break;
					}
				}
				if(!flag){break;}
			}
			if(!flag){break;}
		}
		if(flag){
			
			addPlayers.add(friendly);
			
		}else{
			addFriendly();
		}
	
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
		
		synchronized(playerLock){
			for(BasePlayer a : allPlayers){
				a.render();
			}
			for(GameObject c : corpses){
				c.render();
			}
		}
		
		synchronized(particleLock){
			for(GameObject d : allParticles){
				d.render();
			}
		}
		
		synchronized(projectileLock){
			for(Bullet q : allProjectiles){
				q.render();
			}
		}
		
		synchronized(playerLock){
			if(player != null){
				player.render();
				player.renderHUD();
			}
		}
		
		
	}
	public void update(int delta){
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){world.setMapXOffset(world.getMapXOffset() - delta);}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){world.setMapXOffset(world.getMapXOffset() + delta);}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){world.setMapYOffset(world.getMapYOffset() + delta);}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){world.setMapYOffset(world.getMapYOffset() - delta);}
		
		synchronized(projectileLock){
			//Removes projectiles that are off screen
			for(Bullet a : removeProjectiles){
				allProjectiles.remove(a);
			}
			removeProjectiles.clear();
		}
		
		synchronized(projectileLock){
			//Removes projectiles that are off screen
			for(Bullet a : addProjectiles){
				allProjectiles.add(a);
			}
			addProjectiles.clear();
		}
		
		synchronized(particleLock){
			for(GameObject a : removeParticles){
				allParticles.remove(a);
			}
			removeParticles.clear();
		}
		
		synchronized(particleLock){
			for(GameObject a : addParticles){
				allParticles.add(a);
			}
			addParticles.clear();
		}
		
		synchronized(playerLock){
			for(BasePlayer a : removePlayers){
				if(a instanceof Player){
					player = null;
				}
				allPlayers.remove(a);
				corpses.add(new Corpse(a.x, a.y));
			}
			removePlayers.clear();
		}
		
		synchronized(playerLock){
			for(BasePlayer a : addPlayers){
				allPlayers.add(a);
			}
			addPlayers.clear();
		}
		
		for(BasePlayer a : getAllPlayers()){
			a.update(delta);
		}
		for(GameObject b : getProjectiles()){
			b.update(delta);
		}
		
		for(GameObject c : getCorpses()){
			c.update(delta);
		}
		
		for(GameObject d : allParticles){
			d.update(delta);
		}
	}
}

