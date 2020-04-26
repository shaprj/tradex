package ru.shaprj.util;
/*
 * Created by O.Shalaevsky on 24.04.2020
 */

import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.function.BiFunction;

public class GeneratorsHelper {

    /*
     *
     * Uniformly distributed Double generator
     *
     *@param low - lower limit for output value
     *@param high - high limit for output value
     *@return next Double value
     *
     * */

    public static BiFunction<Double, Double, Double> uniformDoubleDistributedGenerator() {
        return (low, high) -> new RandomDataGenerator().nextUniform(low, high);
    }

    /*
     *
     * Integer generator
     *
     *@param low - lower limit for output value
     *@param high - high limit for output value
     *@return next Double value
     *
     * */

    public static BiFunction<Integer, Integer, Integer> simpleIntegerDistributedGenerator() {
        return (low, high) -> new RandomDataGenerator().nextInt(low, high);
    }

}
