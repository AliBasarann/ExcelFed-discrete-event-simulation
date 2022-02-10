
public class Player implements Comparable<Player>{
	
	int id;
	int skillLevel;
	int massageCount = 3;
	int invalidAttempts = 0;
	int canceledAttempts = 0;
	double trainingTime = 0;
	double massageTime = 0;
	double therapyTime = 0;
	double trainingWaitingTime = 0;
	double massageWaitingTime = 0;
	double therapyWaitingTime = 0;
	double endTime = 0;
	boolean isAvailable = true;
	
	public Player(int id, int skillLevel) {
		this.skillLevel = skillLevel;
		this.id = id;
	}
	
	public void massage(Massage massage) {
			massageWaitingTime += massage.waitingTime;
			massageTime += massage.duration;

	}
	public void training(Training t) {
		this.trainingTime += t.duration;
		this.trainingWaitingTime += t.waitingTime;
		return;
	}
	
	
	public void physiotherapy(Physiotherapy p) {
		this.therapyTime += p.duration;
		this.therapyWaitingTime += p.waitingTime;
		return;
	}
	
	public int compareTo(Player p){
		if(this.endTime >= p.endTime) {
			return 1;}
			else { return -1;}
	}
	
	
}
