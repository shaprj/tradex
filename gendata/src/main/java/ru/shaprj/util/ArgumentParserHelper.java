package ru.shaprj.util;
/*
 * Created by O.Shalaevsky on 24.04.2020
 */

import ru.shaprj.model.ArgumentParam;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArgumentParserHelper {

    /*
     *
     * Parse arguments functor
     *
     *
     *
     * */

    static public Function<String, Map<ArgumentParam, List<String>>> inputArgumentsParser() {
        return s -> Arrays.stream(s.split("-"))
                .filter(ss -> ss != null && !ss.equals(""))
                .map(ss ->
                        Arrays.stream(ArgumentParam.values())
                                .filter(p -> ss.contains(p.getValue()))
                                .map(p -> new AbstractMap.SimpleEntry(p, getObjectList(p, ss)))
                                .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry<ArgumentParam, List<String>>::getKey, Map.Entry<ArgumentParam, List<String>>::getValue));
    }

    /*
     *
     * Build list of param values
     *
     * */

    private static List<String> getObjectList(ArgumentParam param, String s) {
        return Arrays.stream(s.split(" "))
                .skip(1)
                .filter(ss -> !ss.equals(param.toString()))
                .collect(Collectors.toList());
    }

}
