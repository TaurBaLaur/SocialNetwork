package map.lab11.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.lab11.MainApplication;
import map.lab11.domain.Utilizator;
import map.lab11.domain.validators.ValidationException;
import map.lab11.service.Service;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    private Service service;
    @FXML
    TextField username;
    @FXML
    TextField password;
    @FXML
    Button login;
    @FXML
    Button creare;

    public void setService(Service service) {
        this.service = service;
    }

    public void onClickLogin(ActionEvent actionEvent) {
        try {
            String userName=username.getText();
            String passWord=password.getText();
            Optional<Utilizator> utilizatorLogat = service.findUser(userName,passWord);
            if(utilizatorLogat.isEmpty()){
                throw new ValidationException("Username sau parola incorecte!");
            }
            Utilizator user=new Utilizator(utilizatorLogat.get().getFirstName(),utilizatorLogat.get().getLastName());
            user.setId(utilizatorLogat.get().getId());


            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/nrPagini-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            NrPaginiController controller = fxmlLoader.getController();
            controller.setService(service);
            controller.setLogedUser(user);
            Stage stage = new Stage();
            stage.setTitle("Start Aplicatie");
            stage.setScene(scene);
            stage.show();



            Stage currentStage = (Stage) login.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void onPressCreare(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/newAccount-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            NewAccountController controller = fxmlLoader.getController();
            controller.setService(service);
            Stage stage = new Stage();
            stage.setTitle("Creare cont");
            stage.setScene(scene);
            //stage.setOnCloseRequest(ev->{initModel();ev.consume();});

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


