package controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import database.Database;
import model.Agent;
import model.Message;
import model.User;

@RestController
@RequestMapping(value="rest/accommodation")
public class AccommodationController
{

	

	@RequestMapping(
			value = "/",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllAccommodation()
	{
		return ResponseEntity.accepted().body(Database.getAllAccommodations());	
	}
	
	@RequestMapping(
			value = "/message/{id}/{body}/{user}",
			method = RequestMethod.PUT)
	public void sendMessage(@PathVariable("id") Long id, @PathVariable("body") String message,  @PathVariable("user") Long userId)
	{
		User user = Database.findUserByID(userId);
		
		Agent agent = Database.findAgentByAccommodationId(id);
		agent.getInbox().add(new Message( user.getName().toUpperCase() + " " + user.getLastname().toUpperCase() + ": " + message));
	}
	
	
}
