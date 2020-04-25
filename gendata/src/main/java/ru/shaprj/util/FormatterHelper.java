package ru.shaprj.util;
/*
 * Created by O.Shalaevsky on 25.04.2020
 */

public class FormatterHelper {

    private final static String orderPattern = "%s;%.2f;%d;%s;\r\n";

    public static String getOrderFormatted(String active, Double price, Integer ordersCount, String orderMarker){
        return String.format(orderPattern, active, price, ordersCount, orderMarker);
    }

}
