package vahovsky.LabBook.gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.fxmodels.TaskFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.NoteDAO;


public class SelectNoteController {

	private NoteDAO noteDao = DAOfactory.INSTANCE.getNoteDAO();
	private ObservableList<Note> notesModel;
	private Map<String, BooleanProperty> columnsVisibility = new LinkedHashMap<>();
	private ObjectProperty<Note> selectedNote = new SimpleObjectProperty<>();
	private TaskFxModel taskModel;
	private Project project;

	@FXML
	private Button editButton;

	@FXML
	private Button deleteButton;

	@FXML
	private Button newNoteButton;

	@FXML
	private Button tasksButton;

	@FXML
	private TableView<Note> notesTableView;

	public SelectNoteController(Task task, Project project) {
		this.taskModel = new TaskFxModel(task);
		this.project = project;
	}

	@FXML
	void initialize() {
		notesModel = FXCollections.observableArrayList(getNotes());

		notesTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					if (mouseEvent.getClickCount() == 2) {
						EditNoteController editNoteController = new EditNoteController(selectedNote.get());
						showModalWindow(editNoteController, "editNote.fxml", "Note Editing");
						notesModel.setAll(getNotes());
					}
				}
			}
		});

		editButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				EditNoteController editNoteController = new EditNoteController(selectedNote.get());
				showModalWindow(editNoteController, "editNote.fxml", "Note Editing");
				notesModel.setAll(getNotes());
			}
		});

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DeleteNoteController deleteNoteController = new DeleteNoteController(selectedNote.get());
				showModalWindow(deleteNoteController, "deleteNote.fxml", "Note Deleting");
				notesModel.setAll(getNotes());
			}
		});

		newNoteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NewNoteController newNoteController = new NewNoteController(taskModel.getTask());
				showModalWindow(newNoteController, "newNote.fxml", "New Note");
				notesModel.setAll(getNotes());
			}
		});

		tasksButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				SelectTaskController controller = new SelectTaskController(project);
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("selectTask.fxml"));
					loader.setController(controller);

					Parent parentPane = loader.load();
					Scene scene = new Scene(parentPane);

					Stage stage = new Stage();
					stage.setScene(scene);
					stage.setTitle("Projects");
					stage.show();
					tasksButton.getScene().getWindow().hide();

				} catch (IOException iOException) {
					iOException.printStackTrace();
				}
			}
		});

		TableColumn<Note, String> textCol = new TableColumn<>("Text");
		textCol.setCellValueFactory(new PropertyValueFactory<>("text"));
		notesTableView.getColumns().add(textCol);
		columnsVisibility.put("text", textCol.visibleProperty());

		TableColumn<Note, LocalDate> dateCol = new TableColumn<>("Date");
		dateCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
		notesTableView.getColumns().add(dateCol);
		columnsVisibility.put("timestamp", dateCol.visibleProperty());

		notesTableView.setItems(notesModel);
		notesTableView.setEditable(true);

		ContextMenu contextMenu = new ContextMenu();
		for (Entry<String, BooleanProperty> entry : columnsVisibility.entrySet()) {
			CheckMenuItem menuItem = new CheckMenuItem(entry.getKey());
			menuItem.selectedProperty().bindBidirectional(entry.getValue());
			contextMenu.getItems().add(menuItem);
		}
		notesTableView.setContextMenu(contextMenu);

		notesTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Note>() {

			@Override
			public void changed(ObservableValue<? extends Note> observable, Note oldValue, Note newValue) {
				if (newValue == null) {
					editButton.setDisable(true);
					deleteButton.setDisable(true);
				} else {
					editButton.setDisable(false);
					deleteButton.setDisable(false);
				}
				selectedNote.set(newValue);
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

	private List<Note> getNotes() {
		List<Note> notes = new ArrayList<>();
		List<Note> allNotes = noteDao.getAll();
		for (Note n : allNotes) {
			if (n.getTask() != null)
				if (n.getTask().getTaskID().equals(taskModel.getTaskId())) {
					notes.add(n);
				}
		}
		return notes;

	}
}
