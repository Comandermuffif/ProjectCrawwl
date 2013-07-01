package org.projectcrawwl.weapons;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.projectile.Bullet;


public class BaseRangedWeapon extends BaseWeapon{
	
	/**
	 * Bullet velocity, will figure out what the value equivocates to later
	 */
	float velocity;
	
	/**
	 * Current clip and max clip
	 */
	int currentClip;
	int maxClip;
	
	/**
	 * Time to reload
	 */
	int reloadTime;
	
	/**
	 * The current reload time
	 */
	int currentReload;
	
	/**
	 * Number of bullets in each shot
	 */
	int pellets = 1;
	
	/**
	 * Boolean whether the weapon is reloading
	 */
	boolean reloading = false;
	
	/**
	 * The sound to play when the weapon fires
	 */
	Audio onFire = null;
	
	/**
	 * The rate at which spread increases, in milliseconds
	 */
	double spread;
	
	/**
	 * The current spread of the weapon, in milliseconds
	 */
	double currentSpread = 0;
	
	/**
	 * The spread increase as degrees per second
	 */
	double spreadAngle = 1.5;
	
	/**
	 * The angle of the cone for multiple pellets
	 */
	double cone = 0;
	
	/**
	 * The max spread in degrees, half of the spread cone
	 */
	int maxSpread = 45;
	
	/**
	 * The min spread in degrees, half of the cone
	 */
	int minSpread = 1;
	
	/**
	 * Creates a weapon with default parameters
	 * @param tempO - The owner of the weapon
	 */
	protected BaseRangedWeapon(BasePlayer tempO){
		super("BaseRangedWeapon",0);
		owner = tempO;
		damage = 0;
		velocity = 1;
		coolDown = 250;
		currentCoolDown = coolDown;
		spread = 0;
		
		maxClip = 30;
		
		currentClip = 0;
		
		reloadTime = 1000;
		currentReload = reloadTime;
	}
	
	/**
	 * Where information is drawn on the screen
	 */
	public void render(){
		super.render();
		//BEEP!
		
		GL11.glColor4d(0.0, 0.0, 0.0, 0.1);
		
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(owner.renderX + Math.cos(Math.toRadians(owner.facingAngle + (currentSpread/1000)*spreadAngle + cone/2 + minSpread))*owner.getFarthest(), owner.renderY + Math.sin(Math.toRadians(owner.facingAngle + (currentSpread/1000)*spreadAngle + cone/2 + minSpread))*owner.getFarthest());
		GL11.glVertex2d(owner.renderX + Math.cos(Math.toRadians(owner.facingAngle + (currentSpread/1000)*spreadAngle + cone/2 + minSpread))*(owner.getFarthest() + 200), owner.renderY + Math.sin(Math.toRadians(owner.facingAngle + (currentSpread/1000)*spreadAngle + cone/2 + minSpread))*(owner.getFarthest() + 200));
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(owner.renderX + Math.cos(Math.toRadians(owner.facingAngle - (currentSpread/1000)*spreadAngle - cone/2 - minSpread))*owner.getFarthest(), owner.renderY + Math.sin(Math.toRadians(owner.facingAngle - (currentSpread/1000)*spreadAngle - cone/2 - minSpread))*owner.getFarthest());
		GL11.glVertex2d(owner.renderX + Math.cos(Math.toRadians(owner.facingAngle - (currentSpread/1000)*spreadAngle - cone/2 - minSpread))*(owner.getFarthest() + 200), owner.renderY + Math.sin(Math.toRadians(owner.facingAngle - (currentSpread/1000)*spreadAngle - cone/2 - minSpread))*(owner.getFarthest() + 200));
		GL11.glEnd();
		
	}
	
	public BaseRangedWeapon(BasePlayer o, String filename){
		super("BaseRangedWeapon",0);
		owner = o;
		try {
			
			if(!filename.split("\\.")[1].equalsIgnoreCase("RangedWeapon")){
				System.out.println("Error loading weapon: " + filename);
				return;
			}
			
			BufferedReader wordStream = new BufferedReader(new FileReader(filename));
			
			String l;
			
			while((l = wordStream.readLine()) != null){
				
				String[] s = l.split("=");
				
				for(int i = 0; i < s.length; i ++){
					s[i] = s[i].trim();
				}
				
				////////////////////////////////////////
				if(s[0].equalsIgnoreCase("name")){
					name = s[1].replaceAll("^\"|\"$", "");;
				}
				if(s[0].equalsIgnoreCase("velocity")){
					velocity = Float.parseFloat(s[1]);
				}
				if(s[0].equalsIgnoreCase("spread")){
					spread = Double.parseDouble(s[1]);
				}
				if(s[0].equalsIgnoreCase("spreadangle")){
					spreadAngle = Double.parseDouble(s[1]);
				}
				if(s[0].equalsIgnoreCase("damage")){
					damage = Double.parseDouble(s[1]);
				}
				if(s[0].equalsIgnoreCase("cooldown")){
					coolDown = Float.parseFloat(s[1]);
				}
				if(s[0].equalsIgnoreCase("reloadtime")){
					reloadTime = Integer.parseInt(s[1]);
				}
				if(s[0].equalsIgnoreCase("pellets")){
					pellets = Integer.parseInt(s[1]);
				}
				if(s[0].equalsIgnoreCase("cone")){
					cone = Integer.parseInt(s[1]);
				}
				if(s[0].equalsIgnoreCase("maxclip")){
					maxClip = Integer.parseInt(s[1]);
				}
				if(s[0].equals("maxSpread")){
					maxSpread = Integer.parseInt(s[1]);
				}
				if(s[0].equalsIgnoreCase("onfire")){
					
					try {
						onFire = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(s[1].replaceAll("^\"|\"$", "")));
					} catch (IOException e) {e.printStackTrace();}
				}
				
				if(s[0].equalsIgnoreCase("automatic")){
					automatic = Boolean.parseBoolean(s[1]);
				}
				
				
			}
			
			wordStream.close();
			
		}catch(IOException e){e.printStackTrace();}
		
