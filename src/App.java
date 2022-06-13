import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class App extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try{ // Loader : Objeto - Para manipular a view antes de carregar.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
            ScrollPane scrollPane = loader.load();
            scrollPane.setFitToHeight(true); scrollPane.setFitToWidth(true); // Ocupar todo o espaço que lhe derem
            Scene mainScene = new Scene(scrollPane);
            stage.setTitle("Simple JavaFx Applicatin");
            stage.setScene(mainScene);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
