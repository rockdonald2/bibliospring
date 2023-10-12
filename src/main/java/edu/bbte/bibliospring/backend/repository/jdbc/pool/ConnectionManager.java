package edu.bbte.bibliospring.backend.repository.jdbc.pool;

import edu.bbte.bibliospring.backend.repository.RepositoryException;
import edu.bbte.bibliospring.backend.util.PropertyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

public final class ConnectionManager {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);

    private static final String DB_ADDR;
    private static final String DB_NAME;
    private static final String CONN_STRING;
    private static final String DB_USER;
    private static final String DB_PWD;

    private static final ConcurrentLinkedQueue<Connection> connections = new ConcurrentLinkedQueue<>();
    private static final int MAX_CONNECTIONS;

    static {
        DB_ADDR = PropertyProvider.getProperty("db.address").orElse("localhost");
        DB_NAME = PropertyProvider.getProperty("db.name").orElse("");
        DB_USER = PropertyProvider.getProperty("db.user").orElse("default");
        DB_PWD = PropertyProvider.getProperty("db.password").orElse("default");
        CONN_STRING = String.format("jdbc:mysql://%s:3306/%s", DB_ADDR, DB_NAME);
        MAX_CONNECTIONS = Integer.parseInt(PropertyProvider.getProperty("db.max-connections").orElse("1"));

        IntStream.range(0, MAX_CONNECTIONS).forEach(connectionIdx -> {
            try {
                connections.add(DriverManager.getConnection(CONN_STRING, DB_USER, DB_PWD)); // NOSONAR
            } catch (SQLException e) {
                LOG.error("Failed to initialize connection", e);
                throw new RepositoryException("Failed to initialize connection at idx " + connectionIdx);
            }
        });

        if (connections.size() != MAX_CONNECTIONS) {
            LOG.error("Couldn't initialize connection pool for repository operations");
            throw new RepositoryException("Couldn't initialize connection pool for repository operations");
        }
    }

    private ConnectionManager() {

    }

    public static synchronized Connection getConnection() {
        if (connections.isEmpty()) {
            LOG.error("No available connection found");
            throw new RepositoryException("No available connection found", new IllegalArgumentException("Empty connection pool"));
        }

        return connections.poll();
    }

    public static synchronized void releaseConnection(final Connection connection) {
        if (connections.size() < MAX_CONNECTIONS) {
            connections.add(connection);
        }
    }

}
