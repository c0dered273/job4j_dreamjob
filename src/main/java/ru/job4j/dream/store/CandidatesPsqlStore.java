package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class CandidatesPsqlStore implements Store<Candidate> {
    private final BasicDataSource pool = new BasicDataSource();

    private static final class Lazy {
        private static final Store<Candidate> INSTANCE = new CandidatesPsqlStore();
    }

    public static Store<Candidate> instOf() {
        return Lazy.INSTANCE;
    }

    private CandidatesPsqlStore() {
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
    public Collection<Candidate> findAll() {
        List<Candidate> post = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidates ORDER BY id")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    post.add(new Candidate(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void save(Candidate item) {
        if (item.getId() == 0) {
            create(item);
        } else {
            update(item);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM candidates WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Candidate findById(int id) {
        Candidate result = new Candidate(-1, "");
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * from candidates WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result.setId(rs.getInt("id"));
                    result.setName(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private Candidate create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("INSERT INTO candidates(name) VALUES (?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("UPDATE candidates " +
                    "SET name = ? WHERE id = ?")) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
