package org.projectcrawwl;

import org.lwjgl.Sys;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
public class Update extends Thread{
	
	static GameSettings settings = GameSettings.getInstance();
	static GameData data = GameData.getInstance();
	
	public volatile boolean quit = false;
	long lastFrame;
	int delta;
	int ups;
	/** last fps time */
	long lastUPS;
	public void run(){
		System.out.println("Update thread started");
		lastUPS = getTime();
		while(!quit){
			
			delta = getDelta();
			
			data.update(delta);
			
			updateUPS();
		}
	}
	
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	public void updateUPS() {
		if (getTime() - lastUPS > 1000) {
			data.setUPS(ups);
			ups = 0;
			lastUPS += 1000;
		}
		ups++;
	}
	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
	    	
	    return delta;
	}
	
	public void cancel(){
		quit = true;
	}
	public boolean isCancelled(){
		return quit;
	}
	
}
