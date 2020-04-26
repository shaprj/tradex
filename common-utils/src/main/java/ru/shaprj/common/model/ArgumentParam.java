package ru.shaprj.common.model;

public enum ArgumentParam {

    /*
     *
     * Generator actives list
     *
     * */

    GENERATOR_ACTIVES("ga"),

    /*
     *
     *  Generator sell lower treashold
     *
     * */
    GENERATOR_SELL_LOW("gsl"),

    /*
     *
     * Generator sell high treashold
     *
     * */
    GENERATOR_SELL_HIGH("gsh"),

    /*
     *
     *  Generator buy lower treashold
     *
     * */
    GENERATOR_BUY_LOW("gbl"),

    /*
     *
     * Generator buy high treashold
     *
     * */
    GENERATOR_BUY_HIGH("gbh"),

    /*
     *
     * Generator volume of generated data
     *
     * */
    GENERATOR_VOLUME("gv"),

    /*
     *
     * Generator period in milliseconds
     *
     * */
    GENERATOR_PERIOD_MILLS("gpm"),

    /*
     *
     * Consumer topic name
     *
     * */
    CONSUMER_TOPIC_NAME("ctn"),

    /*
     *
     * Consumer Apache Kafka group id
     *
     * */
    CONSUMER_KAFKA_GROUP_ID("cgi"),

    /*
     *
     * Consumer Apache Kafka client id
     *
     * */
    CONSUMER_KAFKA_CLIENT_ID("cci");


    private String value;

    ArgumentParam(String value) {

        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
