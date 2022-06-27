package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;


public class SellerFormController implements Initializable{
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    private SellerService service;
    private Seller entity;
    @FXML private Button btSave;
    @FXML private Button btCancel;
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private Label labelErrorName; 
    
    public void setSeller(Seller seller){
        entity = seller;
    }

    public void subscribeDataChangeListener(DataChangeListener listerner){
        dataChangeListeners.add(listerner);
    }

    public void onBtSaveAction(ActionEvent event){
        if(service == null || entity == null) throw new IllegalStateException("Vendedor ou Serviço vazios");
        try{
            entity = getFormData();
            service.updateOrSave(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }catch(DbException e){
            Alerts.showAlert("Erro ao salvar o vendedor", null, e.getMessage(), AlertType.ERROR);
        }catch(ValidationException e){
            setErrosMessages(e.getErrors());
        }
    }
    
    private void notifyDataChangeListeners() {
        dataChangeListeners.forEach(
            listerner -> listerner.onDataChanged()
        );
    }

    private Seller getFormData() {
        Seller seller = new Seller();
        ValidationException exception = new ValidationException("Erro de Validação");
        seller.setId(Utils.tryParseInt(txtId.getText()));
        if(txtName.getText() == null || txtName.getText().trim().equals(""))
            exception.addError("name", "O campo nao pode ser vazio");
        seller.setName(txtName.getText());
        if(exception.getErrors().size() > 0) throw exception;
        return seller;
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
        if(entity == null) throw new IllegalStateException("Vendedor Vazio");
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }

    public void setSellerService(SellerService sellerService) {
        this.service = sellerService;
    }
    
    private void setErrosMessages(Map<String, String> map){
        Set<String> fields = map.keySet();
        if(fields.contains("name")) {
            labelErrorName.setText(map.get("name"));
        }
    }

}