package org.projectcrawwl.objects;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.World;

public class GameObject implements Comparable<GameObject>{
	public float x;
	public float y;
	public float facingAngle;
	
	public float tempFacing;
	
	public float moveAngle;
	public double speed;
	
	public float renderX;
	public float renderY;
	
	public double speedMult = 1;
	
	public double turnSpeed = 0.1;
	
	public Polygon boundingBox = new Polygon();
	public ArrayList<Line2D.Float> boundingLines = new ArrayList<Line2D.Float>();
	
	private Point center = new Point();
	
	protected double farthest = 0;
	
	public boolean passThroughPlayers = false;
	
	public boolean isReady = false;
	
	public int id;
	
	
	
	/**
	 * A generic Game Object
	 * 
	 * @param tempX The x position
	 * @param tempY The y position
	 * @param tempA Facing angle
	 * @param tempH Health
	 */
	public GameObject(float tempX, float tempY, float tempA){
		x = tempX;
		y = tempY;
		facingAngle = tempA;
		tempFacing = facingAngle;
		
		id = GameData.getNewID();
	}
	
	
	public GameObject(float tempX, float tempY){
		x = tempX;
		y = tempY;
		facingAngle = 0;
		tempFacing = facingAngle;
		
		id = GameData.getNewID();
	}
	public GameObject(){
		x = 0;
		y = 0;
		facingAngle = 0;
		tempFacing = facingAngle;
		
		id = GameData.getNewID();
	}
	
	public void addPoint(Point k){
		boundingBox.addPoint(k.x, k.y);
		updateData();
	}
	
	public ArrayList<Line2D.Float> getLines(){
		return boundingLines;
	}
	
	public Polygon getPolygon(){
		return boundingBox;
	}
	
	public ArrayList<Point> getPoints(){
		ArrayList<Point> points = new ArrayList<Point>();
		
		for(int i = 0; i < boundingBox.npoints; i ++){
			points.add(new Point(boundingBox.xpoints[i], boundingBox.ypoints[i]));
		}
		
		return points;
	}
	
	public void addPoint(double x, double y){
		boundingBox.addPoint((int) x,(int) y);
		updateData();
	}
	
	public void updateData(){
		float deltaX = 0;
		float deltaY = 0;
		
		for(int i = 0; i < boundingBox.npoints; i ++){
			deltaX += boundingBox.xpoints[i];
			deltaY += boundingBox.ypoints[i];
		}
		
		deltaX = deltaX/boundingBox.npoints;
		deltaY = deltaY/boundingBox.npoints;
		
		center.setLocation(deltaX, deltaY);
		
		farthest = 0;
		
		for(int i = 0; i < boundingBox.npoints; i ++){
			double temp = center.distance(boundingBox.xpoints[i], boundingBox.ypoints[i]);
			if(temp > farthest){
				farthest = temp;
			}
		}
	}
	
	public void updateLines(){
		ArrayList<Line2D.Float> temp = new ArrayList<Line2D.Float>();
		
		float[] coord = new float[6];
		float[] lastCoord = new float[2];
		float[] firstCoord = new float[2];
		PathIterator pi = boundingBox.getPathIterator(null);
		
		pi.currentSegment(coord);
		
		pi.currentSegment(firstCoord); //Getting the first coordinate pair
        lastCoord[0] = firstCoord[0]; //Priming the previous coordinate pair
        lastCoord[1] = firstCoord[1];
		
		while(!pi.isDone()){
			final int type = pi.currentSegment(coord);
            switch(type) {
                case PathIterator.SEG_LINETO : {
                	temp.add(new Line2D.Float(coord[0], coord[1], lastCoord[0], lastCoord[1]));
                    lastCoord[0] = coord[0];
                    lastCoord[1] = coord[1];
                    break;
                }
                case PathIterator.SEG_CLOSE : {
                    temp.add(new Line2D.Float(coord[0], coord[1], firstCoord[0], firstCoord[1]));   
                    break;
                }
            }
            pi.next();
		}
		
		boundingLines = temp;
		updateData();
	}
	
