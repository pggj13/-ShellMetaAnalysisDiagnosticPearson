/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unesc.roma.managedbean.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Leandro de Oliveira Reolon
 */
public class FormulasMatematicas {

    public final static BigDecimal E = new BigDecimal("2.71828182845904523536");
//    "2.71828182845904523536028747135266249775724709369995");
    public final static BigDecimal LESS = new BigDecimal("-1");
    public final static BigDecimal HALF = new BigDecimal("0.5");
    public final static BigDecimal TWO = new BigDecimal("2");
    public final static BigDecimal ONE = new BigDecimal("1");
    public final static BigDecimal HUNDRED = new BigDecimal("100");
    public final static int SCALE_DEFAULT = 20;

    public static BigDecimal exp(BigDecimal bd, int escala, RoundingMode roundingMode) {
        double d = bd.doubleValue();
        BigDecimal ret = new BigDecimal(Math.exp(d));
        return ret.setScale(escala, roundingMode);
    }

    public static BigDecimal log(BigDecimal bd, int escala, RoundingMode roundingMode) {
        double d = bd.doubleValue();
        double result;
        result = Math.log(d);
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            return new BigDecimal("0");
        }
        
        BigDecimal ret = new BigDecimal(Math.log(d));
        return ret.setScale(escala, roundingMode);
    }

    public static BigDecimal sqrt(BigDecimal bd, int escala, RoundingMode roundingMode) {
        double d = bd.doubleValue();
        BigDecimal ret = new BigDecimal(Math.sqrt(d));
        return ret.setScale(escala, roundingMode);
    }

    public static BigDecimal pow(BigDecimal bd1, BigDecimal bd2, int escala, RoundingMode roundingMode) {
        if (TWO.equals(bd2)) {
            return bd1.pow(2).setScale(escala, roundingMode);
        }
        double d1 = bd1.doubleValue();
        double d2 = bd2.doubleValue();
        double result = Math.pow(d1, d2);
        if (Double.isInfinite(result) || Double.isNaN(result)) {
            return new BigDecimal("0");
        }
        BigDecimal ret = new BigDecimal(result);
        return ret.setScale(escala, roundingMode);
    }
}
