/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Adiacenza;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaLista"
    private Button btnCreaLista; // Value injected by FXMLLoader

    @FXML // fx:id="btnMassimo"
    private Button btnMassimo; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCanzone"
    private ComboBox<Track> cmbCanzone; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<Genre> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtMemoria"
    private TextField txtMemoria; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void btnCreaLista(ActionEvent event) {
    	
    	txtResult.clear();
    	Track c = cmbCanzone.getValue();
    	int m;
    	
    	if(c == null) {
    		txtResult.appendText("Seleziona una canzona prima di procedere con la ricerca!\n");
    		return;
    	}
    	
    	try{
    		m = Integer.parseInt(txtMemoria.getText());
    	}catch(NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.appendText("Inserisci un valore numerico intero!\n");
    		return;
    	}
    	
    	if(model.getGrafo() == null) {
    		txtResult.setText("Creare il grafo prima di procedere");
    		return;
    	}
    	
    	List<Track> soluzione = this.model.cercaLista(c, m); 
    	
    	txtResult.appendText(String.format("Lista canzoni migliori - (%d tracce)\n", soluzione.size()));
    	for(Track t : soluzione) {
    		txtResult.appendText(String.format("%s\n", t));
    	}
    	
    	

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	cmbCanzone.getItems().clear();
    	txtMemoria.clear();
    	
    	Genre genere = this.cmbGenere.getValue();
    	if(genere ==  null) {
    		txtResult.setText("Seleziona un genere dalla tendina prima di procedere!\n");
    		return;
    	}
    	
    	model.creaGrafo(genere);
    	txtResult.appendText("Grafo creato!\n");
    	txtResult.appendText(String.format("# VERTICI: %d\n", model.nVertici()));
    	txtResult.appendText(String.format("# ARCHI: %d\n", model.nArchi()));
    	
    	this.cmbCanzone.getItems().addAll(this.model.getVertici());
    	
    }

    @FXML
    void doDeltaMassimo(ActionEvent event) {
    	if(model.getGrafo() == null) {
    		txtResult.setText("Creare il grafo prima di procedere");
    		return;
    	}
    	
    	List<Adiacenza> list = model.getArchiPesoMax();
    	for(Adiacenza a : list) {
    		txtResult.appendText(a +"\n");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaLista != null : "fx:id=\"btnCreaLista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnMassimo != null : "fx:id=\"btnMassimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCanzone != null : "fx:id=\"cmbCanzone\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMemoria != null : "fx:id=\"txtMemoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.cmbGenere.getItems().addAll(this.model.getGeneri());
    }

}
