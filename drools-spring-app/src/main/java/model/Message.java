package model;

public class Message 
{
	private String text;
	private int senderid;
	private int reciverid;
	private boolean opened;
	


	public Message(String text, int senderid, int reciverid)
	{
		super();
		this.text = text;
		this.senderid = senderid;
		this.reciverid = reciverid;
	}
	
	public Message(String t)
	{
		text = t;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getSenderid() {
		return senderid;
	}
	public void setSenderid(int senderid) {
		this.senderid = senderid;
	}
	public int getReciverid() {
		return reciverid;
	}
	public void setReciverid(int reciverid) {
		this.reciverid = reciverid;
	}
	
	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}
}
