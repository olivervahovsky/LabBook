package vahovsky.LabBook.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.fxmodels.LaboratoryFxModel;
import vahovsky.LabBook.fxmodels.TaskFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.LaboratoryDAO;
import vahovsky.LabBook.persistent.TaskDAO;

public class EditTaskController {

	@FXML
	private Button saveButton;

	@FXML
	private ComboBox<Laboratory> laboratoryComboBox;

	@FXML
	private DatePicker untilDatePicker;

	@FXML
	private DatePicker fromDatePicker;

	@FXML
	private TextField nameTextField;

	private TaskDAO taskDao;
	private TaskFxModel taskModel;
	private LaboratoryDAO laboratoryDao;
	private LaboratoryFxModel selectedLaboratoryModel;

	public EditTaskController(Task task) {
		taskDao = DAOfactory.INSTANCE.getTaskDAO();
		taskModel = new TaskFxModel(task);
		laboratoryDao = DAOfactory.INSTANCE.getLaboratoryDAO();
		selectedLaboratoryModel = new LaboratoryFxModel();
	}

	@FXML
	void initialize() {

		nameTextField.textProperty().bindBidirectional(taskModel.getNameProperty());
		fromDatePicker.valueProperty().bindBidirectional(taskModel.getFromProperty());
		untilDatePicker.valueProperty().bindBidirectional(taskModel.getUntilProperty());

		laboratoryComboBox.setItems(FXCollections.observableList(laboratoryDao.getAll()));
		laboratoryComboBox.getSelectionModel().select(taskModel.getLaboratory());
		laboratoryComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Laboratory>() {

			@Override
			public void changed(ObservableValue<? extends Laboratory> observable, Laboratory oldValue,
					Laboratory newValue) {
				if (newValue != null) {
					selectedLaboratoryModel.setLaboratory(newValue);
				}
			}
		});
		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				taskModel.setLaboratory(selectedLaboratoryModel.getEntity());
				taskDao.saveTask(taskModel.getEntity());
				saveButton.getScene().getWindow().hide();
			}
		});
	}
}
