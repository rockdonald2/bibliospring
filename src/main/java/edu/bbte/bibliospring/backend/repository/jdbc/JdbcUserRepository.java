package edu.bbte.bibliospring.backend.repository.jdbc;

import edu.bbte.bibliospring.backend.model.User;
import edu.bbte.bibliospring.backend.repository.RepositoryException;
import edu.bbte.bibliospring.backend.repository.UserRepository;
import edu.bbte.bibliospring.backend.repository.jdbc.pool.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String UUID = "uuid";
    public static final String ID = "id";
    private static final Logger LOG = LoggerFactory.getLogger(JdbcUserRepository.class);

    @Override
    public Optional<User> getById(Long id) {
        Connection c = null;
        User u = null;

        try {
            c = ConnectionManager.getConnection();

            try (PreparedStatement prepStmt = c.prepareStatement("SELECT * FROM BS_USER WHERE id = ?;")) {
                prepStmt.setLong(1, id);

                ResultSet rs = prepStmt.executeQuery();
                if (!rs.next()) {
                    return Optional.empty();
                }

                u = new User();
                u.setId(rs.getLong(ID));
                u.setUuid(rs.getString(UUID));
                u.setUsername(rs.getString(USERNAME));
                u.setPassword(rs.getString(PASSWORD));
            }
        } catch (SQLException e) {
            LOG.error("Failed to query user by ID", e);
            throw new RepositoryException("Failed to query user by ID", e);
        } finally {
            if (c != null) {
                ConnectionManager.releaseConnection(c);
            }
        }

        return Optional.of(u);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        Connection c = null;
        User u = null;

        try {
            c = ConnectionManager.getConnection();

            try (PreparedStatement prepStmt = c.prepareStatement("SELECT * FROM BS_USER WHERE username = ?;")) {
                prepStmt.setString(1, username);

                ResultSet rs = prepStmt.executeQuery();
                if (!rs.next()) {
                    return Optional.empty();
                }

                u = new User();
                u.setId(rs.getLong(ID));
                u.setUuid(rs.getString(UUID));
                u.setUsername(rs.getString(USERNAME));
                u.setPassword(rs.getString(PASSWORD));
            }
        } catch (SQLException e) {
            LOG.error("Failed to query user by username", e);
            throw new RepositoryException("Failed to query user by username", e);
        } finally {
            if (c != null) {
                ConnectionManager.releaseConnection(c);
            }
        }

        return Optional.of(u);
    }

    @Override
    public List<User> getAll() {
        Connection c = null;

        try {
            c = ConnectionManager.getConnection();

            try (PreparedStatement prepStmt = c.prepareStatement("SELECT * FROM BS_USER;")) {
                ResultSet rs = prepStmt.executeQuery();

                ArrayList<User> users = new ArrayList<>();

                User tmpUser;
                while (rs.next()) {
                    tmpUser = new User();

                    tmpUser.setId(rs.getLong(ID));
                    tmpUser.setUuid(rs.getString(UUID));
                    tmpUser.setUsername(rs.getString(USERNAME));
                    tmpUser.setPassword(rs.getString(PASSWORD));

                    users.add(tmpUser);
                }

                return users;
            }
        } catch (SQLException e) {
            LOG.error("Failed to fetch all users", e);
            throw new RepositoryException("Failed to fetch all users", e);
        } finally {
            ConnectionManager.releaseConnection(c);
        }
    }

    @Override
    public User create(User user) {
        Connection c = null;

        try {
            c = ConnectionManager.getConnection();

            try (PreparedStatement prepStmt = c.prepareStatement("INSERT INTO BS_USER (`uuid`, `username`, `password`) VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
                prepStmt.setString(1, user.getUuid());
                prepStmt.setString(2, user.getUsername());
                prepStmt.setString(3, user.getPassword());
                prepStmt.execute();

                ResultSet rs = prepStmt.getGeneratedKeys();
                rs.next();

                user.setId(rs.getLong(1));

                return user;
            }
        } catch (SQLException e) {
            LOG.error("Failed to insert new user", e);
            throw new RepositoryException("Failed to insert new user", e);
        } finally {
            if (c != null) {
                ConnectionManager.releaseConnection(c);
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Connection c = null;

        try {
            c = ConnectionManager.getConnection();

            try (PreparedStatement prepStmt = c.prepareStatement("DELETE FROM BS_USER WHERE `id` = ?;")) {
                prepStmt.setLong(1, id);
                return prepStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            LOG.error("Failed to update user", e);
            throw new RepositoryException("Failed to update user", e);
        } finally {
            if (c != null) {
                ConnectionManager.releaseConnection(c);
            }
        }
    }

    @Override
    public User update(User user) {
        Connection c = null;

        try {
            c = ConnectionManager.getConnection();

            try (PreparedStatement prepStmt = c.prepareStatement("UPDATE BS_USER SET `username` = ?, `password` = ? WHERE `id` = ?;")) {
                prepStmt.setString(1, user.getUsername());
                prepStmt.setString(2, user.getPassword());
                prepStmt.setLong(3, user.getId());

                int returnCodeOfUpdate = prepStmt.executeUpdate();

                if (returnCodeOfUpdate == 1) {
                    return user;
                } else if (returnCodeOfUpdate == 0) {
                    LOG.error("No user has been affected by the update");
                    throw new RepositoryException("No user has been affected by the update");
                } else if (returnCodeOfUpdate > 1) {
                    LOG.error("More than one user has been affected by the update");
                    throw new RepositoryException("More than one user has been affected by the update");
                }
            }
        } catch (SQLException | RepositoryException e) {
            LOG.error("Failed to update user", e);
            throw new RepositoryException("Failed to update user", e);
        } finally {
            if (c != null) {
                ConnectionManager.releaseConnection(c);
            }
        }

        LOG.info("Failed to update user");
        throw new RepositoryException("Failed to update user");
    }

}
