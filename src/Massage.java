
public class Massage implements Comparable<Massage>{
	Player player;
	double startTime;
	double duration;
	double activationTime;
	int eventId;
	double endTime;
	double waitingTime;
	Masseur masseur = null;
	
	public Massage(Player player, double startTime, double duration, int eventId) {
		this.eventId = eventId;
		this.player = player;
		this.startTime = startTime;
		this.duration = duration;
	}
	
	public int compareTo(Massage m){
		if (this.player.skillLevel == m.player.skillLevel) {
			if(this.startTime == m.startTime) {
				return (this.player.id- m.player.id);
			}else if(this.startTime > m.startTime) {
				return 1;
			}else {
				return -1;
			}
		}else if(this.player.skillLevel > m.player.skillLevel){
			return -1;
		}else {
			return 1;
		}
	}
}
