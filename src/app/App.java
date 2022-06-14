package app;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class App extends Application{

    public static Scene getMainScene(){
        return mainScene;
    }

    private static Scene mainScene;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try{ // Loader : Objeto - Para manipular a view antes de carregar.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
            ScrollPane scrollPane = loader.load();
            scrollPane.setFitToHeight(true); scrollPane.setFitToWidth(true); // Ocupar todo o espaço que lhe derem
            mainScene = new Scene(scrollPane);
            stage.setTitle("Gestão de Vendendores e Departamentos");
            stage.setScene(mainScene);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
