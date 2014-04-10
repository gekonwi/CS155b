package cs155.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cs155.opengl.R;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;

/**
 * This is a modification of the "Lesson 07: Texture Mapping" NeHe tutorial for
 * the Google Android Platform orginally ported to Android by Savas Ziplies
 * (nea/INsanityDesign)
 * 
 * This file contains the View and Controller components of the game. The Model
 * is in the GameModel07 class
 * 
 * @author Tim Hickey
 */
public class PA07 extends GLSurfaceView implements Renderer {

	private final long startMillis = System.currentTimeMillis();

	/** Cube instance */
	private Cube cube;
	private Plane floor;

	private float width, height;
	private float eyeX, eyeY, eyeZ, centerX, centerY, centerZ;


	private int filter = 0; // Which texture filter? ( NEW )

	/** Is light enabled ( NEW ) */
	private boolean light = true;

	/*
	 * The initial light values for ambient and diffuse as well as the light
	 * position ( NEW )
	 */
	private float[] lightAmbient = { 0.5f, 0.5f, 0.5f, 1.0f };
	private float[] lightDiffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float[] lightPosition = { 50.0f, 25.0f, 50.0f, 1.0f };

	/* The buffers for our light values ( NEW ) */
	private FloatBuffer lightAmbientBuffer;
	private FloatBuffer lightDiffuseBuffer;
	private FloatBuffer lightPositionBuffer;

	/*
	 * These variables store the previous X and Y values as well as a fix touch
	 * scale factor. These are necessary for the rotation transformation added
	 * to this lesson, based on the screen touches. ( NEW )
	 */
	private float oldX;
	private float oldY;

	private GameModel07 game;
	/** The Activity Context */
	private Context context;
	private TrianglePrism triPrism;

	/**
	 * Instance the Cube object and set the Activity Context handed over.
	 * Initiate the light buffers and set this class as renderer for this now
	 * GLSurfaceView. Request Focus and set if focusable in touch mode to
	 * receive the Input from Screen and Buttons
	 * 
	 * @param context
	 *            - The Activity Context
	 */
	public PA07(Context context) {
		super(context);
		game = new GameModel07(10);

		// Set this as Renderer
		this.setRenderer(this);

		// Request focus, otherwise buttons won't react
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
		// And there'll be light!

		// Setup The Ambient Light (NEW)
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbientBuffer);

