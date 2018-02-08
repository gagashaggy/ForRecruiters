package core;

public class Utils {
    public static String checkAccountNumber(String number) throws Error {
        char[] digits = number.toCharArray();
        if (digits.length != 5){
            throw new Error();
        }
        for (char c: digits) {
            if (!Character.isDigit(c)){
                throw new Error();
            }
        }
        return number;
    }

    public static double checkAmount(String number) throws Error {
        double amount;
        try{
            amount = Double.parseDouble(number);
        }
        catch (NumberFormatException e){
            throw new Error();
        }
        if (amount <= 0){
            throw new Error();
        }
        return amount;
    }
}
