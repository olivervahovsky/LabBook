package vahovsky.LabBook.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import vahovsky.LabBook.fxmodels.EntityFxModel;
import vahovsky.LabBook.persistent.EntityDAO;

public class DeleteEntityController {
	
	@FXML
	private Button yesButton;

	@FXML
	private Button noButton;

	private EntityDAO entityDao;
	private EntityFxModel entityModel;

	public DeleteEntityController(EntityDAO entityDao, EntityFxModel entityFxModel) {
		this.entityModel = entityFxModel;
		this.entityDao = entityDao;
	}
	
	@FXML
	void initialize() {
		Utilities.initialize(yesButton, noButton, entityDao, entityModel);
	}

}
