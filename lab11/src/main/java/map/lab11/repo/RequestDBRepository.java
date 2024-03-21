package map.lab11.repo;

import map.lab11.domain.Request;
import map.lab11.domain.RequestStatus;
import map.lab11.domain.Utilizator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestDBRepository implements PagingRepository<Long, Request>{
    private String url;
    private String user;
    private String password;
    private Repository<Long, Utilizator> userDBRep;

    public RequestDBRepository(String url, String user, String password, Repository<Long, Utilizator> userDBRep) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.userDBRep = userDBRep;
    }

    @Override
    public Page<Request> findAll(Pageable pageable) {
        List<Request> lista = new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(url,user,password);
            PreparedStatement pagePreparedStatement=connection.prepareStatement("SELECT * FROM requests " +
                    "LIMIT ? OFFSET ?");

            PreparedStatement countPreparedStatement = connection.prepareStatement
                    ("SELECT COUNT(*) AS count FROM requests ");

        ) {
            pagePreparedStatement.setInt(1, pageable.getPageSize());
            pagePreparedStatement.setInt(2, pageable.getPageSize() * pageable.getPageNumber());
            try (ResultSet pageResultSet = pagePreparedStatement.executeQuery();
                 ResultSet countResultSet = countPreparedStatement.executeQuery(); ) {
                while (pageResultSet.next()) {
                    Long idr = pageResultSet.getLong("idr");
                    Long id1 = pageResultSet.getLong("id1");
                    Long id2 = pageResultSet.getLong("id2");
                    RequestStatus status = RequestStatus.valueOf(pageResultSet.getString("status"));
                    Request req = new Request(id1,id2,status);
                    req.setId(idr);
                    lista.add(req);
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
    public Optional<Request> findOne(Long id) {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("SELECT * FROM requests WHERE idr=?");)
        {
            statement.setLong(1,id);
            ResultSet r = statement.executeQuery();
            if (r.next()){
                Long id1 = r.getLong("id1");
                Long id2 = r.getLong("id2");
                RequestStatus status = RequestStatus.valueOf(r.getString("status"));
                Request req = new Request(id1,id2,status);
                req.setId(id);
                return Optional.of(req);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Request> findAll() {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("SELECT * FROM requests");)
        {
            ArrayList<Request> list = new ArrayList<>();
            ResultSet r = statement.executeQuery();
            while (r.next()){
                Long idr = r.getLong("idr");
                Long id1 = r.getLong("id1");
                Long id2 = r.getLong("id2");
                RequestStatus status = RequestStatus.valueOf(r.getString("status"));
                Request req = new Request(id1,id2,status);
                req.setId(idr);
                list.add(req);
            }
            return list;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Request> save(Request entity) {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("INSERT INTO requests(id1,id2,status) VALUES (?,?,?)");)
        {
            statement.setLong(1,entity.getId1());
            statement.setLong(2,entity.getId2());
            statement.setString(3,entity.getStatus().toString());
            int affectedRows = statement.executeUpdate();
            return affectedRows!=0? Optional.empty():Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Request> delete(Long id) {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("DELETE FROM requests WHERE idr = ?");)
        {
            var cv = findOne(id);
            statement.setLong(1,id);
            int affectedRows = statement.executeUpdate();
            return affectedRows==0? Optional.empty():cv;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Request> update(Request entity) {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("UPDATE requests SET status=? WHERE idr = ?");)
        {
            statement.setString(1,entity.getStatus().toString());
            statement.setLong(2,entity.getId());
            int affectedRows = statement.executeUpdate();
            return affectedRows!=0? Optional.empty():Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Page<Request> findAllForUser(Pageable pageable,Long id) {
        List<Request> lista = new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(url,user,password);
            PreparedStatement pagePreparedStatement=connection.prepareStatement("SELECT * FROM requests WHERE id2=? " +
                    "LIMIT ? OFFSET ?");

            PreparedStatement countPreparedStatement = connection.prepareStatement
                    ("SELECT COUNT(*) AS count FROM requests ");

        ) {
            pagePreparedStatement.setLong(1,id);
            pagePreparedStatement.setInt(2, pageable.getPageSize());
            pagePreparedStatement.setInt(3, pageable.getPageSize() * pageable.getPageNumber());
            try (ResultSet pageResultSet = pagePreparedStatement.executeQuery();
                 ResultSet countResultSet = countPreparedStatement.executeQuery(); ) {
                while (pageResultSet.next()) {
                    Long idr = pageResultSet.getLong("idr");
                    Long id1 = pageResultSet.getLong("id1");
                    Long id2 = pageResultSet.getLong("id2");
                    RequestStatus status = RequestStatus.valueOf(pageResultSet.getString("status"));
                    Request req = new Request(id1,id2,status);
                    req.setId(idr);
                    lista.add(req);
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
