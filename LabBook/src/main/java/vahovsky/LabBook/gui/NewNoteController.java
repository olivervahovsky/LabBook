package vahovsky.LabBook.gui;

import java.io.IOException;
import java.time.LocalDateTime;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vahovsky.LabBook.business.UserIdentificationManager;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.fxmodels.TaskFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.NoteDAO;

public class NewNoteController {

	@FXML
	private TextArea noteTextArea;

	@FXML
	private Button saveButton;

	private TaskFxModel taskModel;

	public NewNoteController(Task task) {
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
					showWrongDataInputWindow();
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

	private void showWrongDataInputWindow() {
		WrongDataInputController controller = new WrongDataInputController();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("WrongDataInput.fxml"));
			loader.setController(controller);

			Parent parentPane = loader.load();
			Scene scene = new Scene(parentPane);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.setTitle("Wrong data");
			stage.show();

		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}
}
