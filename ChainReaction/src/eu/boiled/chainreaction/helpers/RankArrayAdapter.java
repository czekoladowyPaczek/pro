package eu.boiled.chainreaction.helpers;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import eu.boiled.chainreaction.R;
import eu.boiled.chainreaction.models.ModelRank;

public class RankArrayAdapter extends ArrayAdapter<ModelRank> {
	private final Context context;
	private final List<ModelRank> values;
	private int userRankPosition;

	public RankArrayAdapter(Context context, List<ModelRank> results) {
		super(context, R.layout.apater_rank, results);
		this.context = context;
		this.values = results;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.apater_rank, parent, false);
	    TextView rankPlace = (TextView) rowView.findViewById(R.id.rankPlace);
	    TextView points = (TextView) rowView.findViewById(R.id.points);
	    if(userRankPosition - 1 == position){
	    	points.setTextColor(Color.CYAN);
	    	rankPlace.setTextColor(Color.CYAN);
	    }
	    rankPlace.setText(String.valueOf(position + 1));
	    points.setText(String.valueOf(values.get(position).getPoints()));

	    return rowView;
	}
	
	public void setUserPosition(int position){
		this.userRankPosition = position;
	}
} 