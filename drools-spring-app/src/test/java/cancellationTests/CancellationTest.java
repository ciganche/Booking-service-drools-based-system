package cancellationTests;

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
import model.Reservation;
import model.ReservationState;
import model.User;

public class CancellationTest 
{
	public KieContainer kc;
	public KieSession ksession;
	private Reservation rez1;
	private Reservation rez2;
	private Reservation rez3;
	
	private Reservation rezTemp1;
	private Reservation rezTemp2;
	private Reservation rezTemp3;
	
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
        //Location location3 = new Location(7,7,tokyo,50);
        Accommodation accommodation1 = new Accommodation(0,"kod Mice","super sve stv", location1, AccommodationType.HOTEL,5,15,5,1000);
        accommodation1.setCategory(Category.SILVER);
        
        Accommodation accommodation2 = new Accommodation(1,"kod Bice","super sve stv", location2, AccommodationType.HOTEL,5,15,5,1000);
        //Accommodation accommodation3 = new Accommodation(2,"kod Djeljane","super sve stv", location3, AccommodationType.HOTEL,5,15,5,1000);
        
        
        Date date = new Date();
        Date end = new Date(date.getTime() + (1000 * 60 * 60 * 24 * 35));
        Date start15 = new Date(date.getTime() + (1000 * 60 * 60 * 24 * 15));
        Date start30 = new Date(date.getTime() + (1000 * 60 * 60 * 24 * 29));
        Date start3 = new Date(date.getTime() + (1000 * 60 * 60 * 24 * 2));
        
        

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try
        {
        	
        	rezTemp1 = new Reservation(0,3,user,accommodation2,5000,format.parse("2018/12/10"),format.parse("2018/12/15"), 10, ReservationState.FINISHED,0);
        	rezTemp2 = new Reservation(0,3,user,accommodation2,5000,format.parse("2016/12/15"),format.parse("2016/12/16"), 10, ReservationState.FINISHED,0);
        	rezTemp3 = new Reservation(0,3,user,accommodation2,5000,format.parse("2017/12/10"),format.parse("2017/12/15"), 10, ReservationState.FINISHED,0);
        	
	        rez1 = new Reservation(0,3,user,accommodation2,5000,start3,end, 10, ReservationState.TO_BE_CANCELLED,0);
	        rez2 = new Reservation(1,3,user,accommodation2,5000,start30,end, 10, ReservationState.TO_BE_CANCELLED,0);
	        rez3 = new Reservation(2,3,user,accommodation2,5000,start15,end, 10, ReservationState.TO_BE_CANCELLED,0);
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
	public void testCancellation()
	{

		ksession.insert(rez1);
		ksession.insert(rez2);
		ksession.insert(rez3);
		
		ksession.fireAllRules();
     

        assertThat(50, equalTo(rez1.getCancellationPrecentage()));
        assertThat(30, equalTo(rez3.getCancellationPrecentage()));

        
        assertThat(ReservationState.CANCELLED, equalTo(rez1.getState()));
        assertThat(ReservationState.CANCELLED, equalTo(rez2.getState()));
        assertThat(ReservationState.CANCELLED, equalTo(rez3.getState()));

	}

	
	@Test
	public void testCanellation2()
	{
		ksession.insert(rez3);
		ksession.insert(rezTemp1);
		ksession.insert(rezTemp2);
		ksession.insert(rezTemp3);
		
		
		ksession.fireAllRules();
        //1 for free, 1 for calculating successful years, 1 for setting the status 
        //assertThat(3, equalTo(firedRules));
        assertThat(75,equalTo(rez3.getCancellationPrecentage()));
        assertThat(ReservationState.CANCELLED, equalTo(rez3.getState()));
		
	}
	
	@Test
	public void testCanellation3()
	{
		
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try
        {
        	rez1.setStartDate(format.parse("2019/05/15"));
        	rez1.setEndDate(format.parse("2019/05/16"));
        	
        	rez2.setStartDate(format.parse("2019/05/17"));
        	rez2.setEndDate(format.parse("2019/05/18"));
        	
        	rez1.setState(ReservationState.ACTIVE);
        	rez2.setState(ReservationState.ACTIVE);
        	
        }
        catch(Exception e)
        {
        	System.err.println("Wrong date format: " + e);
        }
		
		ksession.insert(rez1);
		ksession.insert(rez2);
		ksession.insert(rez3);
		
		ksession.insert(rezTemp1);
		ksession.insert(rezTemp2);
		ksession.insert(rezTemp3);
		
        ksession.fireAllRules();
        //1 for 30%, 1 for 55%, 1 for calculating successful years, 1 for setting the status 
        //assertThat(4, equalTo(firedRules));
        assertThat(75,equalTo(rez3.getCancellationPrecentage()));
        assertThat(ReservationState.CANCELLED, equalTo(rez3.getState()));


	}
	
}
