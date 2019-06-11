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

public class UserCategoryTest 
{
	
	public Reservation rez1;
	public Reservation rez2;
	public Reservation rez3;
	public Reservation rez4;
	public Reservation rez5;
	public Reservation rez6;
	public Reservation rez7;
	public Reservation rez8;
	public Reservation rez9;
	public Reservation rez10;
	
	public KieContainer kc;
	public KieSession ksession;
	public Accommodation accommodation1;
	public Accommodation accommodation2;
	public Accommodation accommodation3;
	
	public User user;
	
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
        user = new User(0,"asdas@asd.com","12345","Sin","Bakic");
        
        Country japan = new Country(0,"Japan");
        City kyoto = new City(0,"Kyoto",japan);
        City tokyo = new City(1,"Tokyo",japan);
        Location location1 = new Location(15,15,kyoto,15);
        Location location2 = new Location(7,7,tokyo,30);
        Location location3 = new Location(7,7,tokyo,50);
        accommodation1 = new Accommodation(0,"kod Mice","super sve stv", location1, AccommodationType.HOTEL,5,15,0,1000);
        accommodation1.setCategory(Category.PLATINUM);
        
        accommodation2 = new Accommodation(1,"kod Bice","super sve stv", location2, AccommodationType.HOTEL,5,15,1,1000);
        accommodation3 = new Accommodation(2,"kod Djeljane","super sve stv", location3, AccommodationType.HOTEL,5,15,2,1000);
                

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try
        {
	        rez1 = new Reservation(0,3,user,accommodation2,100,format.parse("2019/04/05"),format.parse("2019/04/05"), 10, ReservationState.FINISHED,0);
	        rez2 = new Reservation(1,3,user,accommodation2,100,format.parse("2019/04/05"),format.parse("2019/04/10"), 10, ReservationState.FINISHED,50);
	        rez3 = new Reservation(2,3,user,accommodation2,100,format.parse("2019/04/10"),format.parse("2019/04/11"), 10, ReservationState.FINISHED,0);

	        rez3 = new Reservation(3,3,user,accommodation1,100,format.parse("2019/04/10"),format.parse("2019/04/11"), 10, ReservationState.FINISHED,0);
	        rez4 = new Reservation(4,3,user,accommodation2,100,format.parse("2019/04/10"),format.parse("2019/04/11"), 10, ReservationState.FINISHED,0);
	        rez5 = new Reservation(5,3,user,accommodation2,100,format.parse("2019/04/10"),format.parse("2019/04/11"), 10, ReservationState.FINISHED,0);
	        rez6 = new Reservation(6,3,user,accommodation2,100,format.parse("2019/04/10"),format.parse("2019/04/11"), 10, ReservationState.FINISHED,0);
	        rez7 = new Reservation(7,3,user,accommodation2,100,format.parse("2019/04/10"),format.parse("2019/04/11"), 10, ReservationState.FINISHED,0);
	        rez8 = new Reservation(8,3,user,accommodation2,100,format.parse("2019/04/10"),format.parse("2019/04/11"), 10, ReservationState.FINISHED,0);
	        rez9 = new Reservation(9,3,user,accommodation2,100,format.parse("2019/04/10"),format.parse("2019/04/11"), 10, ReservationState.FINISHED,0);
	        rez10 = new Reservation(9,3,user,accommodation2,100,format.parse("2019/04/10"),format.parse("2019/04/11"), 10, ReservationState.FINISHED,0);
	        
	        
	        
			Rating rating1 = new Rating(0,4.1, null);
			Rating rating2 = new Rating(1,2, null);
			Rating rating3 = new Rating(2,5, null);
			
			rez1.setRating(rating1);
			rez2.setRating(rating2);
			rez3.setRating(rating3);
			rez4.setRating(rating3);
			rez5.setRating(rating3);
			rez6.setRating(rating3);
			rez7.setRating(rating3);
			rez8.setRating(rating3);
			rez9.setRating(rating3);
			rez10.setRating(rating3);

			user.setRegistrationDate(format.parse("2018/06/25"));
	      
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
	public void testBronze()
	{
		ksession.insert(user);
		
		int fired = ksession.fireAllRules();
        assertThat(1, equalTo(fired));
		
        assertThat(Category.BRONZE, equalTo(user.getCategory()));
	}
	
	
	@Test
	public void testSilver()
	{
		ksession.insert(new Date());
		rez1.setPrice(500);

		ksession.insert(rez1);
		ksession.insert(user);
		
		int fired = ksession.fireAllRules();
        assertThat(2, equalTo(fired));
		
        assertThat(Category.SILVER, equalTo(user.getCategory()));
	}
	
	

	@Test
	public void testSilver2()
	{
		ksession.insert(new Date());
		rez1.setPrice(500);

		ksession.insert(rez1);
		ksession.insert(rez2);
		ksession.insert(rez3);
		
		user.setRegistrationDate(new Date());
		ksession.insert(user);
		
		int fired = ksession.fireAllRules();
        assertThat(2, equalTo(fired));
		
        assertThat(Category.SILVER, equalTo(user.getCategory()));
	}
	
	@Test
	public void testGold()
	{
		ksession.insert(rez1);
		ksession.insert(rez2);
		ksession.insert(rez3);
		ksession.insert(rez4);
		ksession.insert(rez5);
		ksession.insert(rez6);
		ksession.insert(rez7);
		ksession.insert(rez8);
		ksession.insert(rez9);
		ksession.insert(rez10);
		
		ksession.insert(user);
		
		int fired = ksession.fireAllRules();
        assertThat(2, equalTo(fired));
        
        assertThat(1, equalTo(user.getCoupons()));
        assertThat(Category.GOLD, equalTo(user.getCategory()));
	}
	
	
	@Test
	public void testPlatinum()
	{
		ksession.insert(new Date());
		rez1.setPrice(500);
		rez2.setPrice(700);
		rez3.setPrice(1000);

		ksession.insert(rez1);
		ksession.insert(rez2);
		ksession.insert(rez3);
		
		ksession.insert(user);
		
		int fired = ksession.fireAllRules();
        assertThat(3, equalTo(fired));
        assertThat(1, equalTo(user.getCoupons()));
        assertThat(Category.PLATINUM, equalTo(user.getCategory()));
	}
	
	
}
