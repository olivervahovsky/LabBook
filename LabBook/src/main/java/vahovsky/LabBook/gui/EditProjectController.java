package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import vahovsky.LabBook.business.UserIdentificationManager;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.fxmodels.ProjectFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ProjectDAO;

public class EditProjectController {

	private ProjectDAO projectDao;
	private ProjectFxModel projectModel;

	@FXML
	private TextField nameTextField;

	@FXML
	private DatePicker fromDatePicker;

	@FXML
	private DatePicker untilDatePicker;

	@FXML
	private Button saveButton;

	public EditProjectController(Project project) {
		projectDao = DAOfactory.INSTANCE.getProjectDAO();
		projectModel = new ProjectFxModel(project);
	}

	@FXML
	void initialize() {
		nameTextField.textProperty().bindBidirectional(projectModel.getNameProperty());
		fromDatePicker.valueProperty().bindBidirectional(projectModel.getFromProperty());
		untilDatePicker.valueProperty().bindBidirectional(projectModel.getUntilProperty());

		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				projectModel.setCreatedBy(UserIdentificationManager.getUser());
				projectDao.saveProject(projectModel.getEntity());
				saveButton.getScene().getWindow().hide();
			}
		});

	}

}
