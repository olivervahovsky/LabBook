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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import vahovsky.LabBook.business.UserIdentificationManager;
import vahovsky.LabBook.entities.Admin;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;

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

					if (UserIdentificationManager.setUser(login, password) == 1) {
						SelectProjectController controller = new SelectProjectController();
						login(controller, "selectProject.fxml", "Projects");
					} else if (UserIdentificationManager.setUser(login, password) == 2) {
						AdminPageController controller = new AdminPageController();
						login(controller, "editDataAdmin.fxml", "Admin Page");
					} else {
						util.showWrongDataInputWindow("alertBoxFailToSignIn.fxml", "Fail to sign in");
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

					if (UserIdentificationManager.setUser(login, password) == 1) {
						SelectProjectController controller = new SelectProjectController();
						login(controller, "selectProject.fxml", "Projects");
					} else if (UserIdentificationManager.setUser(login, password) == 2) {
						AdminPageController controller = new AdminPageController();
						login(controller, "editDataAdmin.fxml", "Admin Page");
					} else {
						util.showWrongDataInputWindow("alertBoxFailToSignIn.fxml", "Fail to sign in");
					}
				}
			}
		});

		registerButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				RegistrationController registrationController = new RegistrationController();
				util.showModalWindow(registrationController, "registration.fxml", "Registration");
			}
		});

		forgottenPasswordHyperlink.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ForgottenPasswordController forgottenPasswordController = new ForgottenPasswordController();
				util.showModalWindow(forgottenPasswordController, "forgottenPassword.fxml", "Forgotten Password");
			}
		});

		signInButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String login = loginTextField.getText();
				String password = passwordTextField.getText();

				if (UserIdentificationManager.setUser(login, password) == 1) {
					SelectProjectController controller = new SelectProjectController();
					login(controller, "selectProject.fxml", "Projects");
				} else if (UserIdentificationManager.setUser(login, password) == 2) {
					AdminPageController controller = new AdminPageController();
					login(controller, "editDataAdmin.fxml", "Admin Page");
				} else {
					util.showWrongDataInputWindow("alertBoxFailToSignIn.fxml", "Fail to sign in");// .../gui/
				}
			}
		});

	}

	/**
	 * Method that hides the login/register page and opens new window. Type of the
	 * window that appears depends on the type of the user logged in - either User
	 * (select project window) or Admin (edit data window).
	 * 
	 * @param controller controller of the window that is going to appear
	 * @param fxml fxml file of the window that is going to appear
	 * @param pageTitle title of the window that is going to appear
	 */
	private void login(Object controller, String fxml, String pageTitle) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
			loader.setController(controller);

			Parent parentPane = loader.load();
			Scene scene = new Scene(parentPane);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle(pageTitle);
			stage.show();
			signInButton.getScene().getWindow().hide();

		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}

	/** Method that returns entity Admin, based on the value of its parameter "name"
	 * @param name value of the admin's parameter "name", based on which is the entity Admin returned
	 * @return entity Admin with the corresponding name
	 */
	public Admin findByName(String name) {
		List<Admin> admins = DAOfactory.INSTANCE.getAdminDAO().getAll();
		for (Admin admin : admins) {
			if (admin.getName().equals(name)) {
				return admin;
			}
		}
		return null;
	}

	/** Method that returns entity User, based on the values of its parameters "name" and "password"
	 * @param name value of the user's parameter "name", based on which is the entity User returned
	 * @param password value of the user's parameter "password", based on which is the entity User returned
	 * @return entity User with the corresponding name and password
	 */
	public User findByName(String name, String password) {
		List<User> users = DAOfactory.INSTANCE.getUserDAO().getAll();
		for (User user : users) {
			if (user.getName().equals(name) && user.getPassword().equals(password)) {
				return user;
			}

		}
		return null;
	}
}
