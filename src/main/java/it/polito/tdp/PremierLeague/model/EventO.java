package it.polito.tdp.PremierLeague.model;

public class EventO implements Comparable<EventO>{
	
	public enum EventType {
		GOAL,
		ESPULSIONE,
		INFORTUNIO,
	};

	private int t;
	private EventType type;
	
	public EventO(int t, EventType type) {
		this.t = t;
		this.type = type;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	@Override
	public int compareTo(EventO other) {
		return this.t-other.t;
	}
	
	
	
	
}
