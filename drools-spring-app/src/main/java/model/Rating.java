package model;

public class Rating 
{
	private long id;
	private double value;
	private Comment comment;
	
	public Rating(long id, double value, Comment comment) 
	{
		super();
		this.id = id;
		this.value = value;
		this.comment = comment;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	
	
	
	
}
