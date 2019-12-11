package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import vahovsky.LabBook.fxmodels.EntityFxModel;
import vahovsky.LabBook.persistent.EntityDAO;

public class DeleteEntityController {

	@FXML
	private Button yesButton;

	@FXML
	private Button noButton;

	private EntityDAO entityDAO;
	private EntityFxModel entityFxModel;

	public DeleteEntityController(EntityDAO entityDAO, EntityFxModel entityFxModel) {
		this.entityFxModel = entityFxModel;
		this.entityDAO = entityDAO;
	}

	/**
	 * Method that controls actions on delete entity window. Pressing enter or
	 * clicking "yes" on prompt deletes entity, while pressing "no" cancels the
	 * action.
	 */
	@FXML
	void initialize() {
		yesButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					entityDAO.deleteEntity(entityFxModel.getEntity());
					yesButton.getScene().getWindow().hide();
				}
			}
		});

		yesButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				entityDAO.deleteEntity(entityFxModel.getEntity());
				yesButton.getScene().getWindow().hide();
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
