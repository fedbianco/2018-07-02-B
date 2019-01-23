package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	private ExtFlightDelaysDAO dao;
	private List<Airport> allAirports;
	Graph< Airport ,DefaultWeightedEdge> graph = null; 
	private double distanza;
	List<Airport> airport;

	public Model() {
		this.dao = new ExtFlightDelaysDAO();
		this.allAirports = this.dao.loadAllAirports();
		this.airport = new ArrayList<>();
	}
	
	public List<Airport> getAllAirports(){
		return allAirports;
	}
	
	public void creaGrafo(int numVoliMin) {
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class); 
		for(Airport a : this.allAirports) {
			if(this.dao.getNumAirport(a)>= numVoliMin) {
				graph.addVertex(a);
				this.airport.add(a);
			}
		}
		
		for(Airport air : this.graph.vertexSet()) {
			distanza = 0.0;
			Airport a1 = this.dao.getAirport(air);
			for(Airport airp : this.graph.vertexSet()) {
				Airport a2 = this.dao.getAllAirportDestination(air, airp);
				if(a1!=null && a2!=null && a1!=a2) {
				distanza = this.dao.getWeight(a1, a2);
				Graphs.addEdge(graph, a1, a2, distanza);
				}
			}
		}
		System.out.println("Grafo creato!");
		System.out.println("# Vertici: " + graph.vertexSet().size());
		System.out.println("# Archi: " + graph.edgeSet().size());

	}

	public List<Airport> getAir(){
		return airport;
	}
	
	public List<NeighbourAirports> getNeigh(Airport a){
		List<Airport> neighbour = new ArrayList<Airport>();
		List<NeighbourAirports> na = new ArrayList<NeighbourAirports>();
		double peso;
		neighbour.addAll(Graphs.neighborListOf(graph, a));
		for(Airport air : neighbour) {
			peso = graph.getEdgeWeight(graph.getEdge(a, air));
			na.add(new NeighbourAirports(air ,peso));
		}
		Collections.sort(na);
		return na;
	}
	
	
}
