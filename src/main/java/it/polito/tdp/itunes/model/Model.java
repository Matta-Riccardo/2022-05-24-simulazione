package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao;
	
	private Graph<Track, DefaultWeightedEdge> grafo;
	private Map<Integer, Track> idMap;
	
	private List<Track> soluzioneMigliore;
	
	public List<Track> cercaLista(Track c, int m){
		Set<Track> componenteConnessa;
		ConnectivityInspector<Track, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo);
		componenteConnessa = ci.connectedSetOf(c);
		
		List<Track> canzoniValide = new ArrayList<>();
		canzoniValide.add(c);
		componenteConnessa.remove(c);
		canzoniValide.addAll(componenteConnessa);
		//Ora avrò la lista di canzoniValide dove ho forzato in posizione zero la canzone preferita c
		
		//Scorrerò le canzoni in ordine associando il livello all'indice dell' ArrayList, in questo modo per ogni livello (ovvero per ogni inserimento)
		//mando avanti la ricorsione con e senza quell'inserimento, in modo tale da non dover riprovare più volte delle soluzioni parziali con gli stessi
		//elementi già provati in posizioni della lista parziale differenti
		
		List<Track> parziale = new ArrayList<>();
		soluzioneMigliore = new ArrayList<Track>();
		parziale.add(c);
		
		cerca(parziale, canzoniValide, m, 1);
		
		return soluzioneMigliore;
	}
	
	private void cerca(List<Track> parziale, List<Track> canzoniValide, int m, int l) {
		
		if(sommaMemoriaUsata(parziale) > m) {
			return;
		}
		
		if(parziale.size() > soluzioneMigliore.size()) {
			soluzioneMigliore = new ArrayList<>(parziale);
		}
		
		if(l == canzoniValide.size()) {
			return;
		}
		
		parziale.add(canzoniValide.get(l));
		cerca(parziale, canzoniValide, m, l+1);
		parziale.remove(canzoniValide.get(l));
		cerca(parziale, canzoniValide, m, l+1);
	}
	
	
//	public List<Track> cercaLista(Track c, int m){
//		
//		//Devo prendere la componente connessa di c:
//		//Posso esplorare il grafo o usare un ConnectivityInspector
//		//In questo caso essendo che tutte le canzoni di formato uguale 
//		//sono direttamente connessenpotevo usare il metodo --> Graphs.neighborListOf(this.grafo, c);
//		
//		List<Track> canzoniValide = new ArrayList<Track>();
//		ConnectivityInspector<Track, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo);
//		canzoniValide.addAll(ci.connectedSetOf(c));
//		
//		List<Track> parziale = new ArrayList<>();
//		parziale.add(c);
//		
//		soluzioneMigliore = new ArrayList<Track>();
//		cerca(parziale, canzoniValide, m);
//		
//		return soluzioneMigliore;
//	}
//	
//	private void cerca(List<Track> parziale, List<Track> canzoniValide, int m) {
//		
//		if(parziale.size() > soluzioneMigliore.size()) {
//			soluzioneMigliore = new ArrayList<>(parziale);
//		}
//		
//		for(Track t : canzoniValide) {
//			if(!parziale.contains(t) && (sommaMemoriaUsata(parziale) + t.getBytes()) <= m) {
//				parziale.add(t);
//				cerca(parziale, canzoniValide, m);
//				parziale.remove(parziale.size()-1);
//			}
//		}
//		
//	}
	
	private int sommaMemoriaUsata(List<Track> lista) {
		int somma = 0;
		for(Track t : lista) {
			somma += t.getBytes();
		}
		
		return somma;
	}

	public Model() {
		dao = new ItunesDAO();
		idMap = new HashMap<>();
		
		this.dao.getAllTracks(idMap);
		
	}
	
	public void creaGrafo(Genre genere) {
		//Grafo
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(genere.getGenreId(), idMap));
		
		System.out.println("Grafo creato!");
		System.out.println("# VERTICI: " + nVertici());
		
		
		//Archi
		for(Adiacenza a : this.dao.getArchi(genere.getGenreId(), idMap)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getT1(), a.getT2(), a.getPeso());
		}
		System.out.println("# ARCHI: " + nArchi());
	}
	
	public List<Adiacenza> getArchiPesoMax(){
		List <Adiacenza> result = new ArrayList<Adiacenza>();
		int pesoMax = 0;
		
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e) > pesoMax) {
				pesoMax = (int)(this.grafo.getEdgeWeight(e));
			}
		}
		
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e) == pesoMax) {
				
				Adiacenza adiacenza = new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), (int)(this.grafo.getEdgeWeight(e)));
				result.add(adiacenza);
				System.out.println(adiacenza);
			}
		}
		return result;
		
//		List <Adiacenza> result1 = new ArrayList<Adiacenza>();
//		int max = 0;
//		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
//			int peso = (int) this.grafo.getEdgeWeight(e);
//			if(peso > max) {
//				result1.clear();
//				result1.add(new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), (int)(this.grafo.getEdgeWeight(e))));
//				max = peso;
//			}else if(peso == max){
//				result1.add(new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), (int)(this.grafo.getEdgeWeight(e))));
//			}
//		}
//		return result1;
		
	}
	
	public List<Genre> getGeneri(){
		return dao.getAllGenres();
	}
	
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Graph<Track, DefaultWeightedEdge> getGrafo(){
		return this.grafo;
	}
	
	public List<Track> getVertici(){
		return new ArrayList<Track>(this.grafo.vertexSet());
	}
}
