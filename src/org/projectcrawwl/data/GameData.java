package org.projectcrawwl.data;

import java.awt.Font;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.projectcrawwl.Main;
import org.projectcrawwl.objects.*;
import org.projectcrawwl.projectile.Bullet;
import org.projectcrawwl.projectile.Particle;

public class GameData 
{	
	
	private static ArrayList<GameObject> allObjects = new ArrayList<GameObject>();
	
	private static ArrayList<Bullet> allProjectiles = new ArrayList<Bullet>();
	private static ArrayList<Bullet> removeProjectiles = new ArrayList<Bullet>();
	private static ArrayList<Bullet> addProjectiles = new ArrayList<Bullet>();
	private static Object projectileLock = new Object();

	private static ArrayList<BasePlayer> allPlayers = new ArrayList<BasePlayer>();
	private static ArrayList<BasePlayer> addPlayers = new ArrayList<BasePlayer>();
	private static ArrayList<BasePlayer> removePlayers = new ArrayList<BasePlayer>();
	private static ArrayList<BasePlayer> killPlayers = new ArrayList<BasePlayer>();
	private static Object playerLock = new Object();
	
	private static ArrayList<GameObject> allParticles = new ArrayList<GameObject>();
	private static ArrayList<GameObject> addParticles = new ArrayList<GameObject>();
	private static ArrayList<GameObject> removeParticles = new ArrayList<GameObject>();
	private static Object particleLock = new Object();
	
	private static ArrayList<GameObject> allBloodStains = new ArrayList<GameObject>();
	private static ArrayList<GameObject> addBloodStains = new ArrayList<GameObject>();
	private static ArrayList<GameObject> removeBloodStains = new ArrayList<GameObject>();
	private static Object bloodStainLock = new Object();
	
	private static BasePlayer player;
	
	public static float zoom = 0;
	
	private static UnicodeFont font;
	
	public static void clearData(){
		allObjects.clear();
		allProjectiles.clear();
		removeProjectiles.clear();
		addProjectiles.clear();
		allPlayers.clear();
		addPlayers.clear();
		removePlayers.clear();
		killPlayers.clear();
		allParticles.clear();
		addParticles.clear();
		removeParticles.clear();
		allBloodStains.clear();
		addBloodStains.clear();
		removeBloodStains.clear();
		
		zoom = 0;
		
		player = null;
	}
	
