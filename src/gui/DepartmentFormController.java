package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable{

    @FXML private Button btSave;
    @FXML private Button btCancel;
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private Label labelErrorName; 
    
    public void onBtSaveAction(){
        System.out.println("OnBtSave");
    }
    public void onBtCancelAction(){
        System.out.println("OnBtCancel");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
        
    }

    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }

    
}