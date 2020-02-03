package vahovsky.LabBook.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vahovsky.LabBook.gui.FrontPageController;

public class App extends Application {

	
	/**
	 * Method that runs the app.
	 * - loads graphic interface of the frontpage
	 * - loads controller of the frontpage
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		FrontPageController mainController = new FrontPageController();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("frontPage.fxml"));
		fxmlLoader.setController(mainController);
		Parent rootPane = fxmlLoader.load();

		Scene scene = new Scene(rootPane);
		primaryStage.setTitle("LabBook login");
		primaryStage.setScene(scene);
		primaryStage.show();
		
//		File file = new File("C:/Users/Oliver/git/LabBook/LabBook/target/classes/vahovsky/LabBook/gui");
//		File[] listOfFiles = file.listFiles();
//		for(File item: listOfFiles)
//			System.out.println(item);
	}

	public static void main(String[] args) {
		launch(args);

	}

}
