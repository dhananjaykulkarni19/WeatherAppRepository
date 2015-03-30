package com.demoweather.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

public class WeatherDetailsTable extends Model
{
	@Column(name="CityID",onUniqueConflict=Column.ConflictAction.REPLACE)
	public int _cityID;
	
	@Column(name="CityName")
	public String _cityName;
	
	@Column(name="Date")
	public String _date;
	
	@Column(name="DayTemp")
	public float _dayTemp;
	
	@Column(name="MinTemp")
	public float _minTemp;
	
	@Column(name="MaxTemp")
	public float _maxTemp;
	
	@Column(name="NightTemp")
	public float _nightTemp;
	
	@Column(name="EveTemp")
	public float _eveTemp;
	
	@Column(name="MornTemp")
	public float _mornTemp;
	
	@Column(name="Pressure")
	public String _pressure;
	
	@Column(name="Humidity")
	public String _humidity;
	
	@Column(name="WheatherTitle")
	public String _wheathertTitle;
	
	@Column(name="WheatherDesc")
	public String _wheatherDesc;
	
	@Column(name="WindSpeed")
	public String _windSpeed;
	
	@Column(name="WindDirection")
	public String _windDIrection;
	
	
	public WeatherDetailsTable()
	{
		super();
	}
	
	public static List<WeatherDetailsTable> getAllRecords()
	{
		return new Select().from(WeatherDetailsTable.class).groupBy("CityName").execute();
	}
	public static List<WeatherDetailsTable> getWeatherDetailsForGivenCity(String city)
	{
		return new Select().from(WeatherDetailsTable.class).where("CityName = ?",city).execute();
	}
	public static WeatherDetailsTable getWeatherDetailsByDate(String date)
	{
		return new Select().from(WeatherDetailsTable.class).where("Date = ?",date).executeSingle();
	}
}
