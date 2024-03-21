package map.lab11.repo;

import map.lab11.domain.Encryptor;
import map.lab11.domain.Utilizator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDBRepository implements map.lab11.repo.PagingRepository<Long, Utilizator> {

    private String url;
    private String user;
    private String password;
    private Encryptor enc;

    public UserDBRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public map.lab11.repo.Page<Utilizator> findAll(map.lab11.repo.Pageable pageable) {
        List<Utilizator> lista = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement pagePreparedStatement = connection.prepareStatement("SELECT * FROM users " +
                     "LIMIT ? OFFSET ?");

             PreparedStatement countPreparedStatement = connection.prepareStatement
                     ("SELECT COUNT(*) AS count FROM users ");

        ) {
            pagePreparedStatement.setInt(1, pageable.getPageSize());
            pagePreparedStatement.setInt(2, pageable.getPageSize() * pageable.getPageNumber());
            try (ResultSet pageResultSet = pagePreparedStatement.executeQuery();
                 ResultSet countResultSet = countPreparedStatement.executeQuery();) {
                while (pageResultSet.next()) {
                    String firstName = pageResultSet.getString("first_name");
                    String lastName = pageResultSet.getString("last_name");
                    Long id = pageResultSet.getLong("id");
                    Utilizator u = new Utilizator(firstName, lastName);
                    u.setId(id);
                    lista.add(u);
                }
                int totalCount = 0;
                if (countResultSet.next()) {
                    totalCount = countResultSet.getInt("count");
                }

                return new map.lab11.repo.Page<>(lista, totalCount);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Utilizator> findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id=?");) {
            statement.setLong(1, id);
            ResultSet r = statement.executeQuery();
            if (r.next()) {
                String firstName = r.getString("first_name");
                String lastName = r.getString("last_name");
                Utilizator u = new Utilizator(firstName, lastName);
                u.setId(id);
                return Optional.of(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Utilizator> findAll() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");) {
            ArrayList<Utilizator> list = new ArrayList<>();
            ResultSet r = statement.executeQuery();
            while (r.next()) {
                String firstName = r.getString("first_name");
                String lastName = r.getString("last_name");
                Long id = r.getLong("id");
                Utilizator u = new Utilizator(firstName, lastName);
                u.setId(id);
                list.add(u);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users(first_name,last_name,username,password) VALUES (?,?,?,?)");) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getUsername());
            statement.setString(4, enc.encrypt(entity.getPassword()));
            int affectedRows = statement.executeUpdate();
            return affectedRows != 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Utilizator> delete(Long id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");) {
            var cv = findOne(id);
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows == 0 ? Optional.empty() : cv;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET first_name = ?, last_name = ? WHERE id = ?");) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setLong(3, entity.getId());
            int affectedRows = statement.executeUpdate();
            return affectedRows != 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Utilizator> findUsername(String username) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username=?");) {
            statement.setString(1, username);
            ResultSet r = statement.executeQuery();
            if (r.next()) {
                String firstName = r.getString("first_name");
                String lastName = r.getString("last_name");
                String passWord = r.getString("password");
                Long id = r.getLong("id");
                Utilizator u = new Utilizator(firstName, lastName, username, passWord);
                u.setId(id);
                return Optional.of(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public Optional<Utilizator> findUsernamePassword(String username, String passWord) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");) {
            statement.setString(1, username);
            statement.setString(2, enc.encrypt(passWord));
            ResultSet r = statement.executeQuery();
            if (r.next()) {
                String firstName = r.getString("first_name");
                String lastName = r.getString("last_name");
                Long id = r.getLong("id");
                Utilizator u = new Utilizator(firstName, lastName, username, passWord);
                u.setId(id);
                return Optional.of(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

}
