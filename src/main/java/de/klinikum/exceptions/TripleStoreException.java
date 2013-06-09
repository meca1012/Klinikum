package de.klinikum.exceptions;

public class TripleStoreException extends SpirontoException {

    /**
     * 
     * SpirontoException.java
     * Purpose: Specialized Exception - Handling 
     *          For Sesame Exceptions
     * @author  Spironto Team 1
     * @version 1.0 08/06/13
     */
    
	private static final long serialVersionUID = 7933874528232881297L;

	public TripleStoreException() {
		super();
	}

	public TripleStoreException(String message) {
		super(message);
	}

	public TripleStoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public TripleStoreException(Throwable cause) {
		super(cause);
	}

}
