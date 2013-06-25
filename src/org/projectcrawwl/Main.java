package org.projectcrawwl;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.StateController;
import org.projectcrawwl.data.World;

public class Main {

	//Make an enum of all states
	public static GameState IN_GAME = new InGameState();
	public static GameState MAIN_MENU = new MainMenuState();
	
	static long lastFrame;
	/** frames per second */
	static int fps;
	/** last fps time */
	static long lastFPS;
	

	public void start() {
		
		GameSettings.setScreenX(1280);
		GameSettings.setScreenY(720);
		
        try {
        	
		    Display.setDisplayMode(new DisplayMode(GameSettings.getScreenX(),GameSettings.getScreenY()));
		    Display.setFullscreen(true);
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
        
        GL11.glDisable(GL11.GL_CULL_FACE);
        
    	GL11.glOrtho(0, GameSettings.getScreenX(), 0, GameSettings.getScreenY(), 1, -1);
  
    	GL11.glEnable(GL11.GL_BLEND);
    	GL11.glEnable(GL11.GL_ALPHA);
    	GL11.glEnable(GL11.GL_ALPHA_TEST);
    	GL11.glEnable(GL11.GL_STENCIL_TEST);
    	GL11.glEnable(GL11.GL_DEPTH_TEST);
    	
    	GL11.glAlphaFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
    	GL11.glDepthFunc(GL11.GL_LESS);
    	
    	
    	lastFPS = getTime();
    	
    	getDelta();
    	
    	GameData.renderInit();
    	World.generateWorld();
    	
		StateController.addGameState(IN_GAME);
		StateController.addGameState(MAIN_MENU);
		
		StateController.setGameState(MAIN_MENU);
		
        while (!Display.isCloseRequested()) {
        	//Display.sync(60);
        	GL11.glDepthMask(true);
    		GL11.glClearDepth(1f);
    		GL11.glClearColor(0.0f,0.0f,0.0f,0.0f);
    		
    	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT |
    	    			 GL11.GL_DEPTH_BUFFER_BIT |
    	    			 GL11.GL_STENCIL_BUFFER_BIT);	
        	
    	    StateController.update(getDelta());
    	    
        	updateFPS();
    	    Display.update();
    	}
        
        AL.destroy();
    	Display.destroy();
        System.exit(0);
	}
	
	public static  long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public static int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
	    	
	    return delta;
	}
	
	public static void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	public static void main(String[] argv) {
        Main main = new Main();
        main.start();
    }
}