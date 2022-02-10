
public class TrainingCoach implements Comparable<TrainingCoach> {
	int id;
	double endTime;
	boolean isBusy = false;
	public TrainingCoach(int id) {
		this.id = id;
	}
	public int compareTo(TrainingCoach c){
		if(this.endTime >= c.endTime) {
			return 1;}
			else { return -1;}
	}
}
