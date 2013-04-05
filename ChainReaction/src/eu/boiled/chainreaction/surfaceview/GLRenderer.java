package eu.boiled.chainreaction.surfaceview;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
public class GLRenderer implements Renderer{
	public static final String LOG = "GLRenderer";
	
	private GameLogic logic;
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		logic.render(gl);
		
		logic.update();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity(); 
		gl.glOrthof(0, width, height, 0, -1, 1);
		gl.glMatrixMode(GL10.GL_MODELVIEW);  
		gl.glLoadIdentity(); 
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glDisable(GL10.GL_DEPTH_TEST);
		logic.loadTextures(gl);
		gl.glEnable(GL10.GL_BLEND);
	    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
//		gl.glShadeModel(GL10.GL_SMOOTH);
//		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
	}

	public void setGameLogic(GameLogic logic){
		this.logic = logic;
	}
	
	
	public void setTouchEvent(float x, float y){
		Log.d(LOG, "touch x " + String.valueOf(x));
		Log.d(LOG, "touch y " + String.valueOf(y));
		logic.setTouchEvent((int)x, (int)y);
	}
	
}
