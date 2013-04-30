package de.klinikum.server;

import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;


public class Connect {

		String sesameServer = "http://localhost:8080/openrdf-sesame";
		String repositoryID = "TestNative";
		
		
			
		public RepositoryConnection GetRepositoryConnection() throws RepositoryException
		{
			Repository repository = new HTTPRepository(sesameServer, repositoryID);
			try {
				repository.initialize();
			} catch (RepositoryException e) {
				
				e.printStackTrace();
			}

			RepositoryConnection repoCon = repository.getConnection();
						
			return repoCon;
		}
		
	}
