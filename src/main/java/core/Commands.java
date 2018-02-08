package core;

public enum Commands {
    NEW_ACCOUNT{
        @Override
        String getDetail(){
            return this.toString() + " <account_number> - завести новый счет.\nПараметры:<account_number> - номер счета - 5-ти значное число.";
        }

        @Override
        void run(String command) throws Error {
            String[] parameters = command.split(" ");
            //проверяем количество параметров в команде
            if (parameters.length != 2){
                throw new Error();
            }
            //проверяем правильность номера аккаунта
            String number = Utils.checkAccountNumber(parameters[1]);
            //проверяем, не существует ли аккаунта с таким номером
            for (Account acc: Account.getList()) {
                if (acc.getNumber().equals(number)){
                    throw new Error();
                }
            }
            //создаём новый аккаунт
            Account account = new Account(number);
            Account.add(account);
            System.out.println("OK");
        }

        @Override
        public String toString(){
            return "NEWACCOUNT";
        }
    },
    DEPOSIT{
        @Override
        String getDetail(){
            return "DEPOSIT <account_number> <amount> - внести сумму на счёт.\n" + "" +
                    "Параметры:<account_number> - номер счета - 5-ти значное число, <amount> - сумма для зачисления.";
        }

        @Override
        void run(String command) throws Error{
            String[] parameters = command.split(" ");
            Account currentAccount = null;
            //проверяем количество параметров в команде
            if (parameters.length != 3){
                throw new Error();
            }
            //проверяем правильность номера аккаунта
            String number = Utils.checkAccountNumber(parameters[1]);
            //проверяем сумму
            double amount = Utils.checkAmount(parameters[2]);
            //проверяем, что нужынй аккаунт существует
            for (Account acc: Account.getList()) {
                if (acc.getNumber().equals(number)){
                    currentAccount = acc;
                }
            }
            if (currentAccount == null){
                throw new Error();
            }
            //добавляем деньги на счёт
            currentAccount.deposit(amount);
            System.out.println("OK");
        }

        @Override
        public String toString(){
            return "DEPOSIT";
        }
    },
    WITHDRAW{
        @Override
        String getDetail(){
            return "WITHDRAW <account_number> <amount> - снять сумму с счёта.\n" +
                    "Параметры:<account_number> - номер счета - 5-ти значное число, <amount> - сумма для снятия";
        }

        @Override
        void run(String command) throws Error {
            String[] parameters = command.split(" ");
            Account currentAccount = null;
            //проверяем количество параметров в команде
            if (parameters.length != 3){
                throw new Error();
            }
            //проверяем правильность номера аккаунта
            String number = Utils.checkAccountNumber(parameters[1]);
            //проверяем сумму
            double amount = Utils.checkAmount(parameters[2]);
            //проверяем, что нужынй аккаунт существует
            for (Account acc: Account.getList()) {
                if (acc.getNumber().equals(number)){
                    currentAccount = acc;
                }
            }
            if (currentAccount == null){
                throw new Error();
            }
            //проверяем остаток на счету
            if (currentAccount.getBalance() < amount){
                throw new Error();
            }
            //снимаем нужную сумму со счёта
            currentAccount.withdraw(amount);
            System.out.println("OK");
        }

        @Override
        public String toString(){
            return "WITHDRAW";
        }
    },
    BALANCE{
        @Override
        String getDetail(){
            return "BALANCE <account_number> - вывести в стандартный поток вывода значение баланса счета.";
        }

        @Override
        void run(String command) throws Error {
            Account currentAccount = null;
            String[] parameters = command.split(" ");
            //проверяем количество параметров в команде
            if (parameters.length != 2){
                throw new Error();
            }
            //проверяем правильность номера аккаунта
            String number = Utils.checkAccountNumber(parameters[1]);
            //проверяем, что нужынй аккаунт существует
            for (Account acc: Account.getList()) {
                if (acc.getNumber().equals(number)){
                    currentAccount = acc;
                }
            }
            if (currentAccount == null){
                throw new Error();
            }
            //проверяем остаток на счету
            System.out.println(currentAccount.getBalance());
        }

        @Override
        public String toString(){
            return "BALANCE";
        }
    };

    abstract String getDetail();//описание команды
    abstract void run(String command) throws Error;//обработка команды
    public abstract String toString();//наименование программы

    //получить полный список команд с описанием
    public static void get() {
        for (Commands c: Commands.values()) {
            System.out.println(c.getDetail());
            System.out.println("");
        }
    }

    //первичная проверка команды
    public static void check(String command) throws Error {
        String currentCommand = null;
        for (Commands c: Commands.values()) {
            if (command.toUpperCase().startsWith(c.toString() + " ")){
                currentCommand = c.name();
            }
        }
        if (currentCommand == null){
            throw new Error();
        }
        else{
            Commands.valueOf(currentCommand).run(command);
        }
    }
}
