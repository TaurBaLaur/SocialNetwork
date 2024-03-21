package map.lab11.service;

import map.lab11.domain.*;
import map.lab11.domain.validators.ValidationException;
import map.lab11.domain.validators.Validator;
import map.lab11.repo.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service {
    private PagingRepository<Long, Utilizator> userRepo;
    private Validator<Utilizator> userValidator;

    private PagingRepository<Tuple<Long,Long>, Prietenie> friendshipRepo;

    private PagingRepository<Long, Request> requestRepo;
    private Repository<Long, Message> messageRepo;

    public Service(PagingRepository<Long,Utilizator> rep,Validator<Utilizator> val, PagingRepository<Tuple<Long,Long>, Prietenie> fRepo, PagingRepository<Long, Request> reqRepo, Repository<Long, Message> msgRepo){
        this.userRepo=rep;
        this.userValidator=val;
        this.friendshipRepo=fRepo;
        this.requestRepo = reqRepo;
        this.messageRepo = msgRepo;
    }
    public Optional<Utilizator> getUserById(Long id){
        return userRepo.findOne(id);
    }
    public void adaugaUtilizator(String prenume,String nume){
        Utilizator utilizatorNou = new Utilizator(prenume,nume);
        userValidator.validate(utilizatorNou);
        userRepo.save(utilizatorNou);
    }
    public Iterable<Utilizator> getAllUsers(){
        return userRepo.findAll();
    }
    public Long stergeUtilizator(Long id){
        Optional<Utilizator> u = userRepo.delete(id);
        if(u.isEmpty()){
            return null;
        }
        return u.get().getId();//u.getId()
    }

    public boolean existaUtilizatorCuId(Long id){
        Optional<Utilizator> u = userRepo.findOne(id);
        return u.isPresent();
    }

    public Iterable<Prietenie> getAllFriendships(){
        return friendshipRepo.findAll();
    }
    public void adaugaPrieten(Long id1,Long id2){
        Tuple<Long,Long> idPrietenie = new Tuple<>(id1,id2);
        Optional<Prietenie> p = friendshipRepo.findOne(idPrietenie);
        if(p.isEmpty()){
            Prietenie f = new Prietenie(LocalDateTime.now());
            f.setId(idPrietenie);
            friendshipRepo.save(f);
        }
        else{
            throw new ValidationException("Utilizatorii cu aceste id-uri sunt deja prieteni!");
        }
    }

    public void stergePrieten(Long id1,Long id2){
        Tuple<Long,Long> idPrietenie = new Tuple<>(id1,id2);
        Optional<Prietenie> p = friendshipRepo.findOne(idPrietenie);
        if(p.isPresent()){
            friendshipRepo.delete(idPrietenie);
        }
        else{
            throw new ValidationException("Nu exista o prietenie intre utilizatorii cu aceste id-uri!");
        }
    }

    public Iterable<Prietenie> prieteni_luna(Long id, String luna){
        Optional<Utilizator> u = userRepo.findOne(id);
        Iterable<Prietenie> toatePrieteniile = getAllFriendships();
        List<Prietenie> prieteniiU = new ArrayList<>();
        if(u.isEmpty()){
            throw new ValidationException("Nu exista utilizator cu acest id!");
        }
        else{
            if(luna.length()==1){
                luna = "0"+luna;
            }
            for(Prietenie p: toatePrieteniile){
                Tuple<Long,Long> idPrietenie = p.getId();
                String lunaPrietenie = p.getDate().toString().substring(5,7);
                if((idPrietenie.getLeft().equals(id) ||idPrietenie.getRight().equals(id)) && luna.equals(lunaPrietenie)){
                    prieteniiU.add(p);
                }
            }
        }
        return prieteniiU;
    }

    public Iterable<Utilizator> cauta(String mostra){
        Iterable<Utilizator> totiUtilizatorii = userRepo.findAll();
        List<Utilizator> utilizatoriGasiti = new ArrayList<>();
        for(Utilizator u: totiUtilizatorii){
            String numeComplet = (u.getFirstName()+u.getLastName()).toLowerCase();
            if(numeComplet.contains(mostra.toLowerCase())){
                utilizatoriGasiti.add(u);
            }
        }
        return utilizatoriGasiti;
    }

    public void actualizeazaUtilizator(Long id, String prenume,String nume){
        Utilizator utilizatorActualizat = new Utilizator(prenume,nume);
        utilizatorActualizat.setId(id);

        userValidator.validate(utilizatorActualizat);
        //utilizatorNou.setId(Utilizator.getGeneratedId());
        //Utilizator.setGeneratedId();
        userRepo.update(utilizatorActualizat);
    }

    public Iterable<Request> getAllRequests(){
        return requestRepo.findAll();
    }

    public void adaugaCerere(Long u1, Long u2) {
        if(userRepo.findOne(u1).isEmpty()){
            throw  new ValidationException("Utilizatorul cu id "+u1+" nu exista!");
        }
        if(userRepo.findOne(u2).isEmpty()){
            throw  new ValidationException("Utilizatorul cu id "+u2+" nu exista!");
        }
        if(cautaPrietenie(u1,u2).isPresent()){
            throw  new ValidationException("Acesti utilizatori sunt deja prieteni!");
        }
        if(cautaCerere(u1,u2).isPresent()){
            throw  new ValidationException("Cererea de prietenie exista deja!");
        }
        Request cerere = new Request(u1,u2);
        requestRepo.save(cerere);
    }

    public Optional<Request> cautaCerere(Long u1, Long u2){
        return StreamSupport.stream(getAllRequests().spliterator(), false)
                .filter(req -> (Objects.equals(req.getId1(), u1) && Objects.equals(req.getId2(), u2))
                        || (Objects.equals(req.getId1(), u2) && Objects.equals(req.getId2(), u1))).findFirst();
    }

    public Optional<Prietenie> cautaPrietenie(Long u1, Long u2){
        return StreamSupport.stream(getAllFriendships().spliterator(), false)
                .filter(p -> (Objects.equals(p.getId1(), u1) && Objects.equals(p.getId2(), u2))
                        || (Objects.equals(p.getId1(), u2) && Objects.equals(p.getId2(), u1))).findFirst();
    }

    public void acceptaCerere(Long idReq){
        Optional<Request> req = requestRepo.findOne(idReq);
        req.get().setStatus(RequestStatus.APPROVED);
        requestRepo.update(req.get());

        adaugaPrieten(req.get().getId1(),req.get().getId2());
    }

    public void respingeCerere(Long idReq){
        Optional<Request> req = requestRepo.findOne(idReq);
        req.get().setStatus(RequestStatus.DECLINED);
        requestRepo.update(req.get());
    }

    public Long stergeCerere(Long id){
        Optional<Request> req = requestRepo.delete(id);
        if(req.isEmpty()){
            return null;
        }
        return req.get().getId();
    }

    public void creeazaMesaj(Long from, Long to, String mesaj, LocalDateTime data){
        Message msg = new Message(from,to,mesaj,data);
        messageRepo.save(msg);
    }
    public void creeazaMesajCuReply(Long from, Long to, String mesaj, LocalDateTime data,Long reply){
        Message msg = new Message(from,to,mesaj,data,reply);
        messageRepo.save(msg);
    }
    /*public Iterable<Message> getAllMessages(){
        return messageRepo.findAll();
    }*/
    public Iterable<Message> getMessagesFor(Utilizator u1,Utilizator u2){
        return StreamSupport.stream(messageRepo.findAll().spliterator(), false)
                .filter(m -> (Objects.equals(m.getFrom(), u1.getId()) && Objects.equals(m.getTo(), u2.getId()))
                        || (Objects.equals(m.getFrom(), u2.getId()) && Objects.equals(m.getTo(), u1.getId()))).collect(Collectors.toList());
    }

    public Page<Utilizator> findAllUsers(Pageable pageable) {
        return userRepo.findAll(pageable);
    }

    public Page<Prietenie> findAllFriendships(Pageable pageable) {
        return friendshipRepo.findAll(pageable);
    }

    public Page<Request> findAllRequests(Pageable pageable) {
        return requestRepo.findAll(pageable);
    }

    public Optional<Utilizator> findUser(String userName,String passWord){
        return ((UserDBRepository)userRepo).findUsernamePassword(userName,passWord);
    }

    public Optional<Utilizator> findUsername(String userName){
        return ((UserDBRepository)userRepo).findUsername(userName);
    }

    public Page<Prietenie> findAllFriendshipsForUser(Pageable pageable,Long id) {
        return ((FriendshipDBRepository)friendshipRepo).findAllForUser(pageable,id);
    }

    public Page<Request> findAllRequestsForUser(Pageable pageable,Long id) {
        return ((RequestDBRepository)requestRepo).findAllForUser(pageable,id);
    }

    public void creareUtilizator(String prenume,String nume,String username,String parola){
        Utilizator utilizatorNou = new Utilizator(prenume,nume,username,parola);
        userValidator.validate(utilizatorNou);
        Optional<Utilizator> u = findUsername(username);
        if(u.isPresent()){
            throw new ValidationException("Exista deja un utilizator cu acest username!");
        }
        userRepo.save(utilizatorNou);
    }

}