package Com.codegnan.customExceptions;

public class InsufficentMachineBalanceException extends Exception{
	
	public InsufficentMachineBalanceException(String errorMsg) {
		super(errorMsg);
		
	}

}
