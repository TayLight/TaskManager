package taskmanager;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.Scanner;

public class StringParser {

    Scanner input = new Scanner(System.in);

    public StringParser(){}

    /** Переработка строки в объект LocalTime
     * @param strTime Обрабатываемая строка
     * @return Объект LocalTime с параметрами, полученными из обрабатываемой строки
     * @throws NumberFormatException Ошибка парсинга String в Integer
     * @throws DateTimeException Ошибка создания объекта LocalTime
     */
    public LocalTime timeParse(String strTime) throws NumberFormatException, DateTimeException{
        int hour = 0, minute = 0;
        String[] splitTime = strTime.split(":");
        hour = Integer.parseInt(splitTime[0]);
        minute = Integer.parseInt(splitTime[1]);
        LocalTime time = LocalTime.of(hour, minute, 0);
        return time;
    }
}
