package core;

import java.util.ArrayList;
import java.util.List;

class Account {

    private String number;
    private double balance;
    private static List<Account> list = new ArrayList<>();

    Account(String number){
        this.number = number;
        balance = 0;
    }

    String getNumber(){
        return number;
    }

    static void add(Account account){
        list.add(account);
    }

    static List<Account> getList(){
        return list;
    }

    void deposit(double amount) {
        balance += amount;
    }

    double getBalance(){
        return balance;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }
}
