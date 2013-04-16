package org.projectcrawwl.objects;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

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

		GL11.glColor4d(0, 1, 0, .039);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		//GL11.glBegin(GL11.GL_POLYGON);
		GL11.glVertex2d(x + data.getMapXOffset(), y+data.getMapYOffset());
		for(ConvexHull hull : World.getInstance().getHulls()){
			for(Point p : hull.getPolygon()){
				GL11.glVertex2d(p.x + data.getMapXOffset(), p.y + data.getMapYOffset());
			}
			
		}
		GL11.glEnd();
		
	}
	
	public void renderHUD(){
		
	}
	
	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		
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
