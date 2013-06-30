package org.projectcrawwl.data;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.projectcrawwl.objects.ConvexHull;

public class World{
	
	private static float mapXOffset = 0;
	private static float mapYOffset = 0;
	
	private static ArrayList<ConvexHull> hulls = new ArrayList<ConvexHull>();
	
	private static ArrayList<WorldTile> tiles = new ArrayList<WorldTile>();
	
	private static int tileLimit = 50;
	
	private World(){}
	
	public static void clearData(){
		mapXOffset = 0;
		mapYOffset = 0;
		
		hulls.clear();
		
		tiles.clear();
		
		tileLimit = 0;
	}
	
	public static void generateWorld(){
		
		HashSet<WorldTile> tileMap = new HashSet<WorldTile>();
		
		GameData.clearData();
		
		tiles.clear();
		tileMap.clear();
		
		hulls.clear();
		
		ArrayList<String> up = new ArrayList<String>();
		ArrayList<String> down = new ArrayList<String>();
		ArrayList<String> left = new ArrayList<String>();
		ArrayList<String> right = new ArrayList<String>();
		
		
		for(final File file : new File("res/WorldTiles").listFiles()){
			if(file.isFile()){
				String extension = file.getName().split("\\.")[file.getName().split("\\.").length - 1].toLowerCase();
				
				if(extension.equals("worldtile")){
					int[] data = WorldTile.possibleTiles(file.getPath());
					
					if(data[0] == 0){
						up.add(file.getPath());
					}
					if(data[1] == 0){
						right.add(file.getPath());
					}
					if(data[2] == 0){
						down.add(file.getPath());
					}
					if(data[3] == 0){
						left.add(file.getPath());
					}
				}
			}
		}
		
		{
			WorldTile t = new WorldTile(0,0, new int[]{0,0,0,0});
			ConvexHull h = new ConvexHull();
			h.addPoint(125,125);
			h.addPoint(375,125);
			h.addPoint(375,375);
			h.addPoint(125,375);
			t.addHull(h);
			tiles.add(t);
			tileMap.add(t);
		}
		
		
		ArrayList<WorldTile> queue = new ArrayList<WorldTile>();
		
		queue.addAll(tiles);
		
		int i = 0;
		
		while(!queue.isEmpty()){
			
			i ++;
			
			WorldTile current = queue.get(0);
			queue.remove(0);
			
			if(i + queue.size() >= tileLimit){
				if(current.getSides()[0] == 0 && !tileMap.contains(new WorldTile(current.getX(), current.getY() + 1))){
					WorldTile t = new WorldTile(current.getX(), current.getY() + 1, new int[]{1,1,0,1});
					tiles.add(t);
					tileMap.add(t);
					queue.add(t);
				}
				
				if(current.getSides()[1] == 0 && !tileMap.contains(new WorldTile(current.getX() + 1, current.getY()))){
					WorldTile t = new WorldTile(current.getX() + 1, current.getY(), new int[]{1,1,1,0});
					tiles.add(t);
					tileMap.add(t);
					queue.add(t);
				}
				
				if(current.getSides()[2] == 0 && !tileMap.contains(new WorldTile(current.getX(), current.getY() - 1))){
					WorldTile t = new WorldTile(current.getX(), current.getY() - 1, new int[]{0,1,1,1});
					tiles.add(t);
					tileMap.add(t);
					queue.add(t);
				}
				if(current.getSides()[3] == 0 && !tileMap.contains(new WorldTile(current.getX() - 1, current.getY()))){
					WorldTile t = new WorldTile(current.getX() - 1, current.getY(), new int[]{1,0,1,1});
					tiles.add(t);
					tileMap.add(t);
					queue.add(t);
				}
			}else{
				//top
				if(current.getSides()[0] == 0 && !tileMap.contains(new WorldTile(current.getX(), current.getY() + 1))){
					WorldTile t = new WorldTile(current.getX(), current.getY() + 1, down.get((int) (Math.random()*down.size())) , 2);
					tiles.add(t);
					tileMap.add(t);
					queue.add(t);
				}
				//Right
				if(current.getSides()[1] == 0 && !tileMap.contains(new WorldTile(current.getX() + 1, current.getY()))){
					WorldTile t = new WorldTile(current.getX() + 1, current.getY(), left.get((int) (Math.random()*left.size())) , 3);
					tiles.add(t);
					tileMap.add(t);
					queue.add(t);
				}
				//bottom
				if(current.getSides()[2] == 0 && !tileMap.contains(new WorldTile(current.getX(), current.getY() - 1))){
					
					WorldTile t = new WorldTile(current.getX(), current.getY() - 1, up.get((int) (Math.random()*up.size())) , 0);
					
					tiles.add(t);
					tileMap.add(t);
					queue.add(t);
				}
				//right
				if(current.getSides()[3] == 0 && !tileMap.contains(new WorldTile(current.getX() - 1, current.getY()))){
					WorldTile t = new WorldTile(current.getX() - 1, current.getY(), right.get((int) (Math.random()*right.size())) , 1);
					tiles.add(t);
					tileMap.add(t);
					queue.add(t);
				}
			}
		}
		
		for(WorldTile t : tiles){
			hulls.addAll(t.getHulls());
		}
		
		GameData.update();
		
	}

	public static ArrayList<WorldTile> getTiles(){
		return tiles;
	}
	
	public static ArrayList<ConvexHull> getHulls(){
		return hulls;
	}
	
	public static void addTile(WorldTile t){
		tiles.add(t);
		hulls.addAll(t.getHulls());
	}
	
	public static void clearHulls(){
		hulls.clear();
	}
	
	public static float getMapXOffset(){
		return mapXOffset;
	}
	public static float getMapYOffset(){
		return mapYOffset;
	}
	
	public static void setMapXOffset(float temp){
		mapXOffset = temp;
	}
	public static void setMapYOffset(float temp){
		mapYOffset = temp;
	}
	
	public static Point getLineLineIntersection(Line2D.Float a, Line2D.Float b) {
		double x1 = a.x1; double y1 = a.y1; double x2 = a.x2; double y2 = a.y2; double x3 = b. x1; double y3 = b.y1; double x4 = b.x2; double y4 = b.y2;
	    double det1And2 = det(x1, y1, x2, y2);
	    double det3And4 = det(x3, y3, x4, y4);
	    double x1LessX2 = x1 - x2;
	    double y1LessY2 = y1 - y2;
	    double x3LessX4 = x3 - x4;
	    double y3LessY4 = y3 - y4;
	    double det1Less2And3Less4 = det(x1LessX2, y1LessY2, x3LessX4, y3LessY4);
	    if (det1Less2And3Less4 == 0){
	       // the denominator is zero so the lines are parallel and there's either no solution (or multiple solutions if the lines overlap) so return null.
	       return null;
	    }
	    double x = (det(det1And2, x1LessX2, det3And4, x3LessX4) / det1Less2And3Less4);
	    double y = (det(det1And2, y1LessY2, det3And4, y3LessY4) / det1Less2And3Less4);
	    return new Point((int)x, (int)y);
	}
	private  static double det(double a, double b, double c, double d) {
		return a * d - b * c;
	}

	public static void renderHulls(){
		
		Collections.sort(hulls);
		
		for(ConvexHull x : hulls){
			x.render();
		}
	}
	
	public static void renderShadows(){
		for(ConvexHull x : hulls){
			x.renderShadow();
		}
	}
	
	public static void renderBackground(){
		for(WorldTile t : tiles){
			t.renderBackground();
		}	
	}
	
	public static void setTileLimit(int limit){
		tileLimit = limit;
	}
}