	public ArrayList<Line2D.Float> boundingBox(){
		return boundingLines;
	}
	
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	
	public float getRenderX(){
		return renderX;
	}
	public float getRenderY(){
		return renderY;
	}
	
	public void setX(int tempX){
		x = tempX;
	}
	public void setY(int tempY){
		y = tempY;
	}
	
	public double getFarthest(){
		return farthest;
	}
	
	public Point getCenter(){
		return new Point((int) (center.x + x), (int) (center.y + y));
	}
	/**
	 * Flag to tell whether an object is visible on screen
	 * @return
	 */
	public boolean isOnScreen(){
		
		float ratio = ((float) (GameSettings.getScreenY())/GameSettings.getScreenX());
		
		if(renderX + getFarthest() < -GameData.zoom || renderX - getFarthest() > GameSettings.getScreenX()  + GameData.zoom){
			return false;
		}
		if(renderY  + getFarthest() < -GameData.zoom*(ratio) || renderY - getFarthest() > GameSettings.getScreenY()  + GameData.zoom*(ratio)){
			return false;
		}
		
		return true;
	}
	
	//Draw everything here
	public void render(){
		if(!isReady){
			return;
		}
		
		if(!isOnScreen()){
			return;
		}
		
		ArrayList<Line2D.Float> lines = new ArrayList<Line2D.Float>();
		
		for(Line2D.Float temp : boundingLines){
			lines.add(new Line2D.Float((float) (temp.x1*Math.cos(Math.toRadians(facingAngle)) - temp.y1*Math.sin(Math.toRadians(facingAngle)) + (renderX - World.getMapXOffset())),(float) (temp.x1*Math.sin(Math.toRadians(facingAngle)) + temp.y1*Math.cos(Math.toRadians(facingAngle)) + (renderY - World.getMapYOffset())),(float) (temp.x2*Math.cos(Math.toRadians(facingAngle)) - temp.y2*Math.sin(Math.toRadians(facingAngle)) + (renderX - World.getMapXOffset())),(float) (temp.x2*Math.sin(Math.toRadians(facingAngle)) + temp.y2*Math.cos(Math.toRadians(facingAngle)) + (renderY - World.getMapYOffset()))));
		}
		
		GL11.glColor4d(0,0,0,1);
		GL11.glColor4d(.3,.3,.3,1);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		for(Line2D.Float line : lines){
			GL11.glVertex3d(line.getX1() + World.getMapXOffset(), line.getY1() + World.getMapYOffset(), .5);
			GL11.glVertex3d(line.getX2() + World.getMapXOffset(), line.getY2() + World.getMapYOffset(), .5);				
		}
		GL11.glEnd();
	
	
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
	}

