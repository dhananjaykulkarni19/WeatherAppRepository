package com.demowheather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demoweather.model.WeatherDetailsTable;
import com.example.demowheather.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	
	Button btnAddCity,btnGetWheatherData,btnNext;
	EditText etCityName;
	ArrayList<String> cityList;
	String mGetWheatherDataUrl;
	public static int NUMBER_OF_DAYS = 14;
	Dialog PleasWaitDialog;
	ConnectionDetector connectionDetector;
	Boolean mIsNetworkAvailable=false;
	Boolean mIsDataDownloaded = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        
        InitUI();
     
        cityList = new ArrayList<String>(); 
        
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		int dialogHeight = (int) (displayMetrics.heightPixels*0.20);
		int dialogWidth =  (int) (displayMetrics.widthPixels*0.90);
		
		PleasWaitDialog = new Dialog(MainActivity.this);
		PleasWaitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		PleasWaitDialog.setContentView(R.layout.please_wait_dialog);
		PleasWaitDialog.getWindow().setLayout(dialogWidth,dialogHeight);
		PleasWaitDialog.setCanceledOnTouchOutside(false);
		
		connectionDetector = new ConnectionDetector(getApplicationContext());
    }
    
   
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnaddcity:
			String cityName = etCityName.getText().toString();
			if(cityName.equalsIgnoreCase(""))
			{
				Toast.makeText(MainActivity.this, "Please enter city name...", Toast.LENGTH_SHORT).show();
			}
			else
			{
				cityList.add(cityName);
			}
			etCityName.setText("");
			break;

		case R.id.btngetwheatherdata:
			mIsNetworkAvailable = connectionDetector.GetNetworkInfo();
			if(!cityList.isEmpty())
			{
				if(mIsNetworkAvailable)
				{
					PleasWaitDialog.show();
					for(int i =0;i<cityList.size();i++)
					{
						prepareURL(cityList.get(i));
						downloadWheatherData(mGetWheatherDataUrl);
					}
				}
				else
				{
					Toast.makeText(MainActivity.this, "Network Not Available ... ", Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				Toast.makeText(MainActivity.this, "Please add at-least one city ...  ", Toast.LENGTH_SHORT).show();
			}
			
			
			break;
			
		case R.id.btnnext:
			
			if(!cityList.isEmpty())
			{
				if(mIsDataDownloaded)
				{
					Intent detailweather = new Intent(MainActivity.this,DetailWeatherView.class);
					detailweather.putStringArrayListExtra("citylist", cityList);
					startActivity(detailweather);
				}
				else
				{
					Toast.makeText(MainActivity.this, "Please download the weather data first ...  ", Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				Toast.makeText(MainActivity.this, "Please add at-least one city ...  ", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		default: 
			break;
		}
		
	}

	private void prepareURL(String cityName)
	{
		mGetWheatherDataUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?q="+cityName+"&cnt="+NUMBER_OF_DAYS+"&APPID="+ApplicationController.API_KEY;
	}
	private void downloadWheatherData(String url) 
	{
		JsonObjectRequest request = new JsonObjectRequest(url, null,new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response){
				
				SaveWheatherData(response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(MainActivity.this, ""+error, Toast.LENGTH_SHORT).show();
			}
		});
			
		ApplicationController.getInstance().addToRequestQueue(request);
	}
	
	private void SaveWheatherData(JSONObject response) 
	{		
			try 
			{	
				int cityId = response.getJSONObject("city").optInt("id");
				String cityName = response.getJSONObject("city").optString("name");
		
				JSONArray list = response.getJSONArray("list");
				for(int j =0; j <list.length(); j++)
				{
					WeatherDetailsTable wheatherDetailsTable = new WeatherDetailsTable();
					long timeStamp = list.getJSONObject(j).optLong("dt");
					Date date = new Date(timeStamp*1000L);
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
					dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-4"));
					wheatherDetailsTable._cityID = cityId; 
				    wheatherDetailsTable._cityName = cityName;
			 		wheatherDetailsTable._date = dateFormat.format(date);
					wheatherDetailsTable._dayTemp = KelvinToCelsius(list.getJSONObject(j).optJSONObject("temp").optLong("day"));
					wheatherDetailsTable._eveTemp = KelvinToCelsius(list.getJSONObject(j).optJSONObject("temp").optLong("eve"));
					wheatherDetailsTable._maxTemp = KelvinToCelsius(list.getJSONObject(j).optJSONObject("temp").optLong("max"));
					wheatherDetailsTable._minTemp = KelvinToCelsius(list.getJSONObject(j).optJSONObject("temp").optLong("min"));
					wheatherDetailsTable._nightTemp = KelvinToCelsius(list.getJSONObject(j).optJSONObject("temp").optLong("night"));
					wheatherDetailsTable._mornTemp = KelvinToCelsius(list.getJSONObject(j).optJSONObject("temp").optLong("morn"));
					wheatherDetailsTable._pressure = list.getJSONObject(j).optString("pressure");
					wheatherDetailsTable._humidity = list.getJSONObject(j).optString("humidity");
					
					JSONArray wheatherDetailsArray = list.getJSONObject(j).optJSONArray("weather");
					for(int k =0; k<wheatherDetailsArray.length(); k++)
					{
						wheatherDetailsTable._wheathertTitle = wheatherDetailsArray.getJSONObject(k).optString("main");
					    wheatherDetailsTable._wheatherDesc = wheatherDetailsArray.getJSONObject(k).optString("description");
					}
					
					wheatherDetailsTable._windSpeed = list.getJSONObject(j).optString("speed");
					wheatherDetailsTable._windDIrection = list.getJSONObject(j).optString("deg");
					wheatherDetailsTable.save();
					mIsDataDownloaded=true;
					PleasWaitDialog.hide();
				}
				
			} 
			catch (Exception e) 
			{	
				e.printStackTrace();
			}
		}
	
	private float KelvinToCelsius(double kelvinValue)
	{
		return (float) (kelvinValue - 273.15);
	}
	
	private void InitUI()
	{
	    btnAddCity = (Button) findViewById(R.id.btnaddcity);
        btnNext = (Button) findViewById(R.id.btnnext);
        btnGetWheatherData  = (Button) findViewById(R.id.btngetwheatherdata);
        etCityName = (EditText) findViewById(R.id.etcityname);
       
        btnAddCity.setOnClickListener(this);
        btnGetWheatherData.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        
        btnAddCity.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
        btnNext.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
        btnGetWheatherData.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
        etCityName.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
	}
}
