package edu.bbte.bibliospring.backend.service.impl;

import edu.bbte.bibliospring.backend.model.User;
import edu.bbte.bibliospring.backend.repository.RepositoryException;
import edu.bbte.bibliospring.backend.repository.RepositoryFactory;
import edu.bbte.bibliospring.backend.repository.UserRepository;
import edu.bbte.bibliospring.backend.service.ServiceException;
import edu.bbte.bibliospring.backend.service.UserService;
import edu.bbte.bibliospring.backend.util.PasswordEncrypter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserServiceImpl implements UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepository;
	private final PasswordEncrypter passwordEncrypter;

	public UserServiceImpl() {
		userRepository = RepositoryFactory.getInstance().getUserRepository();
		passwordEncrypter = new PasswordEncrypter();
	}

	@Override
	public List<User> findAll() {
		return userRepository.getAll();
	}

	@Override
	public boolean login(User user) {
		final Optional<User> repoUser;
		try {
			repoUser = userRepository.getByUsername(user.getUsername());
		} catch (RepositoryException e) {
			LOG.error("Failed to login user");
			throw new ServiceException("Failed to login user", e);
		}

		if (repoUser.isEmpty()) {
			return false;
		}

		user.setUuid(repoUser.get().getUuid());
		user.setId(repoUser.get().getId());

		try {
			user.setPassword(passwordEncrypter.hashPassword(user.getPassword(), user.getUuid()));
		} catch (SecurityException e) {
			LOG.error("Failed to encrypt user password", e);
			throw new ServiceException("Failed to encrypt user password", e);
		}

		AtomicBoolean loginStatus = new AtomicBoolean(false);

		try {
			userRepository.getByUsername(user.getUsername())
					.ifPresentOrElse(dbUser -> loginStatus.set(dbUser.getPassword().equals(user.getPassword())),
							() -> loginStatus.set(false)
					);
		} catch (RepositoryException e) {
			LOG.error("Failed to login user");
			throw new ServiceException("Failed to login user", e);
		}

		return loginStatus.get();
	}

	@Override
	public User register(User user) {
		Optional<User> repoUser;
		try {
			repoUser = userRepository.getByUsername(user.getUsername());
		} catch (RepositoryException e) {
			LOG.error("Failed to execute check for existing user");
			throw new ServiceException("Failed to execute check for existing user");
		}

		if (repoUser.isPresent()) {
			LOG.error("Uniqueness of users violated");
			throw new ServiceException("Uniqueness of users violated");
		}

		try {
			user.setPassword(passwordEncrypter.hashPassword(user.getPassword(), user.getUuid()));
		} catch (SecurityException e) {
			LOG.error("Failed to encrypt user password", e);
			throw new ServiceException("Failed to encrypt user password", e);
		}

		try {
			return userRepository.create(user);
		} catch (RepositoryException e) {
			LOG.error("Failed to register new user");
			throw new ServiceException("Failed to register new user", e);
		}
	}

	@Override
	public User update(User user) {
		try {
			final Optional<User> repoUser = userRepository.getByUsername(user.getUsername());

			if (repoUser.isEmpty()) {
				throw new ServiceException("Failed to update user, non-existent");
			}

			user.setId(repoUser.get().getId());
			user.setUuid(repoUser.get().getUuid());

			if (!repoUser.get().getPassword().equals(user.getPassword())) {
				try {
					user.setPassword(passwordEncrypter.hashPassword(user.getPassword(), user.getUuid()));
				} catch (SecurityException e) {
					LOG.error("Failed to encrypt user password", e);
					throw new ServiceException("Failed to encrypt user password", e);
				}
			}

			return userRepository.update(user);
		} catch (RepositoryException e) {
			LOG.error("Failed to update user");
			throw new RepositoryException("Failed to update user", e);
		}
	}

	@Override
	public Optional<User> findById(long id) {
		try {
			return userRepository.getById(id);
		} catch (RepositoryException e) {
			LOG.error("Failed to get user by id");
			return Optional.empty();
		}
	}

	@Override
	public Optional<User> findByUsername(String username) {
		try {
			return userRepository.getByUsername(username);
		} catch (RepositoryException e) {
			LOG.error("Failed to get user by username");
			return Optional.empty();
		}
	}

	@Override
	public boolean deleteById(long id) {
		try {
			return userRepository.deleteById(id);
		} catch (RepositoryException e) {
			LOG.error("Failed to delete user");
			throw new ServiceException("Failed to delete user", e);
		}
	}

}