	//Do all calculations here
	public void update(int delta){
		if(!isReady){isReady = true;}
		
		
		renderX = x + GameData.getMapXOffset();
		renderY = y + GameData.getMapYOffset();
		
		if(facingAngle != tempFacing){
			if(Math.abs(facingAngle - tempFacing) > delta*(turnSpeed)){
				if(facingAngle - tempFacing > delta*(turnSpeed) && Math.abs(facingAngle - tempFacing) < 180){
					tempFacing = (float) (facingAngle - delta*(turnSpeed));
				}else if(facingAngle - tempFacing < delta*(turnSpeed) && Math.abs(facingAngle - tempFacing) < 180){
					tempFacing = (float) (facingAngle + delta*(turnSpeed));
				}else if(facingAngle - tempFacing > delta*(turnSpeed) && Math.abs(facingAngle - tempFacing) > 180){
					tempFacing = (float) (facingAngle + delta*(turnSpeed));
				}else if(facingAngle - tempFacing < delta*(turnSpeed) && Math.abs(facingAngle - tempFacing) > 180){
					tempFacing = (float) (facingAngle - delta*(turnSpeed));
				}
				
				if(tempFacing < -180){
					tempFacing += 360;
				}
				if(tempFacing > 180){
					tempFacing -= 360;
				}
				
			}
			
			boolean flag = true;
			for(ConvexHull k : World.getHulls()){
				Point p = new Point((int) x, (int) y);
				if(p.distance(k.getCenter()) > k.getFarthest() + getFarthest()){
					continue;
				}
				
				for(Line2D.Float q : k.getLines()){
					for(Line2D.Float bound : boundingBox()){
						
						Line2D.Float temp = new Line2D.Float();
						
						temp.x1 = (float) (bound.x1*Math.cos(Math.toRadians(tempFacing)) - bound.y1*Math.sin(Math.toRadians(tempFacing)) + x);
						temp.y1 = (float) (bound.x1*Math.sin(Math.toRadians(tempFacing)) + bound.y1*Math.cos(Math.toRadians(tempFacing)) + y);
						temp.x2 = (float) (bound.x2*Math.cos(Math.toRadians(tempFacing)) - bound.y2*Math.sin(Math.toRadians(tempFacing)) + x);
						temp.y2 = (float) (bound.x2*Math.sin(Math.toRadians(tempFacing)) + bound.y2*Math.cos(Math.toRadians(tempFacing)) + y);
								
								
						//Line2D.Float temp = new Line2D.Float(bound.x1 + tempx, bound.y1 + tempy, bound.x2 + tempx, bound.y2 + tempy);
						if(temp.intersectsLine(q)){
							flag = false;
							break;
						}
					}
					if(!flag){break;}
				}
				if(!flag){break;}
			}
			if(flag){
				facingAngle = tempFacing;
			}
		}
		
		if(speed != 0){
			float tempx = (float) (x + Math.cos(Math.toRadians(moveAngle)) * delta * speed * speedMult);
			float tempy = (float) (y + Math.sin(Math.toRadians(moveAngle)) * delta * speed * speedMult);
			
			boolean flag = true;
			
			Line2D.Float tempLine = new Line2D.Float(x,y,tempx,tempy);
			
			for(ConvexHull k : World.getHulls()){
				
				if(tempLine.ptSegDist(k.getCenter()) > k.getFarthest() + getFarthest()){
					continue;
				}
				
				
				
				if(k.getPolygon().contains(tempx, tempy)){
					flag = false;
					break;
				}
				
				for(Line2D.Float q : k.getLines()){
					
					if(q.intersectsLine(tempLine)){
						flag = false;
						break;
					}
				}
				
				for(Line2D.Float q : k.getLines()){
					for(Line2D.Float bound : boundingBox()){
						
						Line2D.Float temp = new Line2D.Float();
						
						temp.x1 = (float) (bound.x1*Math.cos(Math.toRadians(facingAngle)) - bound.y1*Math.sin(Math.toRadians(facingAngle)) + tempx);
						temp.y1 = (float) (bound.x1*Math.sin(Math.toRadians(facingAngle)) + bound.y1*Math.cos(Math.toRadians(facingAngle)) + tempy);
						temp.x2 = (float) (bound.x2*Math.cos(Math.toRadians(facingAngle)) - bound.y2*Math.sin(Math.toRadians(facingAngle)) + tempx);
						temp.y2 = (float) (bound.x2*Math.sin(Math.toRadians(facingAngle)) + bound.y2*Math.cos(Math.toRadians(facingAngle)) + tempy);
								
								
						//Line2D.Float temp = new Line2D.Float(bound.x1 + tempx, bound.y1 + tempy, bound.x2 + tempx, bound.y2 + tempy);
						if(temp.intersectsLine(q)){
							flag = false;
							break;
						}
					}
					if(!flag){break;}
				}
				
				
				if(!flag){break;}
				
				
			}
			if(flag){
				
				x = tempx;
				y = tempy;
				
			}else{
				speed = 0;
			}
		}
		
		
	}
	
