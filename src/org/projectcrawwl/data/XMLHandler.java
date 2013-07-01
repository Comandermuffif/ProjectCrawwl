package org.projectcrawwl.data;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.objects.ConvexHull;
import org.projectcrawwl.objects.Player;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler{
	
	private Player p;
	private Point bound = new Point();
	
	private WorldTile tile = new WorldTile();
	
	private ConvexHull hull = new ConvexHull();
	
	private Point point = new Point();
	
	private Boolean player = false;
	private Boolean world = false;
	
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
			
			tile = new WorldTile();
		}else if(qName.equalsIgnoreCase("ConvexHull")){
			tile.addHull(hull);
			hull = new ConvexHull();
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
			}
		}
		
		if(world){
			if(qName.equalsIgnoreCase("width")){
				tile.setWidth(Integer.parseInt(temp));
			}else if(qName.equalsIgnoreCase("height")){
				tile.setHeight(Integer.parseInt(temp));
			}else if(qName.equalsIgnoreCase("color")){
				hull.setColor(new Color(Integer.parseInt(temp)));
			}else if(qName.equalsIgnoreCase("x")){
				tile.setX(Integer.parseInt(temp));
			}else if(qName.equalsIgnoreCase("y")){
				tile.setY(Integer.parseInt(temp));
			}else if(qName.equalsIgnoreCase("pX")){
				point.x = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("pY")){
				point.y = Integer.parseInt(temp);
			}else if(qName.equalsIgnoreCase("point")){
				hull.addPoint(point);
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
		
		System.out.println("Begin Saving");
		
		
		if(GameData.getCurrentSave() == 0){
			System.out.println("No Save");
			return;
		}
		
		try{
			File file = new File("res/Saves/save" + GameData.getCurrentSave() + "/save.xml");
			
			if(!file.exists()){
				System.out.println(file.getAbsolutePath());
				file.createNewFile();
			}else{
				file.delete();
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			BasePlayer p = GameData.getPlayer();
			
			bw.write("<Save>\n");
			
			//bw.write(GameData.getPlayer().toXML());
			
			bw.write(World.toXML());
			
			bw.write(GameData.toXML());
			
			bw.write("</Save>\n");
			
			bw.close();
			
			System.out.println("Done Saving");
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
