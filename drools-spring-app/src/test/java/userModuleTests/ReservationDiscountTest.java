package userModuleTests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
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
import model.BlockDiscountStatus;
import model.Category;
import model.City;
import model.Country;
import model.Location;
import model.Reservation;
import model.ReservationState;
import model.User;
import util.KnowledgeSessionHelper;

public class ReservationDiscountTest 
{
	public KieContainer kc;
	public KieSession ksession;
	private Reservation rez1;
	private Reservation rez2;
	private Reservation rez3;
	
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
       
		ksession.insert(new Date());

        //data
        User user = new User(0,"asdas@asd.com","12345","Sin","Bakic");
        user.setCategory(Category.SILVER);
        
        Country japan = new Country(0,"Japan");
        City kyoto = new City(0,"Kyoto",japan);
        City tokyo = new City(1,"Tokyo",japan);
        Location location1 = new Location(15,15,kyoto,15);
        Location location2 = new Location(7,7,tokyo,30);
        //Location location3 = new Location(7,7,tokyo,50);
        Accommodation accommodation1 = new Accommodation(0,"kod Mice","super sve stv", location1, AccommodationType.HOTEL,5,15,5,1000);
        accommodation1.setCategory(Category.SILVER);
        
        Accommodation accommodation2 = new Accommodation(1,"kod Bice","super sve stv", location2, AccommodationType.HOTEL,5,15,5,1000);
        //Accommodation accommodation3 = new Accommodation(2,"kod Djeljane","super sve stv", location3, AccommodationType.HOTEL,5,15,5,1000);
        
       
        ksession.insert(accommodation2);
        try
        {
	        rez1 = new Reservation(0,3,user,accommodation2,5000,new Date(),new Date(), 0, ReservationState.ACTIVE,0);
	        rez2 = new Reservation(1,3,user,accommodation2,5000,new Date(),new Date(), 0, ReservationState.ACTIVE,0);
	        rez3 = new Reservation(2,3,user,accommodation2,5000,new Date(),new Date(), 0, ReservationState.ACTIVE,0);
	        user.getReservations().add(rez1);
	        user.getReservations().add(rez2);
	        user.getReservations().add(rez3);
	        

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
	public void testSameDayDiscount()
	{		
		//2 rules: one for the recommending system, one for rez1 object, one for rez2
		
		ksession.insert(rez1);

		ksession.insert(rez2);

		ksession.fireAllRules();
		
		
		assertThat(BlockDiscountStatus.NONE, equalTo(rez1.getAccommodation().getBlockDiscountStatus()));


		ksession.insert(rez3);
		
		ksession.fireAllRules();

		
		assertThat(0,equalTo(rez1.getDiscount()));
		assertThat(BlockDiscountStatus.NONE, equalTo(rez1.getAccommodation().getBlockDiscountStatus()));
		ksession.fireAllRules();    
	}	
}
