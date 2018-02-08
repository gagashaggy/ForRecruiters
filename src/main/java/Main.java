import core.Commands;
import core.Error;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main (String[] args){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String command;
        System.out.println("Работа со счетами.");
        System.out.println("");
        System.out.println("Перечень доступных команд:");
        System.out.println("");
        Commands.get();
        while (true){
            try {
                command = reader.readLine();
                Commands.check(command);
            }
            catch (Error e){
                System.out.println(e.toString());
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
