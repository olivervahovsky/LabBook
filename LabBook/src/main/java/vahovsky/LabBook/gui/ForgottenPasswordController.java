package vahovsky.LabBook.gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vahovsky.LabBook.business.ForgottenPasswordManagerSimple;
import vahovsky.LabBook.persistent.DAOfactory;

public class ForgottenPasswordController {

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
				showWrongDataInputWindow();
			else {
				ForgottenPasswordManagerSimple.sendPassword(email);
				sendPasswordButton.getScene().getWindow().hide();
			}
		});
	}

	private void showWrongDataInputWindow() {
		WrongDataInputController controller = new WrongDataInputController();
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("WrongDataInput.fxml"));
			loader.setController(controller);

			Parent parentPane = loader.load();
			Scene scene = new Scene(parentPane);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.setTitle("Wrong data");
			stage.show();

		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}
}
