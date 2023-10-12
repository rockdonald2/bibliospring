package edu.bbte.bibliospring.backend;

import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

public class AbstractContainerBaseTest { // NOSONAR

    private static final int DB_PORT = 3306;
    private static final String DB_CONTAINER = "db";

    static {
        new DockerComposeContainer(new File("src/test/resources/docker-compose.yml")) // NOSONAR
                .withExposedService(DB_CONTAINER, DB_PORT)
                .waitingFor(DB_CONTAINER, Wait.forHealthcheck())
                .withRemoveImages(DockerComposeContainer.RemoveImages.ALL)
                .withBuild(true)
                .start();
    }

}
