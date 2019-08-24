package net.unesc.roma.ClienteWS.Dados;

import java.math.BigDecimal;

public class effect {
    
    public BigDecimal effect;
    public BigDecimal low;
    public BigDecimal high;

    public effect(BigDecimal effect, BigDecimal low, BigDecimal high) {
        this.effect = effect;
        this.low = low;
        this.high = high;
    }
}
