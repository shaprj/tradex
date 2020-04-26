package ru.shaprj;
/*
 * Created by O.Shalaevsky on 26.04.2020
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import ru.shaprj.aggregate.Aggregators;
import ru.shaprj.common.model.ArgumentParam;
import ru.shaprj.common.utils.ArgumentParserHelper;
import ru.shaprj.util.MessageBrokerConsumers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Aggregator {

    static private Logger log = LogManager.getLogger(Aggregator.class);

    public static void main(String[] args){

        String joinedArgs = Arrays.stream(args)
                .collect(Collectors.joining(" "));

        Map<ArgumentParam, List<String>> params = ArgumentParserHelper.inputArgumentsParser().apply(joinedArgs);

        if (!checkIfArgumentsLegal(params)) {
            log.info("Incorrect command line arguments values :(");
            log.info("Example: -ctn aggregate-order-data -cgi group1 -cci client1");
            return;
        }
        String topic = params.get(ArgumentParam.CONSUMER_TOPIC_NAME).get(0);
        String groupId = params.get(ArgumentParam.CONSUMER_TOPIC_NAME).get(0);
        String clientId = params.get(ArgumentParam.CONSUMER_TOPIC_NAME).get(0);
        MessageBrokerConsumers.subscribeKafkaTopic(topic, groupId, clientId, fileName -> {
            log.info(String.format("Preparing to aggregate file: '%s'", fileName));
            Aggregators.insertIntoRedisMemCacheAndGroup(fileName);
            log.info(String.format("Complete aggregation of file: '%s'", fileName));
        });
    }

    private static class OrderParams{
        private final Integer count;
        private final String markerOrder;

        public OrderParams(Integer count, String markerOrder){

            this.count = count;
            this.markerOrder = markerOrder;
        }

        public Integer getCount() {
            return count;
        }

        public String getMarkerOrder() {
            return markerOrder;
        }
    }

    private static boolean checkIfArgumentsLegal(Map<ArgumentParam, List<String>> params) {

        return params.containsKey(ArgumentParam.CONSUMER_TOPIC_NAME) &&
                params.containsKey(ArgumentParam.CONSUMER_KAFKA_GROUP_ID) &&
                params.containsKey(ArgumentParam.CONSUMER_KAFKA_CLIENT_ID);

    }

}
