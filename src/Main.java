import Data.Data;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            throw new RuntimeException("Invalid arguments");
        }
        String settings = args[0];
        String sourceData = args[1];
        String report = args[2];
        Data inputData;
        try {
            inputData = new Data(sourceData, settings);
            Generator.generateReport(inputData, report);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
