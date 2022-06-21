package gui;

import model.entities.Department;
import model.services.DepartmentService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import app.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DepartmentListController implements Initializable{

    private DepartmentService service; // Injeção de dependencia e inversao de controle
    @FXML private TableView<Department> tableViewDepartment;
    @FXML private TableColumn<Department, String> tableColumnName;
    @FXML private TableColumn<Department, Integer> tableColumnId;
    @FXML private Button btAdd;
    ObservableList<Department> obsList;

    public void setDepartmentService(DepartmentService service){
        this.service = service;
    }

    public void updateTableView(){
        if(service == null) throw new IllegalStateException("Serviço de Departamento Vazio");
        List<Department> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewDepartment.setItems(obsList);
    }
    
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
