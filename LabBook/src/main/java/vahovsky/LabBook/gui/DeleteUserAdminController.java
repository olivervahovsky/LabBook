package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.UserDAO;

public class DeleteUserAdminController {

	private UserDAO userDao = DAOfactory.INSTANCE.getUserDAO();
	private User user;

	public DeleteUserAdminController(User user) {
		this.user = user;
	}

	@FXML
	private Button yesButton;

	@FXML
	private Button noButton;

	@FXML
	void initialize() {

		yesButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					userDao.deleteUser(user);
					yesButton.getScene().getWindow().hide();
				}
			}
		});

		noButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				noButton.getScene().getWindow().hide();
			}
		});
	}
}
