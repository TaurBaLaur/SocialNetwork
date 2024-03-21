package map.lab11.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import map.lab11.domain.Message;
import map.lab11.domain.Utilizator;
import map.lab11.service.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConversatieController {
    private Service service;
    private Utilizator from;
    private Utilizator to;
    @FXML
    TextField text;
    @FXML
    TableView<Message> messageTableView;
    @FXML
    TableColumn<Message, Long> expeditor;
    @FXML
    TableColumn<Message, Long> idm;
    @FXML
    TableColumn<Message, String> mesaj;
    @FXML
    TableColumn<Message, Timestamp> dataT;
    @FXML
    TableColumn<Message, Long> replyId;
    private ObservableList<Message> messageModel = FXCollections.observableArrayList();
    private Stage stage;

    public void setService(Service service) {
        this.service = service;
        //service.addObserver(this);
        initModel();
    }

    public void setFrom(Utilizator from) {
        this.from = from;
    }

    public void setTo(Utilizator to) {
        this.to = to;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        messageTableView.setItems(messageModel);
        expeditor.setCellValueFactory(new PropertyValueFactory<>("from"));
        idm.setCellValueFactory(new PropertyValueFactory<>("id"));
        mesaj.setCellValueFactory(new PropertyValueFactory<>("content"));
        dataT.setCellValueFactory(new PropertyValueFactory<>("data"));
        replyId.setCellValueFactory(new PropertyValueFactory<>("replyId"));
    }

    private void initModel() {
        messageModel.setAll(StreamSupport.stream(service.getMessagesFor(from, to).spliterator(),
                false).collect(Collectors.toList()));
    }

    public void onClickRefresh(ActionEvent actionEvent) {
        initModel();
    }

    public void onClickTrimite(ActionEvent actionEvent) {
        Message selectedMesage = messageTableView.getSelectionModel().getSelectedItem();
        String mesaj = text.getText();
        if (mesaj.isBlank()) {
            MessageAlert.showErrorMessage(null, "Mesajul nu poate fi gol!");
        } else {
            LocalDateTime data = LocalDateTime.now();
            if (selectedMesage == null) {
                service.creeazaMesaj(from.getId(), to.getId(), mesaj, data);
            } else {
                service.creeazaMesajCuReply(from.getId(), to.getId(), mesaj, data, selectedMesage.getId());
            }
            text.clear();
            initModel();
        }

    }
}
