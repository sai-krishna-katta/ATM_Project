package Com.codegnan.customExceptions;

public class InvalidAmountException extends Exception{
	
	public InvalidAmountException(String errorMsg) {
		super(errorMsg);
	}

}
