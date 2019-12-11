package vahovsky.LabBook.gui;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.UserDAO;

public class RegistrationController {
	
	private Utilities util = new Utilities();

	@FXML
	private PasswordField confirmPasswordPasswordField;

	@FXML
	private PasswordField passwordPasswordField;

	@FXML
	private TextField nameTextField;

	@FXML
	private Button finishButton;

	@FXML
	private TextField emailAdressTextField;

	@FXML
	void initialize() {
		finishButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String name = nameTextField.getText();
				String email = emailAdressTextField.getText();
				String password1 = passwordPasswordField.getText();
				String password2 = confirmPasswordPasswordField.getText();
				UserDAO userDao = DAOfactory.INSTANCE.getUserDAO();
				List<String> emails = userDao.getAllEmails();
				List<String> names = userDao.getAllNames();
				boolean duplicateEmailOrName = emails.contains(email) || names.contains(name);
				if (name.isEmpty() || email.isEmpty() || password1.isEmpty() || duplicateEmailOrName) {
					util.showWrongDataInputWindow("WrongDataInput.fxml", "Wrong data");
				} else {
					if (password1.equals(password2)) {
						User user = new User(name, password1, email);
						userDao = DAOfactory.INSTANCE.getUserDAO();
						userDao.addUser(user);
						finishButton.getScene().getWindow().hide();
					} else {
						util.showWrongDataInputWindow("WrongDataInput.fxml", "Wrong data");
					}
				}
			}
		});
	}

//	private void showWrongDataInputWindow() {
//		WrongDataInputController controller = new WrongDataInputController();
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("WrongDataInput.fxml"));
//			loader.setController(controller);
//
//			Parent parentPane = loader.load();
//			Scene scene = new Scene(parentPane);
//
//			Stage stage = new Stage();
//			stage.setScene(scene);
//			stage.initModality(Modality.APPLICATION_MODAL);
//			stage.setResizable(false);
//			stage.setTitle("Wrong data");
//			stage.show();
//
//		} catch (IOException iOException) {
//			iOException.printStackTrace();
//		}
//	}

}
