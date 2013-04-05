package eu.boiled.chainreaction.async;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;
import eu.boiled.chainreaction.helpers.UserData;

public class DataRequestFactory {
	public static final String LOG = "DataRequestFactory";
	
	public static HttpPost getRank(){
		String url = "http://www.boiled.eu:8080/galacticaweb/web/getRank";
		HttpPost post = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		credinalsDecorator(post, nameValuePairs);
		return post;
	}
	
	public static HttpPost sendScore(){
		String url = "http://boiled.eu:8080/galacticaweb/web/sendScore";
		HttpPost post = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		credinalsDecorator(post, nameValuePairs);
		return post;
	}
	
	
	
	private static void credinalsDecorator(HttpPost post, List<NameValuePair> nameValuePairs){
		nameValuePairs.add(new BasicNameValuePair("uuid", UserData.getInstance().getUuid()));
		nameValuePairs.add(new BasicNameValuePair("points", String.valueOf(UserData.getInstance().getUserPoints())));
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			Log.e(LOG, "unsupported");
			e.printStackTrace();
		}
	}
}
