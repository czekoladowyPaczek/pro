package eu.boiled.chainreaction.models;

import org.json.JSONException;
import org.json.JSONObject;

public class ModelRank {
	public static final String LOG = "ModelRank";
	
	private String id;
	private int points;
	private int position;
	
	public ModelRank(JSONObject obj) throws JSONException{
//		id = obj.getString("uuid");
		points = obj.getInt("points");
		if(obj.has("position")){
			position = obj.getInt("position");
		}
	}
	public ModelRank(int points){
		this.points = points;
	}
	public ModelRank(String id, int points){
		this.id = id;
		this.points = points;
	}
	
	
//	public String getId() {
//		return id;
//	}
	public int getPoints() {
		return points;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	
}
