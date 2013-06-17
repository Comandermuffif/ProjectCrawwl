package org.projectcrawwl.data;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

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
			h.renderHull();
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
