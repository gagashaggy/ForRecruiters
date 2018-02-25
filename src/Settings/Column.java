package Settings;

public class Column {
    private String title;
    private int width;

    Column(String title, int width){
        this.title = title;
        this.width = width;
    }

    public int width(){
        return width;
    }

    public String title(){
        return title;
    }

}
