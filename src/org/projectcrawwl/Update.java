package org.projectcrawwl;

import org.lwjgl.Sys;
import org.projectcrawwl.data.GameData;
public class Update extends Thread{
	
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
			
			GameData.update(delta);
		}
	}
	
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
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
