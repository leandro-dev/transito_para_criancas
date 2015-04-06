package com.leandroideias.transito.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Leandro on 17/5/2014.
 */
public class MyApplication extends Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
	}

	public static Context getAppContext(){
		return context;
	}


	@Override
	public void onTerminate() {
		context = null;
		super.onTerminate();
	}
}
