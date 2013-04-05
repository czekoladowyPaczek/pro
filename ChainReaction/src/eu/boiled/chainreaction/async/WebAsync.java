package eu.boiled.chainreaction.async;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class WebAsync extends AsyncTask<HttpPost, Void, JSONObject>{
	public static final String LOG = "WebAsync";

	@Override
	protected JSONObject doInBackground(HttpPost... arg0) {
		if(arg0[0] == null){
			return null;
		}
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		InputStream content;
		StringBuilder builder = new StringBuilder();
		try {
			response = httpclient.execute(arg0[0]);
			content = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			String line;
	        while ((line = reader.readLine()) != null) {
	        	builder.append(line);
	        }
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject json = null;
		try {
			json = new JSONObject(builder.toString());
		} catch (JSONException e) {
			Log.e(LOG, "JSONException");
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
	
}
