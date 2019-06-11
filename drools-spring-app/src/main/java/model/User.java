package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kie.api.runtime.KieSession;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import database.Database;

@JsonIgnoreProperties(value = { "ksession" })
public class User implements Serializable
{

	private long id;
	private String email;


	private String password;
	private String name;
	private String lastname;
	private List<Reservation> reservations;
	private Category category;
	private Date registrationDate;
	private int rezervationDiscountAllowence = 0;
	private int coupons;
	private Set<Amenity> amenities = new HashSet<Amenity>();
	@JsonIgnore
	private KieSession ksession;



	public User()
	{
		super();
	}

	public User(long id, String email, String password, String name, String lastname) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.lastname = lastname;
		this.reservations = new ArrayList<Reservation>();		
		this.registrationDate = new Date();
		this.coupons = 0;
						   
    	ksession = Database.kjarContainer.newKieSession("recommendSession");
    }
	
	
	public Date getRegistrationDate() {
		return registrationDate;
	}


	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}


	public double getAverageCityDistance()
	{
		double sum = 0;
		double cnt = 0;
		for(Reservation r : reservations)
		{
				sum = sum + r.getAccommodation().getLocation().getDistanceFromCity();
				cnt = cnt + 1;
		}
		
		if(cnt == 0) return 0;
		
		return (sum/cnt);
	}
	
	public boolean doesContainIntersection(Set<Amenity> currentAmenities)
	{
		Set<Amenity> intersection = new HashSet<Amenity>();
		
		if(reservations.size() != 0)
		{
			intersection = reservations.get(0).getAccommodation().getAmenities();
			
			for( int i = 1 ; i < reservations.size() ; i ++ )
			{
				intersection.retainAll(reservations.get(i).getAccommodation().getAmenities());
			}		
		}
		
		amenities = intersection;
		if(currentAmenities.containsAll(intersection))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public Set<Amenity> getAmenities() {
		return amenities;
	}

	public void setAmenities(Set<Amenity> amenities) {
		this.amenities = amenities;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public List<Reservation> getReservations() {
		return reservations;
	}
	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public int getRezervationDiscountAllowence() {
		return rezervationDiscountAllowence;
	}


	public void setRezervationDiscountAllowence(int rezervationDiscountAllowence) {
		this.rezervationDiscountAllowence = rezervationDiscountAllowence;
	}
	
	public int getCoupons() {
		return coupons;
	}

	public void setCoupons(int coupons) {
		this.coupons = coupons;
	}

	public KieSession getKsession() {
		return ksession;
	}

	public void setKsession(KieSession ksession) {
		this.ksession = ksession;
	}
	
}
