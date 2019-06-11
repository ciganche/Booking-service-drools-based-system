package model;

public class Location 
{
	private double longitude;
	private double latitude;
	private City city;
	private double distanceFromCity;
	
	
	public Location(double longitude, double latitude, City city, double distanceFromCity) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.city = city;
		this.distanceFromCity = distanceFromCity;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public double getDistanceFromCity() {
		return distanceFromCity;
	}
	public void setDistanceFromCity(double distanceFromCity) {
		this.distanceFromCity = distanceFromCity;
	}
	
	
}
