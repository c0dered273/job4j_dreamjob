package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.City;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class CitiesPsqlStore implements Store<City>{
    private static final Logger logger = LoggerFactory.getLogger(CitiesPsqlStore.class);
    private final BasicDataSource pool = new BasicDataSource();

    private static final class Lazy {
        private static final Store<City> INSTANCE = new CitiesPsqlStore();
    }

    public static Store<City> instOf() {
        return Lazy.INSTANCE;
    }

    private CitiesPsqlStore() {
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
    public Collection<City> findAll() {
        List<City> city = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM cities ORDER BY id")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    city.add(new City(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            logger.error("Error DB connection or PrepareStatement execution. Method: findAll()", e);
        }
        return city;
    }

    @Override
    public void save(City item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public City findById(int id) {
        City result = new City(-1, "");
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * from cities WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result.setId(rs.getInt("id"));
                    result.setName(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            logger.error("Error DB connection or PrepareStatement execution. Method: findById()", e);
        }
        return result;
    }
}
