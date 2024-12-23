package codgnan.Operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;



import Com.codegnan.customExceptions.IncorrectPinLimitReachedException;
import Com.codegnan.customExceptions.InsufficentMachineBalanceException;
import Com.codegnan.customExceptions.InsufficientBalanceException;
import Com.codegnan.customExceptions.InvalidAmountException;
import Com.codegnan.customExceptions.InvalidCardException;
import Com.codegnan.customExceptions.InvalidPinException;
import Com.codegnan.customExceptions.NotAOperatorException;
import Com.codegnan.interfaces.IATMService;
import codegnan.cards.AxisDebitCard;
import codegnan.cards.HDFCDebiteCard;
import codegnan.cards.OperatorCard;
import codegnan.cards.SBIDebitCard;

public class ATMOperations {

	// initial ATM Machine Balance
	public static double ATM_MACHINE_BALANCE = 100000.00;
	// activities perfomred on the ATM Machine
	public static ArrayList<String> ACTIVITY = new ArrayList<>();
	// database to map card numbers to card object
	public static HashMap<Long, IATMService> dataBase = new HashMap<>();
	// flag to indicate if the ATM machine is on or off.
	public static boolean MACHINE_ON = true;
	// refrence to the current card in use.
	public static IATMService card;

	// validate the inserted card by checking aganist the database.
	public static IATMService validateCard(long cardNumber) throws InvalidCardException {
		if (dataBase.containsKey(cardNumber)) {
			return dataBase.get(cardNumber);
		} else {
			ACTIVITY.add("Accessed By " + cardNumber + " is not compatible ");
			throw new InvalidCardException("This is Not A valid Card ");
		}
	}

	// Display thee Activities Perfomed on the ATM Machine
	public static void checkATMMAchineActivities() {
		System.out.println("============================== Activities Perfomed ===============");
		for (String activity : ACTIVITY) {
			System.out.println("============================================================");
			System.out.println(activity);
			System.out.println("============================================================");
		}
	}

	// Reset the number of pin attempts for a user.
	public static void resetUserAttempts(IATMService operatorCard) {
		IATMService card = null;
		long number;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter Your Card Number : ");
		number = scanner.nextLong();
		try {
			card = validateCard(number);
			card.resetPinChances();// resetting pin attemps for a user specified card.
			ACTIVITY.add("Accessed By " + operatorCard.getUsername() + "to reset number of chances for user");
		} catch (InvalidCardException ive) {
			ive.printStackTrace();
		}
	}

	// validate user creadintials including PIN.
	public static IATMService validateCredentials(long cardNumber, int pinNumber)
			throws InvalidCardException, InvalidPinException, IncorrectPinLimitReachedException {
		if (dataBase.containsKey(cardNumber)) {
			card = dataBase.get(cardNumber);
		} else {
			throw new InvalidCardException("This card is Not A valid Card");
		}
		try {
			if (card.getuserType().equals("operator")) {
				// operator have a diffrent pin validation process.
				if (card.getPinNumber() != pinNumber) {
					throw new InvalidPinException("Dear Operator. Please Enter Correct Pin Number ");
				} else {
					return card;
				}
			}
		} catch (NotAOperatorException noe) {
			noe.printStackTrace();
		}
		// validate pin and handle incoorect attemmpts.
		if (card.getChances() <= 0) {
			throw new IncorrectPinLimitReachedException(
					" you have Reached wrong limit of Pin Number, Which is 3 attempts");
		}
		if (card.getPinNumber() != pinNumber) {
			card.decreseChances();// decrease the number of remaing chances.
			throw new InvalidPinException(" You have entered A wrong pin Number");
		} else {
			return card;
		}
	}

	// validate the amount for withdrawl to ensure sufficient machine balance.
	public static void validateAmount(double amount) throws InsufficentMachineBalanceException {
		if (amount > ATM_MACHINE_BALANCE) {
			throw new InsufficentMachineBalanceException(" Insufficient cash in the Machine ");
		}
	}

	// validate deposit amount to ensure it meets Atm requirments.'
	public static void validateDepositAmount(double amount)
			throws InvalidAmountException, InsufficentMachineBalanceException {
		if (amount % 100 != 0) {
			throw new InvalidAmountException("Please deposit multiples of 100..");
		}
		if (amount + ATM_MACHINE_BALANCE > 200000.0) {
			ACTIVITY.add("Unable to Deposit Cash in the ATM Machine ...");
			throw new InsufficentMachineBalanceException(
					"you can't deposi cash as the limit of the machine is reached..");
		}
	}

