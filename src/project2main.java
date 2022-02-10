import java.util.*;

import java.io.*;

public class project2main {

	public static void main(String[] args) throws FileNotFoundException {
		Locale.setDefault(Locale.US);
		Scanner in = new Scanner(new File(args[0]));
		PrintStream out = new PrintStream(new File(args[1]));
		
		int numberOfPlayers = in.nextInt();
		HashMap<Integer, Player> playerArray = new HashMap<>();
		PriorityQueue<Massage> massageQueue = new PriorityQueue<>();
		PriorityQueue<Physiotherapy> physiotherapyQueue = new PriorityQueue<>();
		Queue<Training> trainingQueue = new LinkedList<>();
		LinkedList<Training> activeTrainingList = new LinkedList<>();
		LinkedList<Massage> activeMassageList = new LinkedList<>();
		LinkedList<Physiotherapy> activeTherapyList = new LinkedList<>();
		LinkedList<Physiotherapist> therapistQueue = new LinkedList<>();
		LinkedList<TrainingCoach> coachQueue = new LinkedList<>();
		LinkedList<Masseur> masseurQueue = new LinkedList<>();
		PriorityQueue<Physiotherapist> busyTherapistQueue = new PriorityQueue<>();
		PriorityQueue<TrainingCoach> busyCoachQueue = new PriorityQueue<>();
		PriorityQueue<Masseur> busyMasseurQueue = new PriorityQueue<>();
		PriorityQueue<Event> eventQueue = new PriorityQueue<>();
		
		
		for (int i=0; i<numberOfPlayers ; i++) {
			int id = in.nextInt();
			int skill = in.nextInt();
			Player player = new Player(id,skill);
			playerArray.put(id, player);
		}
		
		int numberOfEvents = in.nextInt();
		int eventId = 0;
		// event olusturma
		for (int i=0; i<numberOfEvents ; i++) {
			String temp = in.next();
			if (((int) temp.charAt(0) == (int)"m".charAt(0))) {
				int id = in.nextInt();
				double start = in.nextDouble();
				double duration = in.nextDouble();
				Event event = new Event(eventId, id, start,duration,"m");
				eventQueue.add(event);
				eventId += 1;
			}
			else if (((int) temp.charAt(0) == (int)"t".charAt(0))) {
				int id = in.nextInt();
				double start = in.nextDouble();
				double duration = in.nextDouble();
				Event event = new Event(eventId, id, start,duration,"t");
				eventQueue.add(event);
				eventId += 1;
			}
		}
		int numberOfPhysiotherapists = in.nextInt();
		int idOfPhysiotherapists = 0;
		//fizyoterapist olusturma
		for (int i=0; i<numberOfPhysiotherapists ; i++) {
			float serviceTime = in.nextFloat();
			Physiotherapist physiotherapist = new Physiotherapist(serviceTime, idOfPhysiotherapists);
			idOfPhysiotherapists += 1;
			therapistQueue.add(physiotherapist);
		}
		int numberOfCoaches = in.nextInt();
		int coachId = 0;
		int numberOfMasseurs = in.nextInt();
		int masseurId = 0;
		
		for(int i = 0; i<numberOfCoaches; i++) {
			TrainingCoach coach = new TrainingCoach(coachId);
			coachQueue.add(coach);
			coachId += 1;
		}
		for(int i = 0; i<numberOfMasseurs; i++) {
			Masseur masseur = new Masseur(masseurId);
			masseurQueue.add(masseur);
			masseurId += 1;
		}

		int massageCount = 0;
		int trainingCount = 0;
		int maxTrainingLength = 0;
		int maxMassageLength = 0;
		int maxTherapyLength = 0;
		double globalTime = 0;
		double globalMassageTime = 999999999;
		double globalTrainingTime = 999999999;
		double globalTherapyTime = 999999999;
		double globalEventTime = 999999999;

		if(!eventQueue.isEmpty()) {
			globalTime = eventQueue.peek().startTime;
		}
		while (!massageQueue.isEmpty() || !physiotherapyQueue.isEmpty()|| !trainingQueue.isEmpty() || !eventQueue.isEmpty() || !busyCoachQueue.isEmpty() || !busyTherapistQueue.isEmpty() || !busyMasseurQueue.isEmpty()) {
			//her seferde 1 event cikar
			if(!eventQueue.isEmpty() && globalTime == eventQueue.peek().startTime) {
				if (eventQueue.peek().type == "m") {
					Event e = eventQueue.poll();
					Player p = playerArray.get(e.playerId);
					if(p.massageCount > 0) {
						if(p.isAvailable) {
							Massage massage = new Massage(p, e.startTime, e.duration, e.eventId );
							massageCount += 1;
							p.massageCount -= 1;
							massageQueue.add(massage);
							p.isAvailable = false;
						}else {
							p.canceledAttempts += 1;}
					}else {
						p.invalidAttempts += 1;
					}
				} else if (eventQueue.peek().type == "t") {
					Event e = eventQueue.poll();
					Player p = playerArray.get(e.playerId);
					if(p.isAvailable) {
						Training training = new Training(p, e.startTime, e.duration, e.eventId );
						trainingCount += 1;
						trainingQueue.add(training);
						p.isAvailable = false;
					}else {
						p.canceledAttempts += 1;
					}
				}
			}
			
			Iterator<Physiotherapy> pIterator = activeTherapyList.iterator();
			Iterator<Training> tIterator = activeTrainingList.iterator();
			Iterator<Massage> mIterator = activeMassageList.iterator();
			
			while(mIterator.hasNext()) {
				Massage m = mIterator.next();
				if (globalTime >= m.endTime) {
					m.player.isAvailable = true;
					m.masseur.isBusy = false;
					busyMasseurQueue.poll();
					mIterator.remove();
				}
			}
			while(tIterator.hasNext()) {
				Training t = tIterator.next();
				if (globalTime >= t.endTime) {
					Physiotherapy p = new Physiotherapy(t.player.id, t.endTime, t.duration);
					physiotherapyQueue.add(p);
					t.coach.isBusy = false;
					busyCoachQueue.poll();
					tIterator.remove();
				}
			}
			while(pIterator.hasNext()) {
				Physiotherapy p = pIterator.next();
				if (globalTime >= p.endTime) {
					playerArray.get(p.playerId).isAvailable = true;
					p.therapist.isBusy = false;
					busyTherapistQueue.poll();
					pIterator.remove();
				}
			}
			
			if(!trainingQueue.isEmpty()) {
				for(TrainingCoach c : coachQueue)	{
					if(!c.isBusy && !trainingQueue.isEmpty()) {
						Training t = trainingQueue.poll();
						if(c.endTime > t.startTime) {
							t.activationTime = c.endTime;
						}else {
							t.activationTime = t.startTime;
						}
						t.coach = c;
						c.isBusy = true;
						t.endTime = t.activationTime + t.duration;
						c.endTime = t.endTime;
						busyCoachQueue.add(c);
						t.waitingTime  = t.activationTime - t.startTime;
						activeTrainingList.add(t);
						t.player.training(t);
					}
				}
			}
			if(!massageQueue.isEmpty()) {
				for(Masseur msr : masseurQueue)	{
					if(!msr.isBusy && !massageQueue.isEmpty()) {
						Massage m = massageQueue.poll();
						if(msr.endTime > m.startTime) {
							m.activationTime = msr.endTime;
						}else {
							m.activationTime = m.startTime;
						}
						m.masseur = msr;
						msr.isBusy = true;
						m.endTime = m.activationTime + m.duration;
						msr.endTime = m.endTime;
						m.waitingTime  = m.activationTime - m.startTime;
						busyMasseurQueue.add(msr);
						activeMassageList.add(m);
						m.player.massage(m);
					}
				}
			}
			if(!physiotherapyQueue.isEmpty()) {
				for(Physiotherapist t : therapistQueue)	{
					if(!t.isBusy && !physiotherapyQueue.isEmpty()) {
						Physiotherapy p = physiotherapyQueue.poll();
						if(t.endTime > p.startTime) {
							p.activationTime = t.endTime;
						}else {
							p.activationTime = p.startTime;
						}
						p.therapist = t;
						t.isBusy = true;
						p.duration = t.serviceTime;
						p.endTime = p.activationTime + p.duration;
						t.endTime = p.endTime;
						busyTherapistQueue.add(t);
						p.waitingTime  = p.activationTime - p.startTime;
						activeTherapyList.add(p);
						playerArray.get(p.playerId).physiotherapy(p);
					}
				}
			}
			
			if (maxTherapyLength < physiotherapyQueue.size()) {
				maxTherapyLength = physiotherapyQueue.size();
			}
			if (maxMassageLength < massageQueue.size()) {
				maxMassageLength = massageQueue.size();
			}
			if (maxTrainingLength < trainingQueue.size()) {
				maxTrainingLength = trainingQueue.size();
			}
			
			
			if(!busyCoachQueue.isEmpty()) {
				globalTrainingTime = busyCoachQueue.peek().endTime;
			}else {
				globalTrainingTime = 999999999;
			}
			if(!busyMasseurQueue.isEmpty()) {
				globalMassageTime = busyMasseurQueue.peek().endTime;
			}
			else {
				globalMassageTime = 999999999;
			}
			if(!eventQueue.isEmpty()) {
				globalEventTime = eventQueue.peek().startTime;
			}else {
				globalEventTime = 999999999;
			}
			if(!busyTherapistQueue.isEmpty()) {
				globalTherapyTime = busyTherapistQueue.peek().endTime;
			}else {
				globalTherapyTime = 999999999;
			}
			
			double[] nums={globalTrainingTime,globalEventTime,globalTherapyTime,globalMassageTime};
			Arrays.sort(nums);
			if(nums[0] != 999999999) {
				globalTime = nums[0];
			}

		}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
		double trainingWaitingTime = 0;
		double massageWaitingTime = 0;
		double therapyWaitingTime = 0;
		double totalTrainingTime = 0;
		double totalMassageTime = 0;
		double totalTherapyTime = 0;
		double maxTherapyWaitingTime = 0;
		int maxTherapyId = 0;
		double minMassageWaitingTime = 999999999;
		int minMassageId = 0;
		int canceledAttempts = 0;
		int invalidAttempts = 0;
		
		for(int i = 0; i<numberOfPlayers; i++) {
			Player p = playerArray.get(i);
			trainingWaitingTime += p.trainingWaitingTime;
			massageWaitingTime += p.massageWaitingTime;
			therapyWaitingTime += p.therapyWaitingTime;
			totalTrainingTime += p.trainingTime;
			totalMassageTime += p.massageTime;
			totalTherapyTime += p.therapyTime;
			canceledAttempts += p.canceledAttempts;
			invalidAttempts += p.invalidAttempts;
			if(p.therapyWaitingTime> maxTherapyWaitingTime) {
				maxTherapyWaitingTime = p.therapyWaitingTime;
				maxTherapyId = p.id;
			}
			if(p.massageCount == 0 && p.massageWaitingTime < minMassageWaitingTime) {
				minMassageWaitingTime = p.massageWaitingTime;
				minMassageId = p.id;
			}
			
		}
		if(minMassageWaitingTime == 999999999) {
			minMassageWaitingTime = -1;
			minMassageId = -1;
		}
		if(trainingCount==0) {
			trainingCount = 1;
		}
		if(massageCount==0) {
			massageCount = 1;
		}
		
		
		out.println(maxTrainingLength);
		out.println(maxTherapyLength);
		out.println(maxMassageLength);
		out.println(String.format("%.3f", trainingWaitingTime/(double)trainingCount));
		out.println(String.format("%.3f", therapyWaitingTime/(double)trainingCount));
		out.println(String.format("%.3f", massageWaitingTime/(double)massageCount));
		out.println(String.format("%.3f", totalTrainingTime/(double)trainingCount));
		out.println(String.format("%.3f", totalTherapyTime/(double)trainingCount));
		out.println(String.format("%.3f", totalMassageTime/(double)massageCount));
		out.println(String.format("%.3f", (totalTherapyTime+totalTrainingTime+therapyWaitingTime+trainingWaitingTime)/(double)trainingCount)); // turnaround d�zenle
		out.print(maxTherapyId + " "); out.println(String.format("%.3f",maxTherapyWaitingTime));
		out.print(minMassageId + " ");
		if(minMassageWaitingTime == -1) {
			out.println(-1);
		}else {out.println(String.format("%.3f",minMassageWaitingTime));}
		
		out.println(invalidAttempts);
		out.println(canceledAttempts);
		out.println(String.format("%.3f",globalTime));
	
	
	
	
	
	
	
	
	
	
	
	}

}















