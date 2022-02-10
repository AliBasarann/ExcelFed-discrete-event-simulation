
public class Training {
	Player player;
	double startTime;
	double activationTime;
	double duration;
	int eventId;
	double endTime;
	double waitingTime = activationTime - startTime;
	TrainingCoach coach = null;
	
	public Training(Player playerId, double startTime, double duration, int eventId) {
		this.eventId = eventId;
		this.player = playerId;
		this.startTime = startTime;
		this.duration = duration;
	}
}
