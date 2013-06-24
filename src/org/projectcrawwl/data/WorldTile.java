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
	
	/**
	 * Clockwise, from top
	 * 0 = no wall;
	 * 1 = wall
	 */
	private int[] sides = new int[4];
	
	/**
	 * A World Tile created from a text file
	 * @param file - The file to read
	 */
	public WorldTile(String filename){
		try {
			
			BufferedReader wordStream = new BufferedReader(new FileReader(filename));
			
			String l;
			
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
					x = Integer.parseInt(s[1]) * width;
				}
				if(s[0].equals("y")){
					y = Integer.parseInt(s[1]) * height;
				}
				if(s[0].equals("sides")){
					String ss = s[1].replace("{", "");
					ss = ss.replace("}", "");
					String[] temp = ss.split(",");
					
					sides = new int[]{Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),Integer.parseInt(temp[3])};
				}
				
			}
			
			wordStream.close();
			
		}catch(IOException e){e.printStackTrace();}
		
		System.out.println("Generated");
		System.out.println(x);
		System.out.println(y);
		System.out.println(width);
		System.out.println(height);
		System.out.println(sides[0]);
		System.out.println(sides[1]);
		System.out.println(sides[2]);
		System.out.println(sides[3]);
		
		int subset = 5;
		
		for(int i = 0; i < subset; i ++){
			if(sides[3] == 1)
			{
				ConvexHull a = new ConvexHull(x,y);
				a.addPoint(0,(height/subset)*(i + 1));
				a.addPoint(5,(height/subset)*(i + 1));
				a.addPoint(5,(height/subset)*(i));
				a.addPoint(0,(height/subset)*(i));
				
				hulls.add(a);
			}
			if(sides[2] == 1)
			{
				ConvexHull a = new ConvexHull(x,y);
				a.addPoint((width/subset)*(i),0);
				a.addPoint((width/subset)*(i),5);
				a.addPoint((width/subset)*(i+1),5);
				a.addPoint((width/subset)*(i+1),0);
				
				hulls.add(a);
			}
			if(sides[1] == 1)
			{
				ConvexHull a = new ConvexHull(x,y);
				a.addPoint(width-5,(height/subset)*(i + 1));
				a.addPoint(width,(height/subset)*(i + 1));
				a.addPoint(width,(height/subset)*(i));
				a.addPoint(width-5,(height/subset)*(i));
				
				hulls.add(a);
			}
			if(sides[0] == 1)
			{
				ConvexHull a = new ConvexHull(x,y);
				a.addPoint((width/subset)*(i),height);
				a.addPoint((width/subset)*(i+1),height);
				a.addPoint((width/subset)*(i+1),height-5);
				a.addPoint((width/subset)*(i),height-5);
				
				hulls.add(a);
			}
		}
		
	}
	
	public WorldTile(int ii, int jj){
		x = ii * width;
		y = jj * height;
	}
	
	/**
	 * @param tempSide - the sides to make walls(1), random(-1), and open(0)
	 */
	public WorldTile(int ii, int jj, int[] tempSide){
		
		x = ii * width;
		y = jj * height;
		
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
				ConvexHull a = new ConvexHull(x,y);
				a.addPoint(0,(height/subset)*(i + 1));
				a.addPoint(5,(height/subset)*(i + 1));
				a.addPoint(5,(height/subset)*(i));
				a.addPoint(0,(height/subset)*(i));
				
				hulls.add(a);
			}
			if(sides[2] == 1)
			{
				ConvexHull a = new ConvexHull(x,y);
				a.addPoint((width/subset)*(i),0);
				a.addPoint((width/subset)*(i),5);
				a.addPoint((width/subset)*(i+1),5);
				a.addPoint((width/subset)*(i+1),0);
				
				hulls.add(a);
			}
			if(sides[1] == 1)
			{
				ConvexHull a = new ConvexHull(x,y);
				a.addPoint(width-5,(height/subset)*(i + 1));
				a.addPoint(width,(height/subset)*(i + 1));
				a.addPoint(width,(height/subset)*(i));
				a.addPoint(width-5,(height/subset)*(i));
				
				hulls.add(a);
			}
			if(sides[0] == 1)
			{
				ConvexHull a = new ConvexHull(x,y);
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
		return x/width;
	}
	
	public int getY(){
		return y/height;
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
		World w = World.getInstance();
		
		GL11.glColor3d(1,1,1);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(x + w.getMapXOffset(), y + w.getMapYOffset());
		GL11.glVertex2d(x + width + w.getMapXOffset(), y + w.getMapYOffset());
		GL11.glVertex2d(x + width + w.getMapXOffset(), y + height + w.getMapYOffset());
		GL11.glVertex2d(x + w.getMapXOffset(), y + height + w.getMapYOffset());
		GL11.glEnd();
	}
	
	@Override
	public int hashCode(){
		int hash = 7;
		hash = 71 * hash + x/width;
		hash = 71 * hash + y/height;
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

}
