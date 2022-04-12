package com.sda.currencyexchangeapi.utils;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utility {

    public static Double round(Double toRound, int scale) {
        BigDecimal bd = new BigDecimal(Double.toString(toRound));
        bd = bd.setScale(scale, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
