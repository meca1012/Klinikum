package de.klinikum.exceptions;

public class RDFException extends SpirontoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4867636264832654255L;

	public RDFException(){
		super();
	}
	
	public RDFException(String message) {
		super(message);
	}

	public RDFException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
