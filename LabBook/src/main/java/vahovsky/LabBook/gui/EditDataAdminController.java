package vahovsky.LabBook.gui;

import java.io.IOException;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import vahovsky.LabBook.entities.Admin;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.fxmodels.LaboratoryFxModel;
import vahovsky.LabBook.fxmodels.UserFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.LaboratoryDAO;
import vahovsky.LabBook.persistent.UserDAO;

public class EditDataAdminController {

	private Admin admin;
	private Utilities util = new Utilities();

	@FXML
	private ComboBox<User> userComboBox;

	@FXML
	private Button deleteButton;

	@FXML
	private ComboBox<Laboratory> laboratoriesComboBox;

	@FXML
	private Button editButton;

	@FXML
	private Button createButton;

	@FXML
	private Button signOutButton;

	@FXML
	private Button editAdminButton;

	@FXML
	private Button deleteLabButton;

	@FXML
	private Button newAdminButton;

	private UserDAO userDao;
	private UserFxModel selectedUserModel;
	private ObservableList<User> usersModel;

	private LaboratoryDAO laboratoryDao;
	private LaboratoryFxModel selectedLaboratoryModel;
	private ObservableList<Laboratory> laboratoriesModel;

	public EditDataAdminController(Admin admin) {
		this.admin = admin;
		userDao = DAOfactory.INSTANCE.getUserDAO();
		selectedUserModel = new UserFxModel();
		laboratoryDao = DAOfactory.INSTANCE.getLaboratoryDAO();
		selectedLaboratoryModel = new LaboratoryFxModel();
	}

	@FXML
	void initialize() {
		// inicializujeme Observable list userov, aby sme ich potom mohli
		// prezerať v comboboxe
		usersModel = FXCollections.observableArrayList(userDao.getAll());
		userComboBox.setItems(FXCollections.observableList(userDao.getAll()));
		// zoberieme model comboboxu, z neho model selekcie a napokon model vybraného
		// objektu. Naň pridáme changelistenera - ten je notifikovaný, kedykoľvek sa
		// zmení Observable Value. Potom sa nastaví inštancia userFxModelu na novú
		// value.
		userComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {

			@Override
			public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
				if (newValue != null) {
					selectedUserModel.setUser(newValue);
				}
			}
		});

		laboratoriesModel = FXCollections.observableArrayList(laboratoryDao.getAll());
		laboratoriesComboBox.setItems(laboratoriesModel);
		laboratoriesComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Laboratory>() {

			@Override
			public void changed(ObservableValue<? extends Laboratory> observable, Laboratory oldValue,
					Laboratory newValue) {
				if (newValue != null) {
					selectedLaboratoryModel.setLaboratory(newValue);
				}
			}
		});

		signOutButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				signOut();
			}
		});

		editAdminButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				EditAdminController editController = new EditAdminController(admin);
				util.showModalWindow(editController, "editAdmin.fxml", "Admin editing");
				usersModel.setAll(userDao.getAll());
			}
		});

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				UserFxModel userFxModel = new UserFxModel(selectedUserModel.getEntity());
				DeleteEntityController deleteController = new DeleteEntityController(DAOfactory.INSTANCE.getUserDAO(),
						userFxModel);
				util.showModalWindow(deleteController, "deleteUserAdmin.fxml", "Admin deleting");
				List<User> users = userDao.getAll();
				userComboBox.setItems(FXCollections.observableList(users));
				usersModel.setAll(userDao.getAll());
			}
		});

		deleteLabButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// System.out.println(selectedLaboratoryModel.getLaboratory().toString());
				// DeleteLaboratoryController deleteController = new DeleteLaboratoryController(
				// selectedLaboratoryModel.getLaboratory());
				// showModalWindow(deleteController, "deleteLaboratory.fxml");
				laboratoryDao.deleteEntity(selectedLaboratoryModel.getEntity());
				laboratoriesModel.setAll(laboratoryDao.getAll());
				laboratoriesComboBox.setItems(FXCollections.observableList(laboratoryDao.getAll()));
			}
		});

		createButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NewLaboratoryController laboratoryController = new NewLaboratoryController();
				util.showModalWindow(laboratoryController, "newLaboratory.fxml", "New Laboratory");
				List<Laboratory> laboratories = laboratoryDao.getAll();
				laboratoriesComboBox.setItems(FXCollections.observableList(laboratories));
				laboratoriesModel.setAll(laboratoryDao.getAll());
			}
		});

		editButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				EditLaboratoryController laboratoryController = new EditLaboratoryController(
						selectedLaboratoryModel.getEntity());
				util.showModalWindow(laboratoryController, "editLaboratory.fxml", "Laboratory editing");
				List<Laboratory> laboratories = laboratoryDao.getAll();
				laboratoriesComboBox.setItems(FXCollections.observableList(laboratories));
			}
		});

		newAdminButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NewAdminController newAdminController = new NewAdminController();
				util.showModalWindow(newAdminController, "Admin.fxml", "New Admin");
			}
		});

	}

	public void signOut() {
		FrontPageController controller = new FrontPageController();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../app/frontPage.fxml"));
			loader.setController(controller);

			Parent parentPane = loader.load();
			Scene scene = new Scene(parentPane);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("LabBook login");
			stage.show();
			signOutButton.getScene().getWindow().hide();

		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}

}
