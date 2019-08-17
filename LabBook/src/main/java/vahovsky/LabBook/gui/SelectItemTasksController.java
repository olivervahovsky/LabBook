package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import vahovsky.LabBook.entities.Item;

public class SelectItemTasksController {

	@FXML
	private Button selectButton;

	@FXML
	private ComboBox<Item> itemComboBox;

	@FXML
	void initialize() {

		selectButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				
				selectButton.getScene().getWindow().hide();
				
			}
		});
	}

}
