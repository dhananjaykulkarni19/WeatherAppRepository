package com.demowheather;

import com.activeandroid.ActiveAndroid;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import android.app.Application;

public class ApplicationController extends Application {
	
	public static final String TAG = "VolleyTag";
	public static final String API_KEY = "7253c05e938f06aca10a8a26b2027685";
	private RequestQueue  mRequestQueue;
	private static ApplicationController mRequestInstance;
	
	@Override
	public void onCreate() {
	
		super.onCreate();
		ActiveAndroid.initialize(this);
		mRequestInstance = this;
		
	}
	
	@Override
	public void onTerminate() {
		
		super.onTerminate();
		ActiveAndroid.dispose();
	}
	
	public static synchronized ApplicationController getInstance(){
		return mRequestInstance;
	}
	
	public RequestQueue getRequestQueue(){
		
		if (mRequestQueue == null){
			mRequestInstance.mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
			return mRequestInstance.mRequestQueue;
	}
	public <T> void addToRequestQueue(Request<T> req){
		
		req.setTag(TAG);
		getRequestQueue().add(req);
	}
	
	public void cancelPendingRequests(Object tag){
		
		if(mRequestQueue!=null){
			mRequestQueue.cancelAll(tag);
		}
	}	
}

