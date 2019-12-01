package vahovsky.LabBook.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.fxmodels.ItemFxModel;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ItemDAO;

public class DeleteItemController {

	@FXML
	private Button yesButton;

	@FXML
	private Button noButton;

	private ItemDAO itemDao = DAOfactory.INSTANCE.getItemDAO();
	private ItemFxModel itemModel;

	public DeleteItemController(Item item) {
		this.itemModel = new ItemFxModel(item);
	}
	
	@FXML
	void initialize() {
		Utilities.initialize(yesButton, noButton, itemDao, itemModel);
	}

}
