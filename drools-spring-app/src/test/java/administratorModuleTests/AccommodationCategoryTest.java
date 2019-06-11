package administratorModuleTests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import database.Database;
import model.Accommodation;
import model.AccommodationType;
import model.Category;
import model.City;
import model.Country;
import model.Location;
import model.Rating;
import model.Reservation;
import model.ReservationState;
import model.User;

public class AccommodationCategoryTest 
{
	
	public Reservation rez1;
	public Reservation rez3;
	public Reservation rez2;
	public KieContainer kc;
	public KieSession ksession;
	public Accommodation accommodation1;
	public Accommodation accommodation2;
	public Accommodation accommodation3;
	
	
	
	@Before
	public void setup()
	{

		KieServices ks = KieServices.Factory.get();
		KieContainer kc = ks.newKieContainer(ks.newReleaseId("sbnz.integracija", "drools-spring-kjar", "0.0.1-SNAPSHOT"));
		KieBaseConfiguration config = KieServices.Factory.get().newKieBaseConfiguration();
		config.setOption( EventProcessingOption.STREAM );
		KieBase kb = kc.newKieBase("discount", config);
        ksession = kb.newKieSession();
        Database db = new Database();
        db.init();
                
        //data
        User user = new User(0,"asdas@asd.com","12345","Sin","Bakic");
        user.setCategory(Category.SILVER);
        
        Country japan = new Country(0,"Japan");
        City kyoto = new City(0,"Kyoto",japan);
        City tokyo = new City(1,"Tokyo",japan);
        Location location1 = new Location(15,15,kyoto,15);
        Location location2 = new Location(7,7,tokyo,30);
        Location location3 = new Location(7,7,tokyo,50);
        accommodation1 = new Accommodation(0,"kod Mice","super sve stv", location1, AccommodationType.HOTEL,5,15,0,1000);
        accommodation1.setCategory(Category.SILVER);
        
        accommodation2 = new Accommodation(1,"kod Bice","super sve stv", location2, AccommodationType.HOTEL,5,15,1,1000);
        accommodation3 = new Accommodation(2,"kod Djeljane","super sve stv", location3, AccommodationType.HOTEL,5,15,2,1000);
                

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try
        {
	        rez1 = new Reservation(0,3,user,accommodation2,100,format.parse("2019/06/01"),format.parse("2019/06/04"), 10, ReservationState.FINISHED,0);
	        rez2 = new Reservation(1,3,user,accommodation2,100,format.parse("2019/05/30"),format.parse("2019/06/03"), 10, ReservationState.FINISHED,50);
	        rez3 = new Reservation(2,1,user,accommodation2,100,format.parse("2019/05/29"),format.parse("2019/06/02"), 10, ReservationState.FINISHED,0);
	        user.getReservations().add(rez1);
	        user.getReservations().add(rez2);
	        user.getReservations().add(rez3);
	        
			Rating rating1 = new Rating(0,4.1, null);
			Rating rating2 = new Rating(1,2, null);
			Rating rating3 = new Rating(2,5, null);
			
			rez1.setRating(rating1);
			rez2.setRating(rating2);
			rez3.setRating(rating3);
	      
        }
        catch(Exception e)
        {
        	System.err.println("Wrong date format: " + e);
        }

	}
	
	@After
	public void clear()
	{
		ksession.dispose();
	}

	@Test
	public void testSilver()
	{
		ksession.insert(rez1);
        ksession.insert(rez2);
        ksession.insert(rez3);
        
        
		accommodation1.setRatingAvg(2.2);
		accommodation2.setRatingAvg(3);
		accommodation3.setRatingAvg(5);
		
		
		ksession.insert(accommodation1);
		ksession.insert(accommodation2);
		ksession.insert(accommodation3);
		ksession.insert(new Date());		
		
		
		//for accommodation2 object "Bronze" and "Silver" rules are executed
		ksession.fireAllRules();
        
        
        assertThat(Category.SILVER, equalTo(accommodation2.getCategory()));

	}
	
	@Test
	public void testGold()
	{
		
		ksession.insert(rez1);
        ksession.insert(rez2);
        ksession.insert(rez3);
		
		accommodation1.setRatingAvg(5);
		accommodation2.setRatingAvg(3.5);
		accommodation3.setRatingAvg(5);
		
		
		ksession.insert(accommodation2);
		ksession.insert(new Date());		
		
		//one for silver and one for gold rule
		int fired = ksession.fireAllRules();
        assertThat(2, equalTo(fired));
        
        assertThat(Category.GOLD, equalTo(accommodation2.getCategory()));
 
	}
	
	
	@Test
	public void testPlatinum()
	{
		
		rez1.setPrice(1000);
		
		ksession.insert(rez1);
        ksession.insert(rez2);
        ksession.insert(rez3);
		
		accommodation1.setRatingAvg(5);
		accommodation2.setRatingAvg(3.5);
		accommodation3.setRatingAvg(5);
		
		
		ksession.insert(accommodation2);
		ksession.insert(new Date());		
		
		//one for silver, one for gold and one for platinum rule 
		int fired = ksession.fireAllRules();
        assertThat(3, equalTo(fired));
        
        assertThat(Category.PLATINUM, equalTo(accommodation2.getCategory()));
 
	}


}
