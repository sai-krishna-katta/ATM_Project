package Com.codegnan.customExceptions;

public class InvalidCardException extends Exception {
	
	public InvalidCardException(String errorMsg) {
		super(errorMsg);
	}

}
