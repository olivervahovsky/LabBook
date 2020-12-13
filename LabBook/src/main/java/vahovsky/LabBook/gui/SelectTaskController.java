package vahovsky.LabBook.gui;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.fxmodels.ProjectFxModel;
import vahovsky.LabBook.fxmodels.TaskFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ProjectDAO;

public class SelectTaskController {
	
	private Utilities util;

	private ProjectDAO projectDao;
	private ObservableList<Task> tasksModel;
	private Map<String, BooleanProperty> columnsVisibility;
	private ObjectProperty<Task> selectedTask;
	private ProjectFxModel projectModel;

	@FXML
	private TableView<Task> tasksTableView;

	@FXML
	private Button newTaskButton;

	@FXML
	private Button editButton;

	@FXML
	private Button openButton;

	@FXML
	private Button deleteButton;

	@FXML
	private Button projectsButton;

	public SelectTaskController(Project project) {
		util = new Utilities();
		projectDao = DAOfactory.INSTANCE.getProjectDAO();
		columnsVisibility = new LinkedHashMap<>();
		selectedTask = new SimpleObjectProperty<>();
		projectModel = new ProjectFxModel(project);
	}

	@FXML
	void initialize() {

		tasksModel = FXCollections.observableArrayList(projectDao.getTasksOfProject(projectModel.getEntity()));

		tasksTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					if (mouseEvent.getClickCount() == 2) {
						SelectNoteController controller = new SelectNoteController(selectedTask.get(), projectModel.getEntity());
						util.showModalWindow(controller, "selectNotes.fxml", "Notes", openButton);
					}
				}
			}
		});

		editButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				EditTaskController editTaskController = new EditTaskController(selectedTask.get());
				util.showModalWindow(editTaskController, "editTask.fxml", "Task Editing", null);
				tasksModel.setAll(projectDao.getTasksOfProject(projectModel.getEntity()));
			}
		});

		openButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				SelectNoteController controller = new SelectNoteController(selectedTask.get(), projectModel.getEntity());
				util.showModalWindow(controller, "selectNotes.fxml", "Notes", openButton);
			}
		});

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				TaskFxModel taskFxModel = new TaskFxModel(selectedTask.get());
				DeleteEntityController deleteTaskController = new DeleteEntityController(DAOfactory.INSTANCE.getTaskDAO(), taskFxModel);
				util.showModalWindow(deleteTaskController, "deleteTask.fxml", "Task Deleting", null);
				tasksModel.setAll(projectDao.getTasksOfProject(projectModel.getEntity()));
			}
		});

		newTaskButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NewTaskController newTaskController = new NewTaskController(projectModel.getEntity());
				util.showModalWindow(newTaskController, "newTask.fxml", "New Task", null);
				tasksModel.setAll(projectDao.getTasksOfProject(projectModel.getEntity()));
			}
		});

		TableColumn<Task, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		tasksTableView.getColumns().add(nameCol);
		columnsVisibility.put("name", nameCol.visibleProperty());
		
		TableColumn<Task, String> activeCol = new TableColumn<>("Task status");
		activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
		tasksTableView.getColumns().add(activeCol);
		
		TableColumn<Task, String> dateFromCol = new TableColumn<>("Start of the task");
		dateFromCol.setCellValueFactory(new PropertyValueFactory<>("dateFrom"));
		tasksTableView.getColumns().add(dateFromCol);
		
		TableColumn<Task, String> dateUntilCol = new TableColumn<>("Estimated end of the task");
		dateUntilCol.setCellValueFactory(new PropertyValueFactory<>("dateUntil"));
		tasksTableView.getColumns().add(dateUntilCol);
		
		TableColumn<Task, String> laboratoryCol = new TableColumn<>("Laboratory");
		laboratoryCol.setCellValueFactory(new PropertyValueFactory<>("laboratory"));
		tasksTableView.getColumns().add(laboratoryCol);
		
		TableColumn<Task, String> itemsCol = new TableColumn<>("Items");
		itemsCol.setCellValueFactory(new PropertyValueFactory<>("items"));
		tasksTableView.getColumns().add(itemsCol);

		tasksTableView.setItems(tasksModel);
		tasksTableView.setEditable(true);

		tasksTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {

			@Override
			public void changed(ObservableValue<? extends Task> observable, Task oldValue, Task newValue) {
				if (newValue == null) {
					editButton.setDisable(true);
				} else {
					editButton.setDisable(false);
				}
				selectedTask.set(newValue);
			}
		});

		projectsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				SelectProjectController controller = new SelectProjectController();
				util.showModalWindow(controller, "selectProject.fxml", "Projects", projectsButton);
			}
		});
	}

}
