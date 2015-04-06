package aplicacao;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

/**
 * Created by leandro.castro on 20/05/2014.
 */
public abstract class BaseApplication extends Application{
	public enum TrackerName {
		APP_TRACKER
	}

	private HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

	public synchronized Tracker getTracker(TrackerName trackerId){
		Tracker tracker = mTrackers.get(trackerId);
		if(tracker == null){
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			if(trackerId == TrackerName.APP_TRACKER){
				tracker = analytics.newTracker(getPropertyId());
			}
		}

		return tracker;
	}

	protected abstract String getPropertyId();
}
