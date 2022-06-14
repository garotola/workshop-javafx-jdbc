package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import app.App;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

    @FXML private MenuItem menuItemSeller;
    @FXML private MenuItem menuItemDepartment;
    @FXML private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemSellerAction(){
        System.out.println("Aqui foi!");
    }

    public void onMenuItemDepartmentAction(){
        System.out.println("Aqui foi!");
    }

    public void onMenuItemAboutAction(){
        loadView("/gui/About.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        
    }

    private void loadView(String absoluteName){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();
            Scene mainScene = App.getMainScene();
            VBox mainVbox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();
            Node mainMenu = mainVbox.getChildren().get(0);
            mainVbox.getChildren().clear();
            mainVbox.getChildren().add(mainMenu);
            mainVbox.getChildren().addAll(newVBox.getChildren());

        }catch(IOException e){
            Alerts.showAlert("IO Exception", "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
        }
    }
    
}
