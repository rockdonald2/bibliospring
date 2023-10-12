package edu.bbte.bibliospring.backend.service;

import edu.bbte.bibliospring.backend.service.impl.UserServiceImpl;

public class ServiceFactory {

    private ServiceFactory() {

    }

    public static UserService getUserService() {
        return new UserServiceImpl();
    }

}