	public static void operatorMode(IATMService card) {
		Scanner scanner = new Scanner(System.in);
		double amount;
		boolean flag = true;
		while (flag) {
			System.out.println("Operator Mode : Operator Name : " + card.getUsername());
			System.out.println("===============================================================");
			System.out.println();
			System.out.println("||        0. Switch off the machine       					 ||");
			System.out.println("||        1. Check ATM Machine Balance    					 ||");
			System.out.println("||        2. Depost Cash in The Machine   					 ||");
			System.out.println("||        3. Reset The User Pin Attempts  					 ||");
			System.out.println("||        4. To check activities perfomed in ATM Machine     ||");
			System.out.println("||        5. Exit Operator Mode           					 ||");
			System.out.println();
			System.out.println("===============================================================");
			System.out.println("Enter Your Choice ...");
			int option = scanner.nextInt();
			switch (option) {
			case 0:
				MACHINE_ON = false;
				ACTIVITY.add(
						"Accessed By " + card.getUsername() + " Activity Performed : Switching off the ATM Machine ");
				flag = false;
				break;
			case 1:
				ACTIVITY.add(
						"Accessed By " + card.getUsername() + " Activity Performed :  Checking ATM Machine Balance");
				System.out.println(" The Balance of the ATM Machine is : " + ATM_MACHINE_BALANCE + " IS Available ");
				break;
			case 2:
				System.out.println("Enter The Amount To Deposit ");
				amount = scanner.nextDouble();
				try {
					validateDepositAmount(amount);// validate the Deposit Amount
					ATM_MACHINE_BALANCE += amount;// update ATM Machine Balance
					ACTIVITY.add("Accessed By " + card.getUsername()
							+ " Activity Performed :  Deposit Cah in the ATM Machine");
					System.out.println("===================================================================");
					System.out.println("======================== Cash Added in the ATM Machine =====================");
					System.out.println(
							"=================================================================================");
				} catch (InvalidAmountException | InsufficentMachineBalanceException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				resetUserAttempts(card);// reset user Pin Attempts
				System.out.println("===================================================================");
				System.out.println("======================== User Attempts are Reset =====================");
				System.out.println("========================================================================");
				ACTIVITY.add("Accessed By " + card.getUsername()
						+ " Activity Performed : Resetting the Pin Attempts of user");
				break;
			case 4:
				checkATMMAchineActivities();// Display ATM Activities.
				break;
			case 5:
				flag = false;
				break;
			default:
				System.out.println("You Have Entered A Wrong Option..");
			}
		}
	}


	public static void main(String[] args) throws NotAOperatorException, IncorrectPinLimitReachedException {

		dataBase.put((long) 222222222, new AxisDebitCard(222222222,"Ramu",500000.0,2222));
		dataBase.put((long) 111111111, new HDFCDebiteCard(11111111,"Remo",700000.0,1111));
		dataBase.put((long) 333333333, new SBIDebitCard(333333333,"Nandini",8500000.0,3333));
		dataBase.put((long) 444444444, new OperatorCard(444444444,2222,"operator 1"));
		
		
		
		
		Scanner scanner=new Scanner(System.in);
		long cardNumber=0;
		double depositeAmount=0.0;
		double withdrawAmount=0.0;
		int pin=0;
		
		while(MACHINE_ON) {
			System.out.println("Plese Enter the Debit card Number : ");
			cardNumber=scanner.nextLong();
		
			try {
				System.out.println("please Enter Card number ");
				pin=scanner.nextInt();
				card=validateCredentials(cardNumber,pin);
				
			 if(card==null) {
				 System.out.println("Card Validation falied...!");
				 continue;
			 }
			 ACTIVITY.add("Accessed By"+card.getUsername()+"Status: Access apperence");
			 
			 if (card.getuserType().equals("operator")) {
			 operatorMode(card);
			 continue;
					 
			 
			}
			 while(true) {
				 System.out.println("User: Mode: "+card.getUsername());
				 System.out.println("||==================================================||");
				 System.out.println("||           1. withdraw Amount                     ||");
				 System.out.println("||           2. Deposite Amount                     ||");
				 System.out.println("||           3. Check Balance                       ||");
				 System.out.println("||           4. Change Pin                          ||");
				 System.out.println("||           5. Mini Statement                      ||");
				 System.out.println("||==================================================||");
				 System.out.println("Enter The your Choise....!");
				 
				 int option=scanner.nextInt();
				 try {
					 switch(option) {
					 case 1:
						 System.out.println("Plese Enter with Draw Amount:  ");
						 withdrawAmount=scanner.nextDouble();
						 validateAmount(withdrawAmount);
						 card.withdrawAmount(withdrawAmount);
						 ATM_MACHINE_BALANCE-=withdrawAmount;
						 ACTIVITY.add("Accesed by :"+card.getUsername()+"Activity done: "+withdrawAmount);
						 break;
					 case 2:
						 System.out.println("Plese Enter Deposit amount :  ");
						 depositeAmount=scanner.nextDouble();
						 validateAmount(depositeAmount);
						 card.withdrawAmount(depositeAmount);
						 ATM_MACHINE_BALANCE-=depositeAmount;
						 ACTIVITY.add("Accesed by :"+card.getUsername()+"Activity done: "+depositeAmount);
						 break;
					 case 3:
						 System.out.println("Your Account Balqance is :  "+card.checkBalance());
						 ACTIVITY.add("Accessed by: "+card.getUsername()+"Activity done : check the balance");
						 break;
					 case 4:
						 System.out.println("Enter A new Pin :");
						 pin =scanner.nextInt();
						 card.changePinNumber(pin);
						 ACTIVITY.add("Accesed by"+card.getChances()+"Activity done: ");
						 break;
					 case 5:
						 ACTIVITY.add("Accesed by"+card.getUsername()+"Activity done ");
						 card.getMiniStatement();
						 break;
					default:
						System.out.println("your are enter the worng option");
						 break;
						 
					 }System.out.println("Do you want to continue.....!");
					 String nextOption=scanner.next();
					 if(nextOption.equalsIgnoreCase("n"));
					 break;
				 
				 }catch(InvalidAmountException | InsufficientBalanceException | InsufficentMachineBalanceException e) {
					 e.printStackTrace();
				 }

			 
				 }
		
				 }catch(InvalidPinException | InvalidCardException | IncorrectPinLimitReachedException e) {
					 ACTIVITY.add("Accesed " +card.getUsername()+"Activited : ");
					 e.printStackTrace();
				 }
	
	
				 }
		
		System.out.println("=============================================================");
		System.out.println("==================Thanks for using===========================");
		System.out.println("=============================================================");
	}
	
}