package edu.bbte.bibliospring.backend.repository;

import edu.bbte.bibliospring.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> getById(Long id) throws RepositoryException;

    Optional<User> getByUsername(String userName) throws RepositoryException;

    List<User> getAll() throws RepositoryException;

    User create(User user) throws RepositoryException;

    boolean deleteById(Long id) throws RepositoryException;

    User update(User user) throws RepositoryException;

}
