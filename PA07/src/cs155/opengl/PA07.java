package cs155.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cs155.opengl.R;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;

/**
 * This is a modification of the "Lesson 07: Texture Mapping"
 * NeHe tutorial for the Google Android Platform orginally
 * ported to Android by Savas Ziplies (nea/INsanityDesign)
 * 
 * This file contains the View and Controller components 
 * of the game. The Model is in the GameModel07 class
 * 
 * @author Tim Hickey
 */
public class PA07 extends GLSurfaceView implements Renderer {
	
	/** Cube instance */
	private Cube cube;	
	private Plane floor;
	
	private float width, height;
	

	
	private int filter = 0;				//Which texture filter? ( NEW )
	
	/** Is light enabled ( NEW ) */
	private boolean light = false;

	/* 
	 * The initial light values for ambient and diffuse
	 * as well as the light position ( NEW ) 
	 */
	private float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
	private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
	private float[] lightPosition = {50.0f, 25.0f, 50.0f, 1.0f};
		
	/* The buffers for our light values ( NEW ) */
	private FloatBuffer lightAmbientBuffer;
	private FloatBuffer lightDiffuseBuffer;
	private FloatBuffer lightPositionBuffer;
	
	/*
	 * These variables store the previous X and Y
	 * values as well as a fix touch scale factor.
	 * These are necessary for the rotation transformation
	 * added to this lesson, based on the screen touches. ( NEW )
	 */
	private float oldX;
    private float oldY;

	private GameModel07 game;
	/** The Activity Context */
	private Context context;
	private TrianglePrism triPrism;
	
