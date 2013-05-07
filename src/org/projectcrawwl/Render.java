package org.projectcrawwl;

import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;

public class Render extends Thread{
	
	static GameSettings settings = GameSettings.getInstance();
	static GameData data = GameData.getInstance();
	
	public volatile boolean quit = false;
	
	public void run(){
		data.renderInit();
		while(!quit){
			data.render();
		}
	}
	
	public void cancel(){
		quit = true;
	}
	public boolean isCancelled(){
		return quit;
	}
	
}
