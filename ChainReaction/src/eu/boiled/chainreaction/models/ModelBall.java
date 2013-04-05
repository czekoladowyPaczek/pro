package eu.boiled.chainreaction.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class ModelBall {

	public enum TYPE{
		CLICKED(1.0f, 1.0f, 1.0f, 1.0f, 0, 0, 1, 40, 50, 40),
		RED(0.8f, 0.2f, 0.0f, 0.5f, -2, -2, 5, 16, 50, 50),
		GREEN(0.0f, 0.8f, 0.2f, 0.5f, -4, -4, 8, 9, 60, 30),
		PINK(1.0f, 0.1f, 0.5f, 0.5f, -5, -5, 10, 5, 50, 80);
		
		float r;
		float g;
		float b;
		float alhpa;
		int vx;
		int vy;
		int maxSpeed;
		int rr;
		int timeToDestroy;
		int maxR;
		
		private TYPE(float r, float g, float b, float alpha, int vx, int vy, int maxSpeed, int rr, int timeToDestroy, int maxR){
			this.r = r;
			this.g = g;
			this.b = b;
			this.alhpa = alpha;
			this.vx = vx;
			this.vy = vy;
			this.maxSpeed = maxSpeed;
			this.rr = rr;
			this.timeToDestroy = timeToDestroy;
			this.maxR = maxR;
		}

		public float getR() {
			return r;
		}

		public float getG() {
			return g;
		}

		public float getB() {
			return b;
		}

		public float getAlhpa() {
			return alhpa;
		}

		public int getVx() {
			return vx;
		}

		public int getVy() {
			return vy;
		}

		public int getMaxSpeed() {
			return maxSpeed;
		}

		public int getRr() {
			return rr;
		}
		
		public int getTimeToDestroy(){
			return timeToDestroy;
		}
		
		public int getMaxR(){
			return maxR;
		}
		
	}
	
	protected int r;
	protected int x;
	protected int y;
	protected int speedX;
	protected int speedY;
	protected TYPE type;
	
	private float [] verts;
	private FloatBuffer vertexBuffer;
	
	
	public ModelBall(int x, int y, TYPE type){
		Random random = new Random();
		this.x = x;
		this.y = y;
		this.r = type.getRr();
		this.type = type;
		this.speedX = type.getVx() + random.nextInt(type.getMaxSpeed());
		this.speedY = type.getVy() + random.nextInt(type.getMaxSpeed());
		verts = new float[200];
		
		createVertexBuffer();

	}
	
	protected void createVertexBuffer(){
		int c = 0;
		for(int i = 0; i < 100; i++){
			float theta = 2.0f * 3.1415926f * (float)i / 100f;//get the current angle 
			float xx = (float) ((float)r * (float)Math.cos(theta));
			float yy = (float) ((float)r * (float)Math.sin(theta));
			
			verts[c] = (float)x + xx;
			verts[c + 1] = (float)y + yy;
			c += 2;
		}
		ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(verts.length * 4);
		vertexByteBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = vertexByteBuffer.asFloatBuffer();
		vertexBuffer.put(verts);
		vertexBuffer.position(0);
	}
	
	
	public void draw(GL10 gl){
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glColor4f(type.getR(), type.getG(), type.getB(), type.getAlhpa());
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, verts.length / 2);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

	}
	
	
	public void move(){
		int c = 0;
		for(int i = 0; i < 100; i++){
			vertexBuffer.put(c, vertexBuffer.get(c) + speedX);
			vertexBuffer.put(c + 1, vertexBuffer.get(c + 1) + speedY);
			c += 2;
		}
		x += speedX;
		y += speedY;
		if(y > 480 || y < 0){
			speedY *= -1;
		}
		if(x > 800 || x < 0){
			speedX *= -1;
		}
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getR(){
		return type.getRr();
	}
	
	public TYPE getType(){
		return type;
	}
}
