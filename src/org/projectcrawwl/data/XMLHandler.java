package org.projectcrawwl.data;

import java.awt.Point;
import java.awt.geom.Line2D;
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
		}
		if(qName.equalsIgnoreCase("World")){
			world = true;
			World.clearData();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		
		if(qName.equalsIgnoreCase("Player")){
			player = false;
		}else if(qName.equalsIgnoreCase("World")){
			world = false;
		}else if(qName.equalsIgnoreCase("tile")){
			World.addTile(tile);
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
			}
		}
		
		if(world){
			
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
			
			bw.write("<Save>");
			
			bw.write("<Player>\n");
				bw.write("<Information>\n");
					bw.write("<x>" + p.x + "</x>\n");
					bw.write("<y>" + p.y + "</y>\n");
					bw.write("<level>" + p.level + "</level>\n");
					bw.write("<health>" + p.health + "</health>\n");
					bw.write("<turnSpeed>" + p.turnSpeed + "</turnSpeed>\n");
					bw.write("<boundingBox>\n");
						for(Line2D.Float bound : p.boundingLines){
							bw.write("<point>\n");
								bw.write("<pX>" + (int) bound.x2 + "</pX>\n");
								bw.write("<pY>" + (int) bound.y2 + "</pY>\n");
							bw.write("</point>\n");
						}
					bw.write("</boundingBox>\n");
				bw.write("</Information>\n");
				bw.write("<Inventory>\n");
				bw.write("</Inventory>\n");
			bw.write("</Player>");
			
			
			bw.write("<World>");
				for(WorldTile t : World.getTiles()){
					bw.write("<Tile>");
						bw.write("<width>" + t.getWidth() + "</width>");
						bw.write("<height>" + t.getHeight() + "</height>");
						bw.write("<x>" + t.getX() + "</x>");
						bw.write("<y>" + t.getY() + "</y>");
						
						for(ConvexHull h : t.getHulls()){
							bw.write("<Hull>");
								bw.write("<color>" + h.getColor() + "</color>");
								
								for(Line2D.Float l : h.getLines()){
									bw.write("<point>\n");
										bw.write("<pX>" + (int) l.x2 + "</pX>\n");
										bw.write("<pY>" + (int) l.y2 + "</pY>\n");
									bw.write("</point>\n");
								}
								
							bw.write("</Hull>");
						}
						
					bw.write("</Tile>");
				}
				
			bw.write("</World>");
			
			bw.write("</Save>");
			
			bw.close();
			
			System.out.println("Done Saving");
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
