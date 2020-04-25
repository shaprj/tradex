package ru.shaprj.model;

public enum ArgumentParam {

    /*
    *
    * Param for generator limit value
    *
    * */

    GENERATOR_LIMIT_VALUE("-gl"),

    /*
    *
    * Param for data volume value
    *
    * */

    GENERATOR_DATA_VOLUME_VALUE("-gdv");

    private String value;

    ArgumentParam(String value){

        this.value = value;
    }

}
