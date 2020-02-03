package vahovsky.LabBook.gui;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import vahovsky.LabBook.business.ExportUserDataToExcelManager;
import vahovsky.LabBook.business.UserIdentificationManager;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.fxmodels.ProjectFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ProjectDAO;

public class SelectProjectController {
	
	private Utilities util = new Utilities();

	private ProjectDAO projectDao = DAOfactory.INSTANCE.getProjectDAO();
	private ObservableList<Project> projectsModel;
	private Map<String, BooleanProperty> columnsVisibility = new LinkedHashMap<>();
	private ObjectProperty<Project> selectedProject = new SimpleObjectProperty<>();

	@FXML
	private TableView<Project> projectsTableView;

	@FXML
	private Button openButton;

	@FXML
	private Button editButton;

	@FXML
	private Button newProjectButton;

	@FXML
	private Button deleteButton;

	@FXML
	private Button signOutButton;

	@FXML
	private Button editAccount;

	@FXML
	private Button exportDataButton;

	public SelectProjectController() {
	}

	@FXML
	void initialize() {
		projectsModel = FXCollections.observableArrayList(projectDao.getProjects());

		//https://stackoverflow.com/questions/26563390/detect-doubleclick-on-row-of-tableview-javafx
		projectsTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					if (mouseEvent.getClickCount() == 2) {
						SelectTaskController controller = new SelectTaskController(selectedProject.get());
						util.showModalWindow(controller, "selectTask.fxml", "Tasks", openButton);
					}
				}
			}
		});

		exportDataButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					ExportUserDataToExcelManager.exportUserData(UserIdentificationManager.getUser());
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

		editButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				EditProjectController editController = new EditProjectController(selectedProject.get());
				util.showModalWindow(editController, "editProject.fxml", "Project Editing", null);
				projectsModel.setAll(projectDao.getProjects());
			}
		});

		openButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				SelectTaskController controller = new SelectTaskController(selectedProject.get());
				util.showModalWindow(controller, "selectTask.fxml", "Tasks", openButton);
			}
		});

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ProjectFxModel projectFxModel = new ProjectFxModel(selectedProject.get());
				DeleteEntityController deleteProjectController = new DeleteEntityController(DAOfactory.INSTANCE.getProjectDAO(), projectFxModel);
				util.showModalWindow(deleteProjectController, "deleteProject.fxml", "Project Deleting", null);
				projectsModel.setAll(projectDao.getProjects());
			}
		});

		signOutButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FrontPageController controller = new FrontPageController();
				util.showModalWindow(controller, "../app/frontPage.fxml", "LabBook login", signOutButton);
			}
		});
		newProjectButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NewProjectController newProjectController = new NewProjectController(
						UserIdentificationManager.getUser());
				util.showModalWindow(newProjectController, "newProject.fxml", "New Project", null);
				projectsModel.setAll(projectDao.getProjects());
			}
		});

		editAccount.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				EditUserController editUserController = new EditUserController(UserIdentificationManager.getUser());
				util.showModalWindow(editUserController, "editUser.fxml", "Account Editing", null);

			}
		});

		TableColumn<Project, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		projectsTableView.getColumns().add(nameCol);
		columnsVisibility.put("ID", nameCol.visibleProperty());

		projectsTableView.setItems(projectsModel);
		projectsTableView.setEditable(true);

		ContextMenu contextMenu = new ContextMenu();
		for (Entry<String, BooleanProperty> entry : columnsVisibility.entrySet()) {
			CheckMenuItem menuItem = new CheckMenuItem(entry.getKey());
			menuItem.selectedProperty().bindBidirectional(entry.getValue());
			contextMenu.getItems().add(menuItem);
		}
		projectsTableView.setContextMenu(contextMenu);

		projectsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Project>() {

			@Override
			public void changed(ObservableValue<? extends Project> observable, Project oldValue, Project newValue) {
				if (newValue == null) {
					editButton.setDisable(true);
					deleteButton.setDisable(true);
				} else {
					editButton.setDisable(false);
					deleteButton.setDisable(false);
				}
				selectedProject.set(newValue);
			}
		});
	}

}
