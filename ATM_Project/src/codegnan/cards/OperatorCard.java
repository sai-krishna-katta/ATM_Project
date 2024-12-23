package codegnan.cards;

import Com.codegnan.customExceptions.InsufficentMachineBalanceException;
import Com.codegnan.customExceptions.InsufficientBalanceException;
import Com.codegnan.customExceptions.InvalidAmountException;
import Com.codegnan.customExceptions.NotAOperatorException;
import Com.codegnan.interfaces.IATMService;

public class OperatorCard implements IATMService {

    private int pinNumber;
    private long id;
    private String name;
    private final String type = "operator";

    public OperatorCard(long id, int pin, String name) {
        this.id = id;
        this.pinNumber = pin;
        this.name = name;
    }

    @Override
    public String getuserType() throws NotAOperatorException {
        if (!type.equals("operator")) {
            throw new NotAOperatorException("This card does not belong to an operator.");
        }
        return type;
    }

    @Override
    public double withdrawAmount(double wthAmount)
            throws InvalidAmountException, InsufficientBalanceException, InsufficentMachineBalanceException {
        throw new UnsupportedOperationException("Withdraw operation is not supported for operator cards.");
    }

    @Override
    public double depositeAmount(double dptAmount) throws InvalidAmountException {
        throw new UnsupportedOperationException("Deposit operation is not supported for operator cards.");
    }

    @Override
    public double checkBalance() {
        throw new UnsupportedOperationException("Check balance operation is not supported for operator cards.");
    }

    @Override
    public void changePinNumber(int pinNumber) {
        this.pinNumber = pinNumber;
    }

    @Override
    public int getPinNumber() {
        return pinNumber;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public int getChances() {
        throw new UnsupportedOperationException("Chances are not applicable for operator cards.");
    }

    @Override
    public void decreseChances() {
        throw new UnsupportedOperationException("Decreasing chances is not applicable for operator cards.");
    }

    @Override
    public void resetPinChances() {
        throw new UnsupportedOperationException("Resetting PIN chances is not applicable for operator cards.");
    }

    @Override
    public void getMiniStatement() {
        throw new UnsupportedOperationException("Mini statement operation is not supported for operator cards.");
    }
}
