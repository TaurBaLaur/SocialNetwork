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

public class NrPaginiController {
    private Service service;
    private Utilizator logedUser;

    @FXML
    TextField text;
    @FXML
    Button start;

    public void setService(Service service) {
        this.service = service;
    }
    public void setLogedUser(Utilizator u) {
        this.logedUser = u;
    }

    public void onClickStart(ActionEvent actionEvent){
        try {
            Long pageSize = Long.valueOf(text.getText());
            if(pageSize<3 || pageSize>10){
                MessageAlert.showErrorMessage(null, "Introduceti un numar intre 3 si 10 inclusiv!");
            }
            else {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/users-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    UtilizatorController controller = fxmlLoader.getController();
                    controller.setPageSize(Math.toIntExact(pageSize));
                    controller.setService(service,logedUser);
                    //controller.setLogedUser(logedUser);
                    Stage stage = new Stage();
                    stage.setTitle("Utilizatori");
                    stage.setScene(scene);
                    stage.show();
                    Stage currentStage = (Stage) start.getScene().getWindow();
                    currentStage.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (NumberFormatException e){
            MessageAlert.showErrorMessage(null, "Introduceti un numar intre 3 si 10 inclusiv!");
            //stage.close();
        }
    }
}
