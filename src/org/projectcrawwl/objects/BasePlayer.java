package org.projectcrawwl.objects;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import org.lwjgl.opengl.GL11;
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
	ArrayList<Point> view = new ArrayList<Point>();
	Dictionary<Point, Boolean> view2 = new Hashtable<Point, Boolean>();
	Boolean state = false;
	
	Polygon viewCone = new Polygon();
	
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
		
		//GL11.glColor4d(0, 1, 0, .039);

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

	

	public void renderOld(){
		super.render();
		inventory.render();
		
		GL11.glColor4d(0, 1, 0, .039);
		for(Point p : view){
			
			GL11.glBegin(GL11.GL_TRIANGLE_FAN);{
				GL11.glVertex2d(p.x*data.getGridX() + data.getMapXOffset(), p.y*data.getGridY() + data.getMapYOffset());
				GL11.glVertex2d(p.x*data.getGridX() + data.getMapXOffset() + data.getGridX(),p.y*data.getGridY() + data.getMapYOffset());
				GL11.glVertex2d(p.x*data.getGridX() + data.getMapXOffset() + data.getGridX(),p.y*data.getGridY() + data.getMapYOffset() + data.getGridY());
				GL11.glVertex2d(p.x*data.getGridX() + data.getMapXOffset(), p.y*data.getGridY() + data.getMapYOffset() + data.getGridY());
				GL11.glEnd();
			}
		}
		GL11.glColor4d(0, 1, 0, .039);
		
		
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		for(PathIterator pi = viewCone.getPathIterator(null); !pi.isDone(); pi.next()){
			float[] coord = new float[6];
			pi.currentSegment(coord);
			if(pi.currentSegment(coord) != pi.SEG_CLOSE){
				GL11.glVertex2d(coord[0] + data.getMapXOffset(), coord[1] + data.getMapYOffset());
			}
			
			pi.next();
		}
		GL11.glEnd();
		
	    
		//g.setColor(new Color(0,255,0,10));
		//g.fillArc(renderX - sightRange, renderY - sightRange, 2*sightRange, 2*sightRange,90 -  facingAngle - sightAngle/2,90-  facingAngle + sightAngle/2);
	}

	
	public void renderHUD(){
		
	}
	
	//Do all calculations here
	public void updateOld(int delta){
		super.update(delta);
		if(state == false){
			state = true;
		}else{
			state = false;
		}
		view.clear();
		viewCone.reset();
		viewCone.addPoint((int)x, (int)y);
		for(float a = facingAngle - sightAngle/2; a < facingAngle + sightAngle/2; a += sightAngle/(Math.toRadians(sightAngle)*sightRange)){
			for(int b = 0; b < sightRange; b += data.getGridX()){
				Point temp = new Point((int)((x + Math.sin(Math.toRadians(a))*b)/data.getGridX()), (int) ((y + Math.cos(Math.toRadians(a))*b)/data.getGridY()));
				if(view2.get(temp) == state){
					if(data.getGrid().get(temp.x).get(temp.y) < 0){
						break;
					}
					continue;
				}else{
					view2.put(temp, state);
				}
				
				if(temp.x >= data.getMapX()/data.getGridX() || temp.y >= data.getMapY()/data.getGridY() || temp.x < 0 || temp.y < 0){
					break;
				}

				if(data.getGrid().get(temp.x).get(temp.y) < 0){
					view.add(temp);
					viewCone.addPoint((int)(temp.x*data.getGridX()), (int) (temp.y*data.getGridY()));
					if((int)((sightRange-b)/sightRange*255*(1 - 2*Math.abs(a - facingAngle)/sightAngle)) > data.getLight(temp.x,temp.y)){
						data.setLight(temp.x, temp.y, (int)((sightRange-b)/sightRange*255*(1 - 2*Math.abs(a - facingAngle)/sightAngle)));
					}
					break;
				}
				
				view.add(temp);
				if((int)((sightRange-b)/sightRange*255*(1 - 2*Math.abs(a - facingAngle)/sightAngle)) > data.getLight(temp.x,temp.y)){
					data.setLight(temp.x, temp.y, (int)((sightRange-b)/sightRange*255*(1 - 2*Math.abs(a - facingAngle)/sightAngle)));
				}
				
				if(b + data.getGridX() >= sightRange){
					viewCone.addPoint((int)(temp.x*data.getGridX()), (int) (temp.y*data.getGridY()));
				}
			}
		}
		
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
	
	public void update(int delta){
		super.update(delta);
		

		Polygon tempView = new Polygon();
		
		tempView.addPoint((int)x, (int)y);
		World world = World.getInstance();
		ArrayList<Line2D.Float> walls = world.getLineWalls((int) x,(int) y);//world.getWalls();
		
		
		
		for(float a = facingAngle - sightAngle/2; a < facingAngle + sightAngle/2; a += 1){
			boolean flag = false;
			for(int b = 0; b < sightRange; b += data.getGridX()){
				Line2D.Float temp = new Line2D.Float(x, y, (float)(x + Math.sin(Math.toRadians(a))*b), (float) (y + Math.cos(Math.toRadians(a))*b));
				for(Line2D.Float x : world.getLineWalls((int)temp.x2, (int)temp.y2)){
					if(x.intersectsLine(temp)){
						tempView.addPoint((int) temp.x2,(int) temp.y2);
						flag = true;
						break;
					}
				}
				if(flag){
					break;
				}
			}
			if(!flag){
				tempView.addPoint((int) (x + Math.sin(Math.toRadians(a))*sightRange),(int) (y + Math.cos(Math.toRadians(a))*sightRange));
			}			
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
