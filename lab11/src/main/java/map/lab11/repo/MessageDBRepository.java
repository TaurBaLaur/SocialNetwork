package map.lab11.repo;

import map.lab11.domain.Message;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class MessageDBRepository implements Repository<Long, Message>{
    private String url;
    private String user;
    private String password;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MessageDBRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<Message> findOne(Long id) {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("SELECT * FROM messages WHERE idm=?");)
        {
            statement.setLong(1,id);
            ResultSet r = statement.executeQuery();
            if (r.next()){
                Long from = r.getLong("from_user");
                Long to = r.getLong("to_user");
                String content = r.getString("mesaj");
                LocalDateTime d  = r.getTimestamp("data_trimiterii").toLocalDateTime();
                Long replyId = r.getLong("reply_id");

                Message m = new Message(from,to,content,d,replyId);
                m.setId(id);
                return Optional.of(m);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Message> findAll() {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("SELECT * FROM messages ORDER BY data_trimiterii");)
        {
            ArrayList<Message> list = new ArrayList<>();
            ResultSet r = statement.executeQuery();
            while (r.next()){
                Long idm = r.getLong("idm");
                Long from = r.getLong("from_user");
                Long to = r.getLong("to_user");
                String content = r.getString("mesaj");
                LocalDateTime d  = r.getTimestamp("data_trimiterii").toLocalDateTime();
                Long replyId = r.getLong("reply_id");

                Message m = new Message(from,to,content,d,replyId);
                m.setId(idm);
                list.add(m);
            }
            return list;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> save(Message entity) {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("INSERT INTO messages(from_user,to_user,mesaj,data_trimiterii,reply_id) VALUES (?,?,?,?,?)");)
        {
            statement.setLong(1,entity.getFrom());
            statement.setLong(2,entity.getTo());
            statement.setString(3,entity.getContent());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getData().format(formatter)));
            //statement.setLong(5,entity.getReplyId());
            statement.setObject(5, entity.getReplyId(), Types.BIGINT);
            int affectedRows = statement.executeUpdate();
            return affectedRows!=0? Optional.empty():Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }
}
