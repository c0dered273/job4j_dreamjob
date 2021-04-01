package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class UsersPsqlStore implements UserStore {
    private static final Logger logger = LoggerFactory.getLogger(UsersPsqlStore.class);
    private final BasicDataSource pool = new BasicDataSource();

    private static final class Lazy {
        private static final UserStore INSTANCE = new UsersPsqlStore();
    }

    public static UserStore instOf() {
        return UsersPsqlStore.Lazy.INSTANCE;
    }

    private UsersPsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties"))) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    @Override
    public Collection<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM users ORDER BY id")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    User user = new User(-1, "", "", "");
                    getUserFromResultSet(it, user);
                    users.add(user);
                }
            }
        } catch (Exception e) {
            logger.error("Error DB connection or PrepareStatement execution: findAll()", e);
        }
        return users;
    }

    @Override
    public void save(User item) {
        if (item.getId() == 0) {
            create(item);
        } else {
            update(item);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Error DB connection or PrepareStatement execution: delete(id)", e);
        }
    }

    @Override
    public void delete(String email) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM users WHERE email = ?")) {
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Error DB connection or PrepareStatement execution: delete(email)", e);
        }
    }

    @Override
    public User findById(int id) {
        User user = new User(-1, "", "", "");
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * from users WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    getUserFromResultSet(rs, user);
                }
            }
        } catch (Exception e) {
            logger.error("Error DB connection or PrepareStatement execution: findById()", e);
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        User user = new User(-1, "", "", "");
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * from users WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    getUserFromResultSet(rs, user);
                }
            }
        } catch (Exception e) {
            logger.error("Error DB connection or PrepareStatement execution: findByEmail()", e);
        }
        return user;
    }

    @Override
    public boolean checkPass(User user) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(
                    "SELECT id FROM users WHERE email = ? AND password = crypt(?, password)")) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt("id") != 0) {
                    rsl = true;
                }
            }
        } catch (Exception e) {
            logger.error("Error DB connection or PrepareStatement execution: checkPass()", e);
        }
        return rsl;
    }

    private void create(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO users(name, email, password) VALUES (?, ?, crypt(?, gen_salt('bf')))",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            logger.error("Error DB connection or PrepareStatement execution: create()", e);
        }
    }

    private void update(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE users " +
                     "SET name = ?, email = ?, password = crypt(?, gen_salt('bf')) " +
                     "WHERE id = ?")) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Error DB connection or PrepareStatement execution: update()", e);
        }
    }

    private void getUserFromResultSet(ResultSet rs, User user) throws SQLException {
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword("");
    }
}
