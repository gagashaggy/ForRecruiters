package Settings;

import java.util.ArrayList;
import java.util.List;

public class Page {
    private int width;
    private int height;
    private List<Column> columns = new ArrayList<>();

    Page(int width, int height){
        this.width = width;
        this.height = height;
    }
    //для чего нужна ширина страницы
    //так и не понял
    public int width(){
        return width;
    }

    public int height(){
        return height;
    }

    void addColumn(Column column){
        columns.add(column);
    }

    public Column column(int id){
        return columns.get(id);
    }

    public List<Column> columns(){
        return columns;
    }
}
