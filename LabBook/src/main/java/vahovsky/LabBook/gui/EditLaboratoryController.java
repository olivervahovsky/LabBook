package vahovsky.LabBook.gui;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.fxmodels.ItemFxModel;
import vahovsky.LabBook.fxmodels.LaboratoryFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.LaboratoryDAO;

public class EditLaboratoryController {
	
	private Utilities util;
	
	@FXML
	private Button saveButton;

	@FXML
	private TableView<Item> itemsTableView;

	@FXML
	private Button addButton;

	@FXML
	private Button deleteButton;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField locationTextField;

	private LaboratoryDAO laboratoryDao;
	private LaboratoryFxModel laboratoryModel;
	
	private ObservableList<Item> listOfItemsModel;
	private ObjectProperty<Item> selectedItem;
	
	private Map<String, BooleanProperty> columnsVisibility;

	public EditLaboratoryController(Laboratory laboratory) {
		util = new Utilities();
		laboratoryDao = DAOfactory.INSTANCE.getLaboratoryDAO();
		laboratoryModel = new LaboratoryFxModel(laboratory);
		selectedItem = new SimpleObjectProperty<>();
		columnsVisibility = new LinkedHashMap<>();
	}

	@FXML
	void initialize() {
		listOfItemsModel = FXCollections.observableArrayList(laboratoryDao.getItemsOfLaboratory(laboratoryModel.getEntity()));
		nameTextField.textProperty().bindBidirectional(laboratoryModel.getNameProperty());
		locationTextField.textProperty().bindBidirectional(laboratoryModel.getLocationProperty());

		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				laboratoryDao.saveLaboratory(laboratoryModel.getEntity());
				saveButton.getScene().getWindow().hide();
			}
		});

		addButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NewItemController newItemController = new NewItemController(laboratoryModel.getEntity());
				util.showModalWindow(newItemController, "newItem.fxml", "New Item", null);
				listOfItemsModel.setAll(laboratoryDao.getItemsOfLaboratory(laboratoryModel.getEntity()));
			}
		});

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ItemFxModel itemFxModel = new ItemFxModel(selectedItem.get());
				DeleteEntityController deleteItemController = new DeleteEntityController(DAOfactory.INSTANCE.getItemDAO(), itemFxModel);
				util.showModalWindow(deleteItemController, "deleteItem.fxml", "Delete Item", null);
				listOfItemsModel.setAll(laboratoryDao.getItemsOfLaboratory(laboratoryModel.getEntity()));

			}
		});

		TableColumn<Item, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		itemsTableView.getColumns().add(nameCol);
		columnsVisibility.put("name", nameCol.visibleProperty());

		TableColumn<Item, Integer> quantityCol = new TableColumn<>("Quantity");
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		itemsTableView.getColumns().add(quantityCol);
		columnsVisibility.put("quantity", quantityCol.visibleProperty());

		itemsTableView.setItems(listOfItemsModel);
		itemsTableView.setPlaceholder(new Label("No rows to display"));
		itemsTableView.setEditable(true);

		ContextMenu contextMenu = new ContextMenu();
		for (Entry<String, BooleanProperty> entry : columnsVisibility.entrySet()) {
			CheckMenuItem menuItem = new CheckMenuItem(entry.getKey());
			menuItem.selectedProperty().bindBidirectional(entry.getValue());
			contextMenu.getItems().add(menuItem);
		}
		itemsTableView.setContextMenu(contextMenu);

		itemsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Item>() {

			@Override
			public void changed(ObservableValue<? extends Item> observable, Item oldValue, Item newValue) {
				if (newValue == null) {
					// ak práve nie je vybraný žiadny item, tlačítko delete je deaktivované
					deleteButton.setDisable(true);
				} else {
					// ak je vybraný nejaký item, tlačítko delete je aktívne
					deleteButton.setDisable(false);
				}
				selectedItem.set(newValue);
			}
		});

	}

}
