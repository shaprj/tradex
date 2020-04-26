package ru.shaprj.aggregate;
/*
 * Created by O.Shalaevsky on 26.04.2020
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import ru.shaprj.Aggregator;
import ru.shaprj.common.utils.FilesHelper;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Aggregators {

    static private final Logger log = LogManager.getLogger(Aggregator.class);
//    private static final RedissonClient redisson;
    private static double lastWritten = 0.0;
    private static int counter = 0;

//    static {
//        Config config = new Config();
//        config.useSingleServer()
//                .setAddress("redis://127.0.0.1:6379");
//
//        redisson = Redisson.create(config);
//    }
//
//    public static void insertIntoRedisMemCacheAndGroup(String fileName){
//
//        FilesHelper.readDataFromFile(fileName, s -> {
//
//            String trimmed = s.trim();
//            String[] splited = trimmed.substring(0, trimmed.lastIndexOf(";")).split(";");
//
//            if(splited.length == 4){
//
//                String mapName = splited[0] + "_" + splited[3];
//                Double price = Double.valueOf(Double.valueOf(splited[1].replace(',', '.')));
//
//                Integer count = Integer.valueOf(splited[2]);
//                RMap<Double, Integer> map = redisson.getMap(mapName);
//                Integer prev = map.get(price);
//                map.put(price, prev == null ? count : prev + count);
//                if(lastWritten != price){
//                    lastWritten = price;
//                    System.out.println(lastWritten + "Counter = " + counter++);
//                }
//            }

//        });
//    }

    private static TreeMap<Double, Integer> ordersSortedSell = new TreeMap<>();
    private static TreeMap<Double, Integer> ordersSortedBuy = new TreeMap<>(Collections.reverseOrder());

    public static void insertInTreeMap(String fileName){
        FilesHelper.readDataFromFile(fileName, s -> {
            String trimmed = s.trim();
            String[] splited = trimmed.substring(0, trimmed.lastIndexOf(";")).split(";");

            if(splited.length == 4){

                String markerOrder = splited[3];
                Double price = Double.valueOf(Double.valueOf(splited[1].replace(',', '.')));
                Integer count = Integer.valueOf(splited[2]);

                if(markerOrder.equals("S")){
                    ordersSortedSell.merge(price, count, Integer::sum);
                } else {
                    ordersSortedBuy.merge(price, count, Integer::sum);
                }

            }
        });

        printOrderBook(20);

    }

    private static void printOrderBook(int orderBookDepth){
        System.out.println("||  M  ||  P  ||  V  ||");
        putFirstEntriesReversed(10, ordersSortedSell, true)
                .forEach((price, count) -> System.out.println(String.format("||  S  || %.2f || %d ||", price, count)));
        putFirstEntriesReversed(10, ordersSortedBuy, false)
                .forEach((price, count) -> System.out.println(String.format("|| B || %.2f || %d ||", price, count)));
    }

    public static <K,V> SortedMap<K,V> putFirstEntriesReversed(int max, SortedMap<K,V> source, boolean reverse) {
        int count = 0;
        TreeMap<K,V> target = reverse ? new TreeMap<>(Collections.reverseOrder()) : new TreeMap<>();
        for (Map.Entry<K,V> entry:source.entrySet()) {
            if (count >= max) break;

            target.put(entry.getKey(), entry.getValue());
            count++;
        }
        return target;
    }

}
