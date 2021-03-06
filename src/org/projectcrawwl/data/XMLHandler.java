package org.projectcrawwl.data;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.projectcrawwl.objects.ConvexHull;
import org.projectcrawwl.objects.Player;
import org.projectcrawwl.objects.Zombie;
import org.projectcrawwl.projectile.Bullet;
import org.projectcrawwl.weapons.BaseMeleeWeapon;
import org.projectcrawwl.weapons.BaseRangedWeapon;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler{
	
	private Player p;
	private Point bound = new Point();
	private Bullet b;
	private WorldTile tile = new WorldTile();
	private ConvexHull hull = new ConvexHull();
	private Point point = new Point();
	private BaseRangedWeapon rangedWeapon;
	private BaseMeleeWeapon meleeWeapon;
	private Zombie z;
	
	private Boolean zombie = false;
	private Boolean ranged = false;
	private Boolean melee = false;
	private Boolean player = false;
	private Boolean world = false;
	private Boolean hulls = false;
	private Boolean bullet = false;
	
	private String temp;
	
	@Override
	public void characters(char[] buffer, int start, int length){
		temp = new String(buffer, start, length);
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
		temp = "";
		if(qName.equalsIgnoreCase("Player")){
			p = new Player();
			player = true;
		}else if(qName.equalsIgnoreCase("World")){
			world = true;
			World.clearData();
		}else if(qName.equalsIgnoreCase("save")){
			GameData.clearData();
		}else if(qName.equalsIgnoreCase("Tile")){
			tile = new WorldTile();
		}else if(qName.equalsIgnoreCase("hulls")){
			hulls = true;
		}else if(qName.equalsIgnoreCase("bullet")){
			bullet = true;
			b = new Bullet();
		}else if(qName.equalsIgnoreCase("BaseRangedWeapon")){
			ranged = true;
			if(player){
				rangedWeapon = new BaseRangedWeapon(p);
				p.getInventory().addWeapon(rangedWeapon);
			}else if(zombie){
				rangedWeapon = new BaseRangedWeapon(z);
				z.getInventory().addWeapon(rangedWeapon);
			}
		}else if(qName.equalsIgnoreCase("BaseMeleeWeapon")){
			melee = true;
			if(player){
				meleeWeapon = new BaseMeleeWeapon(p);
				p.getInventory().addWeapon(meleeWeapon);
			}else if(zombie){
				meleeWeapon = new BaseMeleeWeapon(z);
				z.getInventory().addWeapon(meleeWeapon);
			}
		}else if(qName.equalsIgnoreCase("zombie")){
			zombie = true;
			z = new Zombie();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		
		if(qName.equalsIgnoreCase("Player")){
			player = false;
			GameData.setPlayer(p);
		}else if(qName.equalsIgnoreCase("World")){
			world = false;
		}else if(qName.equalsIgnoreCase("Tile")){
			World.addTile(tile);
			for(ConvexHull hull : tile.getHulls()){
				GameData.addObject(hull);
			}
			tile = new WorldTile();
		}else if(qName.equalsIgnoreCase("ConvexHull")){
			tile.addHull(hull);
			hull = new ConvexHull();
		}else if(qName.equalsIgnoreCase("hulls")){
			hulls = false;
		}else if(qName.equalsIgnoreCase("bullet")){
			bullet = false;
			GameData.addObject(b);
		}else if(qName.equalsIgnoreCase("zombie")){
			zombie = false;
			GameData.addObject(z);
		}else if(qName.equalsIgnoreCase("DataID")){
			//GameData.setID(Integer.parseInt(temp));
		}else if(qName.equalsIgnoreCase("BaseMeleeWeapon")){
			meleeWeapon.createArea();
			melee = false;
		}else if(qName.equalsIgnoreCase("BaseRangedWeapon")){
			ranged = false;
		}
		
		if(ranged){
			if(qName.equalsIgnoreCase("name")){
				rangedWeapon.name = temp;
			}else if(qName.equalsIgnoreCase("damage")){
				rangedWeapon.damage = Double.parseDouble(temp);
			}else if(qName.equalsIgnoreCase("active")){
				rangedWeapon.active = Boolean.parseBoolean(temp);
			}else if(qName.equalsIgnoreCase("coolDown")){
				rangedWeapon.coolDown = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("currentCoolDown")){
				rangedWeapon.currentCoolDown = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("automatic")){
				rangedWeapon.automatic = Boolean.parseBoolean(temp);
			}else if(qName.equalsIgnoreCase("velocity")){
				rangedWeapon.velocity = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("currentClip")){
				rangedWeapon.currentClip = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("maxClip")){
				rangedWeapon.maxClip = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("reloadTime")){
				rangedWeapon.reloadTime = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("currentReload")){
				rangedWeapon.currentReload = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("pellets")){
				rangedWeapon.pellets = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("reloading")){
				rangedWeapon.reloading = Boolean.parseBoolean(temp);
			}else if(qName.equalsIgnoreCase("onFire")){
				
				rangedWeapon.fire = temp;
				
				try {
					rangedWeapon.onFire = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(temp));
				} catch (IOException e) {e.printStackTrace();}
				
			}else if(qName.equalsIgnoreCase("spread")){
				rangedWeapon.spread = Double.parseDouble(temp);
			}else if(qName.equalsIgnoreCase("currentSpread")){
				rangedWeapon.currentSpread = Double.parseDouble(temp);
			}else if(qName.equalsIgnoreCase("spreadAngle")){
				rangedWeapon.spreadAngle = Double.parseDouble(temp);
			}else if(qName.equalsIgnoreCase("cone")){
				rangedWeapon.cone = Double.parseDouble(temp);
			}else if(qName.equalsIgnoreCase("maxSpread")){
				rangedWeapon.maxSpread = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("minSpread")){
				rangedWeapon.minSpread = Integer.parseInt(temp);
			}
		}
		
		if(melee){
			if(qName.equalsIgnoreCase("name")){
				meleeWeapon.name = temp;
			}else if(qName.equalsIgnoreCase("damage")){
				meleeWeapon.damage = Double.parseDouble(temp);
			}else if(qName.equalsIgnoreCase("active")){
				meleeWeapon.active = Boolean.parseBoolean(temp);
			}else if(qName.equalsIgnoreCase("coolDown")){
				meleeWeapon.coolDown = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("currentCoolDown")){
				meleeWeapon.currentCoolDown = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("automatic")){
				meleeWeapon.automatic = Boolean.parseBoolean(temp);
			}
		}
		
		if(zombie){
			if(qName.equalsIgnoreCase("x")){
				z.x = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("y")){
				z.y = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("health")){
				z.health = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("level")){
				z.level = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("px")){
				bound.x = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("py")){
				bound.y = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("point")){
				z.addPoint(bound);
			}else if(qName.equalsIgnoreCase("boundingBox")){
				z.updateLines();
			}else if(qName.equalsIgnoreCase("turnSpeed")){
				z.turnSpeed = Double.parseDouble(temp);
			}else if(qName.equalsIgnoreCase("speedMult")){
				z.speedMult = Double.parseDouble(temp);
			}else if(qName.equalsIgnoreCase("facingAngle")){
				z.facingAngle = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("moveAngle")){
				z.moveAngle = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("speed")){
				z.speed = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("id")){
				z.id = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("xp")){
				p.xp = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("maxHealth")){
				z.maxHealth = Integer.parseInt(temp);
			}
		}
		
		if(player){
			if(qName.equalsIgnoreCase("x")){
				p.x = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("y")){
				p.y = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("health")){
				p.health = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("level")){
				p.level = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("px")){
				bound.x = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("py")){
				bound.y = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("point")){
				p.addPoint(bound);
			}else if(qName.equalsIgnoreCase("boundingBox")){
				p.updateLines();
			}else if(qName.equalsIgnoreCase("turnSpeed")){
				p.turnSpeed = Double.parseDouble(temp);
			}else if(qName.equalsIgnoreCase("speedMult")){
				p.speedMult = Double.parseDouble(temp);
			}else if(qName.equalsIgnoreCase("facingAngle")){
				p.facingAngle = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("moveAngle")){
				p.moveAngle = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("speed")){
				p.speed = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("id")){
				p.id = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("xp")){
				p.xp = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("maxHealth")){
				p.maxHealth = Integer.parseInt(temp);
			}
		}
		
		if(bullet){
			if(qName.equalsIgnoreCase("x")){
				b.x = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("y")){
				b.y = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("facingAngle")){
				b.facingAngle = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("moveAngle")){
				b.moveAngle = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("speed")){
				b.speed = Float.parseFloat(temp);
			}else if(qName.equalsIgnoreCase("turnSpeed")){
				b.turnSpeed = Double.parseDouble(temp);
			}else if(qName.equalsIgnoreCase("damage")){
				b.damage = Double.parseDouble(temp);
			}else if(qName.equalsIgnoreCase("ownerID")){
				b.ownerID = Integer.parseInt(temp);
			}
		}
		
		if(world){
			if(hulls){
				if(qName.equalsIgnoreCase("color")){
					hull.setColor(new Color(Integer.parseInt(temp)));
				}else if(qName.equalsIgnoreCase("pX")){
					point.x = Integer.parseInt(temp);
				}else if(qName.equalsIgnoreCase("pY")){
					point.y = Integer.parseInt(temp);
				}else if(qName.equalsIgnoreCase("point")){
					hull.addPoint(point);
				}
			}else{
				if(qName.equalsIgnoreCase("width")){
					tile.setWidth(Integer.parseInt(temp));
				}else if(qName.equalsIgnoreCase("height")){
					tile.setHeight(Integer.parseInt(temp));
				}else if(qName.equalsIgnoreCase("x")){
					tile.setX(Integer.parseInt(temp));
				}else if(qName.equalsIgnoreCase("y")){
					tile.setY(Integer.parseInt(temp));
				}else if(qName.equalsIgnoreCase("isGoal")){
					if(Boolean.parseBoolean(temp)){
						tile.setGoal();
					}
				}
			}
		}
	}
	
	public static void loadData(){
		
		try {
			
			SAXParserFactory spfac = SAXParserFactory.newInstance();
			
			SAXParser sp = spfac.newSAXParser();
			
			XMLHandler handler = new XMLHandler();
			
			sp.parse("res/Saves/save" + GameData.getCurrentSave() + "/save.xml", handler);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveData(){
		
		if(GameData.getCurrentSave() == 0){
			System.out.println("No Save");
			return;
		}
		
		try{
			File file = new File("res/Saves/save" + GameData.getCurrentSave() + "/save.xml");
			
			file.getParentFile().mkdirs();
			
			if(!file.exists()){
				System.out.println(file.getAbsolutePath());
				file.createNewFile();
			}else{
				file.delete();
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write("<Save>\n");
			
			bw.write(World.toXML());
			
			bw.write(GameData.toXML());
			
			bw.write("</Save>\n");
			
			bw.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
