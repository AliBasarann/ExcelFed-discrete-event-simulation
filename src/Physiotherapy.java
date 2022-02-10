
public class Physiotherapy implements Comparable<Physiotherapy>{
	int playerId;
	double startTime;
	double tiredness;
	double duration;
	double activationTime;
	double endTime;
	double waitingTime = activationTime - startTime;
	Physiotherapist therapist = null;
	
	public Physiotherapy(int playerId, double startTime, double tiredness) {
		this.playerId = playerId;
		this.startTime = startTime;
		this.tiredness = tiredness;
	}
	
	public int compareTo(Physiotherapy p){
		if (this.tiredness == p.tiredness) {
			if(this.startTime == p.startTime) {
				return (this.playerId - p.playerId);
			}else if(this.startTime > p.startTime) {
				return 1;
			}else {
				return -1;
			}
		}else if(this.tiredness > p.tiredness){
			return -1;
		}else {
			return 1;
		}
	}
}
