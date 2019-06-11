package database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Bean;

import model.Accommodation;
import model.AccommodationType;
import model.Agent;
import model.Amenity;
import model.Category;
import model.City;
import model.Country;
import model.Location;
import model.Message;
import model.Rating;
import model.Reservation;
import model.ReservationState;
import model.User;

public class Database
{
	public static HashMap<Long,Accommodation> accommodations;
	public static HashMap<Long,Agent> agents;
	public static HashMap<Long,Message> messages;
	public static HashMap<Long,Reservation> reservations;
	public static HashMap<Long,User> users;
	public static KieSession globalSession;
	public static KieContainer kjarContainer;
	
	
	public Database()
	{
	
	}	

	@Bean
	public KieContainer connectWithKjar()
	{
		KieServices ks = KieServices.Factory.get();
		kjarContainer = ks.newKieContainer(ks.newReleaseId("sbnz.integracija", "drools-spring-kjar", "0.0.1-SNAPSHOT"));
		KieScanner kScanner = ks.newKieScanner(kjarContainer);
		kScanner.start(2000L);
		
		return kjarContainer;	
	}
	
	public void init()
	{
		accommodations = new HashMap<Long,Accommodation>();
		agents = new HashMap<Long,Agent>();
		messages = new HashMap<Long,Message>();
		reservations = new HashMap<Long,Reservation>();
		users = new HashMap<Long,User>();
		
		KieBaseConfiguration config = KieServices.Factory.get().newKieBaseConfiguration();
		config.setOption( EventProcessingOption.STREAM );
		
		kjarContainer = connectWithKjar();
		
    	KieBase kb = kjarContainer.newKieBase("discount", config);
        globalSession = kb.newKieSession();
	}
	
