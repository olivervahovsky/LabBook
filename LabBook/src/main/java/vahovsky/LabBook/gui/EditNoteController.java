package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import vahovsky.LabBook.business.UserIdentificationManager;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.fxmodels.NoteFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.NoteDAO;

public class EditNoteController {

	@FXML
	private Button saveButton;

	@FXML
	private TextArea noteTextArea;

	//private TaskFxModel taskModel;

	private NoteFxModel notesModel;

	private NoteDAO noteDao;

	public EditNoteController(Note note) {
		this.notesModel = new NoteFxModel(note);
		this.noteDao = DAOfactory.INSTANCE.getNoteDAO();
	}

	@FXML
	void initialize() {
		noteTextArea.setText(notesModel.getText());

		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				notesModel.setAuthor(UserIdentificationManager.getUser());
				notesModel.setText(noteTextArea.getText());
				noteDao.saveNote(notesModel.getEntity());
				saveButton.getScene().getWindow().hide();
			}
		});
	}
}