	/**
	 * Instance the Cube object and set the Activity Context 
	 * handed over. Initiate the light buffers and set this 
	 * class as renderer for this now GLSurfaceView.
	 * Request Focus and set if focusable in touch mode to
	 * receive the Input from Screen and Buttons  
	 * 
	 * @param context - The Activity Context
	 */
	public PA07(Context context) {
		super(context);
		game = new GameModel07(10);
		
		//Set this as Renderer
		this.setRenderer(this);
		
		//Request focus, otherwise buttons won't react
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		
		//
		this.context = context;		
		
		//
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(lightAmbient.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightAmbientBuffer = byteBuf.asFloatBuffer();
		lightAmbientBuffer.put(lightAmbient);
		lightAmbientBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(lightDiffuse.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightDiffuseBuffer = byteBuf.asFloatBuffer();
		lightDiffuseBuffer.put(lightDiffuse);
		lightDiffuseBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(lightPosition.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightPositionBuffer = byteBuf.asFloatBuffer();
		lightPositionBuffer.put(lightPosition);
		lightPositionBuffer.position(0);
		
		triPrism = new TrianglePrism();
		cube = new Cube();
		floor = new Plane();
	}

	/**
	 * The Surface is created/init()
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {		
		//And there'll be light!
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbientBuffer);		//Setup The Ambient Light ( NEW )
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuseBuffer);		//Setup The Diffuse Light ( NEW )
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPositionBuffer);	//Position The Light ( NEW )
		gl.glEnable(GL10.GL_LIGHT0);											//Enable Light 0 ( NEW )


		//Settings
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glDisable(GL10.GL_DITHER);				//Disable dithering ( NEW )
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1f); 	//White Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
				
		//Load the texture for the cube once during Surface creation
		cube.loadGLTexture(gl, this.context,R.drawable.crate);
		floor.loadGLTexture(gl, this.context, R.drawable.tiles);
	}
	
	/**
	 * If the surface changes, reset the viewport 
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { height = 1;} 		//Prevent A Divide By Zero By
		            						//Making Height Equal One
		
		this.width = width;    // store width/height for use by other view methods
		this.height = height;

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
	}


	/**
	 * Here we do our drawing
	 */
	public void onDrawFrame(GL10 gl) {

		//Clear Screen And Depth Buffer
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();					

		
		//setViewFromAvatar(gl);
		setMyView(gl);
		
		drawFloor(gl);
		
//		drawFoes(gl);
		
		drawAvatar(gl);
		
		game.update();
		
	}	
	
	
	private void setViewFromAvatar(GL10 gl){
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		// Set the properties of the camera 
		GLU.gluPerspective(gl, 60.0f, width / height, 0.1f, 1000.0f);

		// Point and aim the camera
		float x = game.avatar.pos[0];
		float y = game.avatar.pos[1];
		float z = game.avatar.pos[2];

		float vx = game.avatar.vel[0];
		float vy = game.avatar.vel[1];
		float vz = game.avatar.vel[2];
		
		GLU.gluLookAt(gl, 
				      x,   y+2f,    z,      // eye position
				      x+vx,y+vy+2f, z+vz,   // target position
				      0f,  1f,      0f);    // up direction

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		
	}
	

	private void setViewFromLeft(GL10 gl){
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		// Set the properties of the camera 
		GLU.gluPerspective(gl, 60.0f, width / height, 0.1f, 1000.0f);

		// Point and aim the camera
		GLU.gluLookAt(gl, 
				     -20f, 40f, game.width/2,              // eye position above Left of game board
				      game.width/2f, 0f, game.height/2f,   // target position at center of board
				      0f, 1f, 0f);                         // up direction

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
	}

	
	private void setMyView(GL10 gl){
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		// Set the properties of the camera 
		GLU.gluPerspective(gl, 60.0f, width / height, 0.1f, 1000.0f);

		float eyeX, eyeY, eyeZ, centerX, centerY, centerZ;
		eyeX = game.width / 2f;
		eyeY = 5f;
		eyeZ = 40f;

		
		centerX = game.avatar.pos[0];
		centerY = game.avatar.pos[1];
		centerZ = game.avatar.pos[2];

		
		// Point and aim the camera
		GLU.gluLookAt(gl, 
				     eyeX, eyeY, eyeZ,         		// eye position above Left of game board
				      centerX, centerY, centerZ,	// target position at center of board
				      0f, 1f, 0f);              	// up direction

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
	}

	
/*
 * View Methods which use information in the model to draw the objects on the screen
 */
	private void drawFoes(GL10 gl){
		for(Foe f:game.foes){
			drawFoe(gl,f);
		}	
	}
	

	
	private void drawFoe(GL10 gl, Foe f){
		gl.glPushMatrix();
		gl.glTranslatef(f.pos[0],0f,f.pos[2]);
		//gl.glScalef(1f,2f,1f);
		gl.glRotatef((float)calcHeading(f),0f,1f,0f);
		cube.draw(gl, filter);
//		System.out.println("drawing foe:"+f.pos[0]+" "+f.pos[2]);
		gl.glPopMatrix();
	}
	
	private double calcHeading(Foe f){
		float x = f.pos[0];
		float z = f.pos[2];
		double heading = Math.atan2(z,x)/Math.PI*180;
		return heading;
	}
	
	float angle = 0;
	private void drawAvatar(GL10 gl){
		
		gl.glPushMatrix();
		gl.glTranslatef(game.avatar.pos[0],0f,game.avatar.pos[2]);
		gl.glScalef(1f,1.5f,1f);
//		gl.glRotatef((float)calcHeading(game.avatar),0f,1f,0f);
		angle++;
		gl.glRotatef(angle,0f,1f,0f);
		triPrism.draw(gl, filter);
//		System.out.println("drawing avatar:"+game.avatar.pos[0]+" "+game.avatar.pos[2]);
		gl.glPopMatrix();
	}
	
	

	private void drawFloor(GL10 gl){
		gl.glPushMatrix();
		gl.glScalef(game.width,1f,game.height);
		floor.draw(gl,filter);
		gl.glPopMatrix();
	}


	
	
	
	
	
	
	
/* ***** Listener Events ( NEW ) ***** */		
	/**
	 * Override the touch screen listener.
	 * 
	 * React to moves and presses on the touchscreen.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//
		float x = event.getX();
        float y = event.getY();
        
        //If a touch is moved on the screen
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
        	//Calculate the change
        	float dx = x - oldX;
	        float dy = y - oldY;
        	//Define an upper area of 10% on the screen
        	int upperArea = this.getHeight() / 10;
        	
    
        
        //A press on the screen
        } else if(event.getAction() == MotionEvent.ACTION_UP) {
        	//Define an upper area of 10% to define a lower area
        	int upperArea = this.getHeight() / 10;
        	int lowerArea = this.getHeight() - upperArea;
        	
        	//Change the light setting if the lower area has been pressed 
        	if(y > lowerArea) {
        		if(light) {
        			light = false;
        		} else {
        			light = true;
        		}
        	}
        }
        
        //Remember the values
        oldX = x;
        oldY = y;
        
        //We handled the event
		return true;
	}
}
