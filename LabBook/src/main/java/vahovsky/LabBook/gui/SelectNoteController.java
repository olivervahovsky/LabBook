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
import javafx.stage.Stage;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.fxmodels.NoteFxModel;
import vahovsky.LabBook.fxmodels.TaskFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.NoteDAO;


public class SelectNoteController {
	
	private Utilities util = new Utilities();

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
						util.showModalWindow(editNoteController, "editNote.fxml", "Note Editing");
						notesModel.setAll(getNotes());
					}
				}
			}
		});

		editButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				EditNoteController editNoteController = new EditNoteController(selectedNote.get());
				util.showModalWindow(editNoteController, "editNote.fxml", "Note Editing");
				notesModel.setAll(getNotes());
			}
		});

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NoteFxModel noteFxModel = new NoteFxModel(selectedNote.get());
				DeleteEntityController deleteNoteController = new DeleteEntityController(DAOfactory.INSTANCE.getNoteDAO(), noteFxModel);
				util.showModalWindow(deleteNoteController, "deleteNote.fxml", "Note Deleting");
				notesModel.setAll(getNotes());
			}
		});

		newNoteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NewNoteController newNoteController = new NewNoteController(taskModel.getEntity());
				util.showModalWindow(newNoteController, "newNote.fxml", "New Note");
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

	private List<Note> getNotes() {
		List<Note> notes = new ArrayList<>();
		List<Note> allNotes = noteDao.getAll();
		for (Note note : allNotes) {
			if (note.getTask() != null)
				if (note.getTask().getEntityID().equals(taskModel.getTaskId())) {
					notes.add(note);
				}
		}
		return notes;

	}
}
