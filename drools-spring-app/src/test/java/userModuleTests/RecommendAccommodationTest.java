package userModuleTests;

import java.util.ArrayList;
import java.util.Date;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.Globals;
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
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class RecommendAccommodationTest 
{

	
	@Test
	public void testRecommend()
	{
		KieServices ks = KieServices.Factory.get();
		KieContainer kc = ks.newKieContainer(ks.newReleaseId("sbnz.integracija", "drools-spring-kjar", "0.0.1-SNAPSHOT"));
		KieBaseConfiguration config = KieServices.Factory.get().newKieBaseConfiguration();
		config.setOption( EventProcessingOption.STREAM );
		KieBase kb = kc.newKieBase("recommend", config);
        KieSession ksession = kb.newKieSession();
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
        Accommodation accommodation1 = new Accommodation(0,"kod Mice","super sve stv", location1, AccommodationType.HOTEL,5,15,5,1000);
        accommodation1.setCategory(Category.SILVER);
        
        Accommodation accommodation2 = new Accommodation(1,"kod Bice","super sve stv", location2, AccommodationType.HOTEL,5,15,5,1000);
        Accommodation accommodation3 = new Accommodation(2,"kod Djeljane","super sve stv", location3, AccommodationType.HOTEL,5,15,5,1000);
        
        Reservation rez1 = new Reservation(0,3,user,accommodation1,5000,new Date(),new Date(), 10, ReservationState.FINISHED,0);
        Reservation rez2 = new Reservation(1,3,user,accommodation2,5000,new Date(),new Date(), 10, ReservationState.FINISHED,0);
        user.getReservations().add(rez1);
        user.getReservations().add(rez2);
        
        assertThat(user.getAverageCityDistance(),equalTo(22.5));
        
        
        ksession.setGlobal("user", user);
        
        Globals globals = ksession.getGlobals();
        System.out.println( globals.getGlobalKeys() );
        
        ArrayList<Accommodation> list = new ArrayList<Accommodation>();
       	ksession.setGlobal("list", list);   
        
        
        globals = ksession.getGlobals();
        System.out.println( globals.getGlobalKeys() );
        
     
        ksession.insert(accommodation1);
        ksession.insert(accommodation2);
        ksession.insert(accommodation3);
        
        ksession.fireAllRules();
        
        @SuppressWarnings("unchecked")
		ArrayList<Accommodation> recommendations = (ArrayList<Accommodation>) ksession.getGlobal("list");
        assertThat(1, equalTo(recommendations.size()));

        for(Accommodation acc : recommendations)
        {
        	System.out.println(acc.getName() + " - " + acc.getLocation().getDistanceFromCity());
        }
        
        
	}
}
 