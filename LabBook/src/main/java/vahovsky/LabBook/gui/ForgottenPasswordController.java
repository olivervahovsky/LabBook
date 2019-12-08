package vahovsky.LabBook.gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import vahovsky.LabBook.business.ForgottenPasswordManagerSimple;
import vahovsky.LabBook.persistent.DAOfactory;

public class ForgottenPasswordController {
	
	Utilities util;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField emailTextField;

	@FXML
	private Button sendPasswordButton;

	@FXML
	void initialize() {
		sendPasswordButton.setOnAction(eh -> {
			String email = emailTextField.textProperty().get();
			List<String> emails = DAOfactory.INSTANCE.getUserDAO().getAllEmails();
			if (!emails.contains(email))
				util.showWrongDataInputWindow("WrongDataInput.fxml","Wrong data");
			else {
				ForgottenPasswordManagerSimple.sendPassword(email);
				sendPasswordButton.getScene().getWindow().hide();
			}
		});
	}

}
