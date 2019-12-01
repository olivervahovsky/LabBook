package vahovsky.LabBook.gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
//	private ObservableList<Laboratory> laboratoryModel;
	// private ObjectProperty<Item> selectedItem = new SimpleObjectProperty<>();
//	private ObjectProperty<Laboratory> selectedLaboratory = new SimpleObjectProperty<>();
//	private ObservableList<Item> itemsModel;
//	private ItemDAO itemDao;

	public NewTaskController(Project project) {
		this.projectModel = new ProjectFxModel(project);
		laboratoryDao = DAOfactory.INSTANCE.getLaboratoryDAO();
//		itemDao = DAOfactory.INSTANCE.getItemDAO();
		selectedLaboratoryModel = new LaboratoryFxModel();
	}

	@FXML
	void initialize() {
//		laboratoryModel = FXCollections.observableArrayList(laboratoryDao.getAll());
		// itemsModel = FXCollections.observableArrayList(getItems());

		List<Laboratory> laboratories = laboratoryDao.getAll();
		laboratoryComboBox.setItems(FXCollections.observableList(laboratories));
		laboratoryComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Laboratory>() {

			@Override
			public void changed(ObservableValue<? extends Laboratory> observable, Laboratory oldValue,
					Laboratory newValue) {
				if (newValue != null) {
					// System.out.println(newValue.getName());
					selectedLaboratoryModel.setLaboratory(newValue);
				}
			}
		});

		// TableColumn<Item, String> nameCol = new TableColumn<>("Name");
		// nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		// itemsTableView.getColumns().add(nameCol);
		// columnsVisibility.put("Name", nameCol.visibleProperty());

		// itemsTableView.setItems(itemsModel);
		// itemsTableView.setEditable(true);

		// ContextMenu contextMenu = new ContextMenu();
		// for (Entry<String, BooleanProperty> entry : columnsVisibility.entrySet()) {
		// CheckMenuItem menuItem = new CheckMenuItem(entry.getKey());
		// menuItem.selectedProperty().bindBidirectional(entry.getValue());
		// contextMenu.getItems().add(menuItem);
		// }
		// itemsTableView.setContextMenu(contextMenu);
		//
		// itemsTableView.getSelectionModel().selectedItemProperty().addListener(new
		// ChangeListener<Item>() {
		//
		// @Override
		// public void changed(ObservableValue<? extends Item> observable, Item
		// oldValue, Item newValue) {
		// if (newValue == null) {
		// saveButton.setDisable(true);
		// } else {
		// saveButton.setDisable(false);
		// }
		// selectedItem.set(newValue);
		// }
		// });

		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String name = nameTextField.getText();
				LocalDate from = fromDatePicker.getValue();
				LocalDate until = untilDatePicker.getValue();

				if (name.isEmpty()) {
					showWrongDataInputWindow();
				} else if (!isAvailable(name)) {
					showTakenNameWindow();
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

		// addButton.setOnAction(new EventHandler<ActionEvent>() {
		//
		// @Override
		// public void handle(ActionEvent event) {
		// SelectItemTasksController itemController = new
		// SelectItemTasksController(selectedLaboratory.get());
		// showModalWindow(itemController, "selectItemTasks.fxml");
		//
		// }
		// });
		//
		// removeButton.setOnAction(new EventHandler<ActionEvent>() {
		//
		// @Override
		// public void handle(ActionEvent event) {
		// DeleteItemController deleteItemController = new
		// DeleteItemController(selectedItem.get());
		// showModalWindow(deleteItemController, "deleteItem.fxml");
		// itemsModel.setAll(itemDao.getAll());
		//
		// }
		// });

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

	// private List<Item> getItems() {
	// List<Item> items = new ArrayList<>();
	// List<Item> allItems = itemDao.getAll();
	// for (Item i : allItems) {
	// if (i.getLaboratory() == selectedLaboratoryModel.getLaboratory()) {
	// items.add(i);
	// }
	// }
	// return items;
	//
	// }

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
		List<Task> tasks = taskDao.getAll();
		for (Task t : tasks) {
			if (t.getName().equals(name)) {
				return false;
			}
		}
		return true;
	}
}
