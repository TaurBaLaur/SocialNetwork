package map.lab11.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import map.lab11.MainApplication;
import map.lab11.domain.Utilizator;
import map.lab11.repo.Page;
import map.lab11.repo.Pageable;
import map.lab11.service.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageController {
    private Service service;

    @FXML
    TableView<Utilizator> fromUserTableView;
    @FXML
    TableColumn<Utilizator, Long> fromIdUser;
    @FXML
    TableColumn<Utilizator, String> fromFirstName;
    @FXML
    TableColumn<Utilizator, String> fromLastName;
    private ObservableList<Utilizator> fromUsersModel = FXCollections.observableArrayList();
    @FXML
    TableView<Utilizator> toUserTableView;
    @FXML
    TableColumn<Utilizator, Long> toIdUser;
    @FXML
    TableColumn<Utilizator, String> toFirstName;
    @FXML
    TableColumn<Utilizator, String> toLastName;
    private ObservableList<Utilizator> toUsersModel = FXCollections.observableArrayList();

    @FXML
    TextField text;
    private Stage stage;


    private int pageSize = 3;
    private int currentPageFrom = 0;
    private int totalNumberOfFrom = 0;
    private int currentPageTo = 0;
    private int totalNumberOfTo = 0;

    @FXML
    Button nextButtonFrom;
    @FXML
    Button previousButtonFrom;
    @FXML
    Button nextButtonTo;
    @FXML
    Button previousButtonTo;


    public void setService(Service service) {
        this.service = service;
        //service.addObserver(this);
        initModel();
    }
    public void setPageSize(int size){
        this.pageSize=size;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        fromUserTableView.setItems(fromUsersModel);

        fromIdUser.setCellValueFactory(new PropertyValueFactory<>("id"));
        fromFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        fromLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        toUserTableView.setItems(toUsersModel);

        toIdUser.setCellValueFactory(new PropertyValueFactory<>("id"));
        toFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        toLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        toUserTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void initModel() {
        initFrom();
        initTo();
    }

    private void initFrom(){
        /*Page<Utilizator> page = service.findAllUsers(new Pageable(currentPageFrom, pageSize));

        int maxPage = (int) Math.ceil((double) page.getTotalElementCount() / pageSize ) - 1;
        if(currentPageFrom > maxPage) {
            currentPageFrom = maxPage;
            page = service.findAllUsers(new Pageable(currentPageFrom, pageSize));
        }

        fromUsersModel.setAll(StreamSupport.stream(page.getElementsOnPage().spliterator(),
                false).collect(Collectors.toList()));
        totalNumberOfFrom = page.getTotalElementCount();

        previousButtonFrom.setDisable(currentPageFrom == 0);
        nextButtonFrom.setDisable((currentPageFrom+1)*pageSize >= totalNumberOfFrom);*/


        fromUsersModel.setAll(StreamSupport.stream(service.getAllUsers().spliterator(),
                false).collect(Collectors.toList()));
    }
    private void initTo(){
        /*Page<Utilizator> page = service.findAllUsers(new Pageable(currentPageTo, pageSize));

        int maxPage = (int) Math.ceil((double) page.getTotalElementCount() / pageSize ) - 1;
        if(currentPageTo > maxPage) {
            currentPageTo = maxPage;
            page = service.findAllUsers(new Pageable(currentPageTo, pageSize));
        }

        toUsersModel.setAll(StreamSupport.stream(page.getElementsOnPage().spliterator(),
                false).collect(Collectors.toList()));
        totalNumberOfTo = page.getTotalElementCount();

        previousButtonTo.setDisable(currentPageTo == 0);
        nextButtonTo.setDisable((currentPageTo+1)*pageSize >= totalNumberOfTo);*/


        toUsersModel.setAll(StreamSupport.stream(service.getAllUsers().spliterator(),
                false).collect(Collectors.toList()));
    }

    public void clickTrimite(ActionEvent actionEvent) {
        Utilizator selectedFrom = fromUserTableView.getSelectionModel().getSelectedItem();
        if (selectedFrom == null) {
            MessageAlert.showErrorMessage(null, "Nu ati selectat utilizatorul care trimite mesajul!");
        } else {
            List<Utilizator> selectedTo = toUserTableView.getSelectionModel().getSelectedItems();
            if (selectedTo.isEmpty()) {
                MessageAlert.showErrorMessage(null, "Nu ati selectat utilizatori carora sa le trimiteti mesajul!");
            } else {
                String mesaj = text.getText();
                if (mesaj.isBlank()) {
                    MessageAlert.showErrorMessage(null, "Mesajul nu poate fi gol!");
                } else {
                    LocalDateTime data = LocalDateTime.now();
                    for (Utilizator u : selectedTo) {
                        service.creeazaMesaj(selectedFrom.getId(), u.getId(), mesaj, data);
                    }
                    text.clear();
                    MessageAlert.showInfoMessage(null, "Mesaj trimis cu succes!");
                }

            }
        }
    }

    public void clickVeziConversatie(ActionEvent actionEvent) {
        Utilizator selectedFrom = fromUserTableView.getSelectionModel().getSelectedItem();
        List<Utilizator> selectedTo = toUserTableView.getSelectionModel().getSelectedItems();
        if (selectedFrom!=null && selectedTo.size()==1) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/conversatie-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                ConversatieController controller = fxmlLoader.getController();
                controller.setFrom(selectedFrom);
                controller.setTo(selectedTo.get(0));
                controller.setService(service);
                Stage stage = new Stage();
                stage.setTitle("Conversatie");
                stage.setScene(scene);
                controller.setStage(stage);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            MessageAlert.showInfoMessage(null, "Selectati un utilizator din tabelul din stanga si un utilizator din tabelul din dreapta!");
        }
    }
}