		currentCoolDown = coolDown;
		
		currentClip = maxClip;
		
		currentReload = reloadTime;
		
		reloading = false;
	}
	
	/**
	 * Returns a point with (currentClip, maxClip)
	 */
	public Point getClip(){
		return new Point(currentClip,maxClip);
	}
	
	/**
	 * Updates data for this file, with the delta for the time between frames
	 */
	public void update(int delta){
		
		if(reloading){
			currentReload -= delta;
		}
		
		if(currentReload <= 0){
			reloading = false;
			
			if(owner.getInventory().bullets >= maxClip){
				owner.getInventory().bullets -= (maxClip - currentClip);
				
				currentReload = reloadTime;
				currentClip = maxClip;
				currentSpread = 0;
				
			}else{
				
				currentReload = reloadTime;
				currentClip = owner.getInventory().bullets;
				owner.getInventory().bullets = 0;
				currentSpread = 0;
			}
		}
		
		if(active){
			currentCoolDown -= delta;
		}
		if(currentCoolDown <= 0){
			active = false;
			currentCoolDown = coolDown;
		}
		
		if(currentSpread > 0){
			currentSpread -= delta;
		}
		
		if(currentSpread <= 0){
			currentSpread = 0;
		}
		
	}
	
	/**
	 * Returns a boolean if the weapon is reloading
	 */
	public boolean isReloading(){
		return reloading;
	}
	
	/**
	 * Sets reloading to true
	 */
	public void reload(){
		reloading = true;
	}
	
	/**
	 * Called when a weapon is fired
	 */
	public void fire(){
		if(active == false && reloading == false){
			if(currentClip == 0){
				//reloading = true;
				return;
			}
			
			if(onFire != null){
				onFire.playAsSoundEffect(1.0f, 1.0f, false);
			}
			
			active = true;
			
			currentCoolDown = coolDown;
			
			Random random = new Random();
			double tempG = random.nextGaussian();
			
			for(int x = 0; x < pellets; x++){
				
				if(tempG < -1){tempG = -1;}
				if(tempG > 1){tempG = 1;}
				
				double coneSpread = random.nextGaussian();
				if(coneSpread < -1){coneSpread = -1;}
				if(coneSpread > 1){coneSpread = 1;}
				
				if(currentSpread == 0){
					GameData.addProjectile(new Bullet((float) (owner.x + Math.cos(Math.toRadians(owner.facingAngle))*(owner.getFarthest())),(float) (owner.y + Math.sin(Math.toRadians(owner.facingAngle))*(owner.getFarthest())),(float) (velocity + random.nextGaussian()*.02),(float) ((float) owner.facingAngle + (coneSpread)*cone), damage, owner));
				}else{
					GameData.addProjectile(new Bullet((float) (owner.x + Math.cos(Math.toRadians(owner.facingAngle))*(owner.getFarthest())),(float) (owner.y + Math.sin(Math.toRadians(owner.facingAngle))*(owner.getFarthest())),(float) (velocity + random.nextGaussian()*.02),(float) ((float) owner.facingAngle + tempG*((currentSpread/1000)*spreadAngle + minSpread) + (coneSpread)*cone), damage, owner));
				}
				
			}
			if(currentSpread/1000*spreadAngle < maxSpread){
				currentSpread += spread;
			}
			
			currentClip -= 1;
			if(currentClip == 0){
				//reloading = true;
			}
		}
	}
	
	public String toString(){
		return "[ " + owner  + ", " + name + ", " + velocity + ", " + maxClip + ", " + reloadTime + ", " + pellets + ", " + spread + ", " + cone + ", " + damage + "]";
	}
	
	@Override
	public String toXML() {
		String data = "";
		
		data += "\t\t<BaseRangedWeapon>\n";
		{
			data += "\t\t\t<name>" + name + "</name>\n";
			data += "\t\t\t<damage>" + damage + "</damage>\n";
			data += "\t\t\t<active>" + active + "</active>\n";
			data += "\t\t\t<coolDown>" + coolDown + "</coolDown>\n";
			data += "\t\t\t<currentCoolDown>" + currentCoolDown + "</currentCoolDown>\n";
			data += "\t\t\t<automatic>" + automatic + "</automatic>\n";
			
			data += "\t\t\t<velocity>" + velocity + "</velocity>\n";
			data += "\t\t\t<currentClip>" + currentClip + "</currentClip>\n";
			data += "\t\t\t<maxClip>" + maxClip + "</maxClip>\n";
			data += "\t\t\t<reloadTime>" + reloadTime + "</reloadTime>\n";
			data += "\t\t\t<currentReload>" + currentReload + "</currentReload>\n";
			data += "\t\t\t<pellets>" + pellets + "</pellets>\n";
			data += "\t\t\t<reloading>" + reloading + "</reloading>\n";
			data += "\t\t\t<onFire>" + onFire.getBufferID() + "</onFire>\n";
			data += "\t\t\t<spread>" + spread + "</spread>\n";
			data += "\t\t\t<currentSpread>" + currentSpread + "</currentSpread>\n";
			data += "\t\t\t<spreadAngle>" + spreadAngle + "</spreadAngle>\n";
			data += "\t\t\t<cone>" + cone + "</cone>\n";
			data += "\t\t\t<maxSpread>" + maxSpread + "</maxSpread>\n";
			data += "\t\t\t<minSpread>" + minSpread + "</minSpread>\n";
		}
		data += "\t\t</BaseRangedWeapon>\n";
		
		return data;
	}
}
