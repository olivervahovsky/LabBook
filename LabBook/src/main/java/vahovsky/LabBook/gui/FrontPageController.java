package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import vahovsky.LabBook.business.UserIdentificationManager;

public class FrontPageController {

	private Utilities util = new Utilities();

	@FXML
	private TextField loginTextField;

	@FXML
	private PasswordField passwordTextField;

	@FXML
	private Button signInButton;

	@FXML
	private Button registerButton;

	@FXML
	private Hyperlink forgottenPasswordHyperlink;

	@FXML
	void initialize() {
		// https://www.programcreek.com/java-api-examples/?class=javafx.scene.Scene&method=setOnKeyPressed
		loginTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					String login = loginTextField.getText();
					String password = passwordTextField.getText();

					if (UserIdentificationManager.setUserOrAdmin(login, password) == 1) {
						SelectProjectController controller = new SelectProjectController();
						util.showModalWindow(controller, "selectProject.fxml", "Projects", signInButton);
					} else if (UserIdentificationManager.setUserOrAdmin(login, password) == 2) {
						AdminPageController controller = new AdminPageController();
						util.showModalWindow(controller, "editDataAdmin.fxml", "Admin Page", signInButton);
					} else {
						WrongDataInputController controller = new WrongDataInputController();
						util.showModalWindow(controller, "alertBoxFailToSignIn.fxml", "Fail to sign in", null);
					}
				}
			}
		});
		passwordTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					String login = loginTextField.getText();
					String password = passwordTextField.getText();

					if (UserIdentificationManager.setUserOrAdmin(login, password) == 1) {
						SelectProjectController controller = new SelectProjectController();
						util.showModalWindow(controller, "selectProject.fxml", "Projects", signInButton);
					} else if (UserIdentificationManager.setUserOrAdmin(login, password) == 2) {
						AdminPageController controller = new AdminPageController();
						util.showModalWindow(controller, "editDataAdmin.fxml", "Admin Page", signInButton);
					} else {
						WrongDataInputController controller = new WrongDataInputController();
						util.showModalWindow(controller, "alertBoxFailToSignIn.fxml", "Fail to sign in", null);
					}
				}
			}
		});

		registerButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				RegistrationController registrationController = new RegistrationController();
				util.showModalWindow(registrationController, "registration.fxml", "Registration", null);
			}
		});

		forgottenPasswordHyperlink.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ForgottenPasswordController forgottenPasswordController = new ForgottenPasswordController();
				util.showModalWindow(forgottenPasswordController, "forgottenPassword.fxml", "Forgotten Password", null);
			}
		});

		signInButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String login = loginTextField.getText();
				String password = passwordTextField.getText();

				if (UserIdentificationManager.setUserOrAdmin(login, password) == 1) {
					SelectProjectController controller = new SelectProjectController();
					util.showModalWindow(controller, "selectProject.fxml", "Projects", signInButton);
				} else if (UserIdentificationManager.setUserOrAdmin(login, password) == 2) {
					AdminPageController controller = new AdminPageController();
					util.showModalWindow(controller, "editDataAdmin.fxml", "Admin Page", signInButton);
				} else {
					WrongDataInputController controller = new WrongDataInputController();
					util.showModalWindow(controller, "alertBoxFailToSignIn.fxml", "Fail to sign in", null);
				}
			}
		});

	}

}