		// Setup The Diffuse Light (NEW)
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuseBuffer);

		// Position The Light (NEW)
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPositionBuffer);

		gl.glEnable(GL10.GL_LIGHT0); // Enable Light 0 ( NEW )

		// Settings
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glDisable(GL10.GL_DITHER); // Disable dithering ( NEW )
		gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping
		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1f); // White Background
		gl.glClearDepthf(1.0f); // Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do

		// Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		// Load the texture for the cube once during Surface creation
		cube.loadGLTexture(gl, this.context, R.drawable.crate);
		floor.loadGLTexture(gl, this.context, R.drawable.tiles);
	}

	/**
	 * If the surface changes, reset the viewport
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) {
			height = 1;
		} // Prevent A Divide By Zero By
			// Making Height Equal One

		this.width = width; // store width/height for use by other view methods
		this.height = height;

		gl.glViewport(0, 0, width, height); // Reset The Current Viewport

		gl.glLoadIdentity(); // Reset The Modelview Matrix
	}

	/**
	 * Here we do our drawing
	 */
	public void onDrawFrame(GL10 gl) {

		// Clear Screen And Depth Buffer
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		setViewAtAvatar(gl);;

		drawFloor(gl);

		drawFoes(gl);

		drawAvatar(gl);

		game.update();

	}

	private void setViewFromAvatar(GL10 gl) {
		eyeX = game.avatar.pos[0];
		eyeY = game.avatar.pos[1];
		eyeZ = game.avatar.pos[2];

		centerX = eyeX + game.avatar.vel[0];
		centerY = eyeY + game.avatar.vel[1];
		centerZ = eyeZ + game.avatar.vel[2];
		
		updateCam(gl);
	}

	private void setViewFromLeft(GL10 gl) {
		eyeX = -20f;
		eyeY = 40f;
		eyeZ = game.width / 2;

		centerX = game.width / 2f;
		centerY = 0f;
		centerZ = game.height / 2f;
		
		updateCam(gl);
	}

	private void setMyView(GL10 gl) {
		eyeX = game.width / 2f;
		// eyeY is changed by touch events;
		eyeZ = game.height / 2f + 5;

		centerX = game.width / 2f;
		centerY = 2f;
		centerZ = game.height / 2f;
		
		updateCam(gl);
	}

	private void setViewAtAvatar(GL10 gl) {
		eyeX = game.avatar.pos[0];
		eyeY = 10f;
		eyeZ = game.avatar.pos[2] + 10;

		centerX = game.avatar.pos[0];
		centerY = 2f;
		centerZ = game.avatar.pos[2];
		
		updateCam(gl);
	}

	private void updateCam(GL10 gl) {
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select The Projection Matrix
		gl.glLoadIdentity(); // Reset The Projection Matrix

		// Set the properties of the camera
		GLU.gluPerspective(gl, 60.0f, width / height, 0.1f, 1000.0f);

		// Point and aim the camera
		GLU.gluLookAt(gl, eyeX, eyeY, eyeZ, // eye position above Left of game
											// board
				centerX, centerY, centerZ, // target position at center of board
				0f, 1f, 0f); // up direction

		gl.glMatrixMode(GL10.GL_MODELVIEW); // Select The Modelview Matrix
	}

	
	
	/*
	 * View Methods which use information in the model to draw the objects on
	 * the screen
	 */
	private void drawFoes(GL10 gl) {
		for (Foe f : game.foes) {
			drawFoe(gl, f);
		}
	}

	private void drawFoe(GL10 gl, Foe f) {
		// === draw body ===================
		gl.glPushMatrix();
		gl.glTranslatef(f.pos[0], 0f, f.pos[2]);
		// gl.glScalef(1f,2f,1f);
		gl.glRotatef(calcHeading(f), 0f, 1f, 0f);
		// gl.glRotatef(foeAngle, 0f, 1f, 0f);
		cube.draw(gl, filter);

		float armWidth = 0.1f;
		gl.glTranslatef(0f, 1f - armWidth, 0f);

		// === draw left arm ===================
		gl.glPushMatrix();
		drawArmParts(gl, f, armWidth);
		gl.glPopMatrix();

		// === draw right arm ===================
		gl.glTranslatef(1f, 0f, armWidth);
		gl.glRotatef(180f, 0f, 1f, 0f);
		drawArmParts(gl, f, armWidth);

		// System.out.println("drawing foe:"+f.pos[0]+" "+f.pos[2]);
		gl.glPopMatrix();

	}

	private void drawArmParts(GL10 gl, Foe foe, float armWidth) {
		float armLength = 0.1f;
		float armParts = 10;
		float maxPartAngle = 120f / armParts;
		float armSwingMillis = 2000f / foe.speed;

		long passedMillis = System.currentTimeMillis() - startMillis;
		double cyclePi = Math.PI * passedMillis / armSwingMillis;
		float partAngle = maxPartAngle * (float) Math.sin(cyclePi);

		gl.glRotatef(90f, 0f, 0f, 1f);
		// gl.glRotatef(90f + partAngle, 0f, 0f, 1f);

		for (int i = 0; i < armParts; i++) {
			gl.glPushMatrix();
			gl.glScalef(armWidth, armLength, armWidth);
			cube.draw(gl, filter);
			gl.glPopMatrix();
			gl.glTranslatef(0f, armLength, 0f);
			gl.glRotatef(partAngle, 1f, 0f, 0f);
		}
	}

	private float calcHeading(Foe f) {
		float x = f.vel[0];
		float z = f.vel[2];

		double heading = Math.atan2(-z, x) / Math.PI * 180f;
		return (float) heading + f.getStartAngle();
	}

	private void drawAvatar(GL10 gl) {		
		gl.glPushMatrix();
		gl.glTranslatef(game.avatar.pos[0], 0f, game.avatar.pos[2]);
		gl.glScalef(1f, 1.5f, 1f);
		gl.glRotatef(calcHeading(game.avatar), 0f, 1f, 0f);
		triPrism.draw(gl, filter);
		gl.glPopMatrix();
	}

	private void drawFloor(GL10 gl) {
		gl.glPushMatrix();
		gl.glScalef(game.width, 1f, game.height);
		floor.draw(gl, filter);
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

		// If a touch is moved on the screen
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// Calculate the change
			float dx = x - oldX;
			float dy = y - oldY;
			// Define an upper area of 10% on the screen

			if (Math.abs(dx) > Math.abs(dy))
				// right-left movement
				game.avatar.rotateBy(dx / 100f);
			else
				// up-down movement
				game.avatar.speed += -dy * 0.1;

			// A press on the screen
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			// Define an upper area of 10% to define a lower area
			int upperArea = this.getHeight() / 10;
			int lowerArea = this.getHeight() - upperArea;

			// Change the light setting if the lower area has been pressed
			if (y > lowerArea) {
				if (light) {
					light = false;
				} else {
					light = true;
				}
			}
		}

		// Remember the values
		oldX = x;
		oldY = y;

		// We handled the event
		return true;
	}
}
