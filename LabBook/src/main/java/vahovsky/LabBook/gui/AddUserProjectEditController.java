package vahovsky.LabBook.gui;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.fxmodels.ProjectFxModel;
import vahovsky.LabBook.fxmodels.UserFxModel;
import vahovsky.LabBook.persistent.UserDAO;

public class AddUserProjectEditController {

	@FXML
    private Button addButton;

	@FXML
    private ComboBox<User> userComboBox;
	
	private ProjectFxModel project; 
	private UserDAO userDao;
	private UserFxModel selectedUserModel;
	//private ObservableList<User> userModel;
	
	public AddUserProjectEditController(ProjectFxModel project) {
		this.project = project;
	}

    @FXML
    void initialize() {
    	//userModel = FXCollections.observableArrayList(userDao.getAll());

		List<User> users = userDao.getAll();
    	userComboBox.setItems(FXCollections.observableList(users));
    	userComboBox.getSelectionModel().selectedItemProperty()
    		.addListener(new ChangeListener<User>() {

				@Override
				public void changed(ObservableValue<? extends User> observable, 
						User oldValue, User newValue) {
					if (newValue != null) {
						System.out.println(newValue.getName());
						selectedUserModel.setUser(newValue);
					}
				}
			});
    	
    	addButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				List<User> completedBy = project.getCompletedBy();
				completedBy.add(selectedUserModel.getUser());
				project.setCompletedBy(completedBy);
			}
		});
    	
    }
}
