package org.projectcrawwl.objects;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.Inventory;
import org.projectcrawwl.weapons.BaseWeapon;

public abstract class BasePlayer extends GameObject{
	
	public int health;
	Inventory inventory = new Inventory(this);
	//public double speed = 0; //0 units per millisecond or 0 units per second
	public int kills = 0;
	BasePlayer lastHit = this;
	
	public int level = 0;
	
	public int ID;
	
	public BasePlayer(float tempX, float tempY, float tempA, int tempH){
		super();
		x = tempX;
		y = tempY;
		facingAngle = tempA;
		health = tempH;
		
		ID = GameData.getNewID();
	}
	public BasePlayer(float tempX, float tempY){
		super(tempX, tempY);
		x = tempX;
		y = tempY;
		facingAngle = 0;
		health = 100;
		
		ID = GameData.getNewID();
	}
	public BasePlayer(){
		super();
		x = 0;
		y = 0;
		facingAngle = 0;
		health = 100;
		
		ID = GameData.getNewID();
	}
	
	public void createBoundingBox(){
		
		boundingBox.reset();
		
        for (float angle=0; angle<=Math.PI*2; angle+=((Math.PI*2)/16) )
        {
        	addPoint((int)((25)*(float)Math.cos(angle)), (int)((25)*(float)Math.sin(angle)));
        }
        updateLines();
	}
	
	public void damage(double damage, BasePlayer shooter){
		health -= damage;
		lastHit = shooter;
	}
	public float getHealth(){
		return health;
	}
	
	public int getLevel(){
		return level;
	}
	
	public void render(){
		if(!isReady){return;}
		super.render();
		inventory.render();
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LESS);
		
		GL11.glColor4d(1.0, 0.0, 0,.5);
		GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(renderX, renderY, .5);
		GL11.glVertex3d(renderX + 75 * Math.cos(Math.toRadians(facingAngle)), renderY + 75 * Math.sin(Math.toRadians(facingAngle)), .5);
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
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
			if(this instanceof org.projectcrawwl.objects.Zombie){
				GameData.addZombie();
			}
			GameData.killPlayer(this);
		}
	}
	
	
	public void newKill(){
		kills += 1;
	}
	
	public Inventory getInventory(){
		return inventory;
	}
	
	public void mouseInput(ArrayList<Integer> a){}
	
	public void keyboardInput(ArrayList<Integer> a){}
	
	@Override
	public String toXML(){
		String data = "";
		
		data += "<BasePlayer>\n";
		{
				data += "\t<x>" + x + "</x>\n";
				data += "\t<y>" + y + "</y>\n";
				data += "\t<id>" + ID + "</id>\n";
				
				data += "\t<facingAngle>" + facingAngle + "</facingAngle>\n";
				
				data += "\t<moveAngle>" + moveAngle + "</moveAngle>\n";
				
				data += "\t<speed>" + speed + "</speed>\n";
				
				data += "\t<turnSpeed>" + turnSpeed + "</turnSpeed>\n";
				
				data += "\t<health>" + health + "</health>\n";
				
				data += "\t<level>" + level + "</level>\n";
				
				data += "\t<kills>" + kills + "</kills>\n";
				
				data += "\t<boundingBox>\n";
				{
					for(Point p : this.getPoints()){
						data += "\t\t<point>\n";
						{
							data += "\t\t\t<pX>" + p.x + "</pX>\n";
							data += "\t\t\t<pY>" + p.y + "</pY>\n";
						}
						data += "\t\t</point>\n";
					}
				}
				data += "\t</boundingBox>\n";
			
			data += "\t<Inventory>\n";
			{
				for(BaseWeapon w : inventory.getWeapons()){
					data += w.toXML();
				}
			}
			data += "\t</Inventory>\n";
			
		}
		data += "</BasePlayer>\n";
		
		return data;
	}
}
