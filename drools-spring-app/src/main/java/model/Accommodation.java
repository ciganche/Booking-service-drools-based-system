package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Accommodation 
{
	private long id;
	private String name;
	private String description;
	private Location location;
	private AccommodationType type;
	private int capacity;
	private int cancellationPeriod;
	private double ratingAvg;
	private Set<Amenity> amenities;
	private double price;
	private Category category;
	private List<Reservation> reservations;
	private Long agentId;
	private BlockDiscountStatus blockDiscountStatus;

	
	private int regular;
	private int extraPersonPrecentage;
	private int regularDiscount;

	public Accommodation(long id,String name, String description, Location location, AccommodationType type, int capacity,
			int cancellationPeriod, double ratingAvg, double price) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.location = location;
		this.type = type;
		this.capacity = capacity;
		this.cancellationPeriod = cancellationPeriod;
		this.ratingAvg = ratingAvg;
		this.price = price;
		amenities = new HashSet<Amenity>();
		this.category = Category.BRONZE;
		this.reservations = new ArrayList<Reservation>();
		blockDiscountStatus = BlockDiscountStatus.NONE;
		
		regular = 0;
		extraPersonPrecentage = 0;
		regularDiscount = 0;
	}
	
	
	public BlockDiscountStatus getBlockDiscountStatus() {
		return blockDiscountStatus;
	}


	public void setBlockDiscountStatus(BlockDiscountStatus blockDiscountStatus) {
		this.blockDiscountStatus = blockDiscountStatus;
	}

	public Long getAgentId() {
		return agentId;
	}


	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	
	public List<Reservation> getReservations() {
		return reservations;
	}
	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public AccommodationType getType() {
		return type;
	}
	public void setType(AccommodationType type) {
		this.type = type;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getCancellationPeriod() {
		return cancellationPeriod;
	}
	public void setCancellationPeriod(int cancellationPeriod) {
		this.cancellationPeriod = cancellationPeriod;
	}
	public double getRatingAvg() {
		return ratingAvg;
	}
	public void setRatingAvg(double ratingAvg) {
		this.ratingAvg = ratingAvg;
	}
	public Set<Amenity> getAmenities() {
		return amenities;
	}
	public void setAmenities(Set<Amenity> amenities) {
		this.amenities = amenities;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
/*	
	public Agent getAgent() {
		return agent;
	}


	public void setAgent(Agent agent) {
		this.agent = agent;
	}
*/
	public int getRegular() {
		return regular;
	}
	public void setRegular(int regular) {
		this.regular = regular;
	}
	public int getExtraPersonPrecentage() {
		return extraPersonPrecentage;
	}
	public void setExtraPersonPrecentage(int extraPersonPrecentage) {
		this.extraPersonPrecentage = extraPersonPrecentage;
	}
	public int getRegularDiscount() {
		return regularDiscount;
	}
	public void setRegularDiscount(int regularDiscount) {
		this.regularDiscount = regularDiscount;
	}
	
}
