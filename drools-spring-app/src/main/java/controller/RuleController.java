package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.kie.api.runtime.rule.FactHandle;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import database.Database;
import database.Performative;
import model.Accommodation;
import model.User;


@RestController
@RequestMapping(value="rest/")
public class RuleController
{

	

	@RequestMapping(
			value = "/recommendations/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<Accommodation> gimmieRecommendations(@PathVariable("id") Long id)
	{
		
		User user = Database.findUserByID(id);
		
		//remove all objects from session
        for (FactHandle factHandle : user.getKsession().getFactHandles())
        {
        	user.getKsession().delete(factHandle);
        }		
		
        
		//insert facts
		Database.insertIntoSession(user.getKsession(), Performative.ACCOMMODATIONS);

		user.getKsession().setGlobal("user", user);
        
        
        ArrayList<Accommodation> list = new ArrayList<Accommodation>();
        user.getKsession().setGlobal("list", list); 

        user.getKsession().fireAllRules();
        ArrayList<Accommodation> recommendations = (ArrayList<Accommodation>) user.getKsession().getGlobal("list");
        
        return recommendations;
	}
	
	
	@RequestMapping(
			value = "/defineRule",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String insertRule(@RequestBody String rule)
	{				
		/*
        VerifierBuilder vBuilder = VerifierBuilderFactory.newVerifierBuilder();
        Verifier verifier = vBuilder.newVerifier();
        verifier.addResourcesToVerify(new ReaderResource(new StringReader(rule)), ResourceType.DRL);
		
        
        if (verifier.getErrors().size() != 0)  
        {
        	String errorLog = "The rule is invalid:\n";
            for (int i = 0; i < verifier.getErrors().size(); i++) 
            {
                errorLog = errorLog + verifier.getErrors().get(i).getMessage() + "\n";
            }

            return errorLog;
        }
		*/
		
        try
        {
            File file = new File("../drools-spring-kjar/src/main/resources/drools/spring/rules/userSessionRules/user-defined.drl");
            System.out.println("* * * Absolute path: " + file.getAbsolutePath());
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(rule);
            bw.close();
            
            return "Successfully added. Maven kJar Clean  ---> Maven kJar Install";
        }
        catch(Exception e)
        {
             System.out.println(e);
             return "Error while trying to write the file.";
        }
	
    }
}