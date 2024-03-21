package map.lab11;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import map.lab11.controller.LoginController;
import map.lab11.controller.NrPaginiController;
import map.lab11.controller.UtilizatorController;
import map.lab11.domain.*;
import map.lab11.domain.validators.UtilizatorValidator;
import map.lab11.repo.*;
import map.lab11.service.Service;

import java.io.IOException;
import java.util.Optional;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String url="jdbc:postgresql://localhost:5432/socialnetwork";//this is default, socialnetwork is the database
        String user="postgres";//this should your username
        String password="pass";//this is your password
        PagingRepository<Long, Utilizator> repUser = new UserDBRepository(url, user, password);
        UtilizatorValidator valUser = new UtilizatorValidator();
        PagingRepository<Tuple<Long, Long>, Prietenie> repFriendship = new FriendshipDBRepository(url, user, password);
        PagingRepository<Long, Request> repRequest = new RequestDBRepository(url, user, password,repUser);
        Repository<Long, Message> repMessage = new MessageDBRepository(url, user, password);
        Service srv = new Service(repUser, valUser, repFriendship, repRequest, repMessage);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController controller = fxmlLoader.getController();
        controller.setService(srv);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();


        /*FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/nrPagini-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        NrPaginiController controller = fxmlLoader.getController();
        controller.setService(srv);
        stage.setTitle("Start Aplicatie");
        stage.setScene(scene);
        stage.show();*/




    }

    public static void main(String[] args) {
        launch();
    }
}