	@SuppressWarnings("unchecked")
	public static void renderInit(){
		
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
	
	public static void loadPlayer(){
		if(player != null){
			System.err.println("Player loaded when player existed");
		}
		player = new Player("res/Saves/save1.Player");
		addPlayers.add(player);
	}
	
	public static UnicodeFont getFont(){
		return font;
	}
	
	public static ArrayList<GameObject> getBloodStains(){
		return allBloodStains;
	}
	
	public static void addBloodStain(BloodStain g){
		addBloodStains.add(g);
	}
	
	public static float getMapXOffset(){
		return World.getMapXOffset();
	}
	public static float getMapYOffset(){
		return World.getMapYOffset();
	}
	
	public static void setMapXOffset(float temp){
		World.setMapXOffset(temp);
	}
	public static void setMapYOffset(float temp){
		World.setMapYOffset(temp);
	}
	public static BasePlayer getPlayer(){
		return player;
	}
	public static ArrayList<BasePlayer> getAI(){
		ArrayList<BasePlayer> temp = new ArrayList<BasePlayer>();
		for(BasePlayer a : allPlayers){
			if(a instanceof org.projectcrawwl.objects.Zombie){
				temp.add(a);
			}
		}
		return temp;
	}
	public static void setPlayer(Player tempPlayer){
		if(player != null){
			System.err.println("Player loaded when player existed");
		}
		player = tempPlayer;
		addPlayers.add(player);
	}
	
	public static void addPlayer(){
		
		if(player != null){
			System.err.println("Player loaded when player existed");
		}
		
		WorldTile t = World.getTiles().get((int) Math.floor(Math.random()*World.getTiles().size()));
		
		int tempX = (int) ((Math.random() + t.getX()) * t.getWidth());
		int tempY = (int) ((Math.random() + t.getY()) * t.getHeight());
		
		player = new Player(tempX, tempY);
		
		boolean flag = true;
		
		for(ConvexHull k : World.getHulls()){
			
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
	
	public static void addZombie(){
		WorldTile t = World.getTiles().get((int) Math.floor(Math.random()*World.getTiles().size()));
		
		int tempX = (int) ((Math.random() + t.getX()) * t.getWidth());
		int tempY = (int) ((Math.random() + t.getY()) * t.getHeight());
		
		BasePlayer zombie = new Zombie(tempX, tempY);
		
		boolean flag = true;
		
		for(ConvexHull k : World.getHulls()){
			
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
	
	public static void addZombie(BasePlayer temp){
		addPlayers.add(temp);
	}
	public static void removePlayer(BasePlayer gameObject) {
		removePlayers.add(gameObject);
	}
	public static void killPlayer(BasePlayer gameObject) {
		killPlayers.add(gameObject);
	}
	public static void addProjectile(Bullet temp){
		addProjectiles.add(temp);
	}
	public static void removeProjectile(Bullet temp){
		removeProjectiles.add(temp);
	}
	
	public static void addParticle(Particle p){
		addParticles.add(p);
	}
	
	public static void removeParticle(Particle p){
		removeParticles.add(p);
	}
	
	public static ArrayList<Bullet> getProjectiles(){
		return allProjectiles;
	}
	public static ArrayList<BasePlayer> getAllPlayers(){
		return allPlayers;
	}
	
	public static void addFriendly(){
		WorldTile t = World.getTiles().get((int) Math.floor(Math.random()*World.getTiles().size()));
		
		int tempX = (int) ((Math.random() + t.getX()) * t.getWidth());
		int tempY = (int) ((Math.random() + t.getY()) * t.getHeight());

		
		BasePlayer friendly = new Friendly(tempX, tempY);
		
		boolean flag = true;
		
		for(ConvexHull k : World.getHulls()){
			
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
	
	public static ArrayList<BasePlayer> getFriendlies(){
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
	
	public static void render(){

		World.renderBackground();
		
		//synchronized(bloodStainLock){
			for(GameObject a : allBloodStains){
				a.render();
			}
		//}
		
		for(GameObject a : allObjects){
			a.render();
		}
		
		//synchronized(playerLock){
			if(player != null && StateController.getGameState() == Main.IN_GAME){
				player.renderHUD();
			}
		//}
	}
	public static void update(int delta){
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){World.setMapXOffset(World.getMapXOffset() - delta);}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){World.setMapXOffset(World.getMapXOffset() + delta);}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){World.setMapYOffset(World.getMapYOffset() - delta);}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){World.setMapYOffset(World.getMapYOffset() + delta);}
		
		//synchronized(bloodStainLock){
			for(GameObject a : addBloodStains){
				allBloodStains.add(a);
			}
			addBloodStains.clear();
		//}
		
		//synchronized(bloodStainLock){
			for(GameObject a : removeBloodStains){
				allBloodStains.remove(a);
			}
			removeBloodStains.clear();
		//}
		
		//synchronized(bloodStainLock){}
		
		//synchronized(projectileLock){
			//Removes projectiles that are off screen
			for(Bullet a : removeProjectiles){
				allProjectiles.remove(a);
			}
			removeProjectiles.clear();
		//}
		
		//synchronized(projectileLock){
			//Removes projectiles that are off screen
			for(Bullet a : addProjectiles){
				allProjectiles.add(a);
			}
			addProjectiles.clear();
		//}
		
		//synchronized(particleLock){
			for(GameObject a : removeParticles){
				allParticles.remove(a);
			}
			removeParticles.clear();
		//}
		
		//synchronized(particleLock){
			for(GameObject a : addParticles){
				allParticles.add(a);
			}
			addParticles.clear();
		//}
		
		//synchronized(playerLock){
			for(BasePlayer a : removePlayers){
				if(a instanceof Player){
					player = null;
				}
				allPlayers.remove(a);
			}
			removePlayers.clear();
			
			for(BasePlayer a : killPlayers){
				if(a instanceof Player){
					player = null;
				}
				allPlayers.remove(a);
				
				if(allBloodStains.size() > 200){
					removeBloodStains.add(allBloodStains.get(0));
				}
				
				addBloodStains.add(new Corpse(a.x, a.y));
			}
			killPlayers.clear();
			
		//}
		
		
		
		//synchronized(playerLock){
			for(BasePlayer a : addPlayers){
				allPlayers.add(a);
			}
			addPlayers.clear();
		//}
		
		for(BasePlayer a : getAllPlayers()){
			a.update(delta);
		}
		for(GameObject b : getProjectiles()){
			b.update(delta);
		}
		
		for(GameObject c : getBloodStains()){
			c.update(delta);
		}
		
		for(GameObject d : allParticles){
			d.update(delta);
		}
		
		allObjects.clear();
		
		allObjects.addAll(allParticles);
		allObjects.addAll(allPlayers);
		allObjects.addAll(allProjectiles);
		allObjects.addAll(World.getHulls());
		
		Collections.sort(allObjects);
		
	}
}

