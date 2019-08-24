package net.unesc.roma.ClienteWS.Dados;

import java.math.BigDecimal;

public class data {

    private String descripStringtion;
    private int descriptionOffset;
    private effect effect;
    private BigDecimal Size;
    private BigDecimal vp;
    private BigDecimal fp;
    private BigDecimal vn;
    private BigDecimal fn;
    
    private BigDecimal sen;
    private BigDecimal spec;

    public data(String description, int descriptionOffset, effect effect, BigDecimal Size, BigDecimal vp, BigDecimal fp, BigDecimal vn, BigDecimal fn) {

        this.descriptionOffset = descriptionOffset;
        this.effect = effect;
        this.Size = Size;
        this.vp = vp;
        this.fp = fp;
        this.vn = vn;
        this.fn = fn;
    }
    
    public data(BigDecimal sen,BigDecimal spec, BigDecimal vp, BigDecimal fp, BigDecimal vn, BigDecimal fn) {

        this.sen = sen;
        this.spec = spec;
        this.vp = vp;
        this.fp = fp;
        this.vn = vn;
        this.fn = fn;
    }

    public data() {

    }
}
