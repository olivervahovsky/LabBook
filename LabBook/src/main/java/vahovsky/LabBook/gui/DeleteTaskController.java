package vahovsky.LabBook.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.fxmodels.TaskFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.TaskDAO;

public class DeleteTaskController {

	private TaskDAO taskDao = DAOfactory.INSTANCE.getTaskDAO();
	private TaskFxModel taskModel;

	@FXML
	private Button yesButton;

	@FXML
	private Button noButton;

	public DeleteTaskController(Task task) {
		this.taskModel = new TaskFxModel(task);
	}
	
	@FXML
	void initialize() {
		Utilities.initialize(yesButton, noButton, taskDao, taskModel);
	}

}