	protected Point2D.Double getNearest(){

		Point2D.Double pp = new Point2D.Double(GameSettings.getScreenX()/2 - World.getMapXOffset(), GameSettings.getScreenY()/2 - World.getMapYOffset());
		
		Point2D.Double q = new Point2D.Double(x,y);
		
		for(Line2D.Float l : boundingLines){
			
			Point2D.Double p = new Point2D.Double();
			
			p.x = l.x1*Math.cos(Math.toRadians(facingAngle)) - l.y1*Math.sin(Math.toRadians(facingAngle)) + x;
			p.y = l.x1*Math.sin(Math.toRadians(facingAngle)) + l.y1*Math.cos(Math.toRadians(facingAngle)) + y;
			
			if(q.x == x || q.y == y){
				q = (Point2D.Double) p.clone();
			}else{
				if(pp.distance(p) < pp.distance(q)){
					q = (Point2D.Double) p.clone();
				}
			}
		}
		
		return q;
		
	}
	
	public double getCompareTo(){
		double d = -1;
		
		Point2D.Double p = new Point2D.Double(GameSettings.getScreenX()/2 - World.getMapXOffset(), GameSettings.getScreenY()/2 - World.getMapYOffset());
		
		ArrayList<Line2D.Float> lines = new ArrayList<Line2D.Float>();
		
		for(Line2D.Float temp : boundingLines){
			lines.add(new Line2D.Float((float) (temp.x1*Math.cos(Math.toRadians(facingAngle)) - temp.y1*Math.sin(Math.toRadians(facingAngle)) + x),(float) (temp.x1*Math.sin(Math.toRadians(facingAngle)) + temp.y1*Math.cos(Math.toRadians(facingAngle)) + y),(float) (temp.x2*Math.cos(Math.toRadians(facingAngle)) - temp.y2*Math.sin(Math.toRadians(facingAngle)) + x),(float) (temp.x2*Math.sin(Math.toRadians(facingAngle)) + temp.y2*Math.cos(Math.toRadians(facingAngle)) + y)));
		}
		
		for(Line2D.Float temp : lines){
			double dd = temp.ptSegDist(p);
			
			if(d == -1){
				d = dd;
			}else if(dd < d){
				d = dd;
			}
		}
		
		return d;
	}
	
	public int compareTo(GameObject h) {
		
		if(h.getCompareTo() > this.getCompareTo()){
			return 1;
		}
		
		if(h.getCompareTo() < this.getCompareTo()){
			return -1;
		}
			
		return 0;
	}
	
	public int compareToOLD(GameObject h) {
		
		Point2D.Double p = new Point2D.Double(GameSettings.getScreenX()/2 - World.getMapXOffset(), GameSettings.getScreenY()/2 - World.getMapYOffset());
		
		if(h.getCenter().distance(p) > this.getCenter().distance(p)){
			return 1;
		}
		
		if(h.getCenter().distance(p) < this.getCenter().distance(p)){
			return -1;
		}
			
		return 0;
	}
	
	public String toXML(){
		String data = "";
		
		data += "<GameObject>\n";
		{
			
			data += "\t<x>" + x + "</x>\n";
			data += "\t<y>" + y + "</y>\n";
			
			data += "\t<facingAngle>" + facingAngle + "</facingAngle>\n";
			
			data += "\t<moveAngle>" + moveAngle + "</moveAngle>\n";
			
			data += "\t<speed>" + speed + "</speed>\n";
			
			data += "\t<turnSpeed>" + turnSpeed + "</turnSpeed>\n";
			
			data += "\t<boundingBox>\n";
			{
				for(Point p : this.getPoints()){
					data += "\t\t<point>\n";
					{
						data += "\t\t\t<pX>" + p.x + "</pX>\n";
						data += "\t\t\t\t<pY>" + p.y + "</pY>\n";
					}
					data += "\t\t</point>\n";
				}
			}
			data += "\t</boundingBox>\n";
				
			
		}
		data += "</GameObject>\n";
		
		return data;
	}
	
}