	public void fillWithMockData()
	{
        Country japan = new Country(0,"Japan");
        Country serbia = new Country(1,"Serbia");
        City kyoto = new City(0,"Kyoto",japan);
        City tokyo = new City(1,"Tokyo",japan);
        City bg = new City(2,"Beograd",serbia);
        City sbc = new City(3,"Sabac",serbia);
        Location location1 = new Location(15,15,kyoto,29);
        Location location2 = new Location(7,7,tokyo,27);
        Location location3 = new Location(7,7,tokyo,5);
        Location location4 = new Location(7,7,tokyo,15);
        
        Location location5 = new Location(7,7,sbc,50);
        Location location6 = new Location(7,7,bg,50);
        
        
        Accommodation accommodation1 = new Accommodation(0,"kod Mice","super sve stv", location1, AccommodationType.HOTEL,5,15,2,17000);
  
        Accommodation accommodation2 = new Accommodation(1,"kod Bice","super sve stv", location2, AccommodationType.HOTEL,5,15,3,1000);
        Accommodation accommodation3 = new Accommodation(2,"kod Djeljane","super sve stv", location3, AccommodationType.HOTEL,5,15,4,1000);
        accommodation3.getAmenities().add(new Amenity(0,"tv"));      
        Accommodation accommodation4 = new Accommodation(3,"Accom1","super sve stv", location4, AccommodationType.HOTEL,5,15,5,2000);
        Accommodation accommodation5 = new Accommodation(4,"Accom5","super sve stv", location5, AccommodationType.HOTEL,5,15,2,3000);
        Accommodation accommodation6 = new Accommodation(5,"Accom1651","super sve stv", location6, AccommodationType.HOTEL,5,15,3,1500);
        Accommodation accommodation7 = new Accommodation(6,"Ovaj1","super sve stv", location1, AccommodationType.HOTEL,5,15,4,3500);
        Accommodation accommodation8 = new Accommodation(7,"Accom888","super sve stv", location2, AccommodationType.HOTEL,5,15,5,4000);
        Accommodation accommodation9 = new Accommodation(8,"Accom99798","super sve stv", location3, AccommodationType.HOTEL,5,15,2,1000);
        Accommodation accommodation10 = new Accommodation(9,"Accom1529","super sve stv", location4, AccommodationType.HOTEL,5,15,3,5000);
        Accommodation accommodation11 = new Accommodation(10,"Accomzzzzz","super sve stv", location5, AccommodationType.HOTEL,5,15,4,8000);
        Accommodation accommodation12 = new Accommodation(11,"Accom buuus","super sve stv", location6, AccommodationType.HOTEL,5,15,5,500);
        Accommodation accommodation13 = new Accommodation(12,"Ovaj2","super sve stv", location1, AccommodationType.HOTEL,5,15,2,750);
        Accommodation accommodation14 = new Accommodation(13,"UBICA","super sve stv", location2, AccommodationType.HOTEL,5,15,3,2500);
        Accommodation accommodation15 = new Accommodation(14,"Ovaj3","super sve stv", location1, AccommodationType.HOTEL,5,15,4,3750);
        
        
        accommodations.put(accommodation1.getId(), accommodation1);
        accommodations.put(accommodation2.getId(), accommodation2);
        accommodations.put(accommodation3.getId(), accommodation3);
        accommodations.put(accommodation4.getId(), accommodation4);
        accommodations.put(accommodation5.getId(), accommodation5);
        accommodations.put(accommodation6.getId(), accommodation6);
        accommodations.put(accommodation7.getId(), accommodation7);
        accommodations.put(accommodation8.getId(), accommodation8);
        accommodations.put(accommodation9.getId(), accommodation9);
        accommodations.put(accommodation10.getId(), accommodation10);
        accommodations.put(accommodation11.getId(), accommodation11);
        accommodations.put(accommodation12.getId(), accommodation12);
        accommodations.put(accommodation13.getId(), accommodation13);
        accommodations.put(accommodation14.getId(), accommodation14);
        accommodations.put(accommodation15.getId(), accommodation15);
        
        User user = new User(0,"user0@asd.com","12345","Sin","Bakic");
        user.setCategory(Category.BRONZE);
        
        User user1 = new User(1,"user1@asd.com","12345","Brat","Bratic");
        user.setCategory(Category.BRONZE);
        
        User user2 = new User(2,"user2@asd.com","12345","Jesus","Mukic");
        user.setCategory(Category.BRONZE);
        
        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);
        users.put(user.getId(), user);
        
        
        // * * * for testing rule "For every successful year 25%" - discount should be 75%
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Reservation year1 = new Reservation(0,10,user,accommodation1,100,format.parse("2017-07-21"), format.parse("2017-07-30"),10,ReservationState.FINISHED,5);
			Reservation year2 = new Reservation(1,10,user,accommodation1,100,format.parse("2016-07-21"), format.parse("2016-07-30"),10,ReservationState.FINISHED,5);
			Reservation year3 = new Reservation(2,10,user1,accommodation1,100,format.parse("2018-07-21"), format.parse("2018-07-30"),10,ReservationState.FINISHED,5);
			//Reservation year4 = new Reservation(3,10,user2,accommodation1,100,format.parse("2015-07-21"), format.parse("2015-07-30"),10,ReservationState.FINISHED,5);
			//Reservation year5 = new Reservation(4,10,user2,accommodation1,100,format.parse("2014-07-21"), format.parse("2014-07-30"),10,ReservationState.FINISHED,5);
			
			reservations.put(year1.getId(), year1);
			reservations.put(year2.getId(), year2);
			reservations.put(year3.getId(), year3);
			//reservations.put(year4.getId(), year4);
			//reservations.put(year5.getId(), year5);
			user.getReservations().add(year1);
			user.getReservations().add(year2);
			user1.getReservations().add(year3);
			//user2.getReservations().add(year4);
			//user2.getReservations().add(year5);
			
			
			
			
			//for user discount of 60%
			Reservation discount1 = new Reservation(3,10,user,accommodation2,100,format.parse("2017-07-21"), format.parse("2017-07-30"),10,ReservationState.FINISHED,5);
			Reservation discount2 = new Reservation(4,10,user,accommodation2,100,format.parse("2017-07-21"), format.parse("2017-07-30"),10,ReservationState.FINISHED,5);
			Reservation discount3 = new Reservation(5,10,user,accommodation3,100,format.parse("2017-07-21"), format.parse("2017-07-30"),10,ReservationState.FINISHED,5);
			Reservation discount4 = new Reservation(6,10,user,accommodation3,100,format.parse("2017-07-21"), format.parse("2017-07-30"),10,ReservationState.FINISHED,5);
			Reservation discount5 = new Reservation(7,10,user,accommodation8,100,format.parse("2017-07-21"), format.parse("2017-07-30"),10,ReservationState.FINISHED,5);
			Reservation discount6 = new Reservation(8,10,user,accommodation9,100,format.parse("2017-07-21"), format.parse("2017-07-30"),10,ReservationState.FINISHED,5);

