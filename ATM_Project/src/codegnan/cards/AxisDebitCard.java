package codegnan.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Com.codegnan.customExceptions.InsufficentMachineBalanceException;
import Com.codegnan.customExceptions.InsufficientBalanceException;
import Com.codegnan.customExceptions.InvalidAmountException;
import Com.codegnan.customExceptions.NotAOperatorException;
import Com.codegnan.interfaces.IATMService;

public class AxisDebitCard implements IATMService {
	
	String name;
	long debiteCardNumber;
	double accountBalance;
	int pinNumber;
	List<String> statement;
	final String type="user";
	int chances;
	
	public AxisDebitCard(long debitCardNumber,String name, double accountBalance,int pinNumber) {
		chances=3;
		this.name=name;
		this.accountBalance=accountBalance;
		this.pinNumber=pinNumber;
		statement=new ArrayList<>();
	}
	
	
	
	
	

	
	public String getuserType() throws NotAOperatorException {
		
		return type;
	}

	@Override
	public double withdrawAmount(double withAmount)
			throws InvalidAmountException, InsufficientBalanceException, InsufficentMachineBalanceException {
		
		if(withAmount<=0) {
			throw new InvalidAmountException("You can't ente Zero amount to withdraw . plese enter a valid Amount");
		}else if(withAmount%100!=0) {
			throw new InvalidAmountException("Please withdraw multiples of 100");
			
		}else if(withAmount<500) {
			throw new InvalidAmountException("please withdraw more then 500");
			
		}else if(withAmount>accountBalance) {
			throw new InsufficientBalanceException("You did not have sufficient Amount in account");
			
		}else {
			accountBalance=accountBalance-withAmount;
			statement.add("Debited: "+withAmount);
		}
		
		return withAmount;
	}

	@Override
	public double depositeAmount(double dptAmount) throws InvalidAmountException {
		
		if(dptAmount<=500) {
			throw new InvalidAmountException("You can not deposite zero or less then zero rupper and deposite more the 500...!");
		}else if(dptAmount%100!=0) {
			throw new InvalidAmountException("please deposit multiples of 100..!");
		}else if(dptAmount<500) {
			throw new InvalidAmountException("please deposit more the 500 ...!");
		}else {
			accountBalance=accountBalance+dptAmount;
			statement.add("creadited :"+dptAmount);
		}
		
		return dptAmount;
	}

	@Override
	public double checkBalance() {
		
		return accountBalance;
	}

	@Override
	public void changePinNumber(int pinNumber) {
		this.pinNumber=pinNumber;
		


	}

	@Override
	public int getPinNumber() {
		// TODO Auto-generated method stub
		return pinNumber;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public int getChances() {
		
		return chances;
	}

	@Override
	public void decreseChances() {

		--chances;
		
	}

	@Override
	public void resetPinChances() {
		chances=3;
		
	}

	@Override
	public void getMiniStatement() {
		int count=5;
		
		if(statement.size()==0) {
			System.out.println(" The are no transaction Happend...!");
			return;
			
		}System.out.println("=====================Last 5 Transaction===================");
		Collections.reverse(statement);
		for(String trans:statement) {
			if(count==0) {
				break;
			}System.out.println(trans);
			count--;
		}
		Collections.reverse(statement);
	}

}

