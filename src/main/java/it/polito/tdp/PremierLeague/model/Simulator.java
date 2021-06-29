package it.polito.tdp.PremierLeague.model;

import java.util.PriorityQueue;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import it.polito.tdp.PremierLeague.model.EventO.EventType;

public class Simulator {
	
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Model model;
	private PriorityQueue<EventO> queue;
	private int T;
	//INPUT
	private int nAzioni; // n azioni da simulare
	private Match match;
	private String teamC;
	private String teamO;
	private String teamBestPlayer;
	private int numGiocatoriC;
	private int numGiocatoriO;
	//OUTPUT
	private int goalCasa;
	private int EspulsiCasa;
	private int goalOspite;
	private int EspulsiOspite;
	
	
	public Simulator (int n, Graph<Player, DefaultWeightedEdge>grafo, Match m ) {
		this.nAzioni= n;
		this.match=m;
		this.grafo=grafo;
		this.T =0;
		this.teamC= m.teamHomeNAME;
		this.teamO=m.teamAwayNAME;
		this.numGiocatoriC=11;
		this.numGiocatoriO=11;
		this.teamBestPlayer=model.getTeamBestP(m);
		this.goalCasa=0;
		this.goalOspite=0;
		this.EspulsiCasa=0;
		this.EspulsiOspite=0;
		this.queue= new PriorityQueue<>();
	}
	
	public void run() {
		for (int i=1; i<=this.nAzioni;i++) {
			int prob= (int) Math.random();
			if (prob<0.5) {
				//si ha un GOAL
				
				queue.add(new EventO(this.T++, EventType.GOAL));
				int diff= this.numGiocatoriC-this.numGiocatoriO;
				if (diff>0) 
					this.goalCasa++;
				else if(diff<0)
					this.goalOspite++;
				else {
					if(this.teamBestPlayer.equals(teamC))
						this.goalCasa++;
					else
						this.goalOspite++;
				}
					
			}
			else if(prob<0.3) {
				queue.add(new EventO(this.T++, EventType.ESPULSIONE));
				int prob1= (int) Math.random();
				if(prob1<0.6) {
					if(this.teamBestPlayer.equals(teamC)) {
						
					}
					else {
						this.numGiocatoriO--;
						this.EspulsiOspite++;
					}
				} else {
				if(this.teamBestPlayer.equals(teamC)) {
					this.numGiocatoriO--;
					this.EspulsiOspite++;
				}
				else {
					this.numGiocatoriC--;
					this.EspulsiCasa++;
				}
			}
			}
			else {
				queue.add(new EventO(this.T++, EventType.INFORTUNIO));
				int prob2= (int) Math.random();
				if(prob2<0.5) {
					this.nAzioni+=2;
				}
				else
					this.nAzioni+=3;
			}
			this.nAzioni--;
		}
	}

	public int getGoalCasa() {
		return goalCasa;
	}

	public int getEspulsiCasa() {
		return EspulsiCasa;
	}

	public int getGoalOspite() {
		return goalOspite;
	}

	public int getEspulsiOspite() {
		return EspulsiOspite;
	}	
	
}
