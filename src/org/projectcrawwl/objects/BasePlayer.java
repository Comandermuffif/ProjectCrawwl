package org.projectcrawwl.objects;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.ConvexHull;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.Inventory;
import org.projectcrawwl.data.World;

public class BasePlayer extends GameObject{
	
	public float health;
	Inventory inventory = new Inventory(this);
	//public double speed = 0; //0 units per millisecond or 0 units per second
	public int kills = 0;
	BasePlayer lastHit = this;
	float sightRange =  500;//data.getGridX()  * 10;
	float sightAngle = 90; //Total view cone
	
	Boolean state = false;
	private Polygon viewCone = new Polygon();
	
	public BasePlayer(float tempX, float tempY, float tempA, float tempH, float tempR){
		super();
		x = tempX;
		y = tempY;
		facingAngle = tempA;
		health = tempH;
		r = tempR;
	}
	public BasePlayer(float tempX, float tempY){
		super(tempX, tempY);
		x = tempX;
		y = tempY;
		facingAngle = 0;
		health = 100;
		r = 10;
	}
	public BasePlayer(){
		super();
		x = 0;
		y = 0;
		facingAngle = 0;
		health = 100;
		r = 10;
	}
	public void damage(float damage, BasePlayer shooter){
		health -= damage;
		lastHit = shooter;
	}
	public float getHealth(){
		return health;
	}


	public void render(){
		super.render();
		inventory.render();
		
		
		GL11.glColor4d(1.0, 0, 0,.5);
		GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(renderX, renderY);
		GL11.glVertex2d(renderX + sightRange * Math.sin(Math.toRadians(facingAngle)), renderY + sightRange * Math.cos(Math.toRadians(facingAngle)));
		GL11.glEnd();
		
		
		GL11.glColor4d(0, 1, 0, 1);
		//GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glBegin(GL11.GL_POLYGON);
		for(PathIterator pi = viewCone.getPathIterator(null); !pi.isDone(); pi.next()){
			float[] coord = new float[6];
			pi.currentSegment(coord);
			if(pi.currentSegment(coord) != pi.SEG_CLOSE){
				GL11.glVertex2d(coord[0] + data.getMapXOffset(), coord[1] + data.getMapYOffset());
			}
			pi.next();
		}
		GL11.glEnd();
		
		
		
		
	}
	
	public void renderHUD(){
		
	}
	
	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		
		
Polygon tempView = new Polygon();
		
		tempView.addPoint((int)x, (int)y);
		World world = World.getInstance();
		
		
		
		for(float a = facingAngle - sightAngle/2; a < facingAngle + sightAngle/2; a += 1){
			Line2D.Float line = new Line2D.Float(x, y,(float) (x + Math.sin(Math.toRadians(a))*sightRange), (float) (y + Math.cos(Math.toRadians(a))*sightRange));
			
			Point intersect = new Point((int)(x + Math.sin(Math.toRadians(a))*sightRange), (int)(y + Math.cos(Math.toRadians(a))*sightRange));
			
			double dist = intersect.distance(x,y);
			
			for(ConvexHull hull : world.getHulls()){
				for(Line2D.Float edge : hull.getLines()){
					if(line.intersectsLine(edge)){
						Point temp = world.getLineLineIntersection(edge, line);
						if(temp.distance(x, y) < dist){
							intersect = temp;
							dist = temp.distance(x, y);
						}
					}
				}
			}
			
			tempView.addPoint(intersect.x, intersect.y);
		}
		
		//viewCone.reset();
		viewCone = tempView;
		
		
		inventory.update(delta);
		//BOOP!
		
		if(health <= 0){
			lastHit.newKill();
			GameData data = GameData.getInstance();
			if(this instanceof org.projectcrawwl.objects.Zombie){
				data.addZombie();
			}
			data.removeObject(this);
		}
	}
	
	
	public void newKill(){
		kills += 1;
	}
}
