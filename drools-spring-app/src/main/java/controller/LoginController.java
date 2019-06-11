package controller;

import java.util.Date;

import org.kie.api.runtime.rule.FactHandle;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import database.Database;
import database.Performative;
import model.Agent;
import model.Message;
import model.User;

@RestController
@RequestMapping(value="rest/")
public class LoginController {

	

	@RequestMapping(
			value = "/login",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody User userTemp)
	{
		
		User user = Database.logginUser(userTemp.getEmail(), userTemp.getPassword());
		
		if(user != null)
		{
			System.out.println("userrrr");
			return ResponseEntity.accepted().body(user);
		}
			 
		
		
		Agent agent = Database.logginAgent(userTemp.getEmail(), userTemp.getPassword());
		
		if(agent != null)
		{			
			//remove all objects from session
	        for (FactHandle factHandle : agent.getKsession().getFactHandles())
	        {
	        	agent.getKsession().delete(factHandle);
	        }			
	        
	        
			Database.insertIntoSession(agent.getKsession(), Performative.RESERVATIONS);
			Database.insertIntoSession(agent.getKsession(), Performative.ACCOMMODATIONS);
			
			
			
			agent.getKsession().insert(new Date());
			for(Message m : agent.getInbox())
			{
				System.out.println(m.getText());
				agent.getKsession().insert(m);
			}
			
			agent.getKsession().insert(agent);
					

			agent.getKsession().getAgenda().getAgendaGroup("notify").setFocus();
			agent.getKsession().fireAllRules();
			
			
			if((agent.getKsession().getGlobal("startPeriod") != null) && (agent.getKsession().getGlobal("endPeriod") != null))
			{
				agent.getKsession().getAgenda().getAgendaGroup("periodRule").setFocus();
				agent.getKsession().fireAllRules();
				agent.getKsession().getAgenda().getAgendaGroup("notify").setFocus();
			}
		
			return ResponseEntity.accepted().body(agent);
		}
			 
		
		
		return null;
		
	}
}
