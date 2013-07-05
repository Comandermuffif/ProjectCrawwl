package org.projectcrawwl.data;

import java.awt.Point;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.projectcrawwl.objects.Player;
import org.projectcrawwl.weapons.BaseMeleeWeapon;
import org.projectcrawwl.weapons.BaseRangedWeapon;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PlayerXMLHandler extends DefaultHandler{
	private Player p;
	private Point bound = new Point();
	private BaseRangedWeapon rangedWeapon;
	private BaseMeleeWeapon meleeWeapon;
	
	private Boolean ranged = false;
	private Boolean melee = false;
	private Boolean player = false;
	
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
		}else if(qName.equalsIgnoreCase("BaseRangedWeapon")){
			ranged = true;
			if(player){
				rangedWeapon = new BaseRangedWeapon(p);
				p.getInventory().addWeapon(rangedWeapon);
			}else if(!player){
				
			}
		}else if(qName.equalsIgnoreCase("BaseMeleeWeapon")){
			melee = true;
			if(player){
				meleeWeapon = new BaseMeleeWeapon(p);
			}else if(!player){
				
			}
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		
		if(qName.equalsIgnoreCase("Player")){
			player = false;
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
			}
		}
	}
	
	public static Player createPlayer(String filename){
		
		try {
			
			SAXParserFactory spfac = SAXParserFactory.newInstance();
			
			SAXParser sp = spfac.newSAXParser();
			
			PlayerXMLHandler handler = new PlayerXMLHandler();
			
			sp.parse(filename, handler);
			
			return handler.p;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
