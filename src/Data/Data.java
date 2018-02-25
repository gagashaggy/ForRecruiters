package Data;

import Settings.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Data {

    private String[][] values;
    private Settings settings;

    public Data(String sourceData, String pathToSettings) throws IOException{
        this.settings = new Settings().get(pathToSettings);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceData), "UTF16"));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        values = new String[lines.size()][settings.page().columns().size()];
        for (int i = 0; i < lines.size(); i++){
            String[] keys = lines.get(i).split("\t");
            for (int j = 0; j < settings.page().columns().size(); j++){
                values[i][j] = keys[j];
            }
        }
    }

    public String getValue(int i, int j){
        return values[i][j];
    }

    public String[][] values(){
        return values;
    }

    public Settings settings(){
        return settings;
    }
}
