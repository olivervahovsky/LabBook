package vahovsky.LabBook.gui;

import java.io.IOException;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.fxmodels.UserFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ProjectDAO;

public class NewProjectController {

	@FXML
	private Button saveButton;

	@FXML
	private TextField nameTextField;

	@FXML
	private DatePicker fromDatePicker;

	@FXML
	private DatePicker untilDatePicker;

	private UserFxModel userModel;

	public NewProjectController(User user) {
		userModel = new UserFxModel(user);
	}

	@FXML
	void initialize() {

		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String name = nameTextField.getText();
				LocalDate from = fromDatePicker.getValue();
				LocalDate until = untilDatePicker.getValue();

				if (name.isEmpty() || from == null || until == null) {
					showWrongDataInputWindow();
				} else {
					Project project = new Project(name, from, until, true);
					project.setCreatedBy(userModel.getUser());
					ProjectDAO projectDao = DAOfactory.INSTANCE.getProjectDAO();
					projectDao.addProject(project);
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
}
