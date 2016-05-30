package net.javierjimenez.Botiga_Escriptori;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.event.ActionEvent;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

/**
 * 
 * @author Surrui
 *
 */
public class StartController {

	/**
	 * Objeto TextField que contiene el String a buscar en la base de datos
	 */
	@FXML
	private TextField nomProd;

	/**
	 * Objeto Button que inicia la busqueda en la Base de Datos.
	 */
	@FXML
	private Button buscarNom;

	/**
	 * Objeto TextField que contiene el nombre de la imagen de Portada del Juego
	 */
	@FXML
	private TextField portada;

	/**
	 * Objeto TextField que contiene el nombre de la imagen del CD del Juego
	 */
	@FXML
	private TextField juego;

	/**
	 * Objeto TextField que contiene el nombre de la imagen de una escena del
	 * Juego
	 */
	@FXML
	private TextField escena1;

	/**
	 * Objeto TextField que contiene el nombre de la imagen de una escena del
	 * Juego
	 */
	@FXML
	private TextField escena2;

	/**
	 * Objeto TextField que contiene el nombre del Juego
	 */
	@FXML
	private TextField nombre;

	/**
	 * Objeto TextField que contiene el genero del Juego
	 */
	@FXML
	private TextField genero;

	/**
	 * Objeto TextField que contiene la distribuidora del Juego
	 */
	@FXML
	private TextField distribuidora;

	/**
	 * Objeto TextField que contiene la plataforma del Juego
	 */
	@FXML
	private TextField plataforma;

	/**
	 * Objeto TextField que contiene las unidades en stock del Juego
	 */
	@FXML
	private TextField cantidad;

	/**
	 * Objeto TextField que contiene el precio del Juego
	 */
	@FXML
	private TextField precio;

	/**
	 * Objeto TextArea que contiene la descripcion del Juego
	 */
	@FXML
	private TextArea descripcion;

	/**
	 * Objeto Button que permite añadir un nuevo Juego en la base de datos
	 */
	@FXML
	private Button nuevo;

	/**
	 * Objeto Button que permite editar un Juego previamente seleccionado
	 */
	@FXML
	private Button editar;

	/**
	 * Objeto ComboBox que contiene las calificaciones de edad del Juego
	 */
	@FXML
	private ComboBox<String> edad = new ComboBox<>();

	/**
	 * Objeto ComboBox que contiene la opcion de tener el Juego activado o no
	 */
	@FXML
	private ComboBox<String> activar = new ComboBox<>();

	/**
	 * Objeto MongoClient encargado de enlazar el proyecto con la base de datos
	 * MongoDB
	 */
	private MongoClient client;

	/**
	 * Objeto Document que contiene el Juego a editar
	 */
	private Document edit;

	/**
	 * Objeto MongoCollection que contiene los Document de la colección indicada
	 * por nosotros.
	 */
	private MongoCollection<Document> col;

	/**
	 * Constructor del objeto StartController
	 */
	public StartController() {

	}

	/**
	 * Metodo encargado de iniciar la conexión a la base de datos y de rellenar
	 * los ComboBox
	 */
	public void initialize() {

		edad.getItems().addAll("3+", "7+", "12+", "16+", "18+");
		activar.getItems().addAll("Si", "No");

		client = new MongoClient(new MongoClientURI("mongodb://admin:admin@ds013212.mlab.com:13212/botiga_daw"));
		MongoDatabase db = client.getDatabase("botiga_daw");
		col = db.getCollection("products");

		editar.setDisable(true);

	}

