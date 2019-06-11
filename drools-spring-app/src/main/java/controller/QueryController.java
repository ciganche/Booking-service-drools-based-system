package controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import database.Database;
import model.Query;

@RestController
@RequestMapping(value="rest/")
public class QueryController 
{
	@RequestMapping(
			value = "query/{city}/{start}/{end}",
			method = RequestMethod.POST)
	public void insertRule(@PathVariable("city") long cityId, @PathVariable("start") String start, @PathVariable("end") String end)
	{			
	
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		try
		{

			Date startDate = format.parse(start);
			Date endDate = format.parse(end);
			
			Query query = new Query(cityId, startDate,endDate);
			Database.globalSession.insert(query);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}

    }
}
