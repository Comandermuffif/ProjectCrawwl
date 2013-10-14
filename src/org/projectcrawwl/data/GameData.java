package org.projectcrawwl.data;

import java.awt.Font;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.projectcrawwl.Main;
import org.projectcrawwl.objects.*;

public class GameData 
{	
	
	private static ConcurrentHashMap<Integer, GameObject> all = new ConcurrentHashMap<Integer, GameObject>();
	private static ConcurrentHashMap<Integer, GameObject> twod = new ConcurrentHashMap<Integer, GameObject>();
	private static HashMap<Integer, BasePlayer> players = new HashMap<Integer, BasePlayer>();
	private static HashMap<Integer, BasePlayer> humans = new HashMap<Integer, BasePlayer>();
	private static HashMap<Integer, BasePlayer> zombies = new HashMap<Integer, BasePlayer>();
	
	private static ArrayList<GameObject> walls = new ArrayList<GameObject>();
	
	private static BasePlayer player = null;
	
	private static int currentSave = 0;
	
	private static int id = 0;
	
	public static float zoom = 0;
	
	private static UnicodeFont font;
	
	public static int getNewID(){
		int temp = id;
		id ++;
		return temp;
	}
	
	public static void setID(int i){
		id = i;
	}
	
	public static void setCurrentSave(int s){
		currentSave = s;
	}
	
	public static int getCurrentSave(){
		return currentSave;
	}
	
	public static void clearData(){
		all.clear();
		twod.clear();
		players.clear();
		humans.clear();
		zombies.clear();
		
		walls.clear();
		
		zoom = 0;
		
		id = 0;
		
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
		
		player = PlayerXMLHandler.createPlayer("res/Saves/save" + currentSave +"/save.xml");
		
		addObject(player);
	}
	
