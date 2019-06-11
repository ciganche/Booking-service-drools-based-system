package controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import database.Database;
import model.User;

@RestController
@RequestMapping(value="rest/")
public class UserController {

	

	@RequestMapping(
			value = "/user/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@PathVariable("id") Long id)
	{

		User u = Database.findUserByID(id);
		
		
		return ResponseEntity.accepted().body(u);

		
	}
}
