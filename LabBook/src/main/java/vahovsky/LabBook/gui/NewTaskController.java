package vahovsky.LabBook.gui;

import java.time.LocalDate;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import vahovsky.LabBook.business.UserIdentificationManager;
import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.fxmodels.LaboratoryFxModel;
import vahovsky.LabBook.fxmodels.ProjectFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.LaboratoryDAO;
import vahovsky.LabBook.persistent.TaskDAO;

public class NewTaskController {
	
	private Utilities util;

	@FXML
	private Button addButton;

	@FXML
	private Button saveButton;

	@FXML
	private TextField nameTextField;

	@FXML
	private DatePicker fromDatePicker;

	@FXML
	private DatePicker untilDatePicker;

	@FXML
	private ComboBox<Laboratory> laboratoryComboBox;

	@FXML
	private TableView<Item> itemsTableView;

	@FXML
	private Button removeButton;

	private TaskDAO taskDao = DAOfactory.INSTANCE.getTaskDAO();
	private ProjectFxModel projectModel;

	private LaboratoryDAO laboratoryDao;
	private LaboratoryFxModel selectedLaboratoryModel;

	public NewTaskController(Project project) {
		util = new Utilities();
		projectModel = new ProjectFxModel(project);
		laboratoryDao = DAOfactory.INSTANCE.getLaboratoryDAO();
		selectedLaboratoryModel = new LaboratoryFxModel();
	}

	@FXML
	void initialize() {

		List<Laboratory> laboratories = laboratoryDao.getAll();
		laboratoryComboBox.setItems(FXCollections.observableList(laboratories));
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
				String name = nameTextField.getText();
				LocalDate from = fromDatePicker.getValue();
				LocalDate until = untilDatePicker.getValue();

				if (name.isEmpty()) {
					WrongDataInputController controller = new WrongDataInputController();
					util.showModalWindow(controller, "WrongDataInput.fxml", "Wrong data", null);
				} else if (!taskDao.isAvailable(name)) {
					WrongDataInputController controller = new WrongDataInputController();
					util.showModalWindow(controller, "takenName.fxml", "Taken Name", null);
				} else {
					Task task = new Task(name, from, until, projectModel.getEntity());
					if (selectedLaboratoryModel.getEntity() != null) {
						task.setLaboratory(selectedLaboratoryModel.getEntity());
					}
					task.setCreatedBy(UserIdentificationManager.getUser());
					TaskDAO taskDao = DAOfactory.INSTANCE.getTaskDAO();
					taskDao.addTask(task);
					saveButton.getScene().getWindow().hide();
				}

			}
		});

	}
	
}
