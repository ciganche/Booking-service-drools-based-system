package util;

public class AgentGlobalDto 
{
	private String spam;
	private String startPeriod;
	private String endPeriod;
	
	
	
	public AgentGlobalDto(String spam, String startPeriod, String endPeriod)
	{
		this.spam = spam;
		this.startPeriod = startPeriod;
		this.endPeriod = endPeriod;
	}
	
	public AgentGlobalDto()
	{
		
	}
	
	public String getSpam() {
		return spam;
	}
	public void setSpam(String spam) {
		this.spam = spam;
	}
	public String getStartPeriod() {
		return startPeriod;
	}
	public void setStartPeriod(String startPeriod) {
		this.startPeriod = startPeriod;
	}
	public String getEndPeriod() {
		return endPeriod;
	}
	public void setEndPeriod(String endPeriod) {
		this.endPeriod = endPeriod;
	}
	
	
}
