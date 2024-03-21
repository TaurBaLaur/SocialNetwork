package map.lab11.repo;

import map.lab11.domain.Prietenie;
import map.lab11.domain.Tuple;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipDBRepository implements PagingRepository<Tuple<Long,Long>, Prietenie>{
    private String url;
    private String user;
    private String password;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FriendshipDBRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    @Override
    public Page<Prietenie> findAll(Pageable pageable) {
        List<Prietenie> lista = new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(url,user,password);
            PreparedStatement pagePreparedStatement=connection.prepareStatement("SELECT * FROM friendships " +
                    "LIMIT ? OFFSET ?");

            PreparedStatement countPreparedStatement = connection.prepareStatement
                    ("SELECT COUNT(*) AS count FROM friendships ");

        ) {
            pagePreparedStatement.setInt(1, pageable.getPageSize());
            pagePreparedStatement.setInt(2, pageable.getPageSize() * pageable.getPageNumber());
            try (ResultSet pageResultSet = pagePreparedStatement.executeQuery();
                 ResultSet countResultSet = countPreparedStatement.executeQuery(); ) {
                while (pageResultSet.next()) {
                    LocalDateTime d  = pageResultSet.getTimestamp("friends_from").toLocalDateTime();
                    Prietenie p = new Prietenie(d);
                    Long idu1 = pageResultSet.getLong("idu1");
                    Long idu2 = pageResultSet.getLong("idu2");
                    p.setId(new Tuple<>(idu1,idu2));
                    p.setId1(idu1);
                    p.setId2(idu2);
                    lista.add(p);
                }
                int totalCount = 0;
                if(countResultSet.next()) {
                    totalCount = countResultSet.getInt("count");
                }

                return new Page<>(lista, totalCount);

            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Prietenie> findOne(Tuple<Long,Long> id) {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("SELECT * FROM friendships WHERE idu1=? AND idu2=?");)
        {
            statement.setLong(1,id.getLeft());
            statement.setLong(2,id.getRight());
            ResultSet r = statement.executeQuery();
            if (r.next()){
                LocalDateTime d  = r.getTimestamp("friends_from").toLocalDateTime();
                Prietenie p = new Prietenie(d);
                p.setId(id);
                p.setId1(id.getLeft());
                p.setId2(id.getRight());
                return Optional.of(p);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Prietenie> findAll() {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("SELECT * FROM friendships");)
        {
            ArrayList<Prietenie> list = new ArrayList<>();
            ResultSet r = statement.executeQuery();
            while (r.next()){
                //LocalDateTime d  = LocalDateTime.parse((CharSequence) r.getTimestamp("friends_from"),formatter);
                LocalDateTime d  = r.getTimestamp("friends_from").toLocalDateTime();
                Prietenie p = new Prietenie(d);
                Long idu1 = r.getLong("idu1");
                Long idu2 = r.getLong("idu2");
                p.setId(new Tuple<>(idu1,idu2));
                p.setId1(idu1);
                p.setId2(idu2);
                list.add(p);
            }
            return list;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<Prietenie> save(Prietenie entity) {

        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("INSERT INTO friendships(idu1,idu2,friends_from) VALUES (?,?,?)");)
        {
            statement.setLong(1,entity.getId().getLeft());
            statement.setLong(2,entity.getId().getRight());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getDate().format(formatter)));
            int affectedRows = statement.executeUpdate();
            return affectedRows!=0? Optional.empty():Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Prietenie> delete(Tuple<Long,Long> id) {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("DELETE FROM friendships WHERE idu1 = ? AND idu2=?");)
        {
            var cv = findOne(id);
            statement.setLong(1,id.getLeft());
            statement.setLong(2,id.getRight());
            int affectedRows = statement.executeUpdate();
            return affectedRows==0? Optional.empty():cv;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Prietenie> update(Prietenie entity) {

        /*try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("UPDATE users SET firstName = ?, lastName = ? WHERE id = ?");)
        {
            statement.setString(1,entity.getFirstName());
            statement.setString(2,entity.getLastName());
            statement.setLong(3,entity.getId());
            int affectedRows = statement.executeUpdate();
            return affectedRows!=0? Optional.empty():Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
        return Optional.of(entity);

    }

    public Page<Prietenie> findAllForUser(Pageable pageable,Long id) {
        List<Prietenie> lista = new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(url,user,password);
            PreparedStatement pagePreparedStatement=connection.prepareStatement("SELECT * FROM friendships WHERE idu1=? OR idu2=? " +
                    "LIMIT ? OFFSET ?");

            PreparedStatement countPreparedStatement = connection.prepareStatement
                    ("SELECT COUNT(*) AS count FROM friendships ");

        ) {
            pagePreparedStatement.setLong(1,id);
            pagePreparedStatement.setLong(2,id);
            pagePreparedStatement.setInt(3, pageable.getPageSize());
            pagePreparedStatement.setInt(4, pageable.getPageSize() * pageable.getPageNumber());
            try (ResultSet pageResultSet = pagePreparedStatement.executeQuery();
                 ResultSet countResultSet = countPreparedStatement.executeQuery(); ) {
                while (pageResultSet.next()) {
                    LocalDateTime d  = pageResultSet.getTimestamp("friends_from").toLocalDateTime();
                    Prietenie p = new Prietenie(d);
                    Long idu1 = pageResultSet.getLong("idu1");
                    Long idu2 = pageResultSet.getLong("idu2");
                    p.setId(new Tuple<>(idu1,idu2));
                    p.setId1(idu1);
                    p.setId2(idu2);
                    lista.add(p);
                }
                int totalCount = 0;
                if(countResultSet.next()) {
                    totalCount = countResultSet.getInt("count");
                }

                return new Page<>(lista, totalCount);

            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
