package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import Db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import gui.util.listeners.DataChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;


public class SellerFormController implements Initializable{
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    private SellerService service;
    private DepartmentService departmentService;

    private Seller entity;
    @FXML private Button btSave;
    @FXML private Button btCancel;
    // Campos de Inserção
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private DatePicker dtBirthDate;
    @FXML private TextField txtBaseSalary;
    @FXML private ComboBox<Department> comboBoxDepartment; 
    // Campos de Erros
    @FXML private Label labelErrorName; 
    @FXML private Label labelErrorEmail; 
    @FXML private Label labelErrorBirthDate; 
    @FXML private Label labelErrorBaseSalary; 

    private ObservableList<Department> obsList;
    
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

        if(txtEmail.getText() == null || txtEmail.getText().trim().equals(""))
            exception.addError("email", "O campo nao pode ser vazio");
        seller.setEmail(txtEmail.getText());
        
        if(dtBirthDate.getValue() == null) exception.addError("birthDate", "O campo nao pode ser vazio");
        else{
            Instant instant = Instant.from(dtBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
            seller.setBirthDate(Date.from(instant));
        }

        if(txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals(""))
            exception.addError("baseSalary", "O campo nao pode ser vazio");
        seller.setBaseSalary(Utils.tryParseDouble(txtBaseSalary.getText()));

        seller.setDepartment(comboBoxDepartment.getValue());

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
        Constraints.setTextFieldMaxLength(txtName, 70);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail, 100);
        Utils.formatDatePicker(dtBirthDate, "dd/MM/yyyy");
        initializeComboBoxDepartment();
    }

    public void updateFormData(){
        if(entity == null) throw new IllegalStateException("Vendedor Vazio");
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
        txtEmail.setText(entity.getEmail());
        txtBaseSalary.setText(String.valueOf(entity.getBaseSalary()));
        if(entity.getBirthDate() != null) dtBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
        if(entity.getDepartment() == null) comboBoxDepartment.getSelectionModel().selectFirst();
        else comboBoxDepartment.setValue(entity.getDepartment());
    }

    public void loadAssociateObjects(){
        List<Department> list = departmentService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxDepartment.setItems(obsList);
    }

    public void setServices(SellerService sellerService, DepartmentService departmentService) {
        this.service = sellerService;
        this.departmentService = departmentService;
    }

    
    
    private void setErrosMessages(Map<String, String> map){
        Set<String> fields = map.keySet();
        
        labelErrorName.setText((fields.contains("name") ? map.get("name") : ""));
        labelErrorEmail.setText((fields.contains("email") ? map.get("email") : ""));
        labelErrorBaseSalary.setText((fields.contains("baseSalary") ? map.get("baseSalary") : ""));
        labelErrorBirthDate.setText((fields.contains("birthDate") ? map.get("birthDate") : ""));

    }

    private void initializeComboBoxDepartment(){
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>(){
            @Override
            protected void updateItem(Department item, boolean empty)  {
                super.updateItem(item, empty);
                setText(empty ? " " : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }
}