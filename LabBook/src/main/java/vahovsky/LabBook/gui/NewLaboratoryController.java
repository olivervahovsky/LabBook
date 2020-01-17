package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.LaboratoryDAO;

public class NewLaboratoryController {
	
	private Utilities util = new Utilities();

	@FXML
	private Button saveButton;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField locationTextField;

	private LaboratoryDAO laboratoryDao = DAOfactory.INSTANCE.getLaboratoryDAO();

	@FXML
	void initialize() {
		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String name = nameTextField.getText();
				String location = locationTextField.getText();

				if (name.isEmpty() || location.isEmpty()) {
					util.showWrongDataInputWindow("WrongDataInput.fxml", "Wrong data");
				} else if (!laboratoryDao.isNameAvailable(name)) {
					util.showWrongDataInputWindow("takenName.fxml", "Taken Name");
				} else {
					Laboratory laboratory = new Laboratory(name, location);
					LaboratoryDAO laboratoryDao = DAOfactory.INSTANCE.getLaboratoryDAO();
					laboratoryDao.addLaboratory(laboratory);
					saveButton.getScene().getWindow().hide();
				}

			}
		});

	}

}
