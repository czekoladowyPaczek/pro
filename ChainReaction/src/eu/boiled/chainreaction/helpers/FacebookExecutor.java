package eu.boiled.chainreaction.helpers;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

import eu.boiled.chainreaction.R;

public class FacebookExecutor {
	public static final String LOG = "FacebookExecutor";
	private Activity parent;
	
	public FacebookExecutor(Activity activity){
		parent = activity;
	}
	
	
	public void execute(){
		Session session = Session.getActiveSession();
		if(session == null){
			session = new Session(parent.getApplicationContext());
			OpenRequest openReq = new OpenRequest(parent);
			List<String> permissions = new ArrayList<String>();
			permissions.add("publish_stream");
			openReq.setPermissions(permissions);
			session = new Session(parent);
			session.addCallback(new Session.StatusCallback(){
				@Override
				public void call(Session session, SessionState state, Exception exception) {
					if(session.isOpened()){
						sendInvitations(session);
					}
					if(session.isClosed()){
						Log.e(LOG, "session close");
					}
				}
				
			});
			Session.setActiveSession(session);
			session.openForPublish(openReq);
		} else if(session != null && !session.getPermissions().contains("publish_stream") && session.isOpened()){
			List<String> permissions = new ArrayList<String>();
			permissions.add("publish_stream");
			session.requestNewPublishPermissions(new Session.NewPermissionsRequest(parent, permissions));
		} else if(session != null && session.isClosed()){
			OpenRequest openReq = new OpenRequest(parent);
			List<String> permissions = new ArrayList<String>();
			permissions.add("publish_stream");
			openReq.setPermissions(permissions);
			session.openForPublish(openReq);
		} else{
			sendInvitations(session);
		}
		
	}
	
	
	private void sendInvitations(Session session){
		Bundle params = new Bundle();
		params.putString("message", parent.getString(R.string.fb_invite_message));
		WebDialog webDialog = new WebDialog.RequestsDialogBuilder(parent, session, params).setOnCompleteListener(new OnCompleteListener() {
			
			@Override
			public void onComplete(Bundle values, FacebookException error) {
				if(error != null){
					if(error.getMessage() != null){
						Log.e(LOG, error.getMessage());
					}
				} else{
					if(values.getString("request") != null){
						Toast.makeText(parent, parent.getString(R.string.fb_invite_success), Toast.LENGTH_SHORT).show();
					}
				}
			}
		}).build();
		webDialog.show();
	}
}
