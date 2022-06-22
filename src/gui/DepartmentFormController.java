package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import gui.util.listeners.DataChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable{
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    private DepartmentService service;
    private Department entity;
    @FXML private Button btSave;
    @FXML private Button btCancel;
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private Label labelErrorName; 
    
    public void setDepartment(Department department){
        entity = department;
    }

    public void subscribeDataChangeListener(DataChangeListener listerner){
        dataChangeListeners.add(listerner);
    }

    public void onBtSaveAction(ActionEvent event){
        if(service == null || entity == null) throw new IllegalStateException("Departamento ou Serviço vazios");
        try{
            entity = getFormData();
            service.updateOrSave(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }catch(DbException e){
            Alerts.showAlert("Erro ao salvar o departamento", null, e.getMessage(), AlertType.ERROR);
        }
    }
    
    private void notifyDataChangeListeners() {
        dataChangeListeners.forEach(
            listerner -> listerner.onDataChanged()
        );
    }

    private Department getFormData() {
        return new Department(Utils.tryParseInt(txtId.getText()), txtName.getText());
    }

    public void onBtCancelAction(ActionEvent event){
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
        
    }

    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }

    public void updateFormData(){
        if(entity == null) throw new IllegalStateException("Departamento Vazio");
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }

    public void setDepartmentService(DepartmentService departmentService) {
        this.service = departmentService;
    }
    
}