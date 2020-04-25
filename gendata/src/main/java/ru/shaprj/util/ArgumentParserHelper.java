package ru.shaprj.util;
/*
 * Created by O.Shalaevsky on 24.04.2020
 */

import ru.shaprj.model.ArgumentParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArgumentParserHelper {

    /*
     *
     * Parse arguments functor
     *
     *
     * */

    static public Function<String, Map<ArgumentParam, List<Object>>> inputArgumentsParser() {
        return s -> Arrays.stream(ArgumentParam.values())
                .filter(p -> Arrays.stream(s.split("-"))
                        .filter(ss -> ss.contains(p.toString()))
                        .count() > 0)
                .collect(Collectors.toMap(p -> p, p -> getObjectList(p, s)));
    }

    /*
     *
     * Build list of param values
     *
     * */

    private static List<Object> getObjectList(ArgumentParam param, String s) {
        return Arrays.stream(s.split(" "))
                .filter(ss -> !ss.equals(param.toString()))
                .collect(Collectors.toList());
    }

}
