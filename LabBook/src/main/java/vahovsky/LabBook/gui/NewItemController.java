package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.fxmodels.LaboratoryFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ItemDAO;

public class NewItemController {
	
	private Utilities util;

	@FXML
	private Button saveButton;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField quantityTextField;

	private ItemDAO itemDao;

	private LaboratoryFxModel laboratoryModel;

	public NewItemController(Laboratory laboratory) {
		util = new Utilities();
		itemDao = DAOfactory.INSTANCE.getItemDAO();
		this.laboratoryModel = new LaboratoryFxModel(laboratory);
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
				} else if (!itemDao.isNameAvailable(name)) {
					util.showWrongDataInputWindow("takenName.fxml", "Taken Name");
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

}
