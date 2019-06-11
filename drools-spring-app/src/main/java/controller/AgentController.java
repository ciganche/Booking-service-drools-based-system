package controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.kie.api.runtime.rule.FactHandle;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import database.Database;
import model.Agent;
import util.AgentGlobalDto;


@RestController
@RequestMapping(value="rest/")
public class AgentController 
{

	@RequestMapping(
			value = "/agent/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@PathVariable("id") Long id)
	{

		Agent agent = Database.findAgentByID(id);

		return ResponseEntity.accepted().body(agent);

		
	}
	
	
	@RequestMapping(
			value = "/agent/globals/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getGlobals(@PathVariable("id") Long id)
	{
		
		Agent agent = Database.findAgentByID(id);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		
		Date start = (Date) agent.getKsession().getGlobal("startPeriod");
		Date end = (Date) agent.getKsession().getGlobal("endPeriod");
		
		AgentGlobalDto dto;
		if(start == null || end == null)
		{
			dto = new AgentGlobalDto((String)agent.getKsession().getGlobal("spam"),null,null);
		}
		else
		{
			dto = new AgentGlobalDto((String)agent.getKsession().getGlobal("spam"),format.format(start), format.format(end));
		}
		
		
		
		return ResponseEntity.accepted().body(dto);
		
	}
	
	
	
	@RequestMapping(
			value = "/agent/{num}/{disc}/{extra}/{spam}/{id}",
			method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public void edit(@PathVariable("num") int regularNumber, @PathVariable("disc") int regularDiscount, @PathVariable("extra") int extraPerson, @PathVariable("spam") String spam, @PathVariable("id") Long id)
	{
		Agent agent = Database.findAgentByID(id);
		

		
		agent.getKsession().setGlobal("spam", spam);
		agent.getAccommodation().setRegular(regularNumber);
		agent.getAccommodation().setRegularDiscount(regularDiscount);
		agent.getAccommodation().setExtraPersonPrecentage(extraPerson);
		
		//replace with fresh items in personal session
		FactHandle fh = agent.getKsession().getFactHandle(agent);
		FactHandle fh1 = agent.getKsession().getFactHandle(agent.getAccommodation());
		agent.getKsession().update(fh, agent);
		agent.getKsession().update(fh1,agent.getAccommodation());
		
		
		//replace with fresh items in global session
		FactHandle fh2 = Database.globalSession.getFactHandle(agent.getAccommodation());
		Database.globalSession.update(fh2, agent.getAccommodation());

	}

	@RequestMapping(
			value = "/agent/setPeriod/{start}/{end}/{id}",
			method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public void setPeriod(@PathVariable("start") String startPeriod, @PathVariable("end") String endPeriod, @PathVariable("id") long id)
	{
		Agent agent = Database.findAgentByID(id);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date start = format.parse(startPeriod);
			Date end = format.parse(endPeriod);
			
			agent.getKsession().setGlobal("startPeriod", start);
			agent.getKsession().setGlobal("endPeriod", end);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}


}

