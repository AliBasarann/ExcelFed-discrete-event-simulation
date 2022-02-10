
public class Physiotherapist implements Comparable<Physiotherapist> {
	int id;
	double serviceTime;
	double endTime;
	boolean isBusy = false;
	public Physiotherapist(double serviceTime, int id) {
		this.serviceTime = serviceTime;
		this.id = id;
	}
	public int compareTo(Physiotherapist p){
		if(this.endTime >= p.endTime) {
			return 1;}
			else { return -1;}
	}
}
