package eu.boiled.chainreaction.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class ModelBackground {
	public static final String LOG = "ModelBackground";
	private Bitmap background;
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;	// buffer holding the texture coordinates
	private int[] textures = new int[1];
	private float texture[] = {    		
			// Mapping coordinates for the vertices
			0.0f, 1.0f,		// bottom left	(V2)
			1.0f, 1.0f,		// top left		(V1)
			0.0f, 0.0f,		// bottom right	(V4)
			1.0f, 0.0f		// top right	(V3)
	};
	private float vertices[] = {
			0.0f, 0.0f,
			0.0f, 0.0f,
			0.0f, 0.0f,
			0.0f, 0.0f,
	};
	
	public ModelBackground(Bitmap bitmap, int width, int height){
		this.background = bitmap;
		vertices[2] = vertices[6] = (float)width;
		vertices[1] = vertices[3] = (float)height;
		
		double y1 = width * bitmap.getHeight() / bitmap.getWidth();
		if(y1 < height){
			texture[5] = texture[7] = (float) (y1 / height) - 1.0f;
		} else if(y1 == height){
			
		} else{
//			double  x1 = bitmap.getWidth() * height / bitmap.getHeight();
//			texture[4] = texture[6] = (float) (x1 / width);
		}
		
		
		
		

		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuffer.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuffer.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}
	
	public void draw(GL10 gl) {
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		// bind the previously generated texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		// Point to our buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		// Set the face rotation
//		gl.glFrontFace(GL10.GL_CW);
		
		// Point to our vertex buffer
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		// Draw the vertices as triangle strip
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 2);

		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
	
	public void loadTexture(GL10 gl){
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		// create nearest filtered texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
	    gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
		// Use Android GLUtils to specify a two-dimensional texture image from our bitmap 
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, background, 0);
	}
}
