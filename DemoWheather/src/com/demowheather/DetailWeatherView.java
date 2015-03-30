package com.demowheather;

import java.util.ArrayList;
import java.util.List;
import com.demoweather.model.WeatherDetailsTable;
import com.example.demowheather.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailWeatherView extends Activity implements OnClickListener{
	
	Dialog CityListDialog;
	ListView cityListView;
	ArrayList<String> cityNameList;
	ArrayAdapter<String> cityAdapter;
	List<WeatherDetailsTable> allRecordsList;
	WeatherDetailsTable detailsTableObject;
	ArrayList<String> dateList;
	ImageView btnNext,btnPrevious;
	TextView lblDayTemp,lblmaxTemp,lblMinTemp,lblNightTemp,lblMornTemp,lblEveTemp,lblPressure,lblHumidity,lblWeather,lblWindSpeed;;
	TextView tvSelectCity,txtDate,txtDayTemp,txtmaxTemp,txtMinTemp,txtNightTemp,txtMornTemp,txtEveTemp,txtPressure,txtHumidity,txtWeather,txtWindSpeed;;
	int mDateListSize; 
	int mPlusCounter  = 0;
	public static char DEGREE =  0x00B0;
	Boolean isListEnd = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_weather_view);
		 
		InitUI();
	}
	
	@Override
	protected void onStart() 
	{
		super.onStart();

		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
	 	int dialogHeight = (int) (displayMetrics.heightPixels*0.50);
		int dialogWidth =  (int) (displayMetrics.widthPixels*0.80);
		
		CityListDialog = new Dialog(DetailWeatherView.this);
		CityListDialog.setContentView(R.layout.city_list);
		CityListDialog.setTitle("Choose a city");
		CityListDialog.getWindow().setLayout(dialogWidth, dialogHeight);
		cityListView = (ListView) CityListDialog.findViewById(R.id.citylistview);
		
		cityNameList = new ArrayList<String>();
		dateList = new ArrayList<String>();
		allRecordsList = WeatherDetailsTable.getAllRecords();
		for(int i=0;i<allRecordsList.size(); i++)
		{
			cityNameList.add(allRecordsList.get(i)._cityName);
		}
	
		cityAdapter = new ArrayAdapter<String>(DetailWeatherView.this, android.R.layout.simple_list_item_single_choice, cityNameList);
		cityListView.setAdapter(cityAdapter);
		
		cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) 
			{		
				tvSelectCity.setText(""+cityNameList.get(position));
				allRecordsList = WeatherDetailsTable.getWeatherDetailsForGivenCity(""+cityNameList.get(position));
				for(int i=0;i<allRecordsList.size(); i++)
				{
					dateList.add(""+allRecordsList.get(i)._date);
					mDateListSize = dateList.size();
					txtDate.setText(""+dateList.get(0));
					CityListDialog.hide();
					detailsTableObject = WeatherDetailsTable.getWeatherDetailsByDate(dateList.get(0));
					SetData(detailsTableObject);
				} 
			}
		});
	}
	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.tvselectcity:
			
			CityListDialog.show(); 
			break;

		case R.id.btnNext:
		if(!isListEnd)
		{
			mPlusCounter++;
			if(mPlusCounter < mDateListSize)
			{
					txtDate.setText(""+dateList.get(mPlusCounter));
					detailsTableObject = WeatherDetailsTable.getWeatherDetailsByDate(dateList.get(mPlusCounter));
					SetData(detailsTableObject);
			}
			else
			{
				mPlusCounter--;
				isListEnd=true;
				Toast.makeText(DetailWeatherView.this, "No record", Toast.LENGTH_SHORT).show();
			}
		}
			break;
			
		case R.id.btnprevious: 
			isListEnd = false;
			if(mPlusCounter>0)
			{
			    --mPlusCounter;
				txtDate.setText(""+dateList.get(mPlusCounter));
				detailsTableObject = WeatherDetailsTable.getWeatherDetailsByDate(dateList.get(mPlusCounter));
				SetData(detailsTableObject);
			}
			else
			{
				Toast.makeText(DetailWeatherView.this, "No record", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		default:
			break;
		}
	}
	
	public void SetData(WeatherDetailsTable object)
	{
		txtDayTemp.setText(""+object._dayTemp + DEGREE+ " Celsius");
		txtMinTemp.setText(""+object._minTemp + DEGREE+ " Celsius");
		txtmaxTemp.setText(""+object._maxTemp + DEGREE+ " Celsius");
		txtEveTemp.setText(""+object._eveTemp + DEGREE+ " Celsius");
		txtMornTemp.setText(""+object._mornTemp + DEGREE+ " Celsius");
		txtNightTemp.setText(""+object._nightTemp + DEGREE+ " Celsius");
		txtPressure.setText(object._pressure+" hPa");
		txtHumidity.setText(object._humidity+" %");
		txtWeather.setText(object._wheatherDesc);
		txtWindSpeed.setText(object._windSpeed+" m/s");
	}
	
	public void InitUI()
	{
	    	tvSelectCity = (TextView) findViewById(R.id.tvselectcity);
			btnNext = (ImageView) findViewById(R.id.btnNext);
			btnPrevious = (ImageView) findViewById(R.id.btnprevious);
			txtDate = (TextView) findViewById(R.id.tvdate);
			txtDayTemp = (TextView) findViewById(R.id.txtdaytemp);
			txtmaxTemp = (TextView) findViewById(R.id.txtmaxtemp);
			txtMinTemp = (TextView) findViewById(R.id.txtmintemp);
			txtNightTemp= (TextView) findViewById(R.id.txtnighttemp);
			txtMornTemp= (TextView) findViewById(R.id.txtmorntemp);
			txtEveTemp = (TextView) findViewById(R.id.txtevetemp);
			txtPressure= (TextView) findViewById(R.id.txtpressure);
			txtHumidity= (TextView) findViewById(R.id.txthumidity);
			txtWeather= (TextView) findViewById(R.id.txtweather);
			txtWindSpeed= (TextView) findViewById(R.id.txtwindspeed);
			
			lblDayTemp = (TextView) findViewById(R.id.lbldaytemp);
			lblmaxTemp = (TextView) findViewById(R.id.lblmaxtemp);
			lblMinTemp = (TextView) findViewById(R.id.lblmintemp);
			lblNightTemp= (TextView) findViewById(R.id.lblnighttemp);
			lblMornTemp= (TextView) findViewById(R.id.lblmorntemp);
			lblEveTemp = (TextView) findViewById(R.id.lblevetemp);
			lblPressure= (TextView) findViewById(R.id.lblpressure);
			lblHumidity= (TextView) findViewById(R.id.lblumidity);
			lblWeather= (TextView) findViewById(R.id.lblweather);
			lblWindSpeed= (TextView) findViewById(R.id.lblwindspeed);
			
			tvSelectCity.setOnClickListener(this);
			btnNext.setOnClickListener(this);
			btnPrevious.setOnClickListener(this);
			
		tvSelectCity.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-extrabold.ttf"));
		txtDate.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-extrabold.ttf"));
		txtDayTemp.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		txtmaxTemp.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		txtMinTemp.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		txtNightTemp.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		txtMornTemp.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		txtEveTemp.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		txtPressure.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		txtHumidity.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		txtWeather.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		txtWindSpeed.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		
		lblDayTemp.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		lblmaxTemp.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		lblMinTemp.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		lblNightTemp.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		lblMornTemp.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		lblEveTemp.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		lblPressure.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		lblHumidity.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		lblWeather.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
		lblWindSpeed.setTypeface(Typeface.createFromAsset(getAssets(), "proxima-nova-semibold.ttf"));
	}
}
