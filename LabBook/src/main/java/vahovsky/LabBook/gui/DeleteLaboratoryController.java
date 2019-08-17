package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.fxmodels.LaboratoryFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.LaboratoryDAO;

public class DeleteLaboratoryController {

	private LaboratoryDAO laboratoryDao = DAOfactory.INSTANCE.getLaboratoryDAO();
	private LaboratoryFxModel laboratoryModel;
	//private Laboratory laboratory;

	@FXML
	private Button yesButton;

	@FXML
	private Button noButton;

	public DeleteLaboratoryController(Laboratory laboratory) {
		//this.laboratory = laboratory;
		this.laboratoryModel = new LaboratoryFxModel(laboratory);
	}

	void initialize() {

		yesButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					laboratoryDao.deleteLaboratory(laboratoryModel.getLaboratory());
					yesButton.getScene().getWindow().hide();
				}
			}
		});

		noButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				noButton.getScene().getWindow().hide();
			}
		});
	}

}
