package ru.shaprj.generator;
/*
 * Created by O.Shalaevsky on 25.04.2020
 *
 * Generate Uniformly distributed sell and buy order strings
 *
 */

import ru.shaprj.model.OrderParam;
import ru.shaprj.util.FilesHelper;
import ru.shaprj.util.FormatterHelper;
import ru.shaprj.util.GeneratorsHelper;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UniformOrderStringGenerator implements StringGenerator {

    private final String active;
    private final Double lowSell;
    private final Double highSell;
    private final Double lowBuy;
    private final Double highBuy;
    private Consumer<String> fileSaver;

    /*
     *
     * active - name of trade active
     *
     * low - low treashold of price
     *
     * high - high treashold of price
     *
     * */

    public UniformOrderStringGenerator(String active, Double lowSell, Double highSell, Double lowBuy, Double highBuy, Consumer<String> fileSaver) {

        this.active = active;
        this.lowSell = lowSell;
        this.highSell = highSell;
        this.lowBuy = lowBuy;
        this.highBuy = highBuy;
        this.fileSaver = fileSaver;
    }

    public UniformOrderStringGenerator(OrderParam orderParam, Consumer<String> fileSaver) {
        this(orderParam.getActive(), orderParam.getLowSell(), orderParam.getHighSell(), orderParam.getLowBuy(), orderParam.getHighBuy(), fileSaver);
    }

    @Override
    public void generate() {
        generateData("S");
        generateData("B");
    }

    private void generateData(String orderMarker) {
        Stream<Double> sdSell = Stream.generate(() -> GeneratorsHelper.uniformDoubleDistributedGenerator().apply(lowSell, highSell));
        String sellData = sdSell.parallel()
                .limit(10000)
                .map(d -> generateOrders(d, orderMarker))
                .collect(Collectors.joining());
        FilesHelper.saveDataToFile(sellData.getBytes(), "priceOrderList.csv");

    }

    private String generateOrders(Double price, String orderMarker) {
        Stream<Integer> siSell = Stream.generate(() -> GeneratorsHelper.simpleIntegerDistributedGenerator().apply(10, 1000));
        return siSell
                .limit(GeneratorsHelper.simpleIntegerDistributedGenerator().apply(10, 1000))
                .map(d -> generateOrder(price, d, orderMarker))
                .collect(Collectors.joining());
    }

    private String generateOrder(Double price, Integer ordersCount, String orderMarker) {
        return FormatterHelper.getOrderFormatted(active, price, ordersCount, orderMarker);
    }

    public void setFileSaver(Consumer<String> fileSaver) {
        this.fileSaver = fileSaver;
    }
}
