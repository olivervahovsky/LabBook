package vahovsky.LabBook.gui;

import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.fxmodels.UserFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ProjectDAO;

public class NewProjectController {
	
	private Utilities util = new Utilities();

	@FXML
	private Button saveButton;

	@FXML
	private TextField nameTextField;

	@FXML
	private DatePicker fromDatePicker;

	@FXML
	private DatePicker untilDatePicker;

	private UserFxModel userModel;

	public NewProjectController(User user) {
		userModel = new UserFxModel(user);
	}

	@FXML
	void initialize() {

		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String name = nameTextField.getText();
				LocalDate from = fromDatePicker.getValue();
				LocalDate until = untilDatePicker.getValue();

				if (name.isEmpty() || from == null || until == null) {
					util.showWrongDataInputWindow("WrongDataInput.fxml", "Wrong data");
				} else {
					Project project = new Project(name, from, until, true);
					project.setCreatedBy(userModel.getEntity());
					ProjectDAO projectDao = DAOfactory.INSTANCE.getProjectDAO();
					projectDao.addProject(project);
					saveButton.getScene().getWindow().hide();
				}

			}
		});

	}

}
