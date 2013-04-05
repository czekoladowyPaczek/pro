package eu.boiled.chainreaction;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;

import eu.boiled.chainreaction.async.DataRequestFactory;
import eu.boiled.chainreaction.async.WebAsync;
import eu.boiled.chainreaction.async.WebHelper;
import eu.boiled.chainreaction.helpers.RankArrayAdapter;
import eu.boiled.chainreaction.models.ModelRank;

public class RankActivity extends Activity{
	public static final String LOG = "RankActivity";
	
	private ListView listView;
	private RankArrayAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank);
		
		listView = (ListView) findViewById(R.id.rankList);
		
		if(!WebHelper.isOnline(this)){
			finish();
		} else{
			new WebAsync(){
				@Override
				public void onPostExecute(JSONObject result) {
					List<ModelRank> results = new ArrayList<ModelRank>();
					JSONArray array;
					ModelRank userRank = null;
					try {
						userRank = new ModelRank(result.getJSONObject("userScore"));
						array = result.getJSONArray("top");
						for(int i = 0; i < array.length(); i++){
							results.add(new ModelRank(array.getInt(i)));
						}
						if(userRank != null && userRank.getPosition() > 100){
							results.add(userRank);
						}
						adapter = new RankArrayAdapter(RankActivity.this, results);
						listView.setAdapter(adapter);
						adapter.setUserPosition(userRank.getPosition());
					} catch (JSONException e) {
						Log.e(LOG, "JSONException " + e.getMessage());
						e.printStackTrace();
					}
	
					
					super.onPostExecute(result);
				}
			}.execute(DataRequestFactory.getRank());
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
}