			//the one that does not trigger accommodation14 - UBICA
			Reservation discount7 = new Reservation(9,10,user,accommodation14,100,format.parse("2017-07-21"), format.parse("2017-07-30"),10,ReservationState.FINISHED,5);
			discount7.setRating(new Rating(0,5,null));
			
			reservations.put(discount1.getId(), discount1);
			reservations.put(discount2.getId(), discount2);
			reservations.put(discount3.getId(), discount3);
			reservations.put(discount4.getId(), discount4);
			reservations.put(discount5.getId(), discount5);
			reservations.put(discount6.getId(), discount6);
			reservations.put(discount7.getId(), discount7);
			
			user.getReservations().add(discount1);
			user.getReservations().add(discount2);
			user.getReservations().add(discount3);
			user.getReservations().add(discount4);
			user.getReservations().add(discount5);
			user.getReservations().add(discount6);
			user.getReservations().add(discount7);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
        
        // * * * 
     
        
		Agent agent0 = new Agent(0,"agent0@asd.com","12345","Mile","3000",accommodation1);
		Agent agent1 = new Agent(1,"agent1@asd.com","12345","Cone","3001",accommodation2);
		Agent agent2 = new Agent(2,"agent2@asd.com","12345","Asdf","3002",accommodation3);
		Agent agent3 = new Agent(3,"agent3@asd.com","12345","Asdf","3002",accommodation4);
		accommodation1.setAgentId(agent0.getId());
		accommodation2.setAgentId(agent1.getId());
		accommodation3.setAgentId(agent2.getId());
		accommodation4.setAgentId(agent3.getId());

		agents.put(agent0.getId(), agent0);
		agents.put(agent1.getId(), agent1);
		agents.put(agent2.getId(), agent2);
		agents.put(agent3.getId(), agent3);

		
		
