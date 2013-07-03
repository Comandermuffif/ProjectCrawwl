package org.projectcrawwl.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.objects.ConvexHull;

public class WorldTile {
	
	private ArrayList<ConvexHull> hulls = new ArrayList<ConvexHull>();
	
	private int width = 500;
	private int height = 500;
	
	private int x;
	private int y;
	
	private Boolean isGoal = false;
	
	public void setGoal(){
		isGoal = true;
	}
	
	/**
	 * Clockwise, from top
	 * 0 = no wall;
	 * 1 = wall
	 */
	private int[] sides = new int[4];
	
	/**
	 * Basic constructor, does nothing
	 */
	public WorldTile(){}
	
	/**
	 * A World Tile created from a text file
	 * @param file - The file to read
	 */
	public WorldTile(String filename){
		try {
			
			BufferedReader wordStream = new BufferedReader(new FileReader(filename));
			
			String l;
			
			ConvexHull h = new ConvexHull();
			
			while((l = wordStream.readLine()) != null){
				
				String[] s = l.split("=");
				
				for(String a : s){
					a = a.trim();
				}
				
				if(s[0].equalsIgnoreCase("width")){
					width = Integer.parseInt(s[1]);
				}
				if(s[0].equalsIgnoreCase("height")){
					height = Integer.parseInt(s[1]);
				}
				if(s[0].equalsIgnoreCase("x")){
					x = Integer.parseInt(s[1]);
				}
				if(s[0].equalsIgnoreCase("y")){
					y = Integer.parseInt(s[1]);
				}
				
				if(s[0].equalsIgnoreCase("hull")){
					
					if(h.getPoints().size() >= 3){
						hulls.add(h);
					}
					h = new ConvexHull(x*width,y*height);
				}
				
				if(s[0].equalsIgnoreCase("p")){
					
					String ss = s[1].replace("{", "");
					ss = ss.replace("}", "");
					String[] temp = ss.split(",");
					
					h.addPoint(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
				}
				
				if(s[0].equalsIgnoreCase("sides")){
					String ss = s[1].replace("{", "");
					ss = ss.replace("}", "");
					String[] temp = ss.split(",");
					
					for(int i = 0; i < 4; i ++){
						switch(Integer.parseInt(temp[i])){
							case 1:
								sides[i] = 1;
								break;
							case -1:
								sides[i] = (int) Math.rint(Math.random());
								break;
							case 0:
								sides[i] = 0;
								break;
						}
					}
				}
				
			}
			
			if(h.getPoints().size() >= 3){
				hulls.add(h);
			}
			
			wordStream.close();
			
		}catch(IOException e){e.printStackTrace();}
		
		int subset = 5;
		
		for(int i = 0; i < subset; i ++){
			if(sides[3] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint(0,(height/subset)*(i + 1));
				a.addPoint(5,(height/subset)*(i + 1));
				a.addPoint(5,(height/subset)*(i));
				a.addPoint(0,(height/subset)*(i));
				
				hulls.add(a);
			}
			if(sides[2] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint((width/subset)*(i),0);
				a.addPoint((width/subset)*(i),5);
				a.addPoint((width/subset)*(i+1),5);
				a.addPoint((width/subset)*(i+1),0);
				
				hulls.add(a);
			}
			if(sides[1] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint(width-5,(height/subset)*(i + 1));
				a.addPoint(width,(height/subset)*(i + 1));
				a.addPoint(width,(height/subset)*(i));
				a.addPoint(width-5,(height/subset)*(i));
				
				hulls.add(a);
			}
			if(sides[0] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint((width/subset)*(i),height);
				a.addPoint((width/subset)*(i+1),height);
				a.addPoint((width/subset)*(i+1),height-5);
				a.addPoint((width/subset)*(i),height-5);
				
				hulls.add(a);
			}
		}
		
	}
	
	public static int[] possibleTiles(String filename){
		
		int[] sides = new int[]{0,0,0,0};
		
		try {
			
			BufferedReader wordStream = new BufferedReader(new FileReader(filename));
			
			String l;
			
			while((l = wordStream.readLine()) != null){
				l = l.replaceAll("\\s", "");
				
				l = l.toLowerCase();
				
				String[] s = l.split("=");
				
				if(s[0].equals("sides")){
					String ss = s[1].replace("{", "");
					ss = ss.replace("}", "");
					String[] temp = ss.split(",");
					
					for(int i = 0; i < 4; i ++){
						switch(Integer.parseInt(temp[i])){
							case 1:
								sides[i] = 1;
								break;
							case -1:
								sides[i] = 0;
								break;
							case 0:
								sides[i] = 0;
								break;
						}
					}
				}
				
			}
			
			wordStream.close();
			
		}catch(IOException e){e.printStackTrace();}
		
		return sides;
	}
	
	public WorldTile(int xx, int yy, String filename, int override){
		
		this.x = xx;
		this.y = yy;
		
		try {
			
			BufferedReader wordStream = new BufferedReader(new FileReader(filename));
			
			String l;
			
			ConvexHull h = new ConvexHull();
			
			while((l = wordStream.readLine()) != null){
				l = l.replaceAll("\\s", "");
				
				l = l.toLowerCase();
				
				String[] s = l.split("=");
				
				if(s[0].equals("width")){
					width = Integer.parseInt(s[1]);
				}
				if(s[0].equals("height")){
					height = Integer.parseInt(s[1]);
				}
				
				if(s[0].equals("hull")){
					
					if(h.getPoints().size() >= 3){
						hulls.add(h);
					}
					h = new ConvexHull(x * width,y * height);
				}
				
				if(s[0].equals("p")){
					
					String ss = s[1].replace("{", "");
					ss = ss.replace("}", "");
					String[] temp = ss.split(",");
					
					h.addPoint(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
				}
				
				if(s[0].equals("sides")){
					String ss = s[1].replace("{", "");
					ss = ss.replace("}", "");
					String[] temp = ss.split(",");
					
					for(int i = 0; i < 4; i ++){
						switch(Integer.parseInt(temp[i])){
							case 1:
								sides[i] = 1;
								break;
							case -1:
								sides[i] = (int) Math.rint(Math.random());
								break;
							case 0:
								sides[i] = 0;
								break;
						}
					}
				}
				
			}
			
			sides[override] = 0;
			
			if(h.getPoints().size() >= 3){
				hulls.add(h);
			}
			
			wordStream.close();
			
		}catch(IOException e){e.printStackTrace();}
		
		
		int subset = 5;
		
		for(int i = 0; i < subset; i ++){
			if(sides[3] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint(0,(height/subset)*(i + 1));
				a.addPoint(5,(height/subset)*(i + 1));
				a.addPoint(5,(height/subset)*(i));
				a.addPoint(0,(height/subset)*(i));
				
				hulls.add(a);
			}
			if(sides[2] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint((width/subset)*(i),0);
				a.addPoint((width/subset)*(i),5);
				a.addPoint((width/subset)*(i+1),5);
				a.addPoint((width/subset)*(i+1),0);
				
				hulls.add(a);
			}
			if(sides[1] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint(width-5,(height/subset)*(i + 1));
				a.addPoint(width,(height/subset)*(i + 1));
				a.addPoint(width,(height/subset)*(i));
				a.addPoint(width-5,(height/subset)*(i));
				
				hulls.add(a);
			}
			if(sides[0] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint((width/subset)*(i),height);
				a.addPoint((width/subset)*(i+1),height);
				a.addPoint((width/subset)*(i+1),height-5);
				a.addPoint((width/subset)*(i),height-5);
				
				hulls.add(a);
			}
		}
		
	}
	
	
	public static ArrayList<WorldTile> generateTiles(String filename){
		ArrayList<WorldTile> data = new ArrayList<WorldTile>();
		
		int width = 500;
		int height = 500;
		int x = 0;
		int y = 0;
		
		ArrayList<ConvexHull> hulls = new ArrayList<ConvexHull>();
		
		int[] sides = new int[4];
		
		try {
			
			BufferedReader wordStream = new BufferedReader(new FileReader(filename));
			
			String l;
			
			ConvexHull h = new ConvexHull();
			
			while((l = wordStream.readLine()) != null){
				l = l.replaceAll("\\s", "");
				
				l = l.toLowerCase();
				
				String[] s = l.split("=");
				
				if(s[0].equals("width")){
					width = Integer.parseInt(s[1]);
				}
				if(s[0].equals("height")){
					height = Integer.parseInt(s[1]);
				}
				if(s[0].equals("x")){
					x = Integer.parseInt(s[1]);
				}
				if(s[0].equals("y")){
					y = Integer.parseInt(s[1]);
				}
				
				if(s[0].equals("hull")){
					
					if(h.getPoints().size() >= 3){
						hulls.add(h);
					}
					
					
					h = new ConvexHull(x * width,y * height);
				}
				
				if(s[0].equals("p")){
					
					String ss = s[1].replace("{", "");
					ss = ss.replace("}", "");
					String[] temp = ss.split(",");
					
					h.addPoint(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
				}
				
				if(s[0].equals("sides")){
					String ss = s[1].replace("{", "");
					ss = ss.replace("}", "");
					String[] temp = ss.split(",");
					
					for(int i = 0; i < 4; i ++){
						sides[i] = Integer.parseInt(temp[i]);
					}
				}
			}
			
			if(h.getPoints().size() >= 3){
				hulls.add(h);
			}
			
			wordStream.close();
			
		}catch(IOException e){e.printStackTrace();}
		
		int subset = 5;
		
		for(int i = 0; i < subset; i ++){
			if(sides[3] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint(0,(height/subset)*(i + 1));
				a.addPoint(5,(height/subset)*(i + 1));
				a.addPoint(5,(height/subset)*(i));
				a.addPoint(0,(height/subset)*(i));
				
				hulls.add(a);
			}
			if(sides[2] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint((width/subset)*(i),0);
				a.addPoint((width/subset)*(i),5);
				a.addPoint((width/subset)*(i+1),5);
				a.addPoint((width/subset)*(i+1),0);
				
				hulls.add(a);
			}
			if(sides[1] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint(width-5,(height/subset)*(i + 1));
				a.addPoint(width,(height/subset)*(i + 1));
				a.addPoint(width,(height/subset)*(i));
				a.addPoint(width-5,(height/subset)*(i));
				
				hulls.add(a);
			}
			if(sides[0] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint((width/subset)*(i),height);
				a.addPoint((width/subset)*(i+1),height);
				a.addPoint((width/subset)*(i+1),height-5);
				a.addPoint((width/subset)*(i),height-5);
				
				hulls.add(a);
			}
		}
		
		ArrayList<int[]> queue = new ArrayList<int[]>();
		
		queue.add(sides);
		
		while(!queue.isEmpty()){
			
			int[] current = queue.remove(0);
			
			for(int i = 0; i < 4; i ++){
				if(current[i] == -1){
					int[] temp = current.clone();
					temp[i] = 0;
					queue.add(temp);
					temp[i] = 1;
					queue.add(temp);
					break;
				}
			}
			
			data.add(new WorldTile(x,y,current));
			
		}
		
		return data;
	}
	
	public WorldTile(int ii, int jj){
		x = ii;
		y = jj;
	}
	
	/**
	 * @param tempSide - the sides to make walls(1), random(-1), and open(0)
	 */
	public WorldTile(int ii, int jj, int[] tempSide){
		
		x = ii;
		y = jj;
		
		for(int i = 0; i < 4; i ++){
			switch(tempSide[i]){
				case 1:
					sides[i] = 1;
					break;
				case -1:
					sides[i] = (int) Math.rint(Math.random());
					break;
				case 0:
					sides[i] = 0;
					break;
			}
		}
		
		int subset = 5;
		
		for(int i = 0; i < subset; i ++){
			if(sides[3] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint(0,(height/subset)*(i + 1));
				a.addPoint(5,(height/subset)*(i + 1));
				a.addPoint(5,(height/subset)*(i));
				a.addPoint(0,(height/subset)*(i));
				
				hulls.add(a);
			}
			if(sides[2] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint((width/subset)*(i),0);
				a.addPoint((width/subset)*(i),5);
				a.addPoint((width/subset)*(i+1),5);
				a.addPoint((width/subset)*(i+1),0);
				
				hulls.add(a);
			}
			if(sides[1] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint(width-5,(height/subset)*(i + 1));
				a.addPoint(width,(height/subset)*(i + 1));
				a.addPoint(width,(height/subset)*(i));
				a.addPoint(width-5,(height/subset)*(i));
				
				hulls.add(a);
			}
			if(sides[0] == 1)
			{
				ConvexHull a = new ConvexHull(x * width,y * height);
				a.addPoint((width/subset)*(i),height);
				a.addPoint((width/subset)*(i+1),height);
				a.addPoint((width/subset)*(i+1),height-5);
				a.addPoint((width/subset)*(i),height-5);
				
				hulls.add(a);
			}
		}
	}
	
	public void addHull(ConvexHull h){
		hulls.add(h);
	}
	
	public void addHulls(ArrayList<ConvexHull> h){
		hulls.addAll(h);
	}
	
	public ArrayList<ConvexHull> getHulls(){
		return hulls;
	}
	
	public int[] getSides(){
		return sides;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setX(int a){
		
		x = a;
	}
	
	public void setY(int a){
		y = a;
	}
	
	public void renderHulls(){
		for(ConvexHull h : hulls){
			h.render();
		}
	}
	
	public void renderShadows(){
		for(ConvexHull h : hulls){
			h.renderShadow();
		}
	}
	
	public void renderBackground(){
		
		
		if(isGoal){
			GL11.glColor3d(0, (double)(171)/255, (double)(102)/255);
		}else{
			GL11.glColor3d(1,1,1);
		}
		
		
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d((x)*width + World.getMapXOffset(), (y)*height + World.getMapYOffset());
		GL11.glVertex2d((x + 1)*width + World.getMapXOffset(), (y)*height + World.getMapYOffset());
		GL11.glVertex2d((x + 1)*width + World.getMapXOffset(), (y+1)*height + World.getMapYOffset());
		GL11.glVertex2d((x)*width + World.getMapXOffset(), (y+1)*height + World.getMapYOffset());
		GL11.glEnd();
	}
	
	@Override
	public int hashCode(){
		int hash = 7;
		hash = 71 * hash + x;
		hash = 71 * hash + y;
		return hash;
	}
	
	@Override
	public boolean equals(Object t){
		WorldTile u = (WorldTile) t;
		if(u.getX() == this.getX() && u.getY() == this.getY()){
			return true;
		}
		return false;
	}
	
	public String toString(){
		String s = "{" + x + ", " +  y + ", " + sides +"}";
		return s;
	}

	public void setWidth(int w) {
		width = w;
	}

	public void setHeight(int h) {
		height = h;
	}
	
	public String toXML(){
		String data = "";
		
		data += "\t\t<Tile>\n";
		{
			data += "\t\t\t<width>" + getWidth() + "</width>\n";
			data += "\t\t\t<height>" + getHeight() + "</height>\n";
			data += "\t\t\t<x>" + getX() + "</x>\n";
			data += "\t\t\t<y>" + getY() + "</y>\n";
			
			data += "\t\t\t<hulls>\n";
			for(ConvexHull h : getHulls()){
				data += h.toXML();
			}
			data += "\t\t\t</hulls>\n";
		}
		data += "\t\t</Tile>\n";
		
		return data;
	}
}
