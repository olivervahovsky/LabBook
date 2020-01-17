package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.fxmodels.UserFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.UserDAO;

public class EditUserController {

	private UserDAO userDao;
	private UserFxModel userModel;

	@FXML
	private Button saveButton;

	@FXML
	private PasswordField newPasswordPasswordField;

	@FXML
	private PasswordField confirmNewPasswordPasswordField;

	@FXML
	private TextField emailTextField;

	@FXML
	private TextField nameTextField;

	public EditUserController(User user) {
		userDao = DAOfactory.INSTANCE.getUserDAO();
		userModel = new UserFxModel(user);
	}

	@FXML
	void initialize() {
		nameTextField.textProperty().bindBidirectional(userModel.getNameProperty());
		emailTextField.textProperty().bindBidirectional(userModel.getEmailProperty());
		newPasswordPasswordField.textProperty().bindBidirectional(userModel.getPasswordProperty());
		confirmNewPasswordPasswordField.textProperty().bindBidirectional(userModel.getPasswordProperty());

		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				userDao.saveUser(userModel.getEntity());
				saveButton.getScene().getWindow().hide();
			}
		});
	}

}
