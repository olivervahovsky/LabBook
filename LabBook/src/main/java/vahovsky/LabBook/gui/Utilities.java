package vahovsky.LabBook.gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Utilities {

	/**
	 * Method that hides the former window and opens new window. Window that appears
	 * depends on the fxml file.
	 * 
	 * @param controller controller of the window that is going to appear
	 * @param fxml       fxml file of the window that is going to appear
	 * @param title  title of the window that is going to appear
	 * @param button	button that closes the window
	 */
	public void showModalWindow(Object controller, String fxml, String title, Button button) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
			fxmlLoader.setController(controller);

			Parent rootPane = fxmlLoader.load();
			Scene scene = new Scene(rootPane);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle(title);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
			if (button != null)
				button.getScene().getWindow().hide();

		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}

}
