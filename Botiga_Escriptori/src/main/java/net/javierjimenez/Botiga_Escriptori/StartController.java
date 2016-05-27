package net.javierjimenez.Botiga_Escriptori;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Filters.*;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.event.ActionEvent;

import javafx.scene.control.ComboBox;

public class StartController implements Initializable {
	@FXML
	private TextField nomProd;
	@FXML
	private Button buscarNom;
	@FXML
	private TextField portada;
	@FXML
	private TextField juego;
	@FXML
	private TextField escena1;
	@FXML
	private TextField escena2;
	@FXML
	private TextField nombre;
	@FXML
	private TextField genero;
	@FXML
	private TextField distribuidora;
	@FXML
	private TextField plataforma;
	@FXML
	private TextField cantidad;
	@FXML
	private TextField precio;
	@FXML
	private Button nuevo;
	@FXML
	private Button editar;
	@FXML
	private ComboBox<String> edad = new ComboBox<>();
	@FXML
	private ComboBox<String> activar = new ComboBox<>();

	private MongoClient client;

	private MongoCollection<Document> col;

	public void initialize(URL arg0, ResourceBundle arg1) {

		edad.getItems().addAll("3+", "7+", "12+", "16+", "18+");
		activar.getItems().addAll("Si", "No");

		client = new MongoClient(new MongoClientURI("mongodb://admin000:admin001@ds013212.mlab.com:13212/botiga_daw"));
		MongoDatabase db = client.getDatabase("botiga_daw");
		col = db.getCollection("products");

		editar.setDisable(true);

	}

	@SuppressWarnings("unchecked")
	@FXML
	public void buscarNombre(ActionEvent event) {

		String name = nomProd.getText();
		Document doc = col.find(eq("nom", name)).first();

		if (doc == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Problema de Búsqueda");
			alert.setHeaderText("ALERTA: Problema en la búsqueda");
			alert.setContentText("Los datos escritos son erróneos o\nno existe dicho nombre.\nRehaga la búsqueda.");
			alert.showAndWait();
		} else {

			nombre.setText((String) doc.get("nom"));
			genero.setText((String) doc.get("genero"));
			distribuidora.setText((String) doc.get("distribuidora"));
			plataforma.setText((String) doc.get("plataforma"));
			edad.setValue((String) doc.get("edad"));
			cantidad.setText(Integer.toString((int) doc.get("cantidad")));
			activar.setValue((String) doc.get("activado"));
			precio.setText(Double.toString((double) doc.get("precio")));

			List<java.lang.String> imagenes = (List<String>) doc.get("imagenes");

			portada.setText(imagenes.get(0));
			juego.setText(imagenes.get(1));
			escena1.setText(imagenes.get(2));
			escena2.setText(imagenes.get(3));

			editar.setDisable(false);
		}
	}

	@FXML
	public void nuevoProducto(ActionEvent event) {

		if (nombre.getText().equals("") || genero.getText().equals("") || distribuidora.getText().equals("")
				|| plataforma.getText().equals("") || cantidad.getText().equals("") || precio.getText().equals("")
				|| portada.getText().equals("") || juego.getText().equals("") || escena1.getText().equals("")
				|| escena2.getText().equals("") || activar.getValue() == null || edad.getValue() == null) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Problema de Búsqueda");
			alert.setHeaderText("ALERTA: Problema en la búsqueda");
			alert.setContentText("Los datos escritos son erróneos o\nno existe dicho nombre.\nRehaga la búsqueda.");
			alert.showAndWait();

		} else {

			List<String> imagenes = new ArrayList<String>();
			imagenes.add(portada.getText());
			imagenes.add(juego.getText());
			imagenes.add(escena1.getText());
			imagenes.add(escena2.getText());

			Document newDoc = new Document("nom", nombre.getText()).append("portada", portada.getText())
					.append("imagenes", imagenes).append("genero", genero.getText())
					.append("distribuidora", distribuidora.getText()).append("plataforma", plataforma.getText())
					.append("edad", edad.getValue()).append("cantidad", Integer.parseInt(cantidad.getText()))
					.append("ventas", 0).append("activado", activar.getValue())
					.append("precio", Double.parseDouble(precio.getText()));

			col.insertOne(newDoc);
			
			nombre.setText("");
			genero.setText("");
			distribuidora.setText("");
			plataforma.setText("");
			cantidad.setText("");
			precio.setText("");
			portada.setText("");
			juego.setText("");
			escena1.setText("");
			escena2.setText("");
		}
	}

	@FXML
	public void editarProducto(ActionEvent event) {

	}
}
