package vahovsky.LabBook.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.fxmodels.ProjectFxModel;
import vahovsky.LabBook.fxmodels.TaskFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.TaskDAO;

public class SelectTaskController {

	private TaskDAO taskDao = DAOfactory.INSTANCE.getTaskDAO();
	private ObservableList<Task> tasksModel;
	private Map<String, BooleanProperty> columnsVisibility = new LinkedHashMap<>();
	private ObjectProperty<Task> selectedTask = new SimpleObjectProperty<>();
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
		this.projectModel = new ProjectFxModel(project);
	}

	public SelectTaskController() {
	}

	@FXML
	void initialize() {

		tasksModel = FXCollections.observableArrayList(getTasks());

		tasksTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					if (mouseEvent.getClickCount() == 2) {
						openNotes();
					}
				}
			}
		});

		editButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				EditTaskController editTaskController = new EditTaskController(selectedTask.get());
				showModalWindow(editTaskController, "editTask.fxml", "Task Editing");
				tasksModel.setAll(getTasks());
			}
		});

		openButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				openNotes();
			}
		});

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				TaskFxModel taskFxModel = new TaskFxModel(selectedTask.get());
				DeleteEntityController deleteTaskController = new DeleteEntityController(DAOfactory.INSTANCE.getTaskDAO(), taskFxModel);
				showModalWindow(deleteTaskController, "deleteTask.fxml", "Task Deleting");
				tasksModel.setAll(getTasks());
			}
		});

		newTaskButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NewTaskController newTaskController = new NewTaskController(projectModel.getEntity());
				showModalWindow(newTaskController, "newTask.fxml", "New Task");
				tasksModel.setAll(getTasks());
			}
		});

		TableColumn<Task, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		tasksTableView.getColumns().add(nameCol);
		columnsVisibility.put("name", nameCol.visibleProperty());

		// TableColumn<Task, Laboratory> labCol = new TableColumn<>("Laboratory");
		// labCol.setCellValueFactory(new PropertyValueFactory<>("lab"));
		// tasksTableView.getColumns().add(labCol);
		// columnsVisibility.put("lab", labCol.visibleProperty());

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
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("selectProject.fxml"));
					loader.setController(controller);

					Parent parentPane = loader.load();
					Scene scene = new Scene(parentPane);

					Stage stage = new Stage();
					stage.setScene(scene);
					stage.setTitle("Projects");
					stage.show();
					projectsButton.getScene().getWindow().hide();

				} catch (IOException iOException) {
					iOException.printStackTrace();
				}
			}
		});
	}

	private void showModalWindow(Object controller, String fxml, String title) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
			fxmlLoader.setController(controller);
			Parent rootPane = fxmlLoader.load();
			Scene scene = new Scene(rootPane);

			Stage dialog = new Stage();
			dialog.setScene(scene);
			dialog.setTitle(title);
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void openNotes() {
		SelectNoteController controller = new SelectNoteController(selectedTask.get(), projectModel.getEntity());
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("selectNotes.fxml"));
			loader.setController(controller);

			Parent parentPane = loader.load();
			Scene scene = new Scene(parentPane);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Notes");
			stage.show();
			openButton.getScene().getWindow().hide();

		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}

	private List<Task> getTasks() {
		List<Task> tasks = new ArrayList<>();
		List<Task> allTasks = taskDao.getAll();
		for (Task task : allTasks) {
			if (task.getProject().getEntityID() == projectModel.getProjectId()) {
				tasks.add(task);
			}
		}
		return tasks;

	}

}
