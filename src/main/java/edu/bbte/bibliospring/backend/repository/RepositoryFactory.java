package edu.bbte.bibliospring.backend.repository;

import edu.bbte.bibliospring.backend.repository.jdbc.JdbcRepositoryFactory;

public interface RepositoryFactory {

    static RepositoryFactory getInstance() {
        return new JdbcRepositoryFactory();
    }

    UserRepository getUserRepository();

}
