package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.kie.api.definition.type.Role;

import java.time.temporal.ChronoUnit;

public class Reservation implements Serializable
{
	private long id;
	private long userId;
	private Accommodation accommodation;
	private double price;
	private Date startDate;
	private Date endDate;
	private int discount;
	private ReservationState state;
	private int cancellationPrecentage;
	private Rating rating;
	private int numberOfPersons;
	
	public Reservation()
	{
		
	}
	
	public Reservation(long id,int personCount ,User user, Accommodation accommodation, double price, Date startDate, Date endDate,
			int discount, ReservationState state, int cancelPrec) 
	{
		super();
		this.id = id;
		this.numberOfPersons = personCount;
		this.userId = user.getId();
		this.accommodation = accommodation;
		this.price = price;
		this.startDate = startDate;
		this.endDate = endDate;
		this.discount = discount;
		this.state = state;
		this.cancellationPrecentage = cancelPrec;
		rating = new Rating(0,0,null);
	}
	
	public int getNumberOfPersons() {
		return numberOfPersons;
	}

	public void setNumberOfPersons(int numberOfPersons) {
		this.numberOfPersons = numberOfPersons;
	}

	public boolean isForToday()
	{
		boolean retVal;
		Date current = DateUtils.truncate(new Date(), Calendar.DATE);
		Date startDateFormatted = DateUtils.truncate(startDate, Calendar.DATE);
		
		if(current.compareTo(startDateFormatted) == 0)
			retVal = true;
		else
			retVal = false;
		
		return retVal;
	}
	
	
	public boolean isInPeriod(Date startPeriod, Date endPeriod)
	{
		boolean retVal = false;
		if((startDate.before(startPeriod)) && (endDate.after(startPeriod)))
			retVal = true;
		if((startDate.after(startPeriod)) && (startDate.before(endPeriod)))
			retVal = true;
		
		return retVal;
		
	}
	
	
	public boolean aboveOrBelowDayThreshold(int n, Relation rel)
	{
		boolean retVal = true;
		Date current = DateUtils.truncate(new Date(), Calendar.DATE);
		Date startDateFormatted = DateUtils.truncate(startDate, Calendar.DATE);
		
	    long diff = startDateFormatted.getTime() - current.getTime();
	    double days = (diff / (1000*60*60*24));
	    
	    switch(rel)
	    {
	    	case UNDER:
		    	if(days <= n)
					retVal = true;
				else
					retVal = false;
			break;
			
			
	    	case OVER:
		    	if(days >= n)
					retVal = true;
				else
					retVal = false;
			break; 	
	    }
	    
	    return retVal;
	}
	

	public int getCancellationPrecentage() {
		return cancellationPrecentage;
	}

	public void setCancellationPrecentage(int cancellationPrecentage) {
		if(cancellationPrecentage > 100)
			this.cancellationPrecentage = 100;
		else
			this.cancellationPrecentage = cancellationPrecentage;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long user) {
		this.userId = user;
	}
	public Accommodation getAccommodation() {
		return accommodation;
	}
	public void setAccommodation(Accommodation accommodation) {
		this.accommodation = accommodation;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
		
	public ReservationState getState() {
		return state;
	}

	public void setState(ReservationState state) {
		this.state = state;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}
	
	@Override
	public String toString()
	{
		return  ("- " + id + " " + state.name() + " " + accommodation.getName());
	}

}
