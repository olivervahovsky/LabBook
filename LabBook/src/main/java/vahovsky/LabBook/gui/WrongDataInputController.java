package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WrongDataInputController {

	@FXML
	private Button closeButton;

	@FXML
	void initialize() {
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
		
			@Override
			public void handle(ActionEvent event) {
				closeButton.getScene().getWindow().hide();
			}
		});
	}
}
