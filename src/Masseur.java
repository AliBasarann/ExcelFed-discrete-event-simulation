
public class Masseur implements Comparable<Masseur>{
	int id;
	double endTime;
	boolean isBusy = false;
	public Masseur(int id) {
		this.id = id;
	}
	public int compareTo(Masseur m){
		if(this.endTime >= m.endTime) {
			return 1;}
			else { return -1;}
	}
}
