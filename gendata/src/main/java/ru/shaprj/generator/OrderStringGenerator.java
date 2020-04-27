package ru.shaprj.generator;
/*
 * Created by O.Shalaevsky on 25.04.2020
 *
 * Generate Uniformly distributed sell and buy order strings
 *
 */

import ru.shaprj.common.model.OrderParam;
import ru.shaprj.util.FormatterHelper;
import ru.shaprj.util.GeneratorsHelper;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderStringGenerator implements StringGenerator {

    private static final int BUFFER_SIZE = 10_000;

    private final String active;
    private final Double lowSell;
    private final Double highSell;
    private final Double lowBuy;
    private final Double highBuy;
    private Integer volume;
    private Consumer<String> fileSaver;
    private BiFunction<Double, Double, Double> doubleGenerator;

    public OrderStringGenerator(String active, Double lowSell, Double highSell, Double lowBuy, Double highBuy, Integer volume,
                                BiFunction<Double, Double, Double> doubleGenerator, Consumer<String> fileSaver) {

        this.active = active;
        this.lowSell = lowSell;
        this.highSell = highSell;
        this.lowBuy = lowBuy;
        this.highBuy = highBuy;
        this.volume = volume;
        this.fileSaver = fileSaver;
        this.doubleGenerator = doubleGenerator;
    }

    public OrderStringGenerator(OrderParam orderParam, Integer volume,
                                BiFunction<Double, Double, Double> doubleGenerator, Consumer<String> fileSaver) {
        this(orderParam.getActive(), orderParam.getLowSell(), orderParam.getHighSell(), orderParam.getLowBuy(), orderParam.getHighBuy(), volume,
                doubleGenerator, fileSaver);
    }

    @Override
    public void generate() {
        new DoWithPercent(() -> {
        }, 0);
        double noise = calcNoise(lowBuy);
        new DoWithPercent(() -> generate("S", lowSell, highSell, noise), 50);
        new DoWithPercent(() -> generate("B", lowBuy, highBuy, noise), 100);
    }

    private void generate(String orderMarker, Double low, Double high, double noise) {
        if (BUFFER_SIZE >= volume) {
            generate(orderMarker, low, high, volume, noise);
        } else {
            int count = (volume % BUFFER_SIZE == 0) ?
                    volume / BUFFER_SIZE :
                    volume / BUFFER_SIZE + 1;
            for (int i = 0; i < count; i++) {
                generate(orderMarker, low, high, BUFFER_SIZE, noise);
            }
        }
    }

    private class DoWithPercent<T> {
        public DoWithPercent(Runnable runner, int percent) {
            runner.run();
            System.out.print(String.format("---> %s[%d%%]", active, percent));
            if (percent == 100) {
                System.out.println("\r\n");
            }
        }
    }

    private void generate(String orderMarker, Double low, Double high, int bufferSize, double noise) {
        Stream<Double> sd = Stream.generate(() -> {
            return GeneratorsHelper.uniformDoubleDistributedGenerator().apply(low + noise, high + noise);
        });
        String data = sd
                .parallel()
                .limit(bufferSize)
                .map(price -> generateOrders(price, orderMarker))
                .collect(Collectors.joining());
        fileSaver.accept(data);
    }

    private String generateOrders(Double price, String orderMarker) {
        Stream<Integer> si = Stream.generate(() -> GeneratorsHelper.simpleIntegerDistributedGenerator().apply(1, 5));
        return si
                .limit(GeneratorsHelper.simpleIntegerDistributedGenerator().apply(1, 10))
                .map(d -> FormatterHelper.getOrderFormatted(active, price, d, orderMarker))
                .collect(Collectors.joining());
    }

    private double calcNoise(double value){
        int divider = (10 + Math.abs(new Random().nextInt(20)) % 20);
        return value / divider;
    }

    public void setFileSaver(Consumer<String> fileSaver) {
        this.fileSaver = fileSaver;
    }
}
