package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
        System.out.println("Aqui foi!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        
    }
    
}
