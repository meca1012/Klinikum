package de.klinikum.server;

import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

public class Connect {

	public class Connection {
		String sesameServer = "http://localhost:8080/openrdf-sesame";
		String repositoryID = "TestNative";
		
		
			
		public Repository GetRepository()
		{
			Repository myRepository = new HTTPRepository(sesameServer, repositoryID);
			try {
				myRepository.initialize();
			} catch (RepositoryException e) {
				
				e.printStackTrace();
			}

			return myRepository;
		}
		
	}

	
	
}