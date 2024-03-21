package map.lab11.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.lab11.domain.validators.ValidationException;
import map.lab11.service.Service;

public class NewAccountController {

    private Service service;
    @FXML
    TextField prenume;
    @FXML
    TextField nume;
    @FXML
    TextField username;
    @FXML
    TextField password;
    @FXML
    Button creare;

    public void setService(Service service) {
        this.service = service;
    }

    public void onClickCreare(ActionEvent actionEvent) {
        String firstName=prenume.getText();
        String lastName=nume.getText();
        String userName=username.getText();
        String passWord=password.getText();
        try{
            service.creareUtilizator(firstName,lastName,userName,passWord);
            MessageAlert.showInfoMessage(null, "Utilizator adaugat cu succes!");

            Stage currentStage = (Stage) creare.getScene().getWindow();
            currentStage.close();
        }catch (ValidationException e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

}
