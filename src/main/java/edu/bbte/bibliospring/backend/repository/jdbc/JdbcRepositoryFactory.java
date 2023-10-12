package edu.bbte.bibliospring.backend.repository.jdbc;

import edu.bbte.bibliospring.backend.repository.RepositoryFactory;
import edu.bbte.bibliospring.backend.repository.UserRepository;

public class JdbcRepositoryFactory implements RepositoryFactory {

    @Override
    public UserRepository getUserRepository() {
        return new JdbcUserRepository();
    }

}
