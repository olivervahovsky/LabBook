package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import vahovsky.LabBook.entities.Admin;
import vahovsky.LabBook.persistent.AdminDAO;
import vahovsky.LabBook.persistent.DAOfactory;

public class NewAdminController {

	private Utilities util = new Utilities();

	@FXML
	private Button saveButton;

	@FXML
	private PasswordField passwordPasswordField;

	@FXML
	private PasswordField confirmPasswordPasswordField;

	@FXML
	private TextField emailTextField;

	@FXML
	private TextField nameTextField;

	private AdminDAO adminDao = DAOfactory.INSTANCE.getAdminDAO();

	@FXML
	void initialize() {
		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String name = nameTextField.getText();
				String email = emailTextField.getText();
				String password1 = passwordPasswordField.getText();
				String password2 = confirmPasswordPasswordField.getText();
				if (name.isEmpty() || email.isEmpty() || password1.isEmpty()) {
					util.showWrongDataInputWindow("WrongDataInput.fxml", "Wrong data");
				} else if (!adminDao.isNameAvailable(name)) {
					util.showWrongDataInputWindow("takenName.fxml", "Taken Name");
				} else {
					if (password1.equals(password2)) {
						Admin admin = new Admin();
						admin.setName(name);
						admin.setEmail(email);
						admin.setPassword(password1);
						AdminDAO adminDao = DAOfactory.INSTANCE.getAdminDAO();
						adminDao.addAdmin(admin);
						saveButton.getScene().getWindow().hide();
					} else {
						util.showWrongDataInputWindow("WrongDataInput.fxml", "Wrong data");
					}

				}
			}
		});
	}
}