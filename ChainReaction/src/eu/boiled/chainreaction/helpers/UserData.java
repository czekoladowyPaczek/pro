package eu.boiled.chainreaction.helpers;

import java.util.UUID;

import android.content.Context;

public class UserData {
	private static final String uuidPrefsName = "eu.boiled.AuthData.prefs";
	private static final String uuidPrefs = "eu.boiled.AuthData.uuid";
	private static final String pointsPrefs = "eu.boiled.AuthData.points";
	private static Context mContext;
	private static UserData instance = null;
	private String uuid = null;
	
	private UserData(){}
	
	public static void initialize(Context context){
		mContext = context;
	}
	
	public static UserData getInstance(){
		if(instance == null){
			instance = new UserData();
		}
		return instance;
	}
	
	public int getUserPoints(){
		return mContext.getSharedPreferences(uuidPrefsName, Context.MODE_PRIVATE).getInt(pointsPrefs, 0);
	}
	
	public void saveUserPoints(int points){
		points += mContext.getSharedPreferences(uuidPrefsName, Context.MODE_PRIVATE).getInt(pointsPrefs, 0);
		mContext.getSharedPreferences(uuidPrefsName, Context.MODE_PRIVATE).edit().putInt(pointsPrefs, points).commit();
	}
	
	public String getUuid(){
		if(uuid == null){
			uuid = mContext.getSharedPreferences(uuidPrefsName, Context.MODE_PRIVATE).getString(uuidPrefs, null);
			if(uuid == null){
				uuid = generateUuid();
			}
		}
		return uuid;
	}
	
	private String generateUuid(){
		String uuid = UUID.randomUUID().toString();
		mContext.getSharedPreferences(uuidPrefsName, Context.MODE_PRIVATE).edit().putString(uuidPrefs, uuid).commit();
		return uuid;
	}
}
