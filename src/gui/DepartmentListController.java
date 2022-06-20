package gui;

import model.entities.Department;
import java.net.URL;
import java.util.ResourceBundle;

import app.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DepartmentListController implements Initializable{

    @FXML private TableView<Department> tableViewDepartment;
    @FXML private TableColumn<Department, String> tableColumnName;
    @FXML private TableColumn<Department, Integer> tableColumnId;
    @FXML private Button btAdd;

    @FXML public void onActionBtAdd(){
        System.out.println("Click");
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
        
    }

    private void initializeNodes() {
        //Inicializar as tabelas
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        Stage stage = (Stage)App.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); // Fazer com a tabela acompanhe a altura da janela
    }
    
}
