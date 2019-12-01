package vahovsky.LabBook.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.fxmodels.ProjectFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ProjectDAO;

public class DeleteProjectController {

	private ProjectDAO projectDao = DAOfactory.INSTANCE.getProjectDAO();
	private ProjectFxModel projectModel;

	@FXML
	private Button yesButton;

	@FXML
	private Button noButton;

	public DeleteProjectController(Project project) {
		this.projectModel = new ProjectFxModel(project);
	}
	
	@FXML
	void initialize() {
		Utilities.initialize(yesButton, noButton, projectDao, projectModel);
	}
	
}
