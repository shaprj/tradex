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

public class Aggregators {

    static private final Logger log = LogManager.getLogger(Aggregator.class);
    private static final RedissonClient redisson;
    private static double lastWritten = 0.0;
    private static int counter = 0;

    static {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379");

        redisson = Redisson.create(config);
    }

    public static void insertIntoRedisMemCacheAndGroup(String fileName){

        FilesHelper.readDataFromFile(fileName, s -> {

            String trimmed = s.trim();
            String[] splited = trimmed.substring(0, trimmed.lastIndexOf(";")).split(";");

            if(splited.length == 4){

                String mapName = splited[0] + "_" + splited[3];

                RMap<Double, Integer> map = redisson.getMap(mapName);
                Double price = Double.valueOf(Double.valueOf(splited[1].replace(',', '.')));
                Integer count = Integer.valueOf(splited[2]);
                Integer prev = map.get(price);
                map.put(price, prev == null ? count : prev + count);
                if(lastWritten != price){
                    lastWritten = price;
                    System.out.println(lastWritten + "Counter = " + counter++);
                }
            }

        });


    }

}
