package gui;

import model.entities.Seller;
import model.services.SellerService;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Db.DataBaseIntegrityException;
import app.App;
import gui.util.Alerts;
import gui.util.Utils;
import gui.util.listeners.DataChangeListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SellerListController implements Initializable, DataChangeListener{

    private SellerService service; // Injeção de dependencia e inversao de controle
    @FXML private TableView<Seller> tableViewSeller;
    @FXML private TableColumn<Seller, String> tableColumnName;
    @FXML private TableColumn<Seller, Integer> tableColumnId;
    @FXML private TableColumn<Seller, String> tableColumnEmail;
    @FXML private TableColumn<Seller, Date> tableColumnBirthDate;
    @FXML private TableColumn<Seller, Double> tableColumnBaseSalary;
    @FXML private Button btAdd;
    @FXML private TableColumn<Seller, Seller> tableColumnEdit;
    @FXML private TableColumn<Seller, Seller> tableColumnRemove;
    ObservableList<Seller> obsList;

    public void setSellerService(SellerService service){
        this.service = service;
    }

    public void updateTableView(){
        if(service == null) throw new IllegalStateException("Serviço de Vendedores Vazio");
        List<Seller> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewSeller.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }
    
    @FXML public void onActionBtAdd(ActionEvent event){
        Seller seller = new Seller();
        createDialogForm(seller , "/gui/SellerForm.fxml", Utils.currentStage(event));
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
        
    }

    private void initializeNodes() {
        //Inicializar as tabelas
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
        Stage stage = (Stage)App.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty()); // Fazer com que a tabela acompanhe a altura da janela
    }

    private void createDialogForm(Seller seller, String absoluteName, Stage currentStage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();
            SellerFormController controller = loader.getController();
            controller.setSeller(seller);
            controller.updateFormData();
            controller.setSellerService(new SellerService());
            controller.subscribeDataChangeListener(this);
            Stage formStage = new Stage();
            formStage.setTitle("Novo vendedor");
            formStage.setScene(new Scene(pane)); // 
            formStage.setResizable(false); //  Nao pode ser redimensionado
            formStage.initOwner(currentStage); // Janela Pai
            formStage.initModality(Modality.WINDOW_MODAL); // A janela pai não funciona enquanto a atual estiver aberta
            formStage.showAndWait();

        } catch (IOException e) {
            Alerts.showAlert("Erro ao criar o formulário", "Erro", "", AlertType.ERROR);
        }
    }

    @Override
    public void onDataChanged() {
       updateTableView();        
    }
    
    private void initEditButtons(){ // Criar button de Edição dos Registros
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Seller, Seller>(){
            private final Button button = new Button("Edit");
            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if(obj == null){
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                    event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event))
                );
            }
        });
    }

    private void initRemoveButtons(){
        tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRemove.setCellFactory(param -> new TableCell<Seller, Seller>(){
            private final Button button = new Button("Remover");
            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if(obj == null){
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Seller obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Quer mesmo apagar ? ");
        if(result.get() == ButtonType.OK){
            if(service == null) throw new IllegalStateException("Serviço Vazio");
            try{
                service.remove(obj);
                updateTableView();
            }catch(DataBaseIntegrityException e){
                Alerts.showAlert("Error ao remover o objeto", null, e.getMessage(), AlertType.ERROR);
            }
        }
    }
}
