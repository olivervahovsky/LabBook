package vahovsky.LabBook.gui;

import java.io.IOException;

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
import javafx.stage.Stage;
import vahovsky.LabBook.business.UserIdentificationManager;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.fxmodels.LaboratoryFxModel;
import vahovsky.LabBook.fxmodels.UserFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.LaboratoryDAO;
import vahovsky.LabBook.persistent.UserDAO;

public class AdminPageController {

	@FXML
	private Button deleteButton;

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

	private Utilities util;

	private UserDAO userDao;
	private UserFxModel selectedUserModel;

	@FXML
	private ComboBox<User> userComboBox;

	private LaboratoryDAO laboratoryDao;
	private LaboratoryFxModel selectedLaboratoryModel;

	@FXML
	private ComboBox<Laboratory> laboratoriesComboBox;

	public AdminPageController() {
		util = new Utilities();
		userDao = DAOfactory.INSTANCE.getUserDAO();
		selectedUserModel = new UserFxModel();
		laboratoryDao = DAOfactory.INSTANCE.getLaboratoryDAO();
		selectedLaboratoryModel = new LaboratoryFxModel();
	}

	/**
	 * Method that initializes the scene - gives roles and functionality to every
	 * button on the scene
	 */
	@FXML
	void initialize() {
		// nastavíme položky comboboxu userov ako observable list na základe výpisu
		// userov v databáze
		userComboBox.setItems(FXCollections.observableList(userDao.getAll()));
		// zoberieme model comboboxu, z neho model selekcie a napokon model vybraného
		// objektu. Naň pridáme changelistenera - ten je notifikovaný, kedykoľvek sa
		// zmení Observable Value. Potom sa nastaví inštancia userFxModelu na novú
		// value.
		userComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {

			@Override
			public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
				if (newValue != null) {
					selectedUserModel.setByUser(newValue);
				}
			}
		});

		laboratoriesComboBox.setItems(FXCollections.observableArrayList(laboratoryDao.getAll()));
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
				EditAdminController editController = new EditAdminController(UserIdentificationManager.getAdmin());
				util.showModalWindow(editController, "editAdmin.fxml", "Admin editing");
			}
		});

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DeleteEntityController deleteController = new DeleteEntityController(DAOfactory.INSTANCE.getUserDAO(),
						selectedUserModel);
				util.showModalWindow(deleteController, "deleteUserAdmin.fxml", "Admin deleting");
				userComboBox.setItems(FXCollections.observableList(userDao.getAll()));
			}
		});

		deleteLabButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DeleteEntityController deleteController = new DeleteEntityController(
						DAOfactory.INSTANCE.getLaboratoryDAO(), selectedLaboratoryModel);
				util.showModalWindow(deleteController, "deleteLaboratory.fxml", "Delete Laboratory");
				laboratoriesComboBox.setItems(FXCollections.observableList(laboratoryDao.getAll()));
			}
		});

		createButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NewLaboratoryController laboratoryController = new NewLaboratoryController();
				util.showModalWindow(laboratoryController, "newLaboratory.fxml", "New Laboratory");
				laboratoriesComboBox.setItems(FXCollections.observableList(laboratoryDao.getAll()));
			}
		});

		editButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				EditLaboratoryController laboratoryController = new EditLaboratoryController(
						selectedLaboratoryModel.getEntity());
				util.showModalWindow(laboratoryController, "editLaboratory.fxml", "Laboratory editing");
				laboratoriesComboBox.setItems(FXCollections.observableList(laboratoryDao.getAll()));
			}
		});

		newAdminButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NewAdminController newAdminController = new NewAdminController();
				util.showModalWindow(newAdminController, "newAdmin.fxml", "New Admin");
			}
		});

	}

	/**
	 * Method to sign out from this window, which means return to the frontpage
	 * (login and registration) window
	 */
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
