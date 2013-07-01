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
import org.projectcrawwl.objects.Player;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PlayerXMLHandler extends DefaultHandler{
	
	public Player p;
	private Point bound = new Point();
	private String temp;
	
	private Boolean player = false;
	
	@Override
	public void characters(char[] buffer, int start, int length){
		temp = new String(buffer, start, length);
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
		temp = "";
		if(qName.equalsIgnoreCase("player")){
			player = false;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		
		
		
		if(qName.equalsIgnoreCase("player")){
			player = true;
			p = new Player();
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
	
	public static void savePlayer(BasePlayer p){
		
		System.out.println("Begin Saving");
		
		try{
			File file = new File("res/Saves/save" + GameData.getCurrentSave() + ".xml");
			
			if(!file.exists()){
				file.createNewFile();
			}else{
				file.delete();
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
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
								bw.write("<pX>" + (int) bound.x1 + "</pX>\n");
								bw.write("<pY>" + (int) bound.y1 + "</pY>\n");
							bw.write("</point>\n");
						}
					bw.write("</boundingBox>\n");
				bw.write("</Information>\n");
				
				bw.write("<Inventory>\n");
				bw.write("</Inventory>\n");
				
			bw.write("</Player>");
			
			bw.close();
			
			System.out.println("Done Saving");
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
