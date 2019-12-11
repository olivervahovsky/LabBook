package vahovsky.LabBook.gui;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
				} else if (!isAvailable(name)) {
					showTakenNameWindow();
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

	private void showTakenNameWindow() {
		WrongDataInputController controller = new WrongDataInputController();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("takenName.fxml"));
			loader.setController(controller);

			Parent parentPane = loader.load();
			Scene scene = new Scene(parentPane);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.setTitle("Taken Name");
			stage.show();

		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}

	private boolean isAvailable(String name) {
		List<Admin> admins = adminDao.getAll();
		for (Admin a : admins) {
			if (a.getName().equals(name)) {
				return false;
			}
		}
		return true;
	}
}
