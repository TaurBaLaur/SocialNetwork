package map.lab11.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import map.lab11.MainApplication;
import map.lab11.domain.Prietenie;
import map.lab11.domain.Request;
import map.lab11.domain.Utilizator;
import map.lab11.repo.Page;
import map.lab11.repo.Pageable;
import map.lab11.service.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UtilizatorController {
    @FXML
    TableView<Request> requestsTableView;
    @FXML
    TableColumn<Request, Long> u1;
    @FXML
    TableColumn<Request, Long> u2;
    @FXML
    TableColumn<Request, String> status;
    ObservableList<Request> requestsModel = FXCollections.observableArrayList();

    @FXML
    TableView<Prietenie> friendsTableView;
    @FXML
    TableColumn<Prietenie, Long> idU1;
    @FXML
    TableColumn<Prietenie, Long> idU2;
    @FXML
    TableColumn<Prietenie, Timestamp> dataF;
    ObservableList<Prietenie> friendshipsModel = FXCollections.observableArrayList();
    private Service service;
    private Utilizator logedUser;

    @FXML
    TableView<Utilizator> userTableView;
    @FXML
    TableColumn<Utilizator, Long> idUser;
    @FXML
    TableColumn<Utilizator, String> firstName;
    @FXML
    TableColumn<Utilizator, String> lastName;
    ObservableList<Utilizator> usersModel = FXCollections.observableArrayList();


    private int pageSize;
    private int currentPageUsers = 0;
    private int totalNumberOfUsers = 0;
    private int currentPageFriendhips = 0;
    private int totalNumberOfFriendships = 0;
    private int currentPageRequests = 0;
    private int totalNumberOfRequests = 0;

    @FXML
    Button nextButtonUsers;
    @FXML
    Button previousButtonUsers;
    @FXML
    Button nextButtonFriendships;
    @FXML
    Button previousButtonFriendships;
    @FXML
    Button nextButtonRequests;
    @FXML
    Button previousButtonRequests;

    public void setService(Service service,Utilizator u) {
        this.service = service;
        //service.addObserver(this);
        this.logedUser = u;
        initModel();
    }
    public void setLogedUser(Utilizator u) {

    }

    public void setPageSize(int ps){
        this.pageSize=ps;
    }

    @FXML
    public void initialize() {
        userTableView.setItems(usersModel);

        idUser.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        friendsTableView.setItems(friendshipsModel);
        idU1.setCellValueFactory(new PropertyValueFactory<>("id1"));
        idU2.setCellValueFactory(new PropertyValueFactory<>("id2"));
        dataF.setCellValueFactory(new PropertyValueFactory<>("date"));

        requestsTableView.setItems(requestsModel);
        u1.setCellValueFactory(new PropertyValueFactory<>("id1"));
        u2.setCellValueFactory(new PropertyValueFactory<>("id2"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void initModel() {
        initUsers();
        initFriendships();
        initRequests();
    }

    private void initUsers() {
        Page<Utilizator> page = service.findAllUsers(new Pageable(currentPageUsers, pageSize));

        int maxPage = (int) Math.ceil((double) page.getTotalElementCount() / pageSize ) - 1;
        if(currentPageUsers > maxPage) {
            currentPageUsers = maxPage;
            page = service.findAllUsers(new Pageable(currentPageUsers, pageSize));
        }

        usersModel.setAll(StreamSupport.stream(page.getElementsOnPage().spliterator(),
                false).collect(Collectors.toList()));
        totalNumberOfUsers = page.getTotalElementCount();

        previousButtonUsers.setDisable(currentPageUsers == 0);
        nextButtonUsers.setDisable((currentPageUsers+1)*pageSize >= totalNumberOfUsers);


        /*usersModel.setAll(StreamSupport.stream(service.getAllUsers().spliterator(),
                false).collect(Collectors.toList()));*/
    }

    private void initFriendships() {
        Page<Prietenie> page = service.findAllFriendshipsForUser(new Pageable(currentPageFriendhips, pageSize),logedUser.getId());

        int maxPage = (int) Math.ceil((double) page.getTotalElementCount() / pageSize ) - 1;
        if(currentPageFriendhips > maxPage) {
            currentPageFriendhips = maxPage;
            page = service.findAllFriendships(new Pageable(currentPageFriendhips, pageSize));
        }

        friendshipsModel.setAll(StreamSupport.stream(page.getElementsOnPage().spliterator(),
                false).collect(Collectors.toList()));
        totalNumberOfFriendships = page.getTotalElementCount();

        previousButtonFriendships.setDisable(currentPageFriendhips == 0);
        nextButtonFriendships.setDisable((currentPageFriendhips+1)*pageSize >= totalNumberOfFriendships);


        /*friendshipsModel.setAll(StreamSupport.stream(service.getAllFriendships().spliterator(),
                false).collect(Collectors.toList()));*/
    }

    private void initRequests() {
        Page<Request> page = service.findAllRequestsForUser(new Pageable(currentPageRequests, pageSize),logedUser.getId());

        int maxPage = (int) Math.ceil((double) page.getTotalElementCount() / pageSize ) - 1;
        if(currentPageRequests > maxPage) {
            currentPageRequests = maxPage;
            page = service.findAllRequests(new Pageable(currentPageRequests, pageSize));
        }

        requestsModel.setAll(StreamSupport.stream(page.getElementsOnPage().spliterator(),
                false).collect(Collectors.toList()));
        totalNumberOfRequests = page.getTotalElementCount();

        previousButtonRequests.setDisable(currentPageRequests == 0);
        nextButtonRequests.setDisable((currentPageRequests+1)*pageSize >= totalNumberOfRequests);


        /*requestsModel.setAll(StreamSupport.stream(service.getAllRequests().spliterator(),
                false).collect(Collectors.toList()));*/
    }

    public void onPressDelete(ActionEvent actionEvent) {
        Utilizator selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            service.stergeUtilizator(selectedUser.getId());
            initUsers();
            MessageAlert.showInfoMessage(null, "Utilizator sters cu succes!");
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat nici un utilizator!");
        }
    }

    public void onPressAdd(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/adaugare-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            AdaugareUtilizatorController controller = fxmlLoader.getController();
            controller.setService(service);
            Stage stage = new Stage();
            stage.setTitle("Adaugare Utilizator");
            stage.setScene(scene);
            //stage.setOnCloseRequest(ev->{initModel();ev.consume();});
            controller.setStage(stage);
            controller.setModel(usersModel);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onPressUpdate(ActionEvent actionEvent) {
        Utilizator selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/actualizare-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                UpdateUtilizatorController controller = fxmlLoader.getController();
                controller.setService(service);
                Stage stage = new Stage();
                stage.setTitle("Actualizare Utilizator");
                stage.setScene(scene);
                //stage.setOnCloseRequest(ev->{initModel();ev.consume();});
                controller.setStage(stage);
                controller.setModel(usersModel);
                controller.setIdUser(selectedUser.getId());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat nici un utilizator!");
        }

    }

    public void onPressDeleteFriendship(ActionEvent actionEvent) {
        Prietenie selectedFriendship = friendsTableView.getSelectionModel().getSelectedItem();
        if (selectedFriendship != null) {
            service.stergePrieten(selectedFriendship.getId().getLeft(), selectedFriendship.getId().getRight());
            initFriendships();
            MessageAlert.showInfoMessage(null, "Prietenie stearsa cu succes!");
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat nici o prietenie!");
        }
    }

    public void onPressSendRequest(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/sendreq-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            TrimiteCerereController controller = fxmlLoader.getController();
            controller.setService(service);
            Stage stage = new Stage();
            stage.setTitle("Adaugare Prieten");
            stage.setScene(scene);
            //stage.setOnCloseRequest(ev->{initModel();ev.consume();});
            controller.setStage(stage);
            controller.setModel(requestsModel);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onPressAccept(ActionEvent actionEvent) throws InterruptedException {
        Request selectedRequest = requestsTableView.getSelectionModel().getSelectedItem();
        Utilizator selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null && selectedUser!=null && Objects.equals(selectedUser.getId(), selectedRequest.getId2())) {
            service.acceptaCerere(selectedRequest.getId());
            initFriendships();
            initRequests();
            MessageAlert.showInfoMessage(null, "Cerere acceptata!");
            Thread.sleep(1000);
            service.stergeCerere(selectedRequest.getId());
            initFriendships();
            initRequests();
        } else if (selectedRequest == null) {
            MessageAlert.showErrorMessage(null, "Nu ati selectat nici o cerere!");
        } else if (selectedUser == null) {
            MessageAlert.showErrorMessage(null, "Nu ati selectat nici un utilizator!");
        } else {
            MessageAlert.showErrorMessage(null, "Nu dumneavoastra ati primit cererea!");
        }
    }

    public void onPressDecline(ActionEvent actionEvent) throws InterruptedException {
        Request selectedRequest = requestsTableView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            service.respingeCerere(selectedRequest.getId());
            initRequests();
            MessageAlert.showInfoMessage(null, "Cerere respinsa!");
            Thread.sleep(1000);
            service.stergeCerere(selectedRequest.getId());
            initRequests();
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat nici o cerere!");
        }
    }

    public void onPressMessages(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/messages-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            MessageController controller = fxmlLoader.getController();
            controller.setService(service);
            Stage stage = new Stage();
            stage.setTitle("Mesaje");
            stage.setScene(scene);
            //stage.setOnCloseRequest(ev->{initModel();ev.consume();});
            controller.setStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onPreviousUsers(ActionEvent actionEvent) {
        currentPageUsers--;
        initModel();
    }

    public void onNextUsers(ActionEvent actionEvent) {
        currentPageUsers++;
        initModel();
    }

    public void onPreviousFriendships(ActionEvent actionEvent) {
        currentPageFriendhips--;
        initModel();
    }

    public void onNextFriendships(ActionEvent actionEvent) {
        currentPageFriendhips++;
        initModel();
    }

    public void onPreviousRequests(ActionEvent actionEvent) {
        currentPageRequests--;
        initModel();
    }

    public void onNextRequests(ActionEvent actionEvent) {
        currentPageRequests++;
        initModel();
    }
}
