package org.projectcrawwl.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.projectcrawwl.objects.BasePlayer;

public class Pathfinding {
	GameData data = GameData.getInstance();
	
	private static Pathfinding instance = null;
	
	public static Pathfinding getInstance()
	{
		if(instance == null)
			instance = new Pathfinding();
		return instance;
	}
	
	public int getH(Point start, Point finish){
		int dx = Math.abs(finish.x - start.x);
		int dy = Math.abs(finish.y - start.y);
		return dx + dy;
		//return 0;
	}
	
	public double getG(Point a, Map<Object, Point> Parents){
		Point b = Parents.get(a);
		if(a == b){
			return 0;
		}else{
			return getG(b, Parents) +  Math.pow(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2), .5);
		}
	}
	
	public ArrayList<Point> getPath(BasePlayer start, BasePlayer target){
		//System.out.println("Starting");
		
		ArrayList<Point> open = new ArrayList<Point>();
		ArrayList<Point> path = new ArrayList<Point>();
		ArrayList<Point> stack = new ArrayList<Point>();
		
		Map<Object,Point> parents = new HashMap<Object, Point>();
		Point destination = new Point((int)(target.x / data.getGridX()),(int) (target.y / data.getGridY()));
		Point source  = new Point((int)(start.x / data.getGridX()),(int) (start.y / data.getGridY()));
		
		open.add(source);
		parents.put(source, source);
		
		Point current  = source;
		
		while(!open.isEmpty()){
			double smallestF = -1;
			for(Point a : open){
				double tempF = getH(a, destination) + getG(a, parents);
				if(smallestF == -1){
					smallestF = tempF;
					current = a;
				}else if(tempF < smallestF){
					smallestF = tempF;
					current = a;
				}
			}
			open.remove(0);
			//System.out.println(current);
			if(current == destination){
				break;
			}
			
			for(int a = -1; a < 2 ; a++){
				for(int b = -1; b < 2 ; b++){
					if(!(a == 0 && b == 0)){
					//if((a == 0 && b != 0) || (a != 0 && b == 0)){
						Point temp = new Point( current.x+a,current.y+ b);
						if(!parents.containsKey(temp) && 0 <= temp.y && temp.y < (data.getMapY()/data.getGridY())  && 0 <= temp.x && temp.x < (data.getMapX()/data.getGridX()) && data.getGrid().get(temp.x).get(temp.y) >= 0 && !open.contains(temp)){
							parents.put(temp, current);
							open.add(temp);
						}
					}
				}
			}
		}
		if(parents.containsKey(destination)){
			Point temp = destination;
			while(temp != source){
				stack.add(0, temp);
				temp = parents.get(temp);
			}
			stack.add(0,source);
		}
		while(!stack.isEmpty()){
			path.add(stack.get(0));
			stack.remove(0);
		}
		return path;
	}
	

	public ArrayList<Point> getPath(BasePlayer start){
		//System.out.println("Starting");
		
		ArrayList<Point> open = new ArrayList<Point>();
		ArrayList<Point> path = new ArrayList<Point>();
		ArrayList<Point> stack = new ArrayList<Point>();
		
		Map<Object,Point> parents = new HashMap<Object, Point>();
		
		int tempX = (int) (Math.random() * (data.getMapX() / data.getGridX()));
		int tempY = (int) (Math.random() * (data.getMapY() / data.getGridY()));
		while(data.getGrid().get(tempX).get(tempY) < 0){
			tempX = (int) (Math.random() * (data.getMapX() / data.getGridX()));
			tempY = (int) (Math.random() * (data.getMapY() / data.getGridY()));
		}
		
		Point destination = new Point((int)(tempX),(int) (tempY));
		
		Point source  = new Point((int)(start.x / data.getGridX()),(int) (start.y / data.getGridY()));
		
		open.add(source);
		parents.put(source, source);
		
		Point current  = source;
		
		while(!open.isEmpty()){
			double smallestF = -1;
			for(Point a : open){
				double tempF = getH(a, destination) + getG(a, parents);
				if(smallestF == -1){
					smallestF = tempF;
					current = a;
				}else if(tempF < smallestF){
					smallestF = tempF;
					current = a;
				}
			}
			open.remove(0);
			//System.out.println(current);
			if(current == destination){
				break;
			}
			for(int a = -1; a < 2 ; a++){
				for(int b = -1; b < 2 ; b++){
					if(!(a == 0 && b == 0)){
					//if((a == 0 && b != 0) || (a != 0 && b == 0)){
						Point temp = new Point( current.x+a,current.y+ b);
						if(!parents.containsKey(temp) && 0 <= temp.y && temp.y < (data.getMapY()/data.getGridY())  && 0 <= temp.x && temp.x < (data.getMapX()/data.getGridX()) && data.getGrid().get(temp.x).get(temp.y) >= 0 && !open.contains(temp)){
							parents.put(temp, current);
							open.add(temp);
						}
					}
				}
			}
		}
		if(parents.containsKey(destination)){
			Point temp = destination;
			while(temp != source){
				stack.add(0, temp);
				temp = parents.get(temp);
			}
			stack.add(0,source);
		}
		while(!stack.isEmpty()){
			path.add(stack.get(0));
			stack.remove(0);
		}
		return path;
	}
	
	
	public ArrayList<Point> getPath2(BasePlayer start){
		//System.out.println("Starting");
		
		ArrayList<Point> open = new ArrayList<Point>();
		ArrayList<Point> path = new ArrayList<Point>();
		ArrayList<Point> stack = new ArrayList<Point>();
		
		Map<Object,Point> parents = new HashMap<Object, Point>();
		
		int tempX = (int) (Math.random() * (data.getMapX() / data.getGridX()));
		int tempY = (int) (Math.random() * (data.getMapY() / data.getGridY()));
		while(data.getGrid().get(tempX).get(tempY) < 0){
			tempX = (int) (Math.random() * (data.getMapX() / data.getGridX()));
			tempY = (int) (Math.random() * (data.getMapY() / data.getGridY()));
		}
		
		Point destination = new Point((int)(tempX),(int) (tempY));
		
		Point source  = new Point((int)(start.x / data.getGridX()),(int) (start.y / data.getGridY()));
		
		open.add(source);
		parents.put(source, source);
		
		Point current  = source;
		
		while(!open.isEmpty()){
			double smallestF = -1;
			for(Point a : open){
				double tempF = getH(a, destination) + getG(a, parents);
				if(smallestF == -1){
					smallestF = tempF;
					current = a;
				}else if(tempF < smallestF){
					smallestF = tempF;
					current = a;
				}
			}
			
			open.remove(0);
			//System.out.println(current);
			if(current == destination){
				break;
			}
			for(int a = -1; a < 2 ; a++){
				for(int b = -1; b < 2 ; b++){
					//if(!(a == 0 && b == 0)){
					if((a == 0 && b != 0) || (a != 0 && b == 0)){
						Point temp = new Point( current.x+a,current.y+ b);
						if(!parents.containsKey(temp) && 0 <= temp.y && temp.y < (data.getMapY()/data.getGridY())  && 0 <= temp.x && temp.x < (data.getMapX()/data.getGridX()) && data.getGrid().get(temp.x).get(temp.y) >= 0 && !open.contains(temp)){
							parents.put(temp, current);
							open.add(temp);
						}
					}
				}
			}
		}
		if(parents.containsKey(destination)){
			Point temp = destination;
			while(temp != source){
				stack.add(0, temp);
				temp = parents.get(temp);
			}
			stack.add(0,source);
		}
		while(!stack.isEmpty()){
			path.add(stack.get(0));
			stack.remove(0);
		}
		return path;
	}
	
	public ArrayList<Point> getPath(BasePlayer start, Point target){
		//System.out.println("Starting");
		
		ArrayList<Point> open = new ArrayList<Point>();
		ArrayList<Point> path = new ArrayList<Point>();
		ArrayList<Point> stack = new ArrayList<Point>();
		
		Map<Object,Point> parents = new HashMap<Object, Point>();
		Point destination = target;
		Point source  = new Point((int)(start.x / data.getGridX()),(int) (start.y / data.getGridY()));
		
		open.add(source);
		parents.put(source, source);
		
		Point current  = source;
		
		while(!open.isEmpty()){
			double smallestF = -1;
			for(Point a : open){
				double tempF = getH(a, destination) + getG(a, parents);
				if(smallestF == -1){
					smallestF = tempF;
					current = a;
				}else if(tempF < smallestF){
					smallestF = tempF;
					current = a;
				}
			}
			
			open.remove(0);
			//System.out.println(current);
			if(current == destination){
				break;
			}
			for(int a = -1; a < 2 ; a++){
				for(int b = -1; b < 2 ; b++){
					if(!(a == 0 && b == 0)){
						Point temp = new Point( current.x+a,current.y+ b);
						if(!parents.containsKey(temp) && 0 <= temp.y && temp.y < (data.getMapY()/data.getGridY())  && 0 <= temp.x && temp.x < (data.getMapX()/data.getGridX()) && data.getGrid().get(temp.x).get(temp.y) != 1 && !open.contains(temp)){
							parents.put(temp, current);
							open.add(temp);
						}
					}
				}
			}
		}
		
		if(parents.containsKey(destination)){
			Point temp = destination;
			while(temp != source){
				stack.add(0, temp);
				temp = parents.get(temp);
			}
			stack.add(0,source);
		}
		while(!stack.isEmpty()){
			path.add(stack.get(0));
			stack.remove(0);
		}
		return path;
	}

}
