package Com.codegnan.customExceptions;

public class IncorrectPinLimitReachedException extends Exception {
	
	public IncorrectPinLimitReachedException(String errorMsg) {
		super (errorMsg);
		
	}

}
