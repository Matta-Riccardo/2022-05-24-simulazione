package it.polito.tdp.itunes.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		Model m = new Model();
		Genre g = new Genre(2, "Jazz");
		
		m.creaGrafo(g);
		
		List<Adiacenza> list = m.getArchiPesoMax();
		for(Adiacenza a : list) {
			System.out.println(a);
		}
		

	}

}
