package ru.shaprj.util;
/*
 * Created by O.Shalaevsky on 24.04.2020
 */

import java.util.Random;
import java.util.function.UnaryOperator;

public class GeneratorsHelper {

    /*
     *
     * Simple random Double generator
     *
     * params:
     *
     *  limit - limiting output Double value
     *
     * returns next Double value
     *
     * */

    public static UnaryOperator<Double> simpleRandomGenerator() {
        return (limit) -> new Random().nextDouble() % limit;
    }

}
