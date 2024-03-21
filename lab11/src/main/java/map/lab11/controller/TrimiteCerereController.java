package map.lab11.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.lab11.domain.Request;
import map.lab11.domain.validators.ValidationException;
import map.lab11.service.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TrimiteCerereController {
    private Service service;

    private ObservableList<Request> requestsModel = FXCollections.observableArrayList();

    private Stage stage;
    @FXML
    TextField idFrom;
    @FXML
    TextField idTo;

    public void setService(Service service) {
        this.service = service;
        //service.addObserver(this);
        //initModel();
    }
    public void setModel(ObservableList<Request> model) {
        this.requestsModel = model;
        //service.addObserver(this);
        //initModel();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    private void initModel() {
        requestsModel.setAll(StreamSupport.stream(service.getAllRequests().spliterator(),
                false).collect(Collectors.toList()));
    }

    public void clickTrimiteCerere(ActionEvent actionEvent) {
        try {
            Long u1 = Long.valueOf(idFrom.getText());
            Long u2 = Long.valueOf(idTo.getText());
            try {
                if (u1.equals(u2)) {
                    MessageAlert.showErrorMessage(null, "Un utilizator nu poate fi prieten cu el insusi!");
                }
                service.adaugaCerere(u1, u2);
                initModel();
                MessageAlert.showInfoMessage(null, "Cerere trimisa cu succes!");
            } catch (ValidationException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            } finally {
                stage.close();
            }
        }catch (NumberFormatException e){
            MessageAlert.showErrorMessage(null, "Id-urile trebuie sa fie numere intregi!");
            stage.close();
        }
    }
}
