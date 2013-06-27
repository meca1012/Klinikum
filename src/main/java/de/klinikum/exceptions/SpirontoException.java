package de.klinikum.exceptions;


/**
 * 
 * SpirontoException.java
 * Purpose: Specialized Exception - Handling 
 *          For Spironto Exceptions
 * @author  Carsten Meiser
 * @version 1.0 08/06/13
 */

public class SpirontoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7933874528232881297L;

	public SpirontoException() {
		super();
	}

	public SpirontoException(String message) {
		super(message);
	}

	public SpirontoException(String message, Throwable cause) {
		super(message, cause);
	}

	public SpirontoException(Throwable cause) {
		super(cause);
	}

}
