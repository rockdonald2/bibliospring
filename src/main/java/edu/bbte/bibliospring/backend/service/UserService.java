package edu.bbte.bibliospring.backend.service;

import edu.bbte.bibliospring.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll() throws ServiceException;

    boolean login(User user) throws ServiceException;

    User register(User user) throws ServiceException;

    User update(User user) throws ServiceException;

    Optional<User> findById(long id) throws ServiceException;

    Optional<User> findByUsername(String username) throws ServiceException;

    boolean deleteById(long id) throws ServiceException;

}
