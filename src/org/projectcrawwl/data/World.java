package org.projectcrawwl.data;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class World implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private float mapXOffset = 0;
	private float mapYOffset = 0;
	
	static GameSettings settings = GameSettings.getInstance();
	
	private ArrayList<ConvexHull> hulls = new ArrayList<ConvexHull>();
	private ArrayList<ConvexHull> tileHulls = new ArrayList<ConvexHull>();
	private ArrayList<ConvexHull> allHulls = new ArrayList<ConvexHull>();
	
	
	private ArrayList<WorldTile> tiles = new ArrayList<WorldTile>();
	
	private HashSet<WorldTile> tileMap = new HashSet<WorldTile>();
	
	public World(){
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
		{
//			WorldTile t = new WorldTile("res/WorldTiles/tile1.WorldTile");
//			tiles.add(t);
//			tileMap.add(t);
		}
		
		
		ArrayList<WorldTile> queue = new ArrayList<WorldTile>();
		
		queue.addAll(tiles);
		
		int i = 0;
		
		while(!queue.isEmpty()){
			
			i ++;
			
			WorldTile current = queue.get(0);
			queue.remove(0);
			
			if(i > 50){
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
				if(current.getSides()[0] == 0 && !tileMap.contains(new WorldTile(current.getX(), current.getY() + 1))){
					WorldTile t = new WorldTile(current.getX(), current.getY() + 1, new int[]{-1,-1,0,-1});
					tiles.add(t);
					tileMap.add(t);
					queue.add(t);
				}
				
				if(current.getSides()[1] == 0 && !tileMap.contains(new WorldTile(current.getX() + 1, current.getY()))){
					WorldTile t = new WorldTile(current.getX() + 1, current.getY(), new int[]{-1,-1,-1,0});
					tiles.add(t);
					tileMap.add(t);
					queue.add(t);
				}
				
				if(current.getSides()[2] == 0 && !tileMap.contains(new WorldTile(current.getX(), current.getY() - 1))){
					WorldTile t = new WorldTile(current.getX(), current.getY() - 1, new int[]{0,-1,-1,-1});
					tiles.add(t);
					tileMap.add(t);
					queue.add(t);
				}
				if(current.getSides()[3] == 0 && !tileMap.contains(new WorldTile(current.getX() - 1, current.getY()))){
					WorldTile t = new WorldTile(current.getX() - 1, current.getY(), new int[]{-1,0,-1,-1});
					tiles.add(t);
					tileMap.add(t);
					queue.add(t);
				}
			}
		}
		
		for(WorldTile t : tiles){
			tileHulls.addAll(t.getHulls());
		}
		
		allHulls.addAll(tileHulls);
		allHulls.addAll(hulls);
	}
	
	@SuppressWarnings("unused")
	private ArrayList<ConvexHull> readFile(String filename){
		ArrayList<ConvexHull> data = new ArrayList<ConvexHull>();
		try {
			
			BufferedReader wordStream = new BufferedReader(new FileReader(filename));
			
			String l;
			ConvexHull hull = null;
			
			
			while((l = wordStream.readLine()) != null){
				if(l.equals("hull")){
					if(hull != null){
						data.add(hull);
					}
					System.out.println("new hull");
					hull = new ConvexHull();
				}else{
					String[] line = l.split(" ");
					if(line.length == 2){
						System.out.println(line[0] + " " + line[1]);
						hull.addPoint(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
					}
				}
			}
			data.add(hull);
			
			wordStream.close();
			
		}catch(IOException e){e.printStackTrace();}
		
		return data;
	}
	
	public ArrayList<WorldTile> getTiles(){
		return tiles;
	}
	
	public ArrayList<ConvexHull> getHulls(){
		return allHulls;
	}
	
	public void addHull(ConvexHull h){
		hulls.add(h);
		allHulls.add(h);
	}
	
	public void clearHulls(){
		hulls.clear();
	}
	
	private static World instance = null;

	public static World getInstance()
	{
		if(instance == null){
			instance = new World();
		}
		return instance;
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
	
	public Point getLineLineIntersection(Line2D.Float a, Line2D.Float b) {
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
	protected double det(double a, double b, double c, double d) {
		return a * d - b * c;
	}

	public void renderHulls(){
		
		Collections.sort(allHulls);
		
		for(ConvexHull x : allHulls){
			x.renderHull();
		}
	}
	
	public void renderShadows(){
		for(ConvexHull x : allHulls){
			x.renderShadow();
		}
	}
	
	public void renderBackground(){
		for(WorldTile t : tiles){
			t.renderBackground();
		}	
	}
}
