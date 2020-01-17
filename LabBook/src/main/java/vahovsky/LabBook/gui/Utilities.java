package vahovsky.LabBook.gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Utilities {
	
	public void showModalWindow(Object controller, String fxml, String title) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
			System.out.println(getClass());
			System.out.println(getClass().getResource(fxml));
			fxmlLoader.setController(controller);
			
			Parent rootPane = fxmlLoader.load();
			Scene scene = new Scene(rootPane);

			Stage dialog = new Stage();
			dialog.setScene(scene);
			dialog.setTitle(title);
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.showAndWait();
			
		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}
	
	public void showWrongDataInputWindow(String fxml, String title) {
		WrongDataInputController controller = new WrongDataInputController();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
			System.out.println(getClass());
			System.out.println(getClass().getResource(fxml));
			fxmlLoader.setController(controller);

			Parent rootPane = fxmlLoader.load();
			Scene scene = new Scene(rootPane);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle(title);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();

		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}

}
