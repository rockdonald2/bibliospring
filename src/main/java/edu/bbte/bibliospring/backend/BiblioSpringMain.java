package edu.bbte.bibliospring.backend;

import edu.bbte.bibliospring.backend.model.User;
import edu.bbte.bibliospring.backend.service.ServiceFactory;
import edu.bbte.bibliospring.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BiblioSpringMain {

    private static final Logger LOG = LoggerFactory.getLogger(BiblioSpringMain.class);

    public static void main(String[] args) {
        final User user = new User("zsolt", "password");
        final UserService userService = ServiceFactory.getUserService();
        LOG.info("{}", userService.login(user));
    }

}
