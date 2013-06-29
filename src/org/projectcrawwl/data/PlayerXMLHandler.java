package org.projectcrawwl.data;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.projectcrawwl.objects.Player;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PlayerXMLHandler extends DefaultHandler{
	
	public Player p;
	private Point bound = new Point();
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
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		if(qName.equalsIgnoreCase("x")){
			p.x = Integer.parseInt(temp);
		}else if(qName.equalsIgnoreCase("y")){
			p.y = Integer.parseInt(temp);
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
	
	public static void savePlayer(Player p){
		try{
			File file = new File("res/Saves/save" + GameData.getCurrentSave() + ".xml");
			
			if(!file.exists()){
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write("");
			
			bw.close();
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