	public static UnicodeFont getFont(){
		return font;
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
	public static void setPlayer(Player tempPlayer){
		if(player != null){
			System.err.println("Player loaded when player existed");
		}
		player = tempPlayer;
		addObject(player);
	}
	
	public static void addPlayer(){
		
		if(player != null){
			System.err.println("Player loaded when player existed " + player);
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
			
			addObject(player);
			
		}else{
			player = null;
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
			
			addObject(zombie);
		}else{
			addZombie();
		}
	}
	
	public static void addObject(GameObject o){
		
		if(o instanceof BloodStain){
			twod.put(o.id, o);
			return;
		}
		
		if(o instanceof ConvexHull){
			walls.add(o);
			return;
		}
		
		all.put(o.id, o);
		if(o instanceof BasePlayer){
			players.put(o.id, (BasePlayer) o);
			if(o instanceof Zombie){
				zombies.put(o.id, (BasePlayer) o);
			}else{
				humans.put(o.id, (BasePlayer) o);
			}
		}
		
	}
	
	public static void addAllObjects(Collection<GameObject> c){
		for(GameObject o : c){
			
			if(o instanceof BloodStain){
				twod.put(o.id, o);
				continue;
			}
			
			all.put(o.id, o);
			if(o instanceof BasePlayer){
				players.put(o.id, (BasePlayer) o);
				if(o instanceof Zombie){
					zombies.put(o.id, (BasePlayer) o);
				}else{
					humans.put(o.id, (BasePlayer) o);
				}
			}
		}
	}
	
	public static void removeObject(GameObject o){
		
		if(o instanceof BloodStain){
			twod.remove(o.id);
			return;
		}
		
		all.remove(o.id);
		if(o instanceof BasePlayer){
			players.remove(o.id);
			if(o instanceof Zombie){
				zombies.remove(o.id);
			}else{
				humans.remove(o.id);
			}
			
			if(o instanceof Player){
				player = null;
			}
		}
	}
	
	public static void killObject(GameObject o){
		
		if(o instanceof BloodStain){
			twod.remove(o.id);
			return;
		}
		
		if(o.getClass() != all.get(o.id).getClass()){
			System.err.print(o.id + " " + o.getClass());
			System.err.print(all.get(o.id).id + " " + all.get(o.id).getClass());
			System.err.println("Improper ID assigned");
		}
		
		
		all.remove(o.id);
		if(o instanceof BasePlayer){
			players.remove(o.id);
			if(o instanceof Zombie){
				zombies.remove(o.id);
			}else{
				humans.remove(o.id);
			}
			
			if(o instanceof Player){
				StateController.setGameState(Main.DEATH_STATE);
			}
			
		}
		Corpse c = new Corpse(o.getX(), o.getY());
		
		GameData.addObject(c);
	}
	
	public static GameObject getObject(int i){
		if(twod.get(i) != null){
			return twod.get(i);
		}
		return all.get(i);
	}
	
	public static Collection<BasePlayer> getZombies() {
		return zombies.values();
	}
	
	public static Collection<BasePlayer> getHumans() {
		return humans.values();
	}
	
	public static Collection<BasePlayer> getPlayers(){
		return players.values();
	}
	
	public static void clearPlayer(){
		player = null;
	}
	
	public static void addChest(){
		WorldTile t = World.getTiles().get((int) Math.floor(Math.random()*World.getTiles().size()));
		
		int tempX = (int) ((Math.random() + t.getX()) * t.getWidth());
		int tempY = (int) ((Math.random() + t.getY()) * t.getHeight());

		
		Chest chest = new Chest(tempX, tempY);
		
		boolean flag = true;
		
		for(ConvexHull k : World.getHulls()){
			
			if(k.getPolygon().contains(chest.x, chest.y)){
				flag = false;
				break;
			}
			
			for(Line2D.Float q : k.getLines()){
				for(Line2D.Float bound : chest.boundingBox()){
					
					Line2D.Float temp = new Line2D.Float();
					
					temp.x1 = (float) (bound.x1*Math.cos(Math.toRadians(chest.facingAngle)) - bound.y1*Math.sin(Math.toRadians(chest.facingAngle)) + chest.x);
					temp.y1 = (float) (bound.x1*Math.sin(Math.toRadians(chest.facingAngle)) + bound.y1*Math.cos(Math.toRadians(chest.facingAngle)) + chest.y);
					temp.x2 = (float) (bound.x2*Math.cos(Math.toRadians(chest.facingAngle)) - bound.y2*Math.sin(Math.toRadians(chest.facingAngle)) + chest.x);
					temp.y2 = (float) (bound.x2*Math.sin(Math.toRadians(chest.facingAngle)) + bound.y2*Math.cos(Math.toRadians(chest.facingAngle)) + chest.y);
							
							
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
			
			addObject(chest);
			
		}else{
			addChest();
		}
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
			
			addObject(friendly);
			
		}else{
			addFriendly();
		}
	
	}
	
	public static void render(){

		World.renderBackground();
		
		List<GameObject> l = new ArrayList<GameObject>(all.values());
		
		Collections.sort(l);
		
		for(GameObject o : twod.values()){
			o.render();
		}
		
		for(GameObject o : l){
			o.render();
			
		}
		
		for(GameObject o : walls){
			((ConvexHull) o).renderShadow();
		}
		
		
		for(GameObject o : walls){
			o.render();
		}
		
		//synchronized(playerLock){
			if(player != null && StateController.getGameState() == Main.IN_GAME){
				player.renderHUD();
			}
		//}
	}
	
	public static void update(){
		
		for(GameObject o : all.values()){
			o.update(0);
		}
		
		for(GameObject o : twod.values()){
			o.update(0);
		}
		
	}
	
	public static void update(int delta){
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){World.setMapXOffset(World.getMapXOffset() - delta);}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){World.setMapXOffset(World.getMapXOffset() + delta);}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){World.setMapYOffset(World.getMapYOffset() - delta);}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){World.setMapYOffset(World.getMapYOffset() + delta);}
		
		if(player != null){
			if((int) Math.floor(player.getX()/World.getGoal().getWidth()) == World.getGoal().getX() && (int)Math.floor(player.getY()/World.getGoal().getHeight()) == World.getGoal().getY()){
				StateController.setGameState(Main.END_LEVEL);
				return;
			}
		}
		
		for(GameObject o : all.values()){
			o.update(delta);
		}
		
		for(GameObject o : twod.values()){
			o.update(delta);
		}
		
	}
	
	public static String toXML(){
		String data = "";
		
		data += "\t<GameData>\n";
		
		data += "\t\t<DataID>" + id + "</DataID>\n";
		
		for(GameObject o : all.values()){
			data += o.toXML();
		}
		
		data += "\t</GameData>\n";
		
		return data;
	}
}

