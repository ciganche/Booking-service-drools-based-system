package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import database.Database;
import util.KnowledgeSessionHelper;


@JsonIgnoreProperties(value = { "ksession" })
public class Agent 
{
	public KieSession getKsession() {
		return ksession;
	}
	public void setKsession(KieSession ksession) {
		this.ksession = ksession;
	}
	private long id;
	private String email;
	private String password;
	private String name;
	private String lastname;
	private Accommodation accommodation;
	private ArrayList<Message> inbox;
	private KieSession ksession;

	
	public Agent(long id, String email, String password, String name, String lastname, Accommodation accommodation) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.lastname = lastname;
		this.accommodation = accommodation;
		this.inbox = new ArrayList<Message>();
		
		KieBaseConfiguration config = KieServices.Factory.get().newKieBaseConfiguration();
		config.setOption( EventProcessingOption.STREAM );
		   
    	KieBase kb = Database.kjarContainer.newKieBase("notify", config);
        ksession = kb.newKieSession();
        ksession.setGlobal("spam", "povecaj popust");
        
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
        	ksession.setGlobal("startPeriod", new Date());
        	ksession.setGlobal("endPeriod", format.parse("2019-09-11"));
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Accommodation getAccommodation() {
		return accommodation;
	}
	public void setAccommodation(Accommodation accommodation) {
		this.accommodation = accommodation;
	}
	public ArrayList<Message> getInbox() {
		return inbox;
	}
	public void setInbox(ArrayList<Message> inbox) {
		this.inbox = inbox;
	}

	
}
