package vahovsky.LabBook.gui;

import javafx.scene.control.Button;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import vahovsky.LabBook.fxmodels.EntityFxModel;
import vahovsky.LabBook.persistent.EntityDAO;

public class Utilities {
	
	public static void initialize(Button yesButton, Button noButton, EntityDAO entityDAO, EntityFxModel entityFxModel) {

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
