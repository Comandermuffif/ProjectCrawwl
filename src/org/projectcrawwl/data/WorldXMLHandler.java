package org.projectcrawwl.data;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WorldXMLHandler  extends DefaultHandler{
	
	private String temp = "";
	
	@Override
	public void characters(char[] buffer, int start, int length){
		temp = new String(buffer, start, length);
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
		temp = "";
		if(qName.equalsIgnoreCase("World")){
			World.clearHulls();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		
	}
	
	public static void createWorld(String filename){
		try {
			
			SAXParserFactory spfac = SAXParserFactory.newInstance();
			
			SAXParser sp = spfac.newSAXParser();
			
			WorldXMLHandler handler = new WorldXMLHandler();
			
			sp.parse(filename, handler);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveWorld(World w){
		
	}
}
