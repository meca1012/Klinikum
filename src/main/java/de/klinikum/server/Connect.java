package de.klinikum.server;

import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

public class Connect {

    private final String sesameServer = "http://localhost:8080/openrdf-sesame";
    private final String repositoryID = "TestNative";

    public RepositoryConnection getRepositoryConnection() throws RepositoryException {
        Repository repository = new HTTPRepository(this.sesameServer, this.repositoryID);
        try {
            repository.initialize();
        }
        catch (RepositoryException e) {

            e.printStackTrace();
        }

        RepositoryConnection repoCon = repository.getConnection();

        return repoCon;
    }

}
