package vahovsky.LabBook.gui;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.fxmodels.LaboratoryFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ItemDAO;

public class NewItemController {
	
	Utilities util;

	@FXML
	private Button saveButton;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField quantityTextField;

	private ItemDAO itemDao = DAOfactory.INSTANCE.getItemDAO();

	private LaboratoryFxModel laboratoryModel;

	public NewItemController(Laboratory laboratory) {
		this.laboratoryModel = new LaboratoryFxModel(laboratory);
	}

	public NewItemController() {
	}

	@FXML
	void initialize() {

		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String name = nameTextField.getText();
				String quantityString = quantityTextField.getText();
				int quantity = Integer.parseInt(quantityString);
				if (name.isEmpty() || quantityString == null) {
					util.showWrongDataInputWindow("WrongDataInput.fxml", "Wrong data");
				} else if (!isAvailable(name)) {
					showTakenNameWindow();
				} else {
					Item item = new Item();
					item.setName(name);
					item.setQuantity(quantity);
					item.setLaboratory(laboratoryModel.getEntity());
					ItemDAO itemDao = DAOfactory.INSTANCE.getItemDAO();
					itemDao.addItem(item);
					saveButton.getScene().getWindow().hide();
				}
			}
		});

	}

	private void showTakenNameWindow() {
		WrongDataInputController controller = new WrongDataInputController();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("takenName.fxml"));
			loader.setController(controller);

			Parent parentPane = loader.load();
			Scene scene = new Scene(parentPane);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.setTitle("Taken Name");
			stage.show();

		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}

	private boolean isAvailable(String name) {
		List<Item> items = itemDao.getAll();
		for (Item i : items) {
			if (i.getName().equals(name)) {
				return false;
			}
		}
		return true;
	}
}
