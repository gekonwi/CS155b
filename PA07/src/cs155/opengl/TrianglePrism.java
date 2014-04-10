package cs155.opengl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import cs155.opengl.R;
//import cs155.opengl.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * This class is an object representation of 
 * a triangle prism containing the vertex information,
 * texture coordinates, the vertex indices
 * and drawing functionality, which is called 
 * by the renderer. Originally created by 
 * Savas Ziplies (nea/INsanityDesign)
 *  
 * @author Tim Hickey
 * @author Georg Konwisser
 * 
 */
public class TrianglePrism {

	/** The buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	/** The buffer holding the texture coordinates */
	private FloatBuffer textureBuffer;
	/** The buffer holding the indices */
	private ByteBuffer indexBuffer;
	/** The buffer holding the normals */
	private FloatBuffer normalBuffer;

	/** Our texture pointer */
	private int[] textures = new int[3];

	private float[] vertices = vertices();	
	private float[] vertices() {
		// define each point in space only once
		float vertices[] = {
				0f, 0f, 0f,		// v0
				0f, 0f, -1f,	// v1
				1f, 0f, -1f,	// v2
				0f, 1f, 0f,		// v3
				0f, 1f, -1f,	// v4
				1f, 1f, -1f		// v5
		};
		
		// referring to the non redundant vertices inside this method
		// cause the repetition of verices only if they are on a different face
		// (will require a different normal)
		byte indices[] = {
				0, 1, 2,	// bottom
				3, 5, 4, 	// top
				0, 2, 5, 3,	// side 1
				0, 3, 4, 1,	// side 2
				1, 4, 5, 2	// side 3
		};
		
		return expandPoints(vertices, indices);
	}	
		
	private byte[] indices = {
			0, 1, 2,				// bottom
			3, 4, 5, 				// top
			6, 7, 8, 6,	8, 9,		// side 1
			10, 11, 12, 10,	12, 13,	// side 2
			14, 15, 16, 14, 16, 17	// side 3
			
	};
	
	
	private float[] normals = normals();
	private float[] normals() {
		float s1 = (float) (1f / Math.sqrt(2));
		
		float normals[] = {
			0f, -1f, 0f,	// bottom
			0f, 1f, 0f,		// top
			s1, 0f, s1,		// side 1
			-1f, 0f, 0f,	// side 2
			0f, 0f, -1f		// side 3
		};

		byte indices[] = {
			0, 0, 0,	// bottom
			1, 1, 1,	// top
			2, 2, 2, 2,	// side 1
			3, 3, 3, 3,	// side 2
			4, 4, 4, 4	// side 3
		};
		
		return expandPoints(normals, indices);
	}

	/** The initial texture coordinates (u, v) */	
	private float texture[] = {
//			0, 1, 2,	// bottom
//			3, 5, 4, 	// top
//			0, 2, 5, 3,	// side 1
//			0, 3, 4, 1,	// side 2
//			1, 4, 5, 2	// side 3
			
			//Mapping coordinates for the vertices
						1f, 0f,	// bottom
						1f, 1f, 
						0f, 1f, 
			
						0f, 0f,	// top
						1f, 1f, 
						0f, 1f,
			
						0f, 1f, // side 1
						1f, 1f, 
						1f, 0f, 
						0f, 0f,
			
						1f, 0f, // side 2
						1f, 1f, 
						0f, 1f, 
						0f, 0f,
			
						1f, 0f, // side 3
						1f, 1f, 
						0f, 1f, 
						0f, 0f
									};
	
	/**
	 * The TrianglePrism constructor.
	 * 
	 * Initiate the buffers.
	 */
	public TrianglePrism() {
		
		//
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		//
		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		//
		byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		normalBuffer = byteBuf.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);

		//
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	/**
	 * The object own drawing function.
	 * Called from the renderer to redraw this instance
	 * with possible changes in values.
	 * 
	 * @param gl - The GL Context
	 * @param filter - Which texture filter to be used
	 */
	public void draw(GL10 gl, int filter) {
		//Bind the texture according to the set texture filter
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[filter]);

		//Enable the vertex, texture and normal state
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

		//Set the face rotation
		gl.glFrontFace(GL10.GL_CCW);
		
		//Point to our buffers
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
		
		//Draw the vertices as triangles, based on the Index Buffer information
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

	/**
	 * Load the textures
	 * 
	 * @param gl - The GL Context
	 * @param context - The Activity context
	 */
	
	public void loadGLTexture(GL10 gl, Context context) {
		loadGLTexture(gl,context,R.drawable.crate);
		
	}
	
	public void loadGLTexture(GL10 gl, Context context, int imageid) {
		//Get the texture from the Android resource directory
		InputStream is = context.getResources().openRawResource(imageid);
		Bitmap bitmap = null;
		try {
			//BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			//Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}

		//Generate there texture pointer
		gl.glGenTextures(3, textures, 0);

		//Create Nearest Filtered Texture and bind it to texture 0
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		//Create Linear Filtered Texture and bind it to texture 1
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		//Create mipmapped textures and bind it to texture 2
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[2]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
		/*
		 * This is a change to the original tutorial, as buildMipMap does not exist anymore
		 * in the Android SDK.
		 * 
		 * We check if the GL context is version 1.1 and generate MipMaps by flag.
		 * Otherwise we call our own buildMipMap implementation
		 */
		if(gl instanceof GL11) {
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			
		//
		} else {
			buildMipmap(gl, bitmap);
		}		
		
		//Clean up
		bitmap.recycle();
	}
	
	/**
	 * Our own MipMap generation implementation.
	 * Scale the original bitmap down, always by factor two,
	 * and set it as new mipmap level.
	 * 
	 * Thanks to Mike Miller (with minor changes)!
	 * 
	 * @param gl - The GL Context
	 * @param bitmap - The bitmap to mipmap
	 */
	private void buildMipmap(GL10 gl, Bitmap bitmap) {
		//
		int level = 0;
		//
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();

		//
		while(height >= 1 || width >= 1) {
			//First of all, generate the texture from our bitmap and set it to the according level
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
			
			//
			if(height == 1 || width == 1) {
				break;
			}

			//Increase the mipmap level
			level++;

			//
			height /= 2;
			width /= 2;
			Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
			
			//Clean up
			bitmap.recycle();
			bitmap = bitmap2;
		}
	}
	
	// list a new point for each entry in indices
	// take the values from points according to the respective index
	// from indices (can be used to shorten the vertices and normals definitions)
	static float[] expandPoints(float[] points, byte[] indices) {
		float[] redundantPoints = new float[indices.length * 3];
		for (int i = 0; i < indices.length; i++) {
			redundantPoints[i * 3 + 0] = points[indices[i] * 3 + 0];
			redundantPoints[i * 3 + 1] = points[indices[i] * 3 + 1];
			redundantPoints[i * 3 + 2] = points[indices[i] * 3 + 2];
		}
		
		return redundantPoints;
	}

}
