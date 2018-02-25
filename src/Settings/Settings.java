package Settings;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Settings {

    private Page page;

    private void setPage(Page newPage){
        page = newPage;
    }

    public Page page(){
        return page;
    }

    public Settings get(String pathToSettings) throws IOException {
        SAXBuilder parser = new SAXBuilder();
        Document xmlDoc;
        try {
            xmlDoc = parser.build(new File(pathToSettings));
            // Получаем список всех элементов page которые
            // содержит корневой элемент
            List pages = xmlDoc.getRootElement()
                    .getContent(new ElementFilter("page"));
            // Для каждого элемента page получаем
            // текст вложенных элементов width и height
            Iterator iterator = pages.iterator();
            while(iterator.hasNext()){
                Element page = (Element)iterator.next();
                int width = Integer.parseInt(page.getChildText("width"));
                int height = Integer.parseInt(page.getChildText("height"));
                setPage(new Page(width, height));
            }
            // Получаем список всех элементов column
            List columns = xmlDoc.getRootElement().getChild("columns").getContent(new ElementFilter("column"));
            // Для каждого элемента column получаем
            // текст вложенных элементов title и width
            iterator = columns.iterator();
            while(iterator.hasNext()){
                Element column = (Element)iterator.next();
                String title = column.getChildText("title");
                int width = Integer.parseInt(column.getChildText("width"));
                page().addColumn(new Column(title, width));
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return this;
    }
}
