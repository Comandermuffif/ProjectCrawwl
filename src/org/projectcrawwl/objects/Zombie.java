package org.projectcrawwl.objects;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.World;
import org.projectcrawwl.weapons.BaseWeapon;


public class Zombie extends BasePlayer {
	Random random = new Random();
	BasePlayer target;
	
	private int move = 0;
	
	public Zombie(int tempX, int tempY){
		super(tempX,tempY, 0 , 100);
		moveAngle = (float) (Math.random()*360);
		speed = .03;
		
		createBoundingBox();
		
	}
	
	//Draw everything here
	public void render(){
		if(!isReady){return;}
		super.render();
		float ratio = ((float) (GameSettings.getScreenY())/GameSettings.getScreenX());
		//Re scale view so text is right side up
		GL11.glLoadIdentity();
		GL11.glOrtho(-GameData.zoom, GameSettings.getScreenX() + GameData.zoom, GameSettings.getScreenY() + GameData.zoom*(ratio),-GameData.zoom*(ratio), -1, 1);
		
		
		//data.getFont().drawString((renderX - 25), settings.getScreenY() - (renderY + 40), "Zombie", Color.red);
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		
		
		GL11.glLoadIdentity();
		GL11.glOrtho(-GameData.zoom, GameSettings.getScreenX()  + GameData.zoom, -GameData.zoom*(ratio), GameSettings.getScreenY() + GameData.zoom*(ratio), -1, 1);
		
	}

	
	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		
		//updateViewCone();
		if(target == null){
			move -= delta;
			if(move <= 0){
				move = 500;
				moveAngle += random.nextGaussian()*(5);
			}
		}
		tempFacing = moveAngle;
		
		if(speed == 0){
			
			target = null;
			
			moveAngle += 1;
			speed = .03;
		}
		
		if(target == null){
			double dist = -1;
			for(BasePlayer friendly : GameData.getFriendlies()){
				Line2D.Float sight = new Line2D.Float(x, y, friendly.x, friendly.y);
				//Time to get crazy
				
				Line2D.Double sightL = new Line2D.Double(
						x + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 - Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*farthest,
						y + Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*farthest,
						x + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 - Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*farthest + (friendly.x - x),
						y + Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*farthest + (friendly.y - y)
						);
				Line2D.Double sightR = new Line2D.Double(
						x + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 - Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*-farthest,
						y + Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*-farthest,
						x + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 - Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*-farthest + (friendly.x - x),
						y + Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*-farthest + (friendly.y - y)
						);
				
				
				boolean flag = true;
				
				for(ConvexHull hull : World.getHulls()){
					for(Line2D.Float line : hull.getLines()){
						if(line.intersectsLine(sightL)){
							flag = false;
							break;
						}
						if(line.intersectsLine(sightR)){
							flag = false;
							break;
						}
					}
					if(!flag){break;}
				}
				if(flag){
					double tempD = sight.getP1().distance(sight.getP2());
					if(dist == -1){
						target = friendly;
						dist = tempD;
					}else if(tempD < dist){
						target = friendly;
						dist = tempD;
					}
				}
			}
			
			speedMult = 1;
			
		}else{
			
			speedMult = 2;
			
			moveAngle = (float) (Math.toDegrees(Math.atan2(target.getY() - y, target.getX() - x)));
			
			if(target.health <= 0){
				target = null;
				return;
			}
			
			if(new Point((int)getX(),(int)getY()).distance(target.getX(), target.getY()) <= getFarthest() + target.getFarthest() + 30){
				if(target.health <= 0){
					target = null;
				}
				inventory.getWeapon().fire();
			}
		}
	}
	
	@Override
	public String toXML(){
		String data = "";
		
		data += "<Zombie>\n";
		{
			data += "\t<x>" + x + "</x>\n";
			data += "\t<y>" + y + "</y>\n";
			
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
		data += "</Zombie>\n";
		
		return data;
	}
	
}