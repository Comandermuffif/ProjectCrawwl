package org.projectcrawwl.data;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class World {
	
	private float mapX = 2000;
	private float mapY = 1000;
	
	private float gridX = 10;//10
	private float gridY = 10;//10
	
	
	private float mapXOffset;// = (1280-600)/2;
	private float mapYOffset;// = (720-600)/2;
	
	private ArrayList<ArrayList<Integer>> grid = new ArrayList<ArrayList<Integer>>();
	
	//0 is dark, 255 is lit
	private ArrayList<ArrayList<Integer>> gridLight = new ArrayList<ArrayList<Integer>>();
	
	static GameSettings settings = GameSettings.getInstance();
	
	private ArrayList<Point> rooms = new ArrayList<Point>();
	
	private ArrayList<Point> walls = new ArrayList<Point>();
	
	/* 0 is nothing
	 * negative values are not walkable
	 * positive values are
	 * -1 is a wall
	 * 0 is nothing
	 * 1 is water
	 */
	
	public World(){
		
		for (int i = 0; i < mapX/gridX; i++){
	         ArrayList<Integer> row = new ArrayList<Integer>();
	         ArrayList<Integer> row2 = new ArrayList<Integer>();
	         for (int j = 0; j < mapY/gridY; j++){
	        	 
	        	 if(i == 0 || j == 0 || i + 1 == mapX/gridX || j + 1 == mapY/gridY || i == 1 || j == 1 || i+2 == mapX/gridX || j+2 == mapY/gridY){
	        		 row.add(-1);
	        		 walls.add(new Point(i,j));
	        	 }else{
	        		 row.add(0);
	        	 }
	        	 
	        	 
	        	 
	        	 
	        	 row2.add(0);
	         }
	         grid.add(row);
	         gridLight.add(row2);
	    }
		
		for(int a = 0; a < Math.random()*5 + 5; a ++){
			generateRoom();
		}
		//connectRooms();
	}
	
	public ArrayList<ArrayList<Integer>> getGrid(){
		return grid;
	}
	
	public float getMapX(){
		return mapX;
	}
	public float getMapY(){
		return mapY;
	}
	
	public float getGridX(){
		return gridX;
	}
	public float getGridY(){
		return gridY;
	}
	
	public void addWall(Point p){
		grid.get(p.x).set(p.y, -1);
	}
	
	public float getMapXOffset(){
		return mapXOffset;
	}
	public float getMapYOffset(){
		return mapYOffset;
	}
	
	public void setMapXOffset(float temp){
		mapXOffset = temp;
	}
	public void setMapYOffset(float temp){
		mapYOffset = temp;
	}
	
	public void generateRoom(){
		int w = (int) (5 + Math.random()*4);
		int h = (int) (4 + Math.random()*5);
		
		int x = (int) (Math.random() * (mapX/gridX - w - 2));
		int y = (int) (Math.random() * (mapY/gridY - h - 2));
		
		rooms.add(new Point(x + w/2,y + h/2));
		
		for(int a = x; a <= x + w; a ++){
			for(int b = y; b <= y + h; b ++){
				grid.get(a).set(b,1);
				if(a == x || a == x+w || b == y){
					grid.get(a).set(b, -1);
					walls.add(new Point(a,b));
				}
			}
		}
	}
	/*
	public void connectRooms(){
		Point current = null;
		for(Point p : rooms){
			if(current == null){
				current = p;
			}else{
				
				int deltaX = Math.abs(p.x - current.x);
				int deltaY = Math.abs(p.y - current.y);
				
				if(p.x > current.x){
					for(int a = 0; a < deltaX; a++){
						grid[p.x - a][p.y-1] = 2;
						grid[p.x - a][p.y] = 2;
						grid[p.x - a][p.y+1] = 2;
					}
				}else{
					for(int a = 0; a < deltaX; a++){
						grid[p.x + a][p.y-1] = 2;
						grid[p.x + a][p.y] = 2;
						grid[p.x + a][p.y+1] = 2;
					}
				}
				
				if(p.y > current.y){
					if(p.x < current.x){
						deltaX = -deltaX;
					}
					for(int a = 0; a < deltaY; a++){
						grid[p.x  - deltaX][p.y - a] = 2;
						grid[p.x  - deltaX+1][p.y - a] = 2;
						grid[p.x  - deltaX-1][p.y - a] = 2;
					}
				}else{
					if(p.x > current.x){
						deltaX = -deltaX;
					}
					for(int a = 0; a < deltaY; a++){
						grid[p.x  + deltaX][p.y + a] = 2;
						grid[p.x  + deltaX+1][p.y + a] = 2;
						grid[p.x  + deltaX-1][p.y + a] = 2;
					}
				}
				
				current = p;
			}
		}
	}*/
	
	public void setLight(int x, int y, int val){
		
		gridLight.get(x).set(y, val);
		
	}
	
	public int getLight(int x, int y){
		
		return gridLight.get(x).get(y);
		
	}
	

	public void renderLights(){
		
		//if(background == null){
		//	renderBackground();
		//}
		
		
		int leftBound = (int) Math.floor(-mapXOffset/gridX);
		int rightBound = (int) Math.floor((-mapXOffset + settings.getScreenX())/gridX);
		int upBound = (int) Math.floor(-mapYOffset/gridY);
		int downBound = (int) Math.floor((-mapYOffset + settings.getScreenY())/gridY);
		
		if(leftBound < 0){
			leftBound = 0;
		}else if (leftBound >= mapX/gridX){
			leftBound = (int) (mapX/gridX - 1);
		}
		
		if(rightBound < 0){
			rightBound = 0;
		}else if (rightBound >= mapX/gridX){
			rightBound = (int) (mapX/gridX - 1);
		}
		
		if(upBound < 0){
			upBound = 0;
		}else if (upBound >= mapY/gridY){
			upBound = (int) (mapX/gridX - 1);
		}
		
		if(downBound < 0){
			downBound = 0;
		}else if (downBound >= mapY/gridY){
			downBound = (int) (mapY/gridY - 1);
		}
		
		for(int x = leftBound; x <= rightBound; x ++){
			for(int y = upBound; y <= downBound; y ++){
				GL11.glColor4d(0, 0, 0,(255 - gridLight.get(x).get(y))/255);
				
				GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
				
				GL11.glVertex3f(x*gridX + mapXOffset,y*gridY + mapYOffset,0);
				GL11.glVertex3f(x*gridX + mapXOffset+ gridX,y*gridY + mapYOffset,0);
				GL11.glVertex3f(x*gridX + mapXOffset,y*gridY + mapYOffset+gridY,0);
				GL11.glVertex3f(x*gridX + mapXOffset+gridX,y*gridY + mapYOffset+gridY,0);
				GL11.glEnd();
				gridLight.get(x).set(y, 0);
			}
		}
	}
	

	public void renderBackgroundOld(){
		int leftBound = (int) Math.floor(-mapXOffset/gridX);
		int rightBound = (int) Math.floor((-mapXOffset + settings.getScreenX())/gridX);
		int upBound = (int) Math.floor(-mapYOffset/gridY);
		int downBound = (int) Math.floor((-mapYOffset + settings.getScreenY())/gridY);
		
		if(leftBound < 0){
			leftBound = 0;
		}else if (leftBound >= mapX/gridX){
			leftBound = (int) (mapX/gridX - 1);
		}
		
		if(rightBound < 0){
			rightBound = 0;
		}else if (rightBound >= mapX/gridX){
			rightBound = (int) (mapX/gridX - 1);
		}
		
		if(upBound < 0){
			upBound = 0;
		}else if (upBound >= mapY/gridY){
			upBound = (int) (mapX/gridX - 1);
		}
		
		if(downBound < 0){
			downBound = 0;
		}else if (downBound >= mapY/gridY){
			downBound = (int) (mapY/gridY - 1);
		}
		
		for(int x = leftBound; x <= rightBound; x ++){
			for(int y = upBound; y <= downBound; y ++){
				int b = grid.get(x).get(y);

				System.out.println(b);
				if(b == 0){
					GL11.glColor3d(.34,.23,.04);
					//GL11.glColor3d((87/255),(59/255),(12/255));
				}
				if(b < 0){
					GL11.glColor3d(1,1,1);
					//GL11.glColor3d((255/255),(255/255),(255/255));
				}
				if(b == 1){
					GL11.glColor3d(.1,.41,.62);
					//GL11.glColor3d((28/255),(107/255),(160/255));
				}
				GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
				
				GL11.glVertex3f(x*gridX + mapXOffset,y*gridY + mapYOffset,0);
				GL11.glVertex3f(x*gridX + mapXOffset+ gridX,y*gridY + mapYOffset,0);
				GL11.glVertex3f(x*gridX + mapXOffset,y*gridY + mapYOffset+gridY,0);
				GL11.glVertex3f(x*gridX + mapXOffset+gridX,y*gridY + mapYOffset+gridY,0);
				GL11.glEnd();
			}
		}
	}
	
	
	public void renderBackground(){
		int leftBound = (int) Math.floor(-mapXOffset/gridX);
		int rightBound = (int) Math.floor((-mapXOffset + settings.getScreenX())/gridX);
		int upBound = (int) Math.floor(-mapYOffset/gridY);
		int downBound = (int) Math.floor((-mapYOffset + settings.getScreenY())/gridY);
		
		if(leftBound < 0){
			leftBound = 0;
		}else if (leftBound >= mapX/gridX){
			leftBound = (int) (mapX/gridX - 1);
		}
		
		if(rightBound < 0){
			rightBound = 0;
		}else if (rightBound >= mapX/gridX){
			rightBound = (int) (mapX/gridX - 1);
		}
		
		if(upBound < 0){
			upBound = 0;
		}else if (upBound >= mapY/gridY){
			upBound = (int) (mapX/gridX - 1);
		}
		
		if(downBound < 0){
			downBound = 0;
		}else if (downBound >= mapY/gridY){
			downBound = (int) (mapY/gridY - 1);
		}
		
		GL11.glColor3d(.34,.23,.04);
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex3f(leftBound*gridX + mapXOffset,upBound*gridY + mapYOffset,0);
		GL11.glVertex3f(rightBound*gridX + mapXOffset+ gridX,upBound*gridY + mapYOffset,0);
		GL11.glVertex3f(leftBound*gridX + mapXOffset,downBound*gridY + mapYOffset+gridY,0);
		GL11.glVertex3f(rightBound*gridX + mapXOffset+gridX,downBound*gridY + mapYOffset+gridY,0);
		GL11.glEnd();
		
		for(Point p : walls){
			GL11.glColor3d(1,1,1);
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			GL11.glVertex3f(p.x*gridX + mapXOffset,p.y*gridY + mapYOffset,0);
			GL11.glVertex3f(p.x*gridX + mapXOffset+ gridX,p.y*gridY + mapYOffset,0);
			GL11.glVertex3f(p.x*gridX + mapXOffset,p.y*gridY + mapYOffset+gridY,0);
			GL11.glVertex3f(p.x*gridX + mapXOffset+gridX,p.y*gridY + mapYOffset+gridY,0);
			GL11.glEnd();
		}
	}
	
	
}
