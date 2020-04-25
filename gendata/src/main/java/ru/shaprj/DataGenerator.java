package ru.shaprj;
/*
 * Created by O.Shalaevsky on 24.04.2020
 */

import ru.shaprj.model.ArgumentParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.shaprj.util.ArgumentParserHelper;
import ru.shaprj.util.FilesHelper;
import ru.shaprj.util.GeneratorsHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataGenerator {

    static private Logger log = LogManager.getLogger(DataGenerator.class);

    static public void main(String[] args) {

        Map<ArgumentParam, List<Object>> params = Arrays.stream(args)
                .map(ArgumentParserHelper.inputArgumentsParser()::apply)
                .collect(Collectors.toMap(m -> (ArgumentParam) m.keySet().toArray()[0], m -> (List<Object>) m.get(m.keySet().toArray()[0])));

        if (!checkIfArgumentsLegal(params)) {
            log.info("Incorrect command line arguments values :(");
        }

        Stream<Double> si = Stream.generate(() -> GeneratorsHelper.simpleRandomGenerator().apply(99999.99));
        Double [] prices = si.limit(10000).collect(Collectors.toList()).toArray(Double[]::new);
        FilesHelper.saveData(prices, 2500, 0, "priceList");

    }

    private static boolean checkIfArgumentsLegal(Map<ArgumentParam, List<Object>> params) {

        return params.containsKey(ArgumentParam.GENERATOR_DATA_VOLUME_VALUE) &&
                params.containsKey(ArgumentParam.GENERATOR_DATA_VOLUME_VALUE);

    }

}
