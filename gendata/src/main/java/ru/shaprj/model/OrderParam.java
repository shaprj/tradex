package ru.shaprj.model;
/*
 * Created by O.Shalaevsky on 25.04.2020
 *
 */

public class OrderParam {

    /*
    * active name
    * */

    private String active;

    /*
    * low sell price treashold
    * */
    private Double lowSell;

    /*
    * high sell price treashold
    * */
    private Double highSell;

    /*
     * low buy price treashold
     * */
    private Double lowBuy;

    /*
     * high buy price treashold
     * */
    private Double highBuy;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Double getLowSell() {
        return lowSell;
    }

    public void setLowSell(Double lowSell) {
        this.lowSell = lowSell;
    }

    public Double getHighSell() {
        return highSell;
    }

    public void setHighSell(Double highSell) {
        this.highSell = highSell;
    }

    public Double getLowBuy() {
        return lowBuy;
    }

    public void setLowBuy(Double lowBuy) {
        this.lowBuy = lowBuy;
    }

    public Double getHighBuy() {
        return highBuy;
    }

    public void setHighBuy(Double highBuy) {
        this.highBuy = highBuy;
    }
}
