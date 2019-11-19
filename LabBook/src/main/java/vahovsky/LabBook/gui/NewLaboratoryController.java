package vahovsky.LabBook.gui;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.LaboratoryDAO;

public class NewLaboratoryController {

	@FXML
	private Button saveButton;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField locationTextField;

	private LaboratoryDAO laboratoryDao = DAOfactory.INSTANCE.getLaboratoryDAO();

	@FXML
	void initialize() {
		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String name = nameTextField.getText();
				String location = locationTextField.getText();

				if (name.isEmpty() || location.isEmpty()) {
					showWrongDataInputWindow();
				} else if (!isAvailable(name)) {
					showTakenNameWindow();
				} else {
					Laboratory laboratory = new Laboratory(name, location);
					LaboratoryDAO laboratoryDao = DAOfactory.INSTANCE.getLaboratoryDAO();
					laboratoryDao.addLaboratory(laboratory);
					saveButton.getScene().getWindow().hide();
				}

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

	private void showTakenNameWindow() {
		WrongDataInputController controller = new WrongDataInputController();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("takenName.fxml"));
			loader.setController(controller);

			Parent parentPane = loader.load();
			Scene scene = new Scene(parentPane);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.setTitle("Taken Name");
			stage.show();

		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}

	private boolean isAvailable(String name) {
		List<Laboratory> labs = laboratoryDao.getAll();
		for (Laboratory l : labs) {
			if (l.getName().equals(name)) {
				return false;
			}
		}
		return true;
	}

//	private void showModalWindow(Object controller, String fxml) {
//		try {
//			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
//			fxmlLoader.setController(controller);
//			Parent rootPane = fxmlLoader.load();
//			Scene scene = new Scene(rootPane);
//
//			Stage dialog = new Stage();
//			dialog.setScene(scene);
//			dialog.initModality(Modality.APPLICATION_MODAL);
//			dialog.showAndWait();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
