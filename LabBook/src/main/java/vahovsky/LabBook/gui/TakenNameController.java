package vahovsky.LabBook.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TakenNameController {

	@FXML
	private Button closeButton;

	@FXML
	void initialize() {

		closeButton.setOnAction(eh -> {
			closeButton.getScene().getWindow().hide();
		});

	}

}