		//insert into session
		insertIntoSession(globalSession, Performative.RESERVATIONS);
		insertIntoSession(globalSession, Performative.ACCOMMODATIONS);
		insertIntoSession(globalSession, Performative.USERS);
		globalSession.insert(new Date());
	}
	
	
	public void fillAccommodationCategoryData()
	{
		
	    Country japan = new Country(0,"Japan");
        City kyoto = new City(0,"Kyoto",japan);
        City tokyo = new City(1,"Tokyo",japan);
        Location location1 = new Location(15,15,kyoto,29);
        Location location2 = new Location(7,7,tokyo,27);
        Location location3 = new Location(7,7,tokyo,5);
        
        
        User user = new User(0,"user0@asd.com","12345","Sin","Bakic");
        user.setCategory(Category.BRONZE);
        User user1 = new User(1,"user1@asd.com","12345","Brat","Bratic");
        user.setCategory(Category.BRONZE);
        users.put(user1.getId(), user1);
        users.put(user.getId(), user);
        
        
        Accommodation accommodation1 = new Accommodation(0,"kod Mice","super sve stv", location1, AccommodationType.HOTEL,5,15,2,17000);
        Accommodation accommodation2 = new Accommodation(1,"kod Bice","super sve stv", location2, AccommodationType.HOTEL,5,15,3,1000);
        Accommodation accommodation3 = new Accommodation(2,"kod Djeljane","super sve stv", location3, AccommodationType.HOTEL,5,15,4,1000);
        accommodations.put(accommodation1.getId(), accommodation1);
        accommodations.put(accommodation2.getId(), accommodation2);
        accommodations.put(accommodation3.getId(), accommodation3);
        
        
        //accommodation1 is SILVER
        accommodation1.setRatingAvg(2.5);
		Reservation rez1 = new Reservation(0,10,user,accommodation1,100,new Date(),new Date(),10,ReservationState.FINISHED,5);
		Reservation rez2 = new Reservation(1,10,user,accommodation1,100,new Date(),new Date(),10,ReservationState.FINISHED,5);
		Reservation rez3 = new Reservation(2,10,user1,accommodation1,100,new Date(),new Date(),10,ReservationState.FINISHED,5);
		reservations.put(rez1.getId(), rez1);
		reservations.put(rez2.getId(), rez2);
		reservations.put(rez3.getId(), rez3);	
		user.getReservations().add(rez1);
		user.getReservations().add(rez2);
		user1.getReservations().add(rez3);
		
		
		
		//accommodation2 is GOLD
		Date today = new Date();
        accommodation2.setRatingAvg(3.5);
		Reservation rez4 = new Reservation(3,10,user,accommodation2,100,new Date(),new Date(),10,ReservationState.FINISHED,5);
		Reservation rez5 = new Reservation(4,10,user,accommodation2,100,new Date(),new Date(),10,ReservationState.FINISHED,5);
		Reservation rez6 = new Reservation(5,10,user1,accommodation3,100,new Date(),new Date(),10,ReservationState.FINISHED,5);
		
		rez4.setRating(new Rating(0,4.4,null));
		rez5.setRating(new Rating(0,5,null));
		rez6.setRating(new Rating(0,3.3,null));
		reservations.put(rez4.getId(), rez4);
		reservations.put(rez5.getId(), rez5);
		reservations.put(rez6.getId(), rez6);	
		user.getReservations().add(rez4);
		user.getReservations().add(rez5);
		user1.getReservations().add(rez6);
		
		
		//accommodation3 is PLATINUM
		accommodation3.setRatingAvg(4);
		accommodation3.setCategory(Category.GOLD);
		Reservation rez7 = new Reservation(6,10,user,accommodation3,10000,new Date(),new Date(),10,ReservationState.FINISHED,5);
		reservations.put(rez7.getId(), rez7);	
		user.getReservations().add(rez7);
		
		
		
		//insert into session
		insertIntoSession(globalSession, Performative.RESERVATIONS);
		insertIntoSession(globalSession, Performative.ACCOMMODATIONS);
		insertIntoSession(globalSession, Performative.USERS);
		globalSession.insert(new Date());
	}
	
	
	public void fillUserCategoryData()
	{
		
	}
	
	
	
	
	
	
	public static Accommodation findAccommodationByID(Long id)
	{
		return accommodations.get(id);
	}
	
	public static Agent findAgentByID(Long id)
	{
		return agents.get(id);
	}
	
	
	public static ArrayList<Reservation> getReservationsByUserID(Long id)
	{
		Collection<Reservation> col = reservations.values();
		
		ArrayList<Reservation> retVal = new ArrayList<Reservation>();
		
		for(Reservation u : col)
		{
			if(u.getUserId() == id)
			{
				retVal.add(u);
			}
		}
		
		return retVal;
	}
	
	public static User findUserByID(Long id)
	{
		return users.get(id);
	}
	
	
	public static User logginUser(String username,String password)
	{
		User retVal = null;
		
		Collection<User> col = users.values();
	
		for(User u : col)
		{
			if(u.getEmail().equals(username) && u.getPassword().equals(password))
			{
				retVal = u;
			}
		}
		
		return retVal;
	}
	
	
	public static Agent logginAgent(String username,String password)
	{
		Agent retVal = null;
		
		Collection<Agent> col = agents.values();
	
		for(Agent u : col)
		{
			if(u.getEmail().equals(username) && u.getPassword().equals(password))
			{
				retVal = u;
			}
		}
		
		return retVal;
	}
	
	
	
	
	
	
	
	
	
	// * * * kieSession * * * 
	
	public static void insertIntoSession(KieSession session, Performative p)
	{
		switch(p)
		{
			case USERS:
				for(User u : users.values())
				{
					session.insert(u);
				}
			break;
			
			case AGENTS:
				for(Agent u : agents.values())
				{
					session.insert(u);
				}
			break;
			
			
			case RESERVATIONS:
				for(Reservation u : reservations.values())
				{
					session.insert(u);
				}
			break;
			
			
			case ACCOMMODATIONS:
				for(Accommodation u : accommodations.values())
				{
					session.insert(u);
				}
			break;
			
			case MESSAGES:
				for(Message u : messages.values())
				{
					session.insert(u);
				}
			break;
		}
	}

	public static Object getAllAccommodations() 
	{
		return accommodations.values();
	}

	public static void removeReservation(Reservation r) 
	{
		reservations.remove(r.getId(), r);
	}
	
	public static Agent findAgentByAccommodationId(Long acc)
	{
		Agent retVal = null;
		
		for(Agent a : agents.values())
		{
			if(a.getAccommodation().getId() == acc)
			{
				retVal = a;
			}
		}
		
		return retVal;
	}
}
