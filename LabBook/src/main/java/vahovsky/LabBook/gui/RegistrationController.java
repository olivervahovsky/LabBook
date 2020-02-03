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
					WrongDataInputController controller = new WrongDataInputController();
					util.showModalWindow(controller, "WrongDataInput.fxml", "Wrong data", null);
				} else {
					if (password1.equals(password2)) {
						User user = new User(name, password1, email);
						userDao = DAOfactory.INSTANCE.getUserDAO();
						userDao.addUser(user);
						finishButton.getScene().getWindow().hide();
					} else {
						WrongDataInputController controller = new WrongDataInputController();
						util.showModalWindow(controller, "WrongDataInput.fxml", "Wrong data", null);
					}
				}
			}
		});
	}

}
