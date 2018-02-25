import Data.Data;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

class Generator {

    private static int s;//количество столбцов

    /**
     * напечатать отчёт
     *
     * @param data - объект с исходными данными
     * @param report - путь к файлу отчёта
     * @throws IOException
     */
    public static void generateReport(Data data, String report) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(report), "UTF16"));
        boolean hyphen = false;//перенос строки
        boolean newPage = false;//новая страница
        int countLines = 0;//количество строк на странице
        int c = data.values().length;//количество строк в файле с данными
        s = data.settings().page().columns().size();
        int height = data.settings().page().height() - 1;//высота страницы
        int i = 0;//итератор для строк данных
        String[] value = new String[s];//массив значений всех столбцов для одной строки
        String[][] remained = new String[c][s];//остаток от этих значений после заполнения одной строки отчёта
        countLines += printHeader(data, writer);//добавляем заголовки колонок
        while (i < c) {
            //проверяем, уместится ли следующая строка с данными на текущую страницу
            for (int j = 0; j < s; j++) {
                if (i < c - 1) {
                    if (countLines > height - getCountLinesRemained(data, i + 1)) {
                        newPage = true;
                    }
                }
            }
            //если достигнут лимит строк на странице или следующая строка с данными не помещается,
            //создаём новую страницу
            if (countLines > height || newPage){
                writer.append("~").append(System.lineSeparator());
                countLines = 0;
                countLines += printHeader(data, writer);
                newPage = false;
            }
            //иначе если мы не находимся в состоянии переноса строки,
            //рисуем разграничительную черту
            else {
                if (i > 0) {
                    if (!hyphen) {
                        if (i < c) {
                            countLines += makeSeparateLine(data, writer);
                        }
                    }
                }
            }
            //начинаем заполнять новую строку
            writer.append("|");
            //если не находимся в состоянии переноса строки,
            if (!hyphen) {
                //считываем новую строку из массива данных,
                //последовательно проходя по столбцам
                for (int j = 0; j < s; j++) {
                    //считываем новую ячейку из массива данных
                    value[j] = data.getValue(i, j);
                    //ширина текущего столбца
                    int width = data.settings().page().column(j).width();
                    //провеярем остаток, который будет перенесён на следующую строку
                    remained[i][j] = getRemainder(value[j], width);
                    //формируем значение для записи в файл
                    value[j] = makeValue(value[j], width);
                    //записываем в файл
                    print(writer, value[j], width);
                }
            }
            //если находимся в состоянни переноса строки,
            //то данные для записи берём из массива остатка с прошлой итерации
            else {
                for (int j = 0; j < s; j++) {
                    value[j] = remained[i][j];
                    int width = data.settings().page().column(j).width();
                    remained[i][j] = getRemainder(value[j], width);
                    value[j] = makeValue(value[j], width);
                    print(writer, value[j], width);
                }
            }
            //завершаем зпись строки
            countLines++;
            hyphen = false;
            writer.append(System.lineSeparator());

            //остался остаток
            for (int j = 0; j < s; j++) {
                if (!remained[i][j].equals("")) {
                    hyphen = true;
                }
            }
            //нет остатка
            if (!hyphen) {
                if (i < c) {
                    i++;
                }
            }
        }
        writer.close();
    }

    /**
     * метод, формирующий строкудля записи в файл так, чтобы
     * она записалась в соответствии с требованиями к переносу строк
     *
     * @param value - переданное значение
     * @param width - ширина столбца, в который его надо записать
     * @return значение, готовое к записи в файл
     */
    private static String makeValue(String value, int width) {
        //если строка начинается с пробела, обрезаем его
        if (!value.equals("")) {
            if (value.substring(0, 1).equals(" ")) {
                value = value.substring(1);
            }
        }
        int length = value.length();
        //строка полностью умещается в столбец без переноса
        if (length <= width) {
            return value;
        }else {
            //иначе пытаемся разбить строку на две
            String[] lines = value.split("(?U)\\W", 2);
            char delimiter;
            //если в ней нет символа разделителя, то в результате останется одна строка
            if (lines.length == 1) {
                //возвращаем только то, что умещается в столбец (разбиение по середние слова)
                return lines[0].substring(0, width);
            } else {
                String nextValue;
                //символ, по которому разбили строку на две
                delimiter = value.charAt(lines[0].length());
                //если первая строка не полностью занимает весь столбец
                if (width - lines[0].length() > 0) {
                    //рекурсивно формируем значение для второй строки, имея ввиду, что некоторая часть столбца уже занята первой строкой
                    nextValue = makeValue(lines[1] + delimiter, width - lines[0].length() - 1);
                    //если вторая строка помещается в столбец вместе с первой,
                    //то возвращаем их вместе с разделителем
                    if (nextValue.length() <= width - lines[0].length()) {
                        return lines[0] + delimiter + nextValue;
                    }
                    //просто возвращаем первую строку
                } else return lines[0];
            }
            //в конечном счёте возвращаем первую строку вместе с разделителем
            return lines[0] + delimiter;
        }
    }

    /**
     * метод, записывающий строку в файл
     *
     * @param writer - поток для записи
     * @param value - значение для записи
     * @param width - ширина столбца для записи дополнительных пробелов
     * @throws IOException
     */
    private static void print(BufferedWriter writer, String value, int width) throws IOException {
        writer.append(" ").append(value);
        int l = value.length();
        while (l < width){
            writer.append(" ");
            l++;
        }
        writer.append(" |");
        writer.flush();
    }

    /**
     * получение остатка от строки для переноса на следующую строку
     * @param value - строка
     * @param width - ширина столбца
     * @return
     */
    private static String getRemainder(String value, int width) {
        //строка полностью умещается в столбец (остатка нет)
        if (value.length() <= width) {
            return "";
        }
        else {
            //если строка начинается с пробела, то обрезаем его
            if (!value.equals("")) {
                if (value.substring(0, 1).equals(" ")) {
                    value = value.substring(1);
                }
            }//формируем значение для записи, возвращаем остаток
            return value.substring(makeValue(value, width).length());
        }
    }

    /**
     * провести разделительную линию
     *
     * @param data - объект с данными
     * @param writer - поток для записи
     * @return
     * @throws IOException
     */
    private static int makeSeparateLine(Data data, BufferedWriter writer) throws IOException {
        int[] w = new int[s];
        int c = 0;
        for (int i = 0; i < s; i++) {
            w[i] = data.settings().page().column(i).width();
            c += w[i];
        }
        writer.append("----");
        for (int i = 0; i < c; i++) {
            writer.append("-");
        }
        for (int i = 0; i < s; i++) {
            writer.append("--");
        }
        writer.append(System.lineSeparator());
        writer.flush();
        return 1;
    }

    /**
     * напечатать заголовок
     *
     * @param data - объект с данными
     * @param writer - поток для записи
     * @return возвращаем количество строк в файле, используемых под заголовок
     * @throws IOException
     */
    private static int printHeader(Data data, BufferedWriter writer) throws IOException {
        boolean hyphen = false;
        boolean finish = false;
        int countLines = 0;
        String[] value = new String[s];
        String[] remained = new String[s];
        while (!finish) {
            writer.append("|");
            if (!hyphen) {
                for (int i = 0; i < s; i++) {
                    value[i] = data.settings().page().column(i).title();
                    int width = data.settings().page().column(i).width();
                    remained[i] = getRemainder(value[i], width);
                    value[i] = makeValue(value[i], width);
                    print(writer, value[i], width);
                }
            } else {
                for (int i = 0; i < s; i++) {
                    value[i] = remained[i];
                    int width = data.settings().page().column(i).width();
                    remained[i] = getRemainder(value[i], width);
                    value[i] = makeValue(value[i], width);
                    print(writer, value[i], width);
                }
            }
            writer.append(System.lineSeparator());
            countLines++;
            hyphen = false;
            for (int i = 0; i < s; i++) {
                if (!remained[i].equals("")) {
                    hyphen = true;
                    value[i] = remained[i];
                }
            }
            if (!hyphen) {
                finish = true;
            }
        }
        countLines += makeSeparateLine(data, writer);
        return countLines;
    }

    /**
     * проверяет, уместится ли строка с данными на текущую страницу
     * или же останется остаток
     *
     * @param data - объект с данными
     * @param i - номер нужной строки
     * @return возвращает максимальное количество строк, которое
     * потребуется для записи остатка исходной строки
     */
    private static int getCountLinesRemained(Data data, int i){
        int[] l = new int[s];
        String r;
        for (int j = 0; j < s; j++){
            int width = data.settings().page().column(j).width();
            //проверяет остаток от строки до тех пор, пока
            //остаток не будет равен "", затем запоминает число
            //строк, которые потребуются для записи остатка исходной строки
            r = getRemainder(data.getValue(i, j), width);
            while (!r.equals("")) {
                r = getRemainder(r, width);
                l[j]++;
            }
        }
        int max = l[0];
        for (int j = 0; j < s; j++){
            max = Math.max(max, l[j]);
        }
        return max;
    }
}
