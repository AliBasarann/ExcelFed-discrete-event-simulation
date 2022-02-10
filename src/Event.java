
public class Event implements Comparable<Event>{
	double startTime;
	double duration;
	String type;
	int eventId;
	int playerId;
	
	public Event(int eventId, int playerId, double startTime, double duration, String type) {
		this.playerId = playerId;
		this.eventId = eventId;
		this.startTime = startTime;
		this.duration = duration;
		this.type = type;
	}
	
	public int compareTo(Event e){
		if (this.startTime == e.startTime) {
			return (this.playerId - e.playerId);
					
		}else if(this.startTime >= e.startTime) {
			return 1;
		}else {
			return -1;
		}
	}
}
