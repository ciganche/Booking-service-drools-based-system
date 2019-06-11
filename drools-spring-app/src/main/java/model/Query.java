package model;

import java.util.Date;

public class Query 
{
	private Long cityId;
	private Date fromDate;
	private Date untilDate;
	
	
	public Query() {
		super();
	}
	
	
	public Query(Long city, Date from, Date until) {
		super();
		this.cityId = city;
		this.fromDate = from;
		this.untilDate = until;
	}

	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long city) {
		this.cityId = city;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date from) {
		this.fromDate = from;
	}
	public Date getUntilDate() {
		return untilDate;
	}
	public void setUntilDate(Date until) {
		this.untilDate = until;
	}
	
}
