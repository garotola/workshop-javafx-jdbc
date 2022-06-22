package gui;

import model.entities.Department;
import model.services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import app.App;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
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
    
    @FXML public void onActionBtAdd(ActionEvent event){
        Department department = new Department();
        createDialogForm(department, "/gui/DepartmentForm.fxml", Utils.currentStage(event));
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
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); // Fazer com que a tabela acompanhe a altura da janela
    }

    private void createDialogForm(Department department, String absoluteName, Stage currentStage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();
            DepartmentFormController controller = loader.getController();
            controller.setDepartment(department);
            controller.updateFormData();
            controller.setDepartmentService(new DepartmentService());
            Stage formStage = new Stage();
            formStage.setTitle("Novo Departamento");
            formStage.setScene(new Scene(pane)); // 
            formStage.setResizable(false); //  Nao pode ser redimensionado
            formStage.initOwner(currentStage); // Janela Pai
            formStage.initModality(Modality.WINDOW_MODAL); // A janela pai não funciona enquanto a atual estiver aberta
            formStage.showAndWait();

        } catch (IOException e) {
            Alerts.showAlert("Erro ao criar o formulário", "Erro", "", AlertType.ERROR);
        }
    }
    
}
