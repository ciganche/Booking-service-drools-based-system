package drools.spring.example;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

import org.kie.api.runtime.KieContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import database.Database;


@SpringBootApplication
@ComponentScan({"controller"})
@Configuration
@EnableScheduling
public class SampleApp {
	
  	
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SampleApp.class, args); 

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
		
		Database db = new Database();
		db.init();
		//db.fillAccommodationCategoryData();
		db.fillWithMockData();
		
	}
	
	@Scheduled(fixedDelay = 10000, initialDelay = 10000)
	public void scheduleFixedRateWithInitialDelayTask() 
	{
		//always insert a fresh date
		for(org.kie.api.runtime.rule.FactHandle fh : Database.globalSession.getFactHandles())
		{
			if(Database.globalSession.getObject(fh).getClass().equals(java.util.Date.class))
			{
				Database.globalSession.delete(fh);
				Database.globalSession.insert(new Date());
				break;
			}
		}
		//Database.globalSession.getAgenda().getAgendaGroup("MAIN").setFocus();
		Database.globalSession.fireAllRules();
		
		String time = Instant.now().toString().substring(Instant.now().toString().indexOf("T")+1,Instant.now().toString().lastIndexOf("."));
		time = time.replace("Z", "");
		System.out.println(time + " - System rules executed.");
	}
}
