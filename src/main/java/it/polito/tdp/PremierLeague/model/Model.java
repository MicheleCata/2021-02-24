package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer, Player> idMap;
	
	private Simulator sim;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap= new HashMap <Integer, Player>();
		dao.listAllPlayers(idMap);
	}
	
	public void creaGrafo(Match m) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		//aggiungo i vertici
		Graphs.addAllVertices(grafo, dao.getVertici(m, idMap));
		//aggiungo gli archi
		
		for (Adiacenza a : dao.getAdiacenze(m, idMap)) {
			if (a.getPeso()>=0) {
				//p1 meglio di p2
				if (grafo.containsVertex(a.getP1())&& grafo.containsVertex(a.getP2()))
					Graphs.addEdgeWithVertices(grafo, a.getP1(), a.getP2(), a.getPeso() );
			}else {
				//p2 meglio di p1
				if (grafo.containsVertex(a.getP1())&& grafo.containsVertex(a.getP2()))
					Graphs.addEdgeWithVertices(grafo, a.getP2(), a.getP1(), -a.getPeso());
			}
		}
		
		System.out.format("Grafo creato con %d vertici e %d archi\n",
 				this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Match> getMatches() {
		 List<Match> matches = dao.listAllMatches();
		 Collections.sort(matches, new Comparator<Match>() {

			@Override
			public int compare(Match o1, Match o2) {
				
				return o1.matchID.compareTo(o2.matchID);
			}
			 
		 });
		return matches;
	}
	
	public GiocatoreMigliore getMigliore() {
		if(grafo ==null) {
			return null;
		}
		
		Player best = null;
		Double maxDelta = (double) Integer.MIN_VALUE;
		
		for (Player p: grafo.vertexSet()) {
			// calcolo somma dei pesi degli archi uscenti
			double pesoUscente =0.0;
			for (DefaultWeightedEdge edge : this.grafo.outgoingEdgesOf(p)) {
				pesoUscente += grafo.getEdgeWeight(edge);
			}
			// calcolo somma dei pesi degli archi entranti
			double pesoEntrante =0.0;
			for (DefaultWeightedEdge edge : this.grafo.incomingEdgesOf(p)) {
				pesoEntrante += grafo.getEdgeWeight(edge);
			}
			
			double delta = pesoUscente - pesoEntrante;
			if (delta> maxDelta) {
				best = p;
				maxDelta = delta;
			}
		}
		
		return new GiocatoreMigliore(best, maxDelta);
		
	}

	public Graph<Player, DefaultWeightedEdge> getGrafo() {
		
		return this.grafo;
	}

	public String getTeamBestP(Match m) {
		
		return dao.getTeamBestP(this.getMigliore().getP(),m);
	}
	
	public void simula(int n, Match m) {
		sim = new Simulator(n,grafo,m);
		sim.run();
	}
	
	public Integer getRossiCasa() {
		return sim.getEspulsiCasa();
	}
	public Integer getRossiOspiti() {
		return sim.getEspulsiOspite();
	}
	public Integer getGoalCasa() {
		return sim.getGoalCasa();
	}
	
	public Integer getGoalOspite() {
		return sim.getGoalOspite();
	}
}