	/**
	 * Metodo que se activa al hacer click en el objeto Button buscarNom,
	 * buscando el nombre escrito en el TextField nomProd en la base de datos.
	 * En caso de que lo encuentre, rellenara los demas campos del programa con
	 * los datos del Juego en question.
	 * 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	@FXML
	public void buscarNombre(ActionEvent event) {

		String name = nomProd.getText();
		edit = col.find(eq("nom", name)).first();

		if (edit == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Problema de Búsqueda");
			alert.setHeaderText("ALERTA: Problema en la búsqueda");
			alert.setContentText("Los datos escritos son erróneos o\nno existe dicho nombre.\nRehaga la búsqueda.");
			alert.showAndWait();
		} else {
			nuevo.setDisable(true);

			nombre.setText((String) edit.get("nom"));
			genero.setText((String) edit.get("genero"));
			distribuidora.setText((String) edit.get("distribuidora"));
			plataforma.setText((String) edit.get("plataforma"));
			edad.setValue((String) edit.get("edad"));
			cantidad.setText(Integer.toString((int) edit.get("cantidad")));
			activar.setValue((String) edit.get("activado"));
			precio.setText(Double.toString((double) edit.get("precio")));
			descripcion.setText((String) edit.getString("descripcion"));

			List<java.lang.String> imagenes = (List<String>) edit.get("imagenes");

			portada.setText(imagenes.get(0));
			juego.setText(imagenes.get(1));
			escena1.setText(imagenes.get(2));
			escena2.setText(imagenes.get(3));

			editar.setDisable(false);
		}
	}

	/**
	 * Metodo que se activa al hacer click en el objeto Button nuevo, generando
	 * un nuevo Juego tras recoger los datos escritos y seleccionados en el
	 * programa. Se comprobara que se hayan rellenado todos los datos y que el
	 * objeto a crear no exista ya en nuestra base de datos.
	 * 
	 * @param event
	 */
	@FXML
	public void nuevoProducto(ActionEvent event) {

		if (nombre.getText().equals("") || descripcion.getText().equals("") || genero.getText().equals("")
				|| distribuidora.getText().equals("") || plataforma.getText().equals("")
				|| cantidad.getText().equals("") || precio.getText().equals("") || portada.getText().equals("")
				|| juego.getText().equals("") || escena1.getText().equals("") || escena2.getText().equals("")
				|| activar.getValue() == null || edad.getValue() == null) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Problema al Guardar");
			alert.setHeaderText("ALERTA: Problema al guardar el producto");
			alert.setContentText("No pueden haber campos en blanco!");
			alert.showAndWait();

		} else {

			String n = nombre.getText();
			Document doc = col.find(eq("nom", n)).first();

			if (doc != null && plataforma.getText().equals((String) doc.get("plataforma"))) {

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Problema al crear Producto");
				alert.setHeaderText("ALERTA: Problema al crear el producto");
				alert.setContentText("Ya existe dicho producto.");
				alert.showAndWait();

				nombre.setText("");
				descripcion.setText("");
				genero.setText("");
				distribuidora.setText("");
				plataforma.setText("");
				cantidad.setText("");
				precio.setText("");
				portada.setText("");
				juego.setText("");
				escena1.setText("");
				escena2.setText("");

			} else {

				if (portada.getText().contains(".") && portada.getText().contains(".")
						&& portada.getText().contains(".") && portada.getText().contains(".")) {

				} else {

					List<String> imagenes = new ArrayList<String>();
					imagenes.add(portada.getText());
					imagenes.add(juego.getText());
					imagenes.add(escena1.getText());
					imagenes.add(escena2.getText());

					Document newDoc = new Document("nom", nombre.getText()).append("descripcion", descripcion.getText())
							.append("portada", portada.getText()).append("imagenes", imagenes)
							.append("genero", genero.getText()).append("distribuidora", distribuidora.getText())
							.append("plataforma", plataforma.getText()).append("edad", edad.getValue())
							.append("cantidad", Integer.parseInt(cantidad.getText())).append("ventas", 0)
							.append("activado", activar.getValue())
							.append("precio", Double.parseDouble(precio.getText()));

					col.insertOne(newDoc);

					nombre.setText("");
					descripcion.setText("");
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
		}
	}

	/**
	 * Metodo que se activa al hacer click en el objeto Button editar, encargado
	 * de editar el Juego previamente buscado en la base de datos, y
	 * actualizandolo con los nuevos datos.
	 * 
	 * @param event
	 */
	@FXML
	public void editarProducto(ActionEvent event) {

		if (nombre.getText().equals("") || descripcion.getText().equals("") || genero.getText().equals("")
				|| distribuidora.getText().equals("") || plataforma.getText().equals("")
				|| cantidad.getText().equals("") || precio.getText().equals("") || portada.getText().equals("")
				|| juego.getText().equals("") || escena1.getText().equals("") || escena2.getText().equals("")
				|| activar.getValue() == null || edad.getValue() == null) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Problema de Edición");
			alert.setHeaderText("ALERTA: Problema en la edición");
			alert.setContentText("No pueden haber campos en blanco!");
			alert.showAndWait();

		} else {

			Document editProd = new Document();

			List<String> imagenes = new ArrayList<String>();
			imagenes.add(portada.getText());
			imagenes.add(juego.getText());
			imagenes.add(escena1.getText());
			imagenes.add(escena2.getText());

			editProd.append("$set",
					new Document().append("nom", nombre.getText()).append("descripcion", descripcion.getText())
							.append("portada", portada.getText()).append("imagenes", imagenes)
							.append("genero", genero.getText()).append("distribuidora", distribuidora.getText())
							.append("plataforma", plataforma.getText()).append("edad", edad.getValue())
							.append("cantidad", Integer.parseInt(cantidad.getText())).append("ventas", 0)
							.append("activado", activar.getValue())
							.append("precio", Double.parseDouble(precio.getText())));

			col.updateOne(edit, editProd);

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Actualización tramitada");
			alert.setContentText("Se ha actualizado el producto");

			nomProd.setText("");
			nombre.setText("");
			descripcion.setText("");
			genero.setText("");
			distribuidora.setText("");
			plataforma.setText("");
			cantidad.setText("");
			precio.setText("");
			portada.setText("");
			juego.setText("");
			escena1.setText("");
			escena2.setText("");

			editar.setDisable(true);
			nuevo.setDisable(false);
		}
	}
}
