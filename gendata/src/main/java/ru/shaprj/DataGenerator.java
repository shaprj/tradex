package ru.shaprj;
/*
 * Created by O.Shalaevsky on 24.04.2020
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.shaprj.generator.UniformOrderStringGenerator;
import ru.shaprj.model.ArgumentParam;
import ru.shaprj.util.ArgumentParserHelper;
import ru.shaprj.util.FilesHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataGenerator {

    static private Logger log = LogManager.getLogger(DataGenerator.class);

    static public void main(String[] args) {

        String joinedArgs = Arrays.stream(args)
                .collect(Collectors.joining(" "));

        Map<ArgumentParam, List<String>> params = ArgumentParserHelper.inputArgumentsParser().apply(joinedArgs);

        if (!checkIfArgumentsLegal(params)) {
            log.info("Incorrect command line arguments values :(");
            return;
        }

        params.get(ArgumentParam.GENERATOR_ACTIVES).stream()
                .forEach(activeName -> {
                    Double lowS = Double.valueOf(params.get(ArgumentParam.GENERATOR_SELL_LOW).get(0));
                    Double highS = Double.valueOf(params.get(ArgumentParam.GENERATOR_SELL_HIGH).get(0));
                    Double lowB = Double.valueOf(params.get(ArgumentParam.GENERATOR_BUY_LOW).get(0));
                    Double highB = Double.valueOf(params.get(ArgumentParam.GENERATOR_BUY_HIGH).get(0));
                    new UniformOrderStringGenerator(activeName, lowS, highS, lowB, highB, s ->
                            FilesHelper.saveData(s.getBytes(), 25000, 0, "testDataFile")).generate();
                });

    }

    private static ArgumentParam consumeKey(Map key){
        System.out.println();
        return null;
    }

    private static List<String> consumeValue(Map key){
        System.out.println("");
        return null;
    }

    private static boolean checkIfArgumentsLegal(Map<ArgumentParam, List<String>> params) {

        return params.containsKey(ArgumentParam.GENERATOR_ACTIVES) &&
                params.containsKey(ArgumentParam.GENERATOR_SELL_LOW) &&
                params.containsKey(ArgumentParam.GENERATOR_SELL_HIGH) &&
                params.containsKey(ArgumentParam.GENERATOR_BUY_LOW) &&
                params.containsKey(ArgumentParam.GENERATOR_BUY_HIGH);

    }

}
