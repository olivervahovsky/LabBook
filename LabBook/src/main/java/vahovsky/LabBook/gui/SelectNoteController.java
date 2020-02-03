package vahovsky.LabBook.gui;

import java.time.LocalDate;
import java.util.LinkedHashMap;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.fxmodels.NoteFxModel;
import vahovsky.LabBook.fxmodels.TaskFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.NoteDAO;

public class SelectNoteController {

	private Utilities util = new Utilities();

	private NoteDAO noteDao;
	private ObservableList<Note> notesModel;
	private Map<String, BooleanProperty> columnsVisibility;
	private ObjectProperty<Note> selectedNote;
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
		noteDao = DAOfactory.INSTANCE.getNoteDAO();
		columnsVisibility = new LinkedHashMap<>();
		selectedNote = new SimpleObjectProperty<>();
		taskModel = new TaskFxModel(task);
		this.project = project;
	}

	@FXML
	void initialize() {
		notesModel = FXCollections.observableArrayList(noteDao.getNotes(taskModel));

		notesTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					if (mouseEvent.getClickCount() == 2) {
						EditNoteController editNoteController = new EditNoteController(selectedNote.get());
						util.showModalWindow(editNoteController, "editNote.fxml", "Note Editing", null);
						notesModel.setAll(noteDao.getNotes(taskModel));
					}
				}
			}
		});

		editButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				EditNoteController editNoteController = new EditNoteController(selectedNote.get());
				util.showModalWindow(editNoteController, "editNote.fxml", "Note Editing", null);
				notesModel.setAll(noteDao.getNotes(taskModel));
			}
		});

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NoteFxModel noteFxModel = new NoteFxModel(selectedNote.get());
				DeleteEntityController deleteNoteController = new DeleteEntityController(
						DAOfactory.INSTANCE.getNoteDAO(), noteFxModel);
				util.showModalWindow(deleteNoteController, "deleteNote.fxml", "Note Deleting", null);
				notesModel.setAll(noteDao.getNotes(taskModel));
			}
		});

		newNoteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NewNoteController newNoteController = new NewNoteController(taskModel.getEntity());
				util.showModalWindow(newNoteController, "newNote.fxml", "New Note", null);
				notesModel.setAll(noteDao.getNotes(taskModel));
			}
		});

		tasksButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				SelectTaskController controller = new SelectTaskController(project);
				util.showModalWindow(controller, "selectTask.fxml", "Projects", tasksButton);
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

	
}
