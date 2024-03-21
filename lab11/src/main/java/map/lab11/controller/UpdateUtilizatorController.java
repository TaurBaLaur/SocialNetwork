package map.lab11.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.lab11.domain.Utilizator;
import map.lab11.domain.validators.ValidationException;
import map.lab11.service.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UpdateUtilizatorController {
    private Service service;

    private ObservableList<Utilizator> usersModel = FXCollections.observableArrayList();

    private Stage stage;
    private Long idUser;
    @FXML
    TextField prenume;
    @FXML
    TextField nume;

    public void setService(Service service) {
        this.service = service;
        //service.addObserver(this);
        //initModel();
    }
    public void setModel(ObservableList<Utilizator> model) {
        this.usersModel = model;
        //service.addObserver(this);
        //initModel();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    private void initModel() {
        usersModel.setAll(StreamSupport.stream(service.getAllUsers().spliterator(),
                false).collect(Collectors.toList()));
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void clickActualizare(ActionEvent actionEvent) {
        String firstName=prenume.getText();
        String lastName=nume.getText();
        try{
            service.actualizeazaUtilizator(idUser,firstName,lastName);
            initModel();
            MessageAlert.showInfoMessage(null, "Utilizator actualizat cu succes!");
        }catch (ValidationException e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        finally {
            stage.close();
        }
    }
}
