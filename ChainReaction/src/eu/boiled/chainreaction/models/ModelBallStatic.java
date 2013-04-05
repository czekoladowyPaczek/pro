package eu.boiled.chainreaction.models;

public class ModelBallStatic extends ModelBall{

	private int currentTime = 0;
	public ModelBallStatic(int x, int y, TYPE type) {
		super(x, y, type);
		this.speedX = 0;
		this.speedY = 0;
	}
	
	public void move(){
		if(r < type.getMaxR()){
			r++;
			createVertexBuffer();
		} else{
			currentTime++;
		}
	}
	
	
	public boolean checkHit(int x, int y, int r){
		if(Math.pow((double) this.x - x, 2.0) + Math.pow((double) this.y - y, 2.0) < Math.pow((double) this.r + r, 2)){
			return true;
		}
		return false;
	}
	
	public boolean isReadyToRemove(){
		if(currentTime > type.getTimeToDestroy()){
			return true;
		}
		return false;
	}
}
