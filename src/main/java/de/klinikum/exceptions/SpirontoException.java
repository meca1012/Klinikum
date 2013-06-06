package de.klinikum.exceptions;

public class SpirontoException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7933874528232881297L;
	
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
