package eu.boiled.chainreaction.surfaceview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyGlSurfaceView extends GLSurfaceView{
	
	private GLRenderer renderer;

	public MyGlSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyGlSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
        case MotionEvent.ACTION_DOWN:
        	renderer.setTouchEvent(x, y);
        }
        return true;
    }

	@Override
	public void setRenderer(Renderer renderer) {
		this.renderer = (GLRenderer) renderer;
		super.setRenderer(renderer);
	}
}
