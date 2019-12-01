package vahovsky.LabBook.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.fxmodels.UserFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.UserDAO;

public class DeleteUserAdminController {

	private UserDAO userDao = DAOfactory.INSTANCE.getUserDAO();
	private UserFxModel userModel;

	public DeleteUserAdminController(User user) {
		this.userModel = new UserFxModel(user);
	}

	@FXML
	private Button yesButton;

	@FXML
	private Button noButton;
	
	@FXML
	void initialize() {
		Utilities.initialize(yesButton, noButton, userDao, userModel);
	}

}
