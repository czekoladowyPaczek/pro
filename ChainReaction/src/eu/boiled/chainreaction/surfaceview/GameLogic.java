package eu.boiled.chainreaction.surfaceview;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import eu.boiled.chainreaction.models.ModelBackground;
import eu.boiled.chainreaction.models.ModelBall;
import eu.boiled.chainreaction.models.ModelBall.TYPE;
import eu.boiled.chainreaction.models.ModelBallStatic;

public class GameLogic {
	public static final String LOG = "GameLogic";

	private Handler mHandler;
	private Random random;
	private List<ModelBall> balls;
	private List<ModelBallStatic> staticBalls;
	
	private int width;
	private int height;
	private int ballsCount;
	private ModelBackground background;
	
	private int points;
	
	private boolean touched = false;
	
	public GameLogic(int width, int height){
		this.width = width;
		this.height = height;
		random = new Random();
		balls = new ArrayList<ModelBall>();
		staticBalls = new ArrayList<ModelBallStatic>();
	}
	
	public void render(GL10 gl){
		gl.glEnable(GL10.GL_TEXTURE_2D); 
		background.draw(gl);
		gl.glDisable(GL10.GL_TEXTURE_2D); 
		for(ModelBall ball : balls){
			ball.draw(gl);
		}
		
		for(ModelBall ball: staticBalls){
			ball.draw(gl);
		}
	}
	
	public void update(){
		ModelBall normalBall;
		ModelBallStatic staticBall;
		ListIterator<ModelBall> nBall;
		ListIterator<ModelBallStatic> sBall;
		if(touched){
			sBall = staticBalls.listIterator();
			nBall = balls.listIterator();
			while (nBall.hasNext()) {
			    normalBall = nBall.next();
				sBall = staticBalls.listIterator();
			    while(sBall.hasNext()){
			    	staticBall = sBall.next();
			    	if(staticBall.checkHit(normalBall.getX(), normalBall.getY(), normalBall.getR())){
			    		points += 10;
			    		sBall.add(new ModelBallStatic(normalBall.getX(), normalBall.getY(), normalBall.getType()));
			    		nBall.remove();
			    		break;
			    	}
			    }
			    
			}
			
			if(staticBalls.size() == 0){
				Message msg = new Message();
				Log.e(LOG, String.valueOf(balls.size()));
				Log.e(LOG, String.valueOf(ballsCount));
				if(balls.size() < ballsCount / 2){
					msg.what = 2;
					msg.arg1 = points;
				} else{
					msg.what = 1;
				}
				mHandler.sendMessage(msg);
			}
		}
		
		
		
		for(ModelBall ball : balls){
			ball.move();
		}
		sBall = staticBalls.listIterator();
		while(sBall.hasNext()){
			staticBall = sBall.next();
			staticBall.move();
			if(staticBall.isReadyToRemove()){
				sBall.remove();
			}
		}
	}
	
	private void reCreateBalls(int count){
		points = 0;
		touched = false;
		balls.clear();
		staticBalls.clear();
		for(int i = 0; i < ballsCount / 3; i++){
			balls.add(new ModelBall(random.nextInt(width), random.nextInt(height), TYPE.RED));
		}
		for(int i = 0; i < ballsCount / 3; i++){
			balls.add(new ModelBall(random.nextInt(width), random.nextInt(height), TYPE.GREEN));
		}
		for(int i = balls.size(); i < ballsCount; i++){
			balls.add(new ModelBall(random.nextInt(width), random.nextInt(height), TYPE.PINK));
		}
	}
	
	public void setTouchEvent(int x, int y){
		if(!touched){
			staticBalls.add(new ModelBallStatic(x, y, TYPE.CLICKED));
			touched = true;
		}
	}

	public void setHandler(Handler gameHandler) {
		mHandler = gameHandler;
		
	}
	
	public void setBackground(Bitmap back){
		background = new ModelBackground(back, width, height);
		
	}
	
	public void loadTextures(GL10 gl){
		background.loadTexture(gl);
	}

	public void setLevel(int i) {
		ballsCount = i * 8;
		reCreateBalls(ballsCount);
	}
}
