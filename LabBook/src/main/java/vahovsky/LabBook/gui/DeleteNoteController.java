package vahovsky.LabBook.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.fxmodels.NoteFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.NoteDAO;

public class DeleteNoteController {
	private NoteDAO noteDao = DAOfactory.INSTANCE.getNoteDAO();
	private NoteFxModel noteModel;
	
	@FXML
	private Button yesButton;

	@FXML
	private Button noButton;

	public DeleteNoteController(Note note) {
		//this.note = note;
		this.noteModel = new NoteFxModel(note);
	}

	@FXML
	void initialize() {

		yesButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					noteDao.deleteNote(noteModel.getNote());
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
