package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.kie.api.runtime.rule.FactHandle;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import database.Database;
import model.Accommodation;
import model.Reservation;
import model.ReservationState;
import model.User;

@RestController
@RequestMapping(value="rest/")
public class ReservationController
{

	
	
	@RequestMapping(
			value = "/reservation/{start}/{end}/{num}/{id}/{user}",
			method = RequestMethod.POST)
	public String createReservation(@PathVariable("start") String startDate, @PathVariable("end") String endDate, @PathVariable("num") int numberOfPersons, @PathVariable("id") Long accommodationId, @PathVariable("user") Long userId)
	{
		Accommodation accommodation = Database.findAccommodationByID(accommodationId);
		User user = Database.findUserByID(userId);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long id = Database.reservations.size(); 
		try
		{
			Date start = format.parse(startDate);
			Date end = format.parse(endDate);
			
			double originalPrice =  TimeUnit.DAYS.convert(end.getTime() - start.getTime(), TimeUnit.MILLISECONDS) * accommodation.getPrice();
				
			
			Reservation reservation = new Reservation(id,numberOfPersons,user,accommodation,0,start,end,0,ReservationState.OFFER_DISCOUNT,0);
			
			user.getReservations().add(reservation);
			Database.reservations.put(reservation.getId(), reservation);
			Database.globalSession.insert(Database.reservations.get(reservation.getId()));

			
			//Database.globalSession.getAgenda().getAgendaGroup("canclleAndReserve").setFocus();
			Database.globalSession.fireAllRules();
			return ("Original price: " + originalPrice + "\nExtra chages and discounts included: " + reservation.getPrice());
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "Error";
		}
	}
	
	
	@RequestMapping(
			value = "reservation/{user}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<Reservation> getAllReservations(@PathVariable("user") Long userId)
	{
		return Database.getReservationsByUserID(userId);	
	}
	
	
	
	
	
	@RequestMapping(
			value = "reservation/{id}/{user}",
			method = RequestMethod.PUT)
	public String deleteRez(@PathVariable("id") Long rezId, @PathVariable("user") Long userId)
	{

		Reservation rez = Database.reservations.get(rezId);
		FactHandle fh = Database.globalSession.getFactHandle(rez);
		
		
		rez.setState(ReservationState.TO_BE_CANCELLED);
		Database.globalSession.update(fh, rez);
		
		Database.globalSession.fireAllRules();
		
		double price = rez.getPrice() * rez.getCancellationPrecentage()/100;
				
		return ("Cancellation price: " + rez.getPrice() + " x " + rez.getCancellationPrecentage() + "% = " + price);
	}
}
