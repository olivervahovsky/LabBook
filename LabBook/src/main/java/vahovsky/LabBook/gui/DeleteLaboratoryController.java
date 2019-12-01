package vahovsky.LabBook.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.fxmodels.LaboratoryFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.LaboratoryDAO;

public class DeleteLaboratoryController {

	@FXML
	private Button yesButton;

	@FXML
	private Button noButton;

	private LaboratoryDAO laboratoryDao = DAOfactory.INSTANCE.getLaboratoryDAO();
	private LaboratoryFxModel laboratoryModel;

	public DeleteLaboratoryController(Laboratory laboratory) {
		this.laboratoryModel = new LaboratoryFxModel(laboratory);
	}
	
	@FXML
	void initialize() {
		Utilities.initialize(yesButton, noButton, laboratoryDao, laboratoryModel);
	}

}
