package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.fxmodels.TaskFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.TaskDAO;

public class DeleteTaskController {

	private TaskDAO taskDao = DAOfactory.INSTANCE.getTaskDAO();
	private TaskFxModel taskModel;
	//private Task task;

	@FXML
	private Button yesButton;

	@FXML
	private Button noButton;

	public DeleteTaskController(Task task) {
		//this.task = task;
		this.taskModel = new TaskFxModel(task);
	}

	@FXML
	void initialize() {

		yesButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					taskDao.deleteTask(taskModel.getTask());
					taskDao.saveTask(taskModel.getTask());
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
