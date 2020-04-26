package ru.shaprj;
/*
 * Created by O.Shalaevsky on 24.04.2020
 */

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.shaprj.generator.OrderStringGenerator;
import ru.shaprj.common.model.ArgumentParam;
import ru.shaprj.schedule.TimerScheduler;
import ru.shaprj.util.FilesHelper;
import ru.shaprj.util.GeneratorsHelper;
import ru.shaprj.common.utils.ArgumentParserHelper;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class DataGenerator {

    static private Logger log = LogManager.getLogger(DataGenerator.class);

    private static final String FILE_NAME_PREFIX = "testDataFile";

    private static final String KAFKA_TOPIC_NAME = "aggregate-order-data";

    private static int fileNamePostfix = 1;

    static public void main(String[] args) {

        String joinedArgs = Arrays.stream(args)
                .collect(Collectors.joining(" "));

        Map<ArgumentParam, List<String>> params = ArgumentParserHelper.inputArgumentsParser().apply(joinedArgs);

        if (!checkIfArgumentsLegal(params)) {
            log.info("Incorrect command line arguments values :(");
            log.info("Example: -gpm 60000 -gv 10000 -ga USD EUR -gsl 75.00 -gsh 80.00 -gbl 70.0 -gbh 74.86");
            return;
        }

        new TimerScheduler(Integer.valueOf(params.get(ArgumentParam.GENERATOR_PERIOD_MILLS).get(0)), () -> {
            try {
                params.get(ArgumentParam.GENERATOR_ACTIVES).stream()
                        .forEach(activeName -> {
                            Double lowS = Double.valueOf(params.get(ArgumentParam.GENERATOR_SELL_LOW).get(0));
                            Double highS = Double.valueOf(params.get(ArgumentParam.GENERATOR_SELL_HIGH).get(0));
                            Double lowB = Double.valueOf(params.get(ArgumentParam.GENERATOR_BUY_LOW).get(0));
                            Double highB = Double.valueOf(params.get(ArgumentParam.GENERATOR_BUY_HIGH).get(0));
                            Integer volume = Integer.valueOf(params.get(ArgumentParam.GENERATOR_VOLUME).get(0));
                            new OrderStringGenerator(activeName, lowS, highS, lowB, highB, volume,
                                    GeneratorsHelper.uniformDoubleDistributedGenerator(), s ->
                                    FilesHelper.saveDataToFile(s.getBytes(), FILE_NAME_PREFIX + fileNamePostfix)).generate();
                        });
                notifyOnGenerationComplete();
                fileNamePostfix++;
            } catch (Exception e) {
                log.error(e.getStackTrace());
            }
        }).schedule();

    }

    private static void notifyOnGenerationComplete() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.16:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer(props);
        producer.send(new ProducerRecord(KAFKA_TOPIC_NAME, Paths.get(FILE_NAME_PREFIX + fileNamePostfix).toAbsolutePath().toString()));

    }

    private static boolean checkIfArgumentsLegal(Map<ArgumentParam, List<String>> params) {

        return params.containsKey(ArgumentParam.GENERATOR_ACTIVES) &&
                params.containsKey(ArgumentParam.GENERATOR_SELL_LOW) &&
                params.containsKey(ArgumentParam.GENERATOR_SELL_HIGH) &&
                params.containsKey(ArgumentParam.GENERATOR_BUY_LOW) &&
                params.containsKey(ArgumentParam.GENERATOR_BUY_HIGH) &&
                params.containsKey(ArgumentParam.GENERATOR_VOLUME) &&
                params.containsKey(ArgumentParam.GENERATOR_PERIOD_MILLS);

    }

}
