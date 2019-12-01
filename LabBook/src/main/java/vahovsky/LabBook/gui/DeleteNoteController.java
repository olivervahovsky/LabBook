package vahovsky.LabBook.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
		this.noteModel = new NoteFxModel(note);
	}

	@FXML
	void initialize() {
		Utilities.initialize(yesButton, noButton, noteDao, noteModel);
	}

}
