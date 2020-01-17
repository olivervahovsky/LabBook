package vahovsky.LabBook.gui;

import java.time.LocalDateTime;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import vahovsky.LabBook.business.UserIdentificationManager;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.fxmodels.TaskFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.NoteDAO;

public class NewNoteController {
	
	private Utilities util;

	@FXML
	private TextArea noteTextArea;

	@FXML
	private Button saveButton;

	private TaskFxModel taskModel;

	public NewNoteController(Task task) {
		util = new Utilities();
		this.taskModel = new TaskFxModel(task);
	}

	@FXML
	void initialize() {

		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Note note = new Note();
				String text = noteTextArea.getText();
				if (text.isEmpty()) {
					util.showWrongDataInputWindow("WrongDataInput.fxml", "Wrong data");
				} else {
					note.setText(text);
					note.setTask(taskModel.getEntity());
					note.setAuthor(UserIdentificationManager.getUser());
					note.setTimestamp(LocalDateTime.now());
					NoteDAO noteDao = DAOfactory.INSTANCE.getNoteDAO();
					noteDao.addNote(note);
				}
				saveButton.getScene().getWindow().hide();
			}
		});
	}

}
