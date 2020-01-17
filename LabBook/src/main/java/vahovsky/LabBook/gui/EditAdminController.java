package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import vahovsky.LabBook.entities.Admin;
import vahovsky.LabBook.fxmodels.AdminFxModel;
import vahovsky.LabBook.persistent.AdminDAO;
import vahovsky.LabBook.persistent.DAOfactory;

public class EditAdminController {

	private AdminDAO adminDao;
	private AdminFxModel adminModel;

	@FXML
	private Button saveButton;

	@FXML
	private PasswordField passwordPasswordField;

	@FXML
	private PasswordField confirmPasswordPasswordField;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField emailTextField;

	public EditAdminController(Admin admin) {
		adminDao = DAOfactory.INSTANCE.getAdminDAO();
		adminModel = new AdminFxModel(admin);
	}

	/**
	 * Method for editing admin according to a user input in text fields. Text
	 * fields are binded to a properties of the fxModel, which is based on an entity
	 * to be edited.
	 */
	@FXML
	void initialize() {
		nameTextField.textProperty().bindBidirectional(adminModel.getNameProperty());
		emailTextField.textProperty().bindBidirectional(adminModel.getEmailProperty());
		passwordPasswordField.textProperty().bindBidirectional(adminModel.getPasswordProperty());
		confirmPasswordPasswordField.textProperty().bindBidirectional(adminModel.getPasswordProperty());

		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				adminDao.saveAdmin(adminModel.getEntity());
				saveButton.getScene().getWindow().hide();
			}

		});
	}
}
