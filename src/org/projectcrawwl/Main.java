package org.projectcrawwl;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.objects.GameObject;

public class Main {

	//Make an enum of all states
	public static int MAIN_STATE = 0;
	
	static GameSettings settings = GameSettings.getInstance();
	static GameData data = GameData.getInstance();
	
	long lastFrame;
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;
	

	public void start() {
        try {
        	
		    Display.setDisplayMode(new DisplayMode(settings.getScreenX(),settings.getScreenY()));
		    Display.create();
		    Display.setTitle("Project Crawwl");
        } catch (LWJGLException e) {
		    e.printStackTrace();
		    System.exit(0);
        }
        
        // init OpenGL
    	GL11.glMatrixMode(GL11.GL_PROJECTION);
    	GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        //GL11.glMatrixMode(GL11.GL_TEXTURE);
        //GL11.glLoadIdentity();
    	
        GL11.glDisable(GL11.GL_CULL_FACE);
        
    	GL11.glOrtho(0, settings.getScreenX(), 0, settings.getScreenY(), 1, -1);
    	GL11.glMatrixMode(GL11.GL_MODELVIEW);
     
    	
    	GL11.glEnable(GL11.GL_BLEND);
    	//GL11.glBlendFunc(GL11.GL_DST_ALPHA, GL11.GL_ONE);
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        
    	
    	System.out.println("Entering Main State");
		
		data.setMapXOffset((settings.getScreenX() - data.getMapX())/2);
		data.setMapYOffset((settings.getScreenY() - data.getMapY())/2);

		data.addPlayer();
    	getDelta();
    	lastFPS = getTime();
    	//Render render = new Render();
    	System.out.println("Update thread started");
    	Update update = new Update();
    	
    	//render.start(); //Render must be main thread
    	//update.start();
    	
    	
    	
        while (!Display.isCloseRequested()) {
        	//Display.sync(60);
        	GL11.glDepthMask(true);
    		GL11.glClearDepth(1f);
    		GL11.glClearColor(0.0f,0.0f,0.0f,0.0f);
    		
    	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT |
    	    			 GL11.GL_DEPTH_BUFFER_BIT |
    	    			 GL11.GL_STENCIL_BUFFER_BIT);	
        	
        	
        	update();
        	render();
        	updateFPS();
    	    Display.update();
    	    
    	    if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
    	    	break;
    	    }
    	    
    	}
     
        
        //render.cancel();
        update.cancel();
    	Display.destroy();
        System.exit(0);
        
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
	
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps + " UPS: " + data.getUPS());
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	public void update(){
		int delta = getDelta();
		
		data.update(delta);
		
		for(BasePlayer a : data.getAllPlayers()){
			a.update(delta);
		}
		for(GameObject b : data.getProjectiles()){
			b.update(delta);
		}
	}
	
	public void render(){
		data.render();
	}
	
	public static void main(String[] argv) {
		settings.setScreenX(1280);
		settings.setScreenY(720);
		
        Main main = new Main();
        main.start();
    }
}